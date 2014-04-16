var http = require('http');
var https = require('https');
var express= require("express");
var fs = require ("fs"), json;
var app = express();
var url = require('url');
var util = require ('util');

// simple logger
app.use(function(req, res, next){
  console.log('%s %s', req.method, req.url);
  next();
});

//app.use(express.logger());

//for POSTS
app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());

//start server
var server = app.listen(8888, function() {
    //app.writeHead(200, {'Content-Type': 'text/plain'}); 
    console.log('Listening on port %d', server.address().port);
});

// app.use(function(req, res, next){
  // res.send(404, 'Sorry cant find that!');
// });

app.param(function(name, fn){
  if (fn instanceof RegExp) {
    return function(req, res, next, val){
      var captures;
      if (captures = fn.exec(String(val))) {
        req.params[name] = captures;
        next();
      } else {
        next('route');
      }
    }
  }
});

//set up params in a URL
app.param('major', /^\d+$/);
app.param('minor', /^\d+$/);

//redirect each URL
// app.get('/:major/:minor', function(req,res,next){
  // var major = req.params.major;
  // var minor = req.params.minor;
  // var beacons = getJson('beacons.json');
 
 // //checks if major and minor are part of the url + redirects
  // for(var i=0; i<beacons['beacons'].length; i++){
    // if (major == beacons['beacons'][i].major && minor == beacons['beacons'][i].minor){
      // if(!req.secure){
        // res.json(beacons['beacons'][i]);
      // }
    // }
  // }
  
  // next()
  
// });

app.get('/:major/:minor/redirect', function(req,res,next){
  var major = req.params.major;
  var minor = req.params.minor;
  var beacons = getJson('beacons.json');
 
 //checks if major and minor are part of the url + redirects
  for(var i=0; i<beacons['beacons'].length; i++){
    if (major == beacons['beacons'][i].major && minor == beacons['beacons'][i].minor){
    // if (major == beacons['beacons'][i].major){
      if(!req.secure){
        res.redirect(301, beacons['beacons'][i].uri);
		//res.json(beacons['beacons'][i]);
      }
    }
  }
  
  next()
  
});

//POST for adding beacons to system
app.post ("/beacons/new", function (req, res) {
  
  var uuid = req.body.uuid;
  var major = req.body.major;
  var minor = req.body.minor;
  var colour = req.body.colour;
  var room = req.body.room;
  var uri = req.body.uri;
  var jBeacons = getJson('beacons.json')
  
  //add POST data to new object
  var jObject = { "uuid": uuid, 'major':major, 'minor':minor, 'colour': colour, 'room':room, 'uri':uri};
  
  //add object to array
  var pos = jBeacons['beacons'].length;
  jBeacons['beacons'][pos]= jObject;
  var strBeacons = JSON.stringify(jBeacons, null, 4);
    
  //write to .json file
    fs.writeFile("beacons.json", strBeacons, function(err) {
        if(err) {
            console.log(err);
        } else {
            console.log("The file was saved.");
        }
    }); 
  
});

//test page for beacon post
app.get('/beacon/new', function(req, res){
  res.writeHead(200, {'Content-Type': 'text/html'});
    res.write('Beacons<br>');
    res.write('<form method="POST" action="/beacons/new">');
    res.write('UUID<input type="text" name="uuid"><br>');
    res.write('Major<input type="text" name="major"><br>');
    res.write('Mnor<input type="text" name="minor"><br>');
    res.write('Colour<input type="text" name="colour"><br>');
    res.write('Room<input type="text" name="room"><br>');
    res.write('URI<input type="text" name="uri"><br>');
    res.write('<input type="submit" name="beacon" value="Submit">');
    res.write('</form>');
    res.end();

});

//retrieves all beacons
app.get('/beacon', function(req, res) {
  var beacons = getJson('beacons.json');
  res.writeHead(200, {'Content-Type': 'text/html'});
  res.write(JSON.stringify(beacons));
  res.end();
});

app.post('/rooms/:room/booking', function(req, res) {

  //POST data
  var room = req.params.room;
  // var room = 'solas';
  var uri = "/here/who/is/where";
  var date = req.body.date;
  var startTime = req.body.startTime;
  var endTime = req.body.endTime;
  var meetingName = req.body.meetingName;
  // var users = req.body.users;
  var users = "appUser";

  var jBookings = getJson('bookings.json');
  
  for (var i = 0; i< jBookings['rooms'].length; i++) {
     if (room == jBookings['rooms'][i].roomname){
     //gets the booking data for the room
      var newBooking = jBookings['rooms'][i];
      //creates and adds new object to the booking array
      var jObject = {"URI": uri, 'date':date, 'StartT':startTime, 'EndT': endTime, 'MeetingName':meetingName, 'Users':users};
      var pos = newBooking['bookings'].length;
      newBooking['bookings'][pos]= jObject;
      //converts the while booking to a string for saving
      var strBookings = JSON.stringify(jBookings, null, 4);
      }
  }

  //write to .json file
    fs.writeFile("bookings.json", strBookings, function(err) {
        if(err) {
            console.log(err);
			res.send({ result: 'failed'});
        } else {
            console.log("The file was saved.");
			res.send({ result: 'OK'});
        }
    }); 
});

app.get('/rooms/:room/booking/:date', function(req, res){
	var room = req.params.room;
	var date = req.params.date;
	
	// console.log("room: " + room);
	// console.log("date: " + date);
	
  var jBookings = getJson('bookings.json');
	var strBookings;
    for (var i = 0; i< jBookings['rooms'].length; i++) {
        if (room == jBookings['rooms'][i].roomname){
          strBookings = jBookings['rooms'][i];
		  //console.log(strBookings);
        }
    }
	
	var bookingsOfTheDate = [];
	for (var i = 0; i < strBookings['bookings'].length; i ++) {
		if (date == strBookings['bookings'][i].date) {
			bookingsOfTheDate.push(strBookings['bookings'][i]);
		}
	}
	res.json(bookingsOfTheDate);
});

//for testing post
app.get('/rooms/:room/booking', function(req, res){
  var myroom = req.params.room;
  //var beacons = getJson('beacons.json');
  res.writeHead(200, {'Content-Type': 'text/html'});
    res.write('Booking<br>');
    res.write('<form method="POST" action="/rooms/solas/booking">');
    res.write('uri<input type="text" name="uri"><br>');
    res.write('date<input type="text" name="date"><br>');
    res.write('starttime<input type="text" name="startTime"><br>');
    res.write('endtime<input type="text" name="endTime"><br>');
    res.write('meetingname<input type="text" name="meetingName"><br>');
    res.write('users<input type="text" name="users"><br>');
    res.write('<input type="submit" name="booking" value="Submit">');
    res.write('</form>');
    res.end();

});

app.get('/:major/:minor', function(req,res,next){
  var major = req.params.major;
  var minor = req.params.minor;
  var beacons = getJson('beacons.json');
 
 
 //checks if major and minor are part of the url + redirects
  for(var i=0; i<beacons['beacons'].length; i++){
    if (major == beacons['beacons'][i].major && minor == beacons['beacons'][i].minor){
    
    var myroom = beacons['beacons'][i].room;
    var bookings = getBooking(myroom);
    var roomAvailability = getCurrentAvailability(bookings);
    var beaconInfo = getBeacon(major,minor)
    console.log(roomAvailability); 
      res.json({beacon :beaconInfo , available: roomAvailability});
        // res.json(beacons['beacons'][i]);
    }
  }
  
  next()
  
});


//used for storing room information before db is set up
app.get('/rooms/:room', function(req,res){
  //var myroom = url.parse(req.url).pathname.split('/')[2];
  var myroom = req.params.room;
  var bookings = getBooking(myroom);
  var roomDetails = getRoomDetails(myroom);
  var roomAvailability = getCurrentAvailability(bookings);
  //res.send(roomDetails + bookings);
  res.json({roomDetails: roomDetails, bookings: bookings, available: roomAvailability});
    
});

//Parses .json file
function readJsonFileSync(filepath, encoding){
  if (typeof (encoding) == 'undefined'){
    encoding = 'utf8';
  }
  var file = fs.readFileSync(filepath, encoding);
  return JSON.parse(file);
}

function getJson(file){
  var filepath = __dirname + '/' + file;
  return readJsonFileSync(filepath);
}

//returns schedule from desc.json
function getRoomDetails(room){
  var jRoom = getJson('desc.json');
  for (var i = 0; i< jRoom['rooms'].length; i++) {
      if (room == jRoom['rooms'][i].name){
        var strRoom = jRoom['rooms'][i];
      }
  } 
  return strRoom;
}

function getBooking(room){
  var jBookings = getJson('bookings.json');
  for (var i = 0; i< jBookings['rooms'].length; i++) {
      if (room == jBookings['rooms'][i].roomname){
        var strBookings = jBookings['rooms'][i];
      }
  } 
  return strBookings;
}

function getCurrentAvailability(bookings){
	var currentDate = new Date();
	var currentHour = currentDate.getHours();
  var currentMinute = currentDate.getMinutes();
  var currentTime = (currentHour + ":" + currentMinute);
  var available = "available";
  for (var i = 0; i< bookings['bookings'].length; i++) {
      if (currentTime >= bookings['bookings'][i].StartT && currentTime <= bookings['bookings'][i].EndT){
		  available = "occupied"; 
      }
  } 
  return available;
}

function getBeacon(major, minor){
  var jBeacon = getJson('beacons.json');
  for (var i = 0; i< jBeacon['beacons'].length; i++) {
  if (major == jBeacon['beacons'][i].major && minor == jBeacon['beacons'][i].minor){
        var strBeacon = jBeacon['beacons'][i];
      }
  } 
  return strBeacon;
}














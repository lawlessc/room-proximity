//
//  DetailsTableViewController.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/21/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "DetailsTableViewController.h"
#import "Beacon.h"
#import "Room.h"
#import "Booking.h"


@interface DetailsTableViewController ()
@property (nonatomic, strong) Beacon *ibeacon;
@property (nonatomic, strong) Room *room;
@property (nonatomic, strong) NSMutableArray *bookings;


@property (nonatomic, weak) NSMutableData *responseData;

@property (nonatomic, strong) NSMutableArray *timeSlots;

@property (nonatomic, strong) IBOutlet UIPickerView *pickerView;

@end

@implementation DetailsTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSString *majorString = self.beacon.major.stringValue;
    NSString *minorString = self.beacon.minor.stringValue;
    
    NSString* url = [NSString stringWithFormat:@"http://yuweixia.local:8888/%@/%@/redirect", majorString, minorString];
    NSURL *jsonURL = [NSURL URLWithString:url];
    NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];

    NSDictionary *twoObjects = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    NSDictionary *roomDetails = [twoObjects objectForKey:@"roomDetails"];
    NSDictionary *bookings = [twoObjects objectForKey:@"bookings"];
    
    self.room = [[Room alloc] initWithRoomDictionary:roomDetails];
    
    NSArray *bookingArray = [bookings objectForKey:@"bookings"];
    self.bookings = [[NSMutableArray alloc] init];
    for (NSDictionary *bookingJson in bookingArray) {
        Booking *booking = [[Booking alloc] initWithBookingDictionary:bookingJson];
        [self.bookings addObject:booking];
    }

    self.name.text = self.room.name;
    self.location.text = self.room.location;
    self.capacity.text = [NSString stringWithFormat:@"%d", self.room.capacity];
    self.phone.text = self.room.phoneNumber.stringValue;
    self.owner.text = self.room.owner;
    self.conference.text = (self.room.conferencing) ? @"Yes" : @"No";
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dismissTheInputView:)];
    
    [self.tableView addGestureRecognizer:tap];
    
    self.timeSlots = [[NSMutableArray alloc] initWithObjects:@"9-10", @"10-11", @"11-12", @"12-13", @"13-14", @"14-15", @"15-16", @"16-17", nil];
    
    self.timeTF.text = @"9-10";
    
    NSDate *today = [NSDate date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateString = [dateFormatter stringFromDate:today];
    self.dateTF.text = [NSString stringWithFormat:@"%@", dateString];
    [self getAvailableTimeSlotsForTheDate:dateString];
    
    UIDatePicker *datePicker = [[UIDatePicker alloc] init];
    datePicker.datePickerMode = UIDatePickerModeDate;
    datePicker.minuteInterval = 30;
    [datePicker addTarget:self action:@selector(updateTextField:) forControlEvents:UIControlEventValueChanged];
    [self.dateTF setInputView:datePicker];
    
    
    self.pickerView = [[UIPickerView alloc] init];
    self.pickerView.dataSource = self;
    self.pickerView.delegate = self;
    [self.timeTF setInputView:self.pickerView];

}

- (NSArray *)getAvailableTimeSlotsForTheDate: (NSString *)dateString {
    
    NSString *url = [NSString stringWithFormat:@"http://yuweixia.local:8888/rooms/%@/booking/%@", self.room.name, dateString];
    NSURL *jsonURL = [NSURL URLWithString:url];
    NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];
    
    NSArray *jsonDict = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    for (NSDictionary *bookingDict in jsonDict) {
        NSString *startTime = [bookingDict objectForKey:@"StartT"];
        NSString *endTime = [bookingDict objectForKey:@"EndT"];
        NSString *timeSlot = [NSString stringWithFormat:@"%@-%@", startTime, endTime];
        //NSLog(@"%@", timeSlot);
        [self.timeSlots removeObject:timeSlot];
    }
    
    return self.timeSlots;
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return self.timeSlots.count;
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [self.timeSlots objectAtIndex:row];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    //NSLog(@"selected row %d", row);
    self.timeTF.text = [self.timeSlots objectAtIndex:row];
}

-(void)updateTextField:(id)sender {
    UIDatePicker *datePicker = (UIDatePicker *)self.dateTF.inputView;
    NSDate *selectedDate = datePicker.date;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *newDate = [dateFormatter stringFromDate:selectedDate];
    self.dateTF.text = [NSString stringWithFormat:@"%@", newDate];
    [self getAvailableTimeSlotsForTheDate:newDate];
    [self.pickerView reloadAllComponents];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if (section == 0) {
        return 6;
    } else if (section == 1) {
        return 4;
    }
    return 0;
}

- (IBAction)saveTheBooking:(id)sender {
    if (![self.timeTF.text isEqualToString:@""] && ![self.meetingNameTF.text isEqualToString:@""] && ![self.usersTF.text isEqualToString:@""]) {
        NSString *urlString = [NSString stringWithFormat:@"http://yuweixia.local:8888/rooms/%@/booking", self.room.name];
        NSURL *url = [NSURL URLWithString:urlString];
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
        [request setHTTPMethod:@"POST"];
        NSString *date = self.dateTF.text;
        NSString *timeslot = self.timeTF.text;
        NSArray *timeArray = [timeslot componentsSeparatedByString:@"-"];
        NSString *startTime = timeArray[0];
        NSString *endTime = timeArray[1];
        NSString *meetingName = self.meetingNameTF.text;
        NSString *users = self.usersTF.text;
        NSString *bodyString = [NSString stringWithFormat:@"date=%@&startTime=%@&endTime=%@&meetingName=%@&users=%@",
                                date, startTime, endTime, meetingName, users];
        [request setHTTPBody:[bodyString dataUsingEncoding:NSUTF8StringEncoding]];
        
        NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
        
        if (connection) {
            NSLog(@"connecting......");
            self.responseData = [NSMutableData data];
        }
    } else {
        UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Hello" message:@"Please provide all booking information" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
        [message show];
    }
    
}

-(void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
}

-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [self.responseData appendData:data];
}

-(void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSString *responseString = [[NSString alloc] initWithData:self.responseData encoding:NSUTF8StringEncoding];
    
    NSLog(@"finished: %@", responseString);
    
    [self.navigationController popToRootViewControllerAnimated:YES];
}

- (void)dismissTheInputView:(id)sender {
    [self.tableView endEditing:YES];

}

@end

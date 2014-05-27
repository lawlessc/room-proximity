//
//  RoomDetailsViewController.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 4/9/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "RoomDetailsViewController.h"
#import "BookViewController.h"
#import "Beacon.h"
#import "Room.h"
#import "Booking.h"
#import "UIView+Toast.h"

@interface RoomDetailsViewController ()

@property (nonatomic, strong) Room *room;
@property (nonatomic, strong) NSMutableArray *bookings;
@property (nonatomic, strong) NSMutableData *responseData;

@end

@implementation RoomDetailsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

//- (UIStatusBarStyle)preferredStatusBarStyle {
//    return UIStatusBarStyleLightContent;
//}

- (void)viewDidLoad
{
    [super viewDidLoad];
    //[self setNeedsStatusBarAppearanceUpdate];
    NSString *majorString = self.beacon.major.stringValue;
    NSString *minorString = self.beacon.minor.stringValue;
    
    //self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    [self.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
    self.navigationController.navigationBar.shadowImage = [UIImage new];
    self.navigationController.navigationBar.translucent = YES;
    
    
    
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
    self.capacity.text = [NSString stringWithFormat:@"Fits %d", self.room.capacity];
    self.phone.text = [NSString stringWithFormat:@"Phone: %@", self.room.phoneNumber.stringValue];
    self.conferencing.text = [NSString stringWithFormat:@"Webcam: %@", (self.room.conferencing ? @"Yes" : @"No")];
    self.owner.text = [NSString stringWithFormat:@"Owned by %@", self.room.owner];
}

- (IBAction)occupyNow:(id)sender {
    NSString *urlString = [NSString stringWithFormat:@"http://yuweixia.local:8888/rooms/%@/booking", self.room.name];
    NSURL *url = [NSURL URLWithString:urlString];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    [request setHTTPMethod:@"POST"];
    
    NSDate *today = [NSDate date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateString = [dateFormatter stringFromDate:today];
    
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *components = [calendar components:NSHourCalendarUnit fromDate:today];
    NSInteger hour = [components hour];
    
    NSString *date = dateString;
    NSString *startTime = [NSString stringWithFormat:@"%d", hour];
    NSString *endTime = [NSString stringWithFormat:@"%d", (hour + 1)];
    NSString *meetingName = @"Kevin's meeting";
    NSString *users = @"Kevin";
    NSString *bodyString = [NSString stringWithFormat:@"date=%@&startTime=%@&endTime=%@&meetingName=%@&users=%@",
                            date, startTime, endTime, meetingName, users];
    [request setHTTPBody:[bodyString dataUsingEncoding:NSUTF8StringEncoding]];
    
    //    NSURLResponse *response;
    //    NSError *error;
    
    //NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    self.responseData = [NSMutableData dataWithCapacity:0];
    if (!connection) {
        NSLog(@"failed to connect......");
        self.responseData = nil;
    }

}

-(void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    NSLog(@"didReceiveResponse...");
}

-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    NSLog(@"didReceiveData %d", data.length);
    [self.responseData appendData:data];
}

-(void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSDictionary *responseDict = [NSJSONSerialization JSONObjectWithData:self.responseData options:0 error:nil];
    NSLog(@"results: %@", [responseDict objectForKey:@"result"]);
    
    if ([[responseDict objectForKey:@"result"] isEqualToString:@"OK"]) {
        [self.navigationController.view makeToast:@"Room occupied during that time!"
         duration:2.0 position:@"bottom"];
    }
}

-(void)modalViewDidDismiss {
    NSLog(@"Delegate method called");
    [self.navigationController.view makeToast:@"Room occupied during that time!"
                                     duration:2.0 position:@"bottom"];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"BookTheRoom"]) {
        NSLog(@"Book the room...");
        BookViewController *bookViewController = [segue destinationViewController];
        NSLog(@"1...");
        bookViewController.room = self.room;
        bookViewController.bookings = self.bookings;
        bookViewController.delegate = self;
        NSLog(@"2...");
    }
}

@end

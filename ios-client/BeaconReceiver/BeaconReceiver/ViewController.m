//
//  ViewController.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/19/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "ViewController.h"
#import "ESTBeaconManager.h"
#import "DetailsTableViewController.h"
#import "Room.h"

#import "Beacon.h"

@interface ViewController () <ESTBeaconManagerDelegate>

@property (strong, nonatomic) ESTBeacon *beacon;
@property (strong, nonatomic) NSArray *beacons;
@property (strong, nonatomic) ESTBeaconManager *beaconManager;
@property (strong, nonatomic) ESTBeaconRegion *beaconRegion;

@end

@interface ESTTableViewCell : UITableViewCell
@end

@implementation ESTTableViewCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:reuseIdentifier];
    if (self) {
        
    }
    return self;
}

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    
    //[self.tableView registerClass:[ESTTableViewCell class] forCellReuseIdentifier:@"CellIdentifier"];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    self.beaconManager = [[ESTBeaconManager alloc] init];
    self.beaconManager.delegate = self;
    
    self.beaconRegion = [[ESTBeaconRegion alloc] initWithProximityUUID:ESTIMOTE_PROXIMITY_UUID identifier:@"EstimoteSampleRegion"];
    [self.beaconManager startRangingBeaconsInRegion:self.beaconRegion];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.beaconManager stopRangingBeaconsInRegion:self.beaconRegion];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)beaconManager:(ESTBeaconManager *)manager didRangeBeacons:(NSArray *)beacons inRegion:(ESTBeaconRegion *)region {
//    ESTBeacon *beacon = [[ESTBeacon alloc] init];
//    beacon.proximityUUID = [[NSUUID alloc] initWithUUIDString:@"B9407F30-F5F8-466E-AFF9-25556B57FE6D"];
//    beacon.major = [[NSNumber alloc] initWithInt:555];
//    beacon.minor = [[NSNumber alloc] initWithInt:6789];
//    beacon.distance = [[NSNumber alloc] initWithFloat:2.15];
//    
//    NSMutableArray *mutableArray = [NSMutableArray new];
//    [mutableArray addObject:beacon];
//    self.beacons = [mutableArray mutableCopy];
    
    self.beacons = beacons;
    [self.tableView reloadData];
    //NSLog(@"%d", beacons.count);
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.beacons count];
    //return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    //ESTTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CellIdentifier" forIndexPath:indexPath];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CellIdentifier"];
    ESTBeacon *beacon = [self.beacons objectAtIndex:indexPath.row];
    NSString *majorString = beacon.major.stringValue;
    NSString *minorString = beacon.minor.stringValue;
    
    
    
    NSString* beaconURL = [NSString stringWithFormat:@"http://yuweixia.local:8888/%@/%@", majorString, minorString];
    
    NSURL *jsonBeaconURL = [NSURL URLWithString:beaconURL];
    NSData *jsonBeaconData = [NSData dataWithContentsOfURL:jsonBeaconURL];
    
    NSDictionary *beaconObject = [NSJSONSerialization JSONObjectWithData:jsonBeaconData options:0 error:nil];
    
    Beacon *ibeacon = [[Beacon alloc] initWithBeaconDictionary:beaconObject];

    NSString* roomURL = [NSString stringWithFormat:@"http://yuweixia.local:8888/rooms/%@", ibeacon.room];
    
    NSURL *jsonRoomURL = [NSURL URLWithString:roomURL];
    NSData *jsonRoomData = [NSData dataWithContentsOfURL:jsonRoomURL];
    
    NSDictionary *twoObjects = [NSJSONSerialization JSONObjectWithData:jsonRoomData options:0 error:nil];
    
    NSDictionary *roomDetails = [twoObjects objectForKey:@"roomDetails"];
    //NSDictionary *bookings = [twoObjects objectForKey:@"bookings"];
    NSString *available = [twoObjects objectForKey:@"available"];

    Room *room = [[Room alloc] initWithRoomDictionary:roomDetails];
    
    cell.textLabel.textColor = [UIColor whiteColor];
    cell.detailTextLabel.textColor = [UIColor whiteColor];
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@", room.name];
    cell.detailTextLabel.text = [NSString stringWithFormat:@"%@", available];
    
    if ([ibeacon.color isEqual: @"darkblue"]) {
        cell.backgroundColor = [UIColor colorWithRed:145.0/255.0 green:188.0/255.0 blue:161.0/255.0 alpha:1.0];
    } else if ([ibeacon.color isEqual:@"green"]) {
        cell.backgroundColor = [UIColor colorWithRed:43.0/255.0 green:42.0/255.0 blue:83.0/255.0 alpha:1.0];

    } else if ([ibeacon.color isEqual:@"blue"]) {
        cell.backgroundColor = [UIColor colorWithRed:99.0/255.0 green:181.0/255.0 blue:218.0/255.0 alpha:1.0];

    }
    
//    cell.textLabel.text = [NSString stringWithFormat:@"Major: %@, Minor: %@", beacon.major, beacon.minor];
//    cell.detailTextLabel.text = [NSString stringWithFormat:@"Distance: %.2f", [beacon.distance floatValue]];
    
    return cell;
}

//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
//    ESTBeacon *selectedBeacon = [self.beacons objectAtIndex:indexPath.row];
//    
//    
//}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"ShowDetails"]) {
        NSLog(@"go to detail...");
        DetailsTableViewController *detailTableViewController = [segue destinationViewController];
        
        detailTableViewController.beacon = [self.beacons objectAtIndex:[self.tableView indexPathForSelectedRow].row];
    }
}

@end

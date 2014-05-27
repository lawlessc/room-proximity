//
//  ViewController.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/19/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "ViewController.h"
#import "RoomDetailsViewController.h"
#import "ESTBeaconManager.h"
#import "Room.h"

#import "Beacon.h"

@interface ViewController () <ESTBeaconManagerDelegate>

@property (strong, nonatomic) ESTBeacon *beacon;

//@property (strong, nonatomic) NSArray *beacons;
@property (strong, nonatomic) NSMutableArray *beacons;
@property (strong, nonatomic) ESTBeaconManager *beaconManager;
@property (strong, nonatomic) ESTBeaconRegion *beaconRegion;

//for keep the mac address of detected beacons, mac address is null, use minor
@property (strong, nonatomic) NSMutableArray *minors;

//@property (strong, nonatomic) UIButton *detectButton;

@end

@interface ESTTableViewCell : UITableViewCell
@end

@implementation ESTTableViewCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:reuseIdentifier];
    if (self) {
        
    }
    return self;
}

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.separatorColor = [UIColor clearColor];
    
    self.beacons = [NSMutableArray array];
    self.minors = [NSMutableArray array];
    
    self.navigationController.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName: [self.tableView tintColor]};
    
    
    UIImageView *tempImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"vetch.png"]];
//    [tempImageView setFrame:self.tableView.frame];
    self.tableView.backgroundView = tempImageView;
    
    [self.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
    self.navigationController.navigationBar.shadowImage = [UIImage new];
    self.navigationController.navigationBar.translucent = YES;
    
    //[self.tableView registerClass:[ESTTableViewCell class] forCellReuseIdentifier:@"CellIdentifier"];
    
    UIButton *detectButton = [UIButton buttonWithType:UIButtonTypeCustom];
    detectButton.frame = CGRectMake(65, 385, 190, 50);
    [detectButton setTitle:@"Rescan" forState:UIControlStateNormal];
    [detectButton addTarget:self action:@selector(DetectBeacons:) forControlEvents:UIControlEventTouchUpInside];
    [detectButton setTitleColor:[UIColor colorWithRed:255.0/255.0 green:83.0/255.0 blue:13.0/255.0 alpha:1.0] forState:UIControlStateNormal];
    [detectButton.layer setBorderColor:[[UIColor whiteColor] CGColor]];
    detectButton.titleLabel.font = [UIFont fontWithName:@"Verdana" size:32];
    [self.tableView addSubview:detectButton];
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];

    self.beaconManager = [[ESTBeaconManager alloc] init];
    self.beaconManager.delegate = self;
    
    self.beaconRegion = [[ESTBeaconRegion alloc] initWithProximityUUID:ESTIMOTE_PROXIMITY_UUID identifier:@"EstimoteSampleRegion"];
    NSLog(@"---------- start range");
    [self.beaconManager startRangingBeaconsInRegion:self.beaconRegion];
    NSLog(@"----------");
}
- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:YES];
    NSLog(@"---------- will Appear");
//    self.beacons = [[self dummyBeacons] mutableCopy];
//    [self.tableView reloadData];
//    [self.beacons removeAllObjects];
//    [self.minors removeAllObjects];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.beaconManager stopRangingBeaconsInRegion:self.beaconRegion];
}

- (NSMutableArray *)dummyBeacons {
    NSMutableArray *mutableArray = [NSMutableArray new];
    ESTBeacon *beacon = [[ESTBeacon alloc] init];
    beacon.proximityUUID = [[NSUUID alloc] initWithUUIDString:@"B9407F30-F5F8-466E-AFF9-25556B57FE6D"];
    beacon.major = [[NSNumber alloc] initWithInt:666];
    beacon.minor = [[NSNumber alloc] initWithInt:49567];
    beacon.distance = [[NSNumber alloc] initWithFloat:2.15];

    [mutableArray addObject:beacon];
    
    ESTBeacon *beacon1 = [[ESTBeacon alloc] init];
    beacon.proximityUUID = [[NSUUID alloc] initWithUUIDString:@"B9407F30-F5F8-466E-AFF9-25556B57FE6D"];
    beacon1.major = [[NSNumber alloc] initWithInt:55149];
    beacon1.minor = [[NSNumber alloc] initWithInt:5016];
    beacon1.distance = [[NSNumber alloc] initWithFloat:4.50];
    [mutableArray addObject:beacon1];
    
    ESTBeacon *beacon2 = [[ESTBeacon alloc] init];
    beacon.proximityUUID = [[NSUUID alloc] initWithUUIDString:@"B9407F30-F5F8-466E-AFF9-25556B57FE6D"];
    beacon2.major = [[NSNumber alloc] initWithInt:24723];
    beacon2.minor = [[NSNumber alloc] initWithInt:63838];
    beacon2.distance = [[NSNumber alloc] initWithFloat:7.30];
    [mutableArray addObject:beacon2];
    
    return mutableArray;
}

- (IBAction)DetectBeacons:(id)sender {
    self.beacons = [[self dummyBeacons] mutableCopy];
    
    [self.tableView reloadData];
    NSLog(@"%d", self.beacons.count);
}

- (void)beaconManager:(ESTBeaconManager *)manager didRangeBeacons:(NSArray *)beacons inRegion:(ESTBeaconRegion *)region {
    NSLog(@"---------- didRange");

//    for (ESTBeacon *detectedbeacon in beacons) {
//        if (![self.minors containsObject:detectedbeacon.minor.stringValue]) {
//            [self.minors addObject:detectedbeacon.minor.stringValue];
//            [self.beacons addObject:detectedbeacon];
//            [self.tableView reloadData];
//        }
//    }
    
    
    //self.beacons = beacons;
    //[self.tableView reloadData];
    //NSLog(@"%d", beacons.count);
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.beacons count];
    //return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80.0f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//    ESTTableViewCell *cell = (ESTTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"CellIdentifier" forIndexPath:indexPath];
    
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
    
    UIImageView *imageView = (UIImageView *)[cell viewWithTag:100];
    if ([available isEqualToString:@"available"]) {
        imageView.image = [UIImage imageNamed:@"green_dot.png"];
    } else {
        imageView.image = [UIImage imageNamed:@"red_dot.png"];
    }
    
    UILabel *label = (UILabel *)[cell viewWithTag:101];
    label.text = room.name;
    label.font = [UIFont systemFontOfSize:36];
    label.textColor = [UIColor whiteColor];
    
    if ([ibeacon.color isEqual: @"darkblue"]) {
        cell.backgroundColor = [UIColor colorWithRed:145.0/255.0 green:188.0/255.0 blue:161.0/255.0 alpha:1.0];
    } else if ([ibeacon.color isEqual:@"green"]) {
        cell.backgroundColor = [UIColor colorWithRed:43.0/255.0 green:42.0/255.0 blue:83.0/255.0 alpha:1.0];

    } else if ([ibeacon.color isEqual:@"blue"]) {
        cell.backgroundColor = [UIColor colorWithRed:99.0/255.0 green:181.0/255.0 blue:218.0/255.0 alpha:1.0];
    }
    
    return cell;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"ShowDetails"]) {
        NSLog(@"go to detail...");
        RoomDetailsViewController *roomDetailsViewController = [segue destinationViewController];
        
        roomDetailsViewController.beacon = [self.beacons objectAtIndex:[self.tableView indexPathForSelectedRow].row];
    }
}

@end

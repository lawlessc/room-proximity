//
//  DetailsTableViewController.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/21/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ESTBeaconManager.h"

@interface DetailsTableViewController : UITableViewController <NSURLConnectionDelegate, UIPickerViewDataSource, UIPickerViewDelegate>

@property (strong, nonatomic) ESTBeacon *beacon;
@property (strong, nonatomic) IBOutlet UILabel *name;
@property (strong, nonatomic) IBOutlet UILabel *location;
@property (strong, nonatomic) IBOutlet UILabel *capacity;
@property (strong, nonatomic) IBOutlet UILabel *phone;
@property (strong, nonatomic) IBOutlet UILabel *owner;
@property (strong, nonatomic) IBOutlet UILabel *conference;


@property (strong, nonatomic) IBOutlet UITextField *timeTF;
@property (strong, nonatomic) IBOutlet UITextField *dateTF;
@property (strong, nonatomic) IBOutlet UITextField *meetingNameTF;
@property (strong, nonatomic) IBOutlet UITextField *usersTF;

@end

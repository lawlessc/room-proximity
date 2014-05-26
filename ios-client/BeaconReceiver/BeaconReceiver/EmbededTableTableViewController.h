//
//  EmbededTableTableViewController.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 4/9/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Room.h"

@interface EmbededTableTableViewController : UITableViewController <UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource>

@property (strong, nonatomic) Room *room;
@property (strong, nonatomic) NSArray *bookings;
@property (strong, nonatomic) IBOutlet UITextField *meetingNameTF;
@property (strong, nonatomic) IBOutlet UITextField *usersTF;
@property (strong, nonatomic) IBOutlet UILabel *dateLabel;
@property (strong, nonatomic) IBOutlet UITableViewCell *datePickerCell;
@property (strong, nonatomic) IBOutlet UIDatePicker *datePicker;
@property (strong, nonatomic) IBOutlet UILabel *timeLabel;
@property (strong, nonatomic) IBOutlet UITableViewCell *timePickerCell;
@property (strong, nonatomic) IBOutlet UIPickerView *timePicker;

@end

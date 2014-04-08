//
//  BookViewController.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 4/4/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Room.h"
#import "Booking.h"

@interface BookViewController : UIViewController <NSURLConnectionDelegate, NSURLConnectionDataDelegate, UIPickerViewDataSource, UIPickerViewDelegate>

@property (strong, nonatomic) Room *room;
@property (strong, nonatomic) NSArray *bookings;
@property (strong, nonatomic) IBOutlet UITextField *timeTF;
@property (strong, nonatomic) IBOutlet UITextField *dateTF;
@property (strong, nonatomic) IBOutlet UITextField *meetingNameTF;
@property (strong, nonatomic) IBOutlet UITextField *usersTF;

@end

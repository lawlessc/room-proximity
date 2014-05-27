//
//  RoomDetailsViewController.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 4/9/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ESTBeaconManager.h"
#import "BookViewController.h"

@interface RoomDetailsViewController : UIViewController <NSURLConnectionDelegate, NSURLConnectionDataDelegate, ModalViewDismissedNotification>

@property (strong, nonatomic) ESTBeacon *beacon;
@property (strong, nonatomic) IBOutlet UILabel *name;
@property (strong, nonatomic) IBOutlet UILabel *location;
@property (strong, nonatomic) IBOutlet UILabel *capacity;
@property (strong, nonatomic) IBOutlet UILabel *phone;
@property (strong, nonatomic) IBOutlet UILabel *conferencing;
//@property (strong, nonatomic) IBOutlet UIButton *cardButton;
@property (strong, nonatomic) IBOutlet UILabel *owner;

@end


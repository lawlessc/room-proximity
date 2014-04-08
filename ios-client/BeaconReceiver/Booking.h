//
//  Booking.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "User.h"

@interface Booking : NSObject

@property (nonatomic, strong) NSString *URI;
@property (nonatomic, strong) NSString *date; //maybe change to NSDate
@property (nonatomic, strong) NSString *startTime; //maybe change to NSDate
@property (nonatomic, strong) NSString *endTime; //maybe change to NSDate
@property (nonatomic, strong) NSString *meetingName;
@property (nonatomic, strong) NSString *user;

-(id)initWithBookingURL: (NSString *)bookingJsonObjectURL;

-(id)initWithBookingDictionary: (NSDictionary *)jsonDictionary;

@end

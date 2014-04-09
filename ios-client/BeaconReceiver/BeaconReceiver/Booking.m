//
//  Booking.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "Booking.h"

@implementation Booking

-(id)initWithBookingURL: (NSString *)bookingJsonObjectURL {
    NSURL *jsonURL = [NSURL URLWithString:bookingJsonObjectURL];
    NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];
    
    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    self.URI = [jsonDictionary objectForKey:@"URI"];
    self.date = [jsonDictionary objectForKey:@"date"];
    self.startTime = [jsonDictionary objectForKey:@"StartT"];
    self.endTime = [jsonDictionary objectForKey:@"EndT"];
    self.meetingName = [jsonDictionary objectForKey:@"MeetingName"];
    
    //NSString *userURL = [jsonDictionary objectForKey:@"Users"]; //may change here
    
    self.user = [jsonDictionary objectForKey:@"Users"];
    
    return self;
}


-(id)initWithBookingDictionary: (NSDictionary *)jsonDictionary {
    self.URI = [jsonDictionary objectForKey:@"URI"];
    self.date = [jsonDictionary objectForKey:@"date"];
    self.startTime = [jsonDictionary objectForKey:@"StartT"];
    self.endTime = [jsonDictionary objectForKey:@"EndT"];
    self.meetingName = [jsonDictionary objectForKey:@"MeetingName"];
    
    //NSString *userURL = [jsonDictionary objectForKey:@"Users"]; //may change here
    
    self.user = [jsonDictionary objectForKey:@"Users"];
    
    return self;
}

@end

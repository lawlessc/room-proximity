//
//  Beacon.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ESTBeacon.h"

@interface Beacon : NSObject //may change to extension of CLBeacon or ESTBeacon

@property (nonatomic, strong) NSString *locationURI;
@property (nonatomic, strong) NSUUID *UUID;
@property (nonatomic, strong) NSNumber *major;
@property (nonatomic, strong) NSNumber *minor;
@property (nonatomic, strong) NSString *room;
@property (nonatomic, strong) NSString *color;

-(id)initWithBeaconURL: (NSString *)beaconJsonObjectURL;

-(id)initWithBeaconDictionary: (NSDictionary *)jsonDictionary;

@end

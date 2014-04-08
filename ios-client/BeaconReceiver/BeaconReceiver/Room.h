//
//  Room.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Room : NSObject

@property (nonatomic, strong) NSString *URI;
@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSString *location;
@property (nonatomic, assign) NSInteger capacity;
@property (nonatomic, strong) NSNumber *phoneNumber;
@property (nonatomic, strong) NSString *owner;
@property (nonatomic, assign) BOOL conferencing;

-(id)initWithRoomURL: (NSString *)roomJsonObjectURL;

-(id)initWithRoomDictionary: (NSDictionary *)jsonDictionary;

@end

//
//  Beacon.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "Beacon.h"

@implementation Beacon

-(id)initWithBeaconURL: (NSString *)beaconJsonObjectURL {
    NSURL *jsonURL = [NSURL URLWithString:beaconJsonObjectURL];
    NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];
    
    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    self.locationURI = [jsonDictionary objectForKey:@"uri"];
    NSString *uuidString = [jsonDictionary objectForKey:@"uuid"];
    self.UUID = [[NSUUID alloc] initWithUUIDString: uuidString];
    self.major = [[NSNumber alloc] initWithInteger:[[jsonDictionary objectForKey:@"major"] integerValue]];
    self.minor = [[NSNumber alloc] initWithInteger:[[jsonDictionary objectForKey:@"minor"] integerValue]];
    self.room = [jsonDictionary objectForKey:@"room"];
    self.color = [jsonDictionary objectForKey:@"colour"];

    return self;
}

-(id)initWithBeaconDictionary: (NSDictionary *)jsonDictionary {
    self.locationURI = [jsonDictionary objectForKey:@"uri"];
    NSString *uuidString = [jsonDictionary objectForKey:@"uuid"];
    self.UUID = [[NSUUID alloc] initWithUUIDString: uuidString];
    self.major = [[NSNumber alloc] initWithInteger:[[jsonDictionary objectForKey:@"major"] integerValue]];
    self.minor = [[NSNumber alloc] initWithInteger:[[jsonDictionary objectForKey:@"minor"] integerValue]];
    self.room = [jsonDictionary objectForKey:@"room"];
    self.color = [jsonDictionary objectForKey:@"colour"];
    
    return self;
}

@end

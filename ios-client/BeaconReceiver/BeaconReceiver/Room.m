//
//  Room.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "Room.h"

@implementation Room

-(id)initWithRoomURL:(NSString *)roomJsonObjectURL {
    NSURL *jsonURL = [NSURL URLWithString:roomJsonObjectURL];
    NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];
    
    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    self.URI = [jsonDictionary objectForKey:@"uri"];
    self.name = [jsonDictionary objectForKey:@"name"];
    self.location = [jsonDictionary objectForKey:@"location"];
    self.capacity = [[jsonDictionary objectForKey:@"capacity"] integerValue];
    self.phoneNumber = [[NSNumber alloc] initWithInteger:[[jsonDictionary objectForKey:@"phone"] integerValue]];
    self.owner = [jsonDictionary objectForKey:@"owner"];
    self.conferencing = [[jsonDictionary objectForKey:@"conferencing"] boolValue];
    
    
    return self;
}

-(id)initWithRoomDictionary: (NSDictionary *)jsonDictionary {
//    NSLog(@" in here");
//    
//    NSLog(@"-------- %@ ", [[NSString alloc] initWithData:roomJsonObjectData encoding:NSUTF8StringEncoding]);
//    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:roomJsonObjectData options:0 error:nil];
//    
//    NSLog(@"%@", jsonDictionary.count);
    
//    for (NSString *key in jsonDictionary.allKeys) {
//        NSLog(@"** %@ : %@", key, [jsonDictionary objectForKey:key]);
//    }
    
    self.URI = [jsonDictionary objectForKey:@"uri"];
    self.name = [jsonDictionary objectForKey:@"name"];
    self.location = [jsonDictionary objectForKey:@"location"];
    self.capacity = [[jsonDictionary objectForKey:@"capacity"] integerValue];
    self.phoneNumber = [[NSNumber alloc] initWithInteger:[[jsonDictionary objectForKey:@"phone"] integerValue]];
    self.owner = [jsonDictionary objectForKey:@"owner"];
    self.conferencing = [[jsonDictionary objectForKey:@"conferencing"] boolValue];
    
    
    return self;
}


@end

//
//  User.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "User.h"

@implementation User

-(id)initWithUserURL: (NSString *)userJsonObjectURL {
    NSURL *jsonURL = [NSURL URLWithString:userJsonObjectURL];
    NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];
    
    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    self.URI = [jsonDictionary objectForKey:@"URI"];
    self.SOEID = [jsonDictionary objectForKey:@"SOEID"];
    self.email = [jsonDictionary objectForKey:@"Email"];
    self.role = [jsonDictionary objectForKey:@"Role"];

    
    return self;
}

@end

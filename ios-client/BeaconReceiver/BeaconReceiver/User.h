//
//  User.h
//  BeaconReceiver
//
//  Created by Yuwei Xia on 3/31/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface User : NSObject

-(id)initWithUserURL: (NSString *)userJsonObjectURL;

@property (nonatomic, strong) NSString *URI;
@property (nonatomic, strong) NSString *SOEID;
@property (nonatomic, strong) NSString *email;
@property (nonatomic, strong) NSString *role;

@end

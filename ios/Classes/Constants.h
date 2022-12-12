//
//  Constants.h
//  UVX
//
//  Created by Chu Si Nguyen on 8/20/15.
//  Copyright (c) 2015 csnguyen. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef enum {
    ENGLISH_US     = 0,
    JAPANESE    = 1,
    SIMPLIFIED_CHINESE = 2,
    TRADITIONAL_CHINESE= 3,
    KOREAN      = 4,
    FRENCH      = 5,
    GERMAN      = 6,
    SPANISH     = 7,
    ITALIAN     = 8,
    PORTUGUESE  = 9,
    RUSSIAN     = 10,
    THAI        = 11,
    INDONESIAN  = 12,
    ARABIC      = 13
} LanguageSetting;

@interface Constants : NSObject

@property (strong, nonatomic, readonly) NSArray* langs;
@property (strong, nonatomic, readonly) NSArray* thicks;
@property (strong, nonatomic, readonly) NSArray* langTags;

+ (instancetype)sharedInstance;
- (NSString*)webTextFormat;
- (LanguageSetting)langCodeFrom:(NSString*)rawCode;

@end

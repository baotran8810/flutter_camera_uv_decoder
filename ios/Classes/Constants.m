//
//  Constants.m
//  UVX
//
//  Created by Chu Si Nguyen on 8/20/15.
//  Copyright (c) 2015 csnguyen. All rights reserved.
//

#import "Constants.h"

@implementation Constants

@synthesize langs;
@synthesize thicks;
@synthesize langTags;

+ (instancetype)sharedInstance {
    static Constants *sharedConstants;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedConstants = [[self alloc] init];
    });
    return sharedConstants;
}

- (NSArray*)langs {
    if (!langs) {
        langs = @[NSLocalizedString(@"EnglishUS", nil),
                  NSLocalizedString(@"Japanese", nil),
                  NSLocalizedString(@"ChineseSimplified", nil),
                  NSLocalizedString(@"ChineseTrandition", nil),
                  NSLocalizedString(@"Korean", nil),
                  NSLocalizedString(@"French", nil),
                  NSLocalizedString(@"German", nil),
                  NSLocalizedString(@"Spanish", nil),
                  NSLocalizedString(@"Italian", nil),
                  NSLocalizedString(@"Portuguese", nil),
                  NSLocalizedString(@"Russian", nil),
                  NSLocalizedString(@"Thai", nil),
                  NSLocalizedString(@"Indonesian", nil),
                  NSLocalizedString(@"Arabic", nil),
                  NSLocalizedString(@"Dutch", nil),
                  NSLocalizedString(@"Hindi", nil),
        ];
    }
    return langs;
}

- (NSArray*)thicks {
    if (!thicks) {
        thicks = @[@(5), @(8), @(11)];
    }
    return thicks;
}

- (NSArray*)langTags {
    if (!langTags) {
        langTags = @[@"eng",@"jpn",@"chi",@"zho",@"kor",@"fre",@"ger",@"spa",@"ita",@"por",@"rus",@"tai",@"ind",@"ara",@"dut",@"hin"];
    }
    return langTags;
}

- (LanguageSetting)langCodeFrom:(NSString*)rawCode {
    if ([rawCode isEqualToString:@"0"]) {
        return ENGLISH_US;
    } else if ([rawCode isEqualToString:@"1"]) {
        return JAPANESE;
    } else if ([rawCode isEqualToString:@"2"]) {
        return SIMPLIFIED_CHINESE;
    } else if ([rawCode isEqualToString:@"3"]) {
        return TRADITIONAL_CHINESE;
    } else if ([rawCode isEqualToString:@"4"]) {
        return KOREAN;
    } else if ([rawCode isEqualToString:@"5"]) {
        return FRENCH;
    } else if ([rawCode isEqualToString:@"6"]) {
        return GERMAN;
    } else if ([rawCode isEqualToString:@"7"]) {
        return SPANISH;
    } else if ([rawCode isEqualToString:@"8"]) {
        return ITALIAN;
    } else if ([rawCode isEqualToString:@"9"]) {
        return PORTUGUESE;
    } else if ([rawCode isEqualToString:@"10"]) {
        return RUSSIAN;
    } else if ([rawCode isEqualToString:@"11"]) {
        return THAI;
    } else if ([rawCode isEqualToString:@"12"]) {
        return INDONESIAN;
    } else if ([rawCode isEqualToString:@"13"]) {
        return ARABIC;
    }
    return JAPANESE;
}

- (NSString*)webTextFormat {
    return @"<div style='font-size:%ldpx; font-weight:%@'>%@</div>";
}

@end

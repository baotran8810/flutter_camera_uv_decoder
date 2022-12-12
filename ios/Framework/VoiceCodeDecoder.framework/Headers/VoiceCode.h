//
//  VoiceCode.h
//  VoiceCodeDecoder
//
//  Created by IVC on 8/25/15.
//  Copyright (c) 2015 IVC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSDataEx.h"
#import "R9HTTPRequest.h"

@interface VoiceCode : NSObject

/**
 ValidCode is an enumeration which indicates this voice code belongs to Uni-Voice corporation or not.
 */
typedef enum {
    UNI_VOICE, NOT_UNI_VOICE
} ValidCode;

/**
 CodeVersion is an emumeration which tells us that the version of voice code is 1.0 or 2.0. If the version is 1.0, you can decode body content with only one character set which is SHIFT_JIS or UTF-8. If the version is 2.0, the voice code may be created with more than one character sets.
 */
typedef enum {
    VERSION_1_0 = 3,
    VERSION_2_0 = 4
} CodeVersion;

typedef void (^RequestCompleted)(void);
typedef void (^RequestFailed)(NSString *);

@property (nonatomic) NSString *header;
@property (nonatomic) const char *bodyContent;
@property (nonatomic) ValidCode validCode;
@property (nonatomic) CodeVersion codeVersion;
@property (nonatomic) NSString *onlineData;
@property (nonatomic) NSMutableArray *urlExtends;

/**
 *  Constructor of VoiceCode
 *
 *  @param header      header of voice code which contains id of content in translation server. In case of header is not null, this voice code can be used in online mode. In case of header is null, this voice code must be only used in offline mode
 *  @param bodyContent byte array from result after decoding voice code
 *  @param validCode   check voice code which created by Uni-Voice or not
 *  @param codeVersion version of voice code
 *
 *  @return id
 */
- (id)initWithHeader:(NSString *)header bodyContent:(const char *)bodyContent validCode:(ValidCode)validCode codeVersion:(CodeVersion)codeVersion;

/**
 *  Get online data if VoiceCode contains header (header is not null)
 *
 *  @param language     specified language used for requesting to server
 *  @param doAction     do completion block in case of request completed
 *  @param handleFail   do this block in case of request failed
 *
 */
- (void)getOnlineDataWithLanguage:(NSString *)language withCompletionBlock:(RequestCompleted)doAction failureBlock:(RequestFailed)handleFail;

@end
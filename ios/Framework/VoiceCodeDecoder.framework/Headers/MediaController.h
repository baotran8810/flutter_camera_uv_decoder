//
//  MediaController.h
//  VoiceCodeDecoder
//
//  Created by IVC on 8/25/15.
//  Copyright (c) 2015 IVC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>

// Recognizing strings for keeping data
#define UNI_VOICE_KEEPING_DATA_KEY @"UNI_VOICE_KEEPING_DATA_KEY_%d"

/**
 All of languages are supported by application
 */
typedef enum {
    LANG_JPN = 0,
    LANG_ENG = 1,
    LANG_CHI = 2,
    LANG_ZHO = 3,
    LANG_KOR = 4,
    LANG_UNKNOWN = 5,
    //[04.2016] Additional languages
    LANG_EGB = 6,
    LANG_FRE = 7,
    LANG_GER = 8,
    LANG_SPA = 9,
    LANG_ITA = 10,
    LANG_POR = 11,
    LANG_RUS = 12,
    LANG_TAI = 13,
    LANG_IND = 14,
    LANG_ARA = 15,
    // Added  [03.2021]
    LANG_HIN = 16,
    LANG_DUT = 17,
    LANG_MAY = 18,
    LANG_VIE = 19
} Language;

@protocol MediaControllerDelegate <NSObject>

/**
 *  A listener called after reading ended
 */
- (void)didFinishReading;

@end

@interface MediaController : NSObject <AVSpeechSynthesizerDelegate>
{
    NSInteger readingIndex;
    NSMutableArray* readingArray;
    float rate;
    
    Language myLanguage;
    AVSpeechSynthesizer *mySynthesizer;
    AVSpeechUtterance *myUtterance;
    NSCondition *readingCondition;
}

@property (nonatomic, weak) id<MediaControllerDelegate> delegate;

/**
 *  Constructor of MediaPlayer
 *
 *  @param readingText text which need to be read out
 *  @param language    select language which must be suitable with language in text
 *
 *  @return id
 */
- (id)initWithText:(NSString *)readingText language:(Language)language;

/**
 * Start playing
 */
- (void)start;

/**
 * Continue playing
 */
- (void)play;

/**
*  Stop playing
*/
- (void)stop;

/**
 *  Pause playing
 */
- (void)pause;

/**
 *  Read again at first sentence
 */
- (void)backToFirstSentence;

/**
 *  Go back to the previous sentence
 */
- (void)backToPreviousSentence;

/**
 *  Go forward to the next sentence
 */
- (void)goToNextSentence;

/**
 *  Set speed value of voice speech
 *
 *  @param speed speed value of voice speech
 */
- (void)setSpeed:(float)speed;

/**
 *  Convert IOS 639-2 language code to Language enum
 *
 *  @param ISO639_2Code language code in IOS 639-2 standard
 *
 *  @return Language enum
 */
+ (Language)convertToLanguageWith:(NSString *)ISO639_2Code;

/**
 *  Check before init speech utterance object
 */
+ (BOOL)isTTsSupportLanguage:(Language)language;

@end

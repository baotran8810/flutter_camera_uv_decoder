//
//  BufferDecoder.h
//  camera
//
//  Created by PT on 12/06/2021.
//

#import <Flutter/Flutter.h>
#import <VoiceCodeDecoder/datatypes.h>
#import <VoiceCodeDecoder/DecoderViewController.h>
#import <VoiceCodeDecoder/VoiceCodeDecoder.h>
#import <VoiceCodeDecoder/OpenCVHelper.h>
#import "FileItem.h"

NS_ASSUME_NONNULL_BEGIN

@protocol BufferDecoderDelegate
- (void)didOutputResult:(FileItem*)item;
@optional
- (void)didDetectedNotUVCode;
@end

@interface BufferDecoder : NSObject
@property (strong, nonatomic) VoiceCode *voiceCode;
@property (weak, nonatomic) id<BufferDecoderDelegate> delegate;
- (NSDictionary*)scanVoiceCode:(CMSampleBufferRef)sampleBuffer;
@end

NS_ASSUME_NONNULL_END

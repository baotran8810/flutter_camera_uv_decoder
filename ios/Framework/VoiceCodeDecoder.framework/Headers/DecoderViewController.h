//
//  DecoderView.h
//  VoiceCodeDecoder
//
//  Created by IVC on 8/25/15.
//  Copyright (c) 2015 IVC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import <sys/sysctl.h>
#import "VoiceCodeDecoder.h"

@protocol DecodeViewControllerDelegate <NSObject>

/**
 *  A listener called after decoding voice code successfully
 *
 *  @param voiceCode result received after decoding successfully
 */
- (void)didDecodeWithVoiceCode:(VoiceCode *)voiceCode;

@end

@interface DecoderViewController : UIViewController <UINavigationControllerDelegate, UIImagePickerControllerDelegate, AVCaptureVideoDataOutputSampleBufferDelegate> {
    UIImageView *overlayImageView;
    VoiceCodeDecoder *decoder;
    AVCaptureSession *session;
    AVCaptureVideoPreviewLayer *previewLayer;
    // UPDATE : Support drawing square code frame
    NSTimer *animationTimer;
    CALayer *codeFrameLayer;
//    UIView *maskView;
}

@property (nonatomic, weak) id<DecodeViewControllerDelegate> delegate;
@property long LUX;
@property int TH;

@property BOOL fullScreenMode;

@end

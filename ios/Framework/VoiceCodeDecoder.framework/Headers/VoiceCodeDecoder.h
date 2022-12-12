//
//  VoiceCodeDecoder.h
//  VoiceCodeDecoder
//
//  Created by IVC on 8/25/15.
//  Copyright (c) 2015 IVC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VoiceCode.h"

#define NEW_DEC
#define NEW_DEC_WITH_ATTR

#define NEW_DEC_WITH_ATTR_DEBUG
#include "bmp_data.h"       // grape
#include "uv_decode_sym.h"    // grape
#include "uv_sym_param.h"   // grape

#ifdef NEW_DEC      // grape
#include "bmp_data.h"       // grape
#include "uv_decode_sym.h"    // grape
#include "uv_sym_param.h"   // grape
#include "uv_text_body_hdr.h"   // grape
#endif

@interface VoiceCodeDecoder : NSObject

/**
 *  Definition of completion block to do something after decoding successfully
 *
 *  @param VoiceCode result of decoding voice code
 */
typedef void (^DecodeComplete)(VoiceCode *);

/**
 *  Decode voice code using Javis library
 *
 *  @param bufferImage image buffer which contains voice code. Please refer below code for understanding how to get bufferImage
 *  @code
 - (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputSampleBuffer
 :(CMSampleBufferRef)sampleBuffer fromConnection
 :(AVCaptureConnection *)connection
 {
	// Get a CMSampleBuffer's Core Video image buffer for the media data
	CVImageBufferRef imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer);
	// Lock the base address of the pixel buffer
	CVPixelBufferLockBaseAddress(imageBuffer, 0);
	// Get image buffer in Plane Y. Format type of this buffer is grayscale 1-channel
	unsigned char *buffer =
 (unsigned char *) CVPixelBufferGetBaseAddressOfPlane(imageBuffer, 0);
	int width = (int)CVPixelBufferGetWidth(imageBuffer);
	int height = (int)CVPixelBufferGetHeight(imageBuffer);
 
    //TODO: do your code with buffer
 }
 *  @endcode
 *  @param width       width of image
 *  @param height      height of image
 *  @param threshold   value for optimizing image buffer. If value of a element is greater than threshold, it becomes 255. Otherwise, it becomes 0
 *  @param doAction    do any stuff with voice code after decoding successfully
 *
 *  @return if decode successfully, return 0
 */
- (int)decodeBuffer:(unsigned char *)bufferImage width:(int)width height:(int)height threshold:(int)threshold withCompletionBlock:(DecodeComplete)doAction;

@end

//
//  OpenCVHelper.h
//  Uni-Voice
//  Khanh fix detect edge
//  Created by IVC on 12/18/14.
//  Copyright (c) 2014 U.Kenkichi. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <iostream>
#include <math.h>
#include <string.h>
#import <AVFoundation/AVFoundation.h>

using namespace cv;
using namespace std;


@interface OpenCVHelper : NSObject{
    CGPoint a;
    CGPoint b;
    CGPoint c;
    CGPoint d;
}

@property (assign,nonatomic) cv::Mat matImg;
@property NSMutableArray* largestSquareAnimation;
@property vector<cv::Point> pointsOfSquare;

// Singleton
+ (instancetype)sharedInstance;

// Convert between UIImage and cv::Mat
- (UIImage *)UIImageFromCVMat:(cv::Mat)cvMat;
- (cv::Mat)cvMatFromUIImage:(UIImage *)image;

// Square detection
- (cv::Mat)detectEdges:(cv::Mat)original;

// Correct image
- (cv::Mat)correctImg:(cv::Mat)original;

// Save and Load image in Document Directory (for debug processed image)
- (void)SaveImg:(cv::Mat)imgMat WithName:(NSString*)name;
- (UIImage*)ImgFromDocumentDirWithName:(NSString*)imgName;

// Write text from code to .txt file
- (void)WriteStringToFile:(NSString*)fileName withContent:(NSString*)textToWrite;
- (void)deleteAttachFileAfterSend;
- (void)writeStringToPDF:(NSString*)fileName with:(NSString*)content;

@end

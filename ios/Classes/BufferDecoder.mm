//
//  BufferDecoder.m
//  camera
//
//  Created by PT on 12/06/2021.
//

#import "BufferDecoder.h"
#import <AVFoundation/AVFoundation.h>
#import "Constants.h"

#define ILLEGAL_QUOTATION   @"”"
#define LANG_TAG            @"<tag lang=\"([^<]*)\">([^<]*)</tag>"
#define LANG_OTHER_TAG      @"<tag lang=\"([^<\"]*)\"[^<]*>([^<]*)</tag>"
#define IDS_TAG             @"<tag lang=\"%ci\"[^<]*>([^<]*)</tag>"
#define EXTEND_TAG          @"<extend\\s*([^<]*)/>"
#define JSL_URL             @"jsl_url\\s*=\\s*([^\\s]*)"

static VoiceCodeDecoder *decoderInstance = nil;

@implementation BufferDecoder
@synthesize voiceCode;
+(VoiceCodeDecoder *) sharedDecoder {
    @synchronized(self) {
        if (!decoderInstance) {
            decoderInstance = [[VoiceCodeDecoder alloc] init];
        }
    }
    return decoderInstance;
}

#pragma mark - Decode processing
- (FileItem*)analyzeVC {
    NSString *text;
    NSString *result = [[NSString alloc] init];
    FileItem *tempFile = [[FileItem alloc] init];
    // Save the created day
    tempFile.createdDate = [NSDate date];
    // Get language setting
    // TODO: Check input
    LanguageSetting sltLang = [[Constants sharedInstance] langCodeFrom:@"1"];
    NSString *lang = [[[Constants sharedInstance] langTags] objectAtIndex:(NSUInteger)sltLang]; // From setting

    // Check version of voice code
    if (voiceCode.codeVersion == VERSION_1_0) {
        // Decode byte array to string by Shift_JIS encoding
        text = [NSString stringWithCString:(const char *)voiceCode.bodyContent encoding:NSShiftJISStringEncoding];

        // If result is null, try decode byte array to string by UTF-8 encoding
        if (text == NULL)
        {
            text = [NSString stringWithCString:(const char *)voiceCode.bodyContent encoding:NSUTF8StringEncoding];
        }

        NSError *error = nil;

        // Replace illegal quotation
        NSRegularExpression *quotation = [NSRegularExpression regularExpressionWithPattern:ILLEGAL_QUOTATION options:NSRegularExpressionDotMatchesLineSeparators error:&error];

        text = [quotation stringByReplacingMatchesInString:text options:0 range:NSMakeRange(0, text.length) withTemplate:@"\""];

        // Filter result by language tag
        // Example: <tag lang="jpn">こんにちはともえです。</tag>
        NSRegularExpression *langTag = [NSRegularExpression regularExpressionWithPattern:LANG_OTHER_TAG options:NSRegularExpressionDotMatchesLineSeparators error:&error];

        NSArray *matches = [langTag matchesInString:text options:0 range:NSMakeRange(0, text.length)];
        // Check if language tag exists
        if (matches.count > 0)
        {
            // Match all texts again after removing language tag
            NSMutableArray *langs = [NSMutableArray new];
            BOOL found = NO;
            for (NSInteger it = 0; it < matches.count; it++) {
                NSTextCheckingResult *match = [matches objectAtIndex:it];
                NSString *subLang = [text substringWithRange:[match rangeAtIndex:1]];
                [langs addObject:subLang];
                NSString *subResult = [text substringWithRange:[match rangeAtIndex:[match numberOfRanges] - 1]];
                // If this language is matching as setting language, store its content in readOutContent
                if ([subLang isEqualToString:lang]) {
                    found = YES;
                    tempFile.targetLanguage = lang;
                    tempFile.readOutContent = subResult;
                    result = [NSString stringWithFormat:@"%@\r%@", subResult, result];
                } else {
                    result = [result stringByAppendingString:[NSString stringWithFormat:@"%@\r", subResult]];
                }
            }
            if (found == NO) {
                for (NSInteger it = 0; it < matches.count; it++) {
                    NSTextCheckingResult *match = [matches objectAtIndex:it];
                    NSString *subLang = [text substringWithRange:[match rangeAtIndex:1]];
                    if ([[Constants.sharedInstance langTags] containsObject:subLang]) {
                        NSString *subResult = [text substringWithRange:[match rangeAtIndex:[match numberOfRanges] - 1]];
                        tempFile.targetLanguage = subLang;
                        tempFile.readOutContent = subResult;
                        break;
                    }
                }
            }
        }
        else
        {
            result = text;
            tempFile.readOutContent = text;
        }
    } else if (voiceCode.codeVersion == VERSION_2_0) { // BufferType == 4
        uchar* inData = (unsigned char *)voiceCode.bodyContent;

        int posI = 0;

        // Travel in every segment from first byte to end character
        while (inData[posI] != 0xFF)
        {
            int szHeader = sizeof(UVTextHeader);
            UVTextHeader *piHd = (UVTextHeader *)&inData[posI];

            // In some cases, body data will add some redundant characters at its end after assign method
            // So we MUST prevent pointer from traveling beyond original data length
            if (piHd->country[0] == '\0'
                || piHd->country[1] == '\0'
                || piHd->country[2] == '\0'
                || piHd->length == 0) {
                break;
            }

            // Get language code in UVTextHeader
            NSString *country = [NSString stringWithFormat:@"%c%c%c", piHd->country[0], piHd->country[1], piHd->country[2]];
            NSLog(@"%@", country);

            // Move pointer to data area
            posI += szHeader;
            uchar *data = (uchar *)malloc(piHd->length + 1);
            memcpy(data, &inData[posI], piHd->length);
            // Add '\0' to the end of body data
            data[piHd->length] = '\0';
            NSInteger dataType = (NSInteger)(piHd->dataType & 0x0F);

            // Decode data depended on data type
            if (dataType == 0) { // Binary
                bool isContainUFT16 = false;
                for (int it = posI; it < piHd->length - 1; it++) {
                    if (*(inData + it) == '\0' && *(inData + it + 1) != '\0') {
                        isContainUFT16 = true;
                        break;
                    }
                }
                if (isContainUFT16) {
                    NSString *targetContent = [NSString stringWithFormat:@"%@", [NSString stringWithCharacters:(unichar *)data length:piHd->length / 2]];
                    if (targetContent != nil) {
                        targetContent = [targetContent stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
                    }
                    text = targetContent;
                } else {
                    NSString *targetContent = [NSString stringWithFormat:@"%@", [NSString stringWithCString:(const char *)data encoding:NSShiftJISStringEncoding]];
                    text = targetContent;
                }
//                result = [result stringByAppendingString:[NSString stringWithFormat:@"%@\r\r", text]];
                if ([text containsString:@"#nf"] || [text containsString:@"#nc"]){
                    result = text;
                }else{
                    result = [NSString stringWithFormat:@"%@\r\r<tag lang=\"%@\">\r\n%@\r\n</tag>", result, country, text];
                }

            }
            else if (dataType == 1 || dataType == 4) // SJIS, Ascii(ISO/IEC-8859-1)
            {
                text = [NSString stringWithFormat:@"%@", [NSString stringWithCString:(const char *)data encoding:NSShiftJISStringEncoding]];
                result = [NSString stringWithFormat:@"%@\r\r<tag lang=\"%@\">\r\n%@\r\n</tag>", result, country, text];
//                // If this language is matching as setting language, store its content in readOutContent
//                if ([country isEqualToString:lang]) {
//                    tempFile.targetLanguage = country;
//                    tempFile.readOutContent = text;
//                    result = [NSString stringWithFormat:@"<tag lang=\".%@\">%@</tag>\r\r%@", country, text, result];
//                } else {
//                    if ([[Constants.sharedInstance langTags] containsObject:country] && (!tempFile.targetLanguage || [tempFile.targetLanguage isEqualToString:@""])) {
//                        tempFile.targetLanguage = country;
//                        tempFile.readOutContent = text;
//                    }
//                    result = [result stringByAppendingString:[NSString stringWithFormat:@"%@\r\r", text]];
//                }
            }
            else if (dataType == 2) // UTF-8
            {
                text = [NSString stringWithFormat:@"%@", [NSString stringWithCString:(const char *)data encoding:NSUTF8StringEncoding]];
                result = [NSString stringWithFormat:@"%@\r\r<tag lang=\"%@\">\r\n%@</tag>", result, country, text];
//                // If this language is matching as setting language, store its content in readOutContent
//                if ([country isEqualToString:lang]) {
//                    tempFile.targetLanguage = country;
//                    tempFile.readOutContent = text;
//                    result = [NSString stringWithFormat:@"%@\r\r%@", text, result];
//                } else {
//                    if ([[Constants.sharedInstance langTags] containsObject:country] && (!tempFile.targetLanguage || [tempFile.targetLanguage isEqualToString:@""])) {
//                        tempFile.targetLanguage = country;
//                        tempFile.readOutContent = text;
//                    }
//                    result = [result stringByAppendingString:[NSString stringWithFormat:@"%@\r\r", text]];
//                }

            }
            else if (dataType == 3) // UTF-16LE
            {
                text = [NSString stringWithFormat:@"%@", [NSString stringWithCharacters:(unichar *)data length:piHd->length / 2]];
                result = [NSString stringWithFormat:@"%@\r\r<tag lang=\"%@\">\r\n%@\r\n</tag>", result, country, text];
//                // If this language is matching as setting language, store its content in readOutContent
//                if ([country isEqualToString:lang]) {
//                    tempFile.targetLanguage = country;
//                    tempFile.readOutContent = text;
//                    result = [NSString stringWithFormat:@"%@\r\r%@", text, result];
//                } else {
//                    if ([[Constants.sharedInstance langTags] containsObject:country] && (!tempFile.targetLanguage || [tempFile.targetLanguage isEqualToString:@""])) {
//                        tempFile.targetLanguage = country;
//                        tempFile.readOutContent = text;
//                    }
//                    result = [result stringByAppendingString:[NSString stringWithFormat:@"%@\r\r", text]];
//                }
            }
            else if (dataType == 10) // Code ID
            {
                text = [NSString stringWithCString:(const char *)data encoding:NSShiftJISStringEncoding];
                // If this language is matching as setting language, store its content in readOutContent
                if ([country isEqualToString:lang]) {
                    tempFile.targetLanguage = country;
                    tempFile.readOutContent = text;
                    result = [NSString stringWithFormat:@"\r\n<tag lang=\"%%ci\">\r\n%@\r\r%@\r\n</tag>", text, result];
                } else {
                    if ([[Constants.sharedInstance langTags] containsObject:country] && (!tempFile.targetLanguage || [tempFile.targetLanguage isEqualToString:@""])) {
                        tempFile.targetLanguage = country;
                        tempFile.readOutContent = text;
                    }
                    result = [result stringByAppendingString:[NSString stringWithFormat:@"\r\n<tag lang=\"%%ci\">\r\n%@</tag>\r\r", text]];
                }
            }

            // Move pointer to next segment
            posI += piHd->length;
        }
    }

    // TODO: Check => Pass all texts scanned to flutter handle
//    if (result && ![result isEqualToString:@""]) {
//        CodeIdentifier *ids = [Common.sharedInstance getIdsFromCode:result];
//        if (ids && ![ids.codeId isEqualToString:@""]) {
//            tempFile.ids = [ids toString];
//            result = [Common.sharedInstance dropIds:result];
//        }
//    }

    if (!tempFile.targetLanguage || [tempFile.targetLanguage isEqualToString:@""]) {
        tempFile.targetLanguage = [[[Constants sharedInstance] langTags] objectAtIndex:1]; // JPN
    }

    // Body content
    NSString *trimmedResult = [result stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    tempFile.fileName = [trimmedResult substringWithRange:NSMakeRange(0, MIN(16, trimmedResult.length))];
    NSRegularExpression *newLine = [NSRegularExpression regularExpressionWithPattern:@"\r|\n" options:NSRegularExpressionDotMatchesLineSeparators error:nil];
    tempFile.fileName = [newLine stringByReplacingMatchesInString:tempFile.fileName options:0 range:NSMakeRange(0, tempFile.fileName.length) withTemplate:@" "];
    tempFile.codeHeader = voiceCode.header;
    tempFile.sourceContent = result;

    return tempFile;
}
- (NSDictionary*)scanVoiceCode:(CMSampleBufferRef)sampleBuffer {
    NSMutableDictionary *result = [[NSMutableDictionary alloc] init];

    // Get a CMSampleBuffer's Core Video image buffer for the media data
    CVImageBufferRef imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer);
    // Lock the base address of the pixel buffer
    CVPixelBufferLockBaseAddress(imageBuffer, 0);
    // Get image buffer in Plane Y. Format type of this buffer is grayscale 1-channel
    unsigned char *baseAddress = (unsigned char *)CVPixelBufferGetBaseAddressOfPlane(imageBuffer, 0);

    int width = (int)CVPixelBufferGetWidth(imageBuffer);
    int height = (int)CVPixelBufferGetHeight(imageBuffer);
//    NSLog(@"%d %d", width, height);

    unsigned char *buffer = (unsigned char *) malloc(height * width);
//    unsigned char *buffer = (unsigned char *) malloc(crop_height * crop_width);
    unsigned char *newbuffer = NULL;
    unsigned char *wptr = buffer;
    unsigned char *rptr = (unsigned char *) baseAddress;
//    CGRect rect = [UIScreen mainScreen].bounds;
    BOOL needToCleanNewBuffer = NO;

    memset(buffer, 0xff, height * width);

    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            *wptr++ = *rptr++;
        }
    }

    // 中心部の300x300の明るさ
//    x_offset += 300;
//    y_offset += 300;

    //MARK:-----------------------------Process image--------------------------

//    cv::Mat matImg = Mat(688, 688, CV_8UC1, buffer);
    cv::Mat matImg = Mat(height, width, CV_8UC1, buffer);

    long totalLumiaOrgin = 0;
//    int biggest = 0;
//    int smallest = 255;

//    cv::Rect originRect = cv::Rect((688 - 200) / 2, (688 - 200) / 2, 200, 200);
//    cv::Mat croppedOriginMat = Mat(matImg, originRect);

    totalLumiaOrgin = (long)(cv::sum(matImg)[0]);
//    double max, min;

//    if (croppedOriginMat.data != NULL)
//    {
//        cv::minMaxIdx(croppedOriginMat, &min, &max);
//    }
//    else
//    {
//        cv::minMaxIdx(matImg, &min, &max);
//    }

//    biggest = (int)max;
//    smallest = (int)min;
    long _LUX = totalLumiaOrgin / (matImg.rows * matImg.cols);

    // Detect if too low or over brightness
    int biggest = 0;
    int smallest = 255;
    double max, min;

    cv::minMaxIdx(matImg, &min, &max);
    biggest = (int)max;
    smallest = (int)min;

    int brightnessValue;
    if (_LUX < 70) {
        brightnessValue = -1;
    } else if (_LUX >= 180 && biggest - smallest > 130) {
        brightnessValue = 1;
    } else {
        brightnessValue = 0;
    }
    [result setObject:@(brightnessValue) forKey:@"brightness"];

    // Enhance contrast of image
    if (biggest - smallest < 130) {
        if (biggest - smallest > 0) {
            if (_LUX >= 170) {
                matImg.convertTo(matImg, -1, 1.7f, -100);
            } else {
                matImg.convertTo(matImg, -1, 1.7f, -60);
            }
        }
    }

    // De-skew image
    cv::Mat squareMat = [[OpenCVHelper sharedInstance] detectEdges:matImg];

    int finalW = 0;
    int finalH = 0;

    int _TH = 0;

    if (squareMat.data != NULL)
    {
        finalW = squareMat.cols;
        finalH = squareMat.rows;

        // Histogram Equalization (Wiki)
//        cv::equalizeHist(squareMat, squareMat);

        // Apply CLAHE (Constrast Limited Adaptive Histogram Equalization)
        cv::Ptr<cv::CLAHE> clahe = cv::createCLAHE();
        clahe->setClipLimit(2.0);
        clahe->apply(squareMat, squareMat);

        cv::Rect croppedRect = cv::Rect(finalW / 4, finalH / 4, finalW / 2, finalH / 2);
        cv::Mat croppedSquareMat = Mat(squareMat, croppedRect);

        if (croppedSquareMat.data != NULL) {
            long totalLumia = (long)(cv::sum(croppedSquareMat)[0]);

            // Set dynamic Threshold for Javis
            _TH = (int)(totalLumia / (croppedSquareMat.rows * croppedSquareMat.cols));

            NSData *data = [NSData dataWithBytes:squareMat.data length:squareMat.elemSize() * squareMat.total()];

            if (data != NULL)
            {
                newbuffer = (uchar *)data.bytes;
                needToCleanNewBuffer = YES;
            }
            else
            {
                _TH = (int)(_LUX);
                newbuffer = buffer;
            }
        }
        croppedSquareMat.release();
    }
    else
    {
        matImg.release();
        squareMat.release();
        [OpenCVHelper sharedInstance].pointsOfSquare.clear();
        CVPixelBufferUnlockBaseAddress(imageBuffer, 0);
        free(buffer);
        return result;
    }

    //----------------------------end process image---------------------------------

    int ret = [[BufferDecoder sharedDecoder] decodeBuffer:newbuffer width:finalW height:finalH threshold:_TH withCompletionBlock:^(VoiceCode *newVoiceCode) {
        if (newVoiceCode.validCode != NOT_UNI_VOICE) {
            //Check multi language code
            if (newVoiceCode.codeVersion == VERSION_1_0) {
                NSString *resultVoiceCode = [NSString stringWithCString:(const char *)newVoiceCode.bodyContent encoding:NSShiftJISStringEncoding];
                if (resultVoiceCode == NULL) {
                    resultVoiceCode = [NSString stringWithCString:(const char *)newVoiceCode.bodyContent encoding:NSUTF8StringEncoding];
                }
                if (![resultVoiceCode containsString:@"<tag"]) {
                    NSRange range = [resultVoiceCode rangeOfString:@"[ぁ-んァ-ンｧ-ﾝﾞﾟ]" options:NSRegularExpressionSearch];
                    BOOL isHiraganaKatakana = range.location != NSNotFound;
                    NSRange rangeKanji = [resultVoiceCode rangeOfString:@"[一-龯]" options:NSRegularExpressionSearch];
                    BOOL isKanji = rangeKanji.location != NSNotFound;
                    if (isHiraganaKatakana || isKanji) {
                        self->voiceCode = newVoiceCode;
                        FileItem *tempFile = [self analyzeVC];
                        // Push ReadOutScreenVC to show and read out result
                        [self submitResult:tempFile];
                    } else {
                        [self signalNotUVCode];
                    }
                } else {
                    self->voiceCode = newVoiceCode;
                    FileItem *tempFile = [self analyzeVC];
                    [self submitResult:tempFile];
                }
            } else {
                self->voiceCode = newVoiceCode;
                FileItem *tempFile = [self analyzeVC];
                [self submitResult:tempFile];
            }
        } else {
            // Not UV Code
            [self signalNotUVCode];
        }
    }];

    // Update return value
    [result setObject:@(ret) forKey:@"code"];

    NSLog(@"result: %d", ret);

    matImg.release();
    squareMat.release();
//    croppedOriginMat.release();
End:
    // Unlock the pixel buffer
    CVPixelBufferUnlockBaseAddress(imageBuffer, 0);
    free(buffer);

    return result;
}

- (void)submitResult:(FileItem*)item {
    // TODO: Remove log on production
    NSLog(@"%@", item.sourceContent);
    if (self.delegate != nil) {
        [self.delegate didOutputResult:item];
    }
}

- (void)signalNotUVCode {
    // TODO: Remove log on production
    NSLog(@"NOT UV Code");
    if (self.delegate != nil) {
        [self.delegate didDetectedNotUVCode];
    }
}

@end

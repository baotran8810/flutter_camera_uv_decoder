//
//  FileItem.h
//  camera
//
//  Created by PT on 15/06/2021.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FileItem : NSObject
@property (strong, nonatomic) NSString *fileId;
@property (strong, nonatomic) NSString *fileName;
@property (strong, nonatomic) NSDate *createdDate;
@property (strong, nonatomic) NSString *_Nullable codeHeader;
@property (strong, nonatomic) NSString *sourceContent;
@property (strong, nonatomic) NSString *translatedContent;
@property (strong, nonatomic) NSString *readOutContent;
@property (strong, nonatomic) NSString *targetLanguage;
@property (strong, nonatomic) NSString *ids;
@property (assign) BOOL *isJanCode;
@property (assign) BOOL *isQRCode;
@property (assign) bool *isFavourite;
// 0: Faci || 1: Navi
@property (strong, nonatomic) NSString *codeType;
@end

NS_ASSUME_NONNULL_END

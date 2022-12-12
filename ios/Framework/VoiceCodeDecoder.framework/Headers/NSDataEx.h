#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonCryptor.h>

@class NSString;

@interface NSData (Additions)

- (NSData *)AES256EncryptWithKey:(const NSString *)key;
- (NSData *)AES256DecryptWithKey:(NSString *)key;
- (NSString *)newStringInBase64FromData;

@end

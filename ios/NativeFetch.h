
// #ifdef RCT_NEW_ARCH_ENABLED
// #import "RNNativeFetchSpec.h"

// @interface NativeFetch : NSObject <NativeNativeFetchSpec>
// #else
#import <React/RCTBridgeModule.h>

@interface NativeFetch : NSObject <RCTBridgeModule>
// #endif

@end

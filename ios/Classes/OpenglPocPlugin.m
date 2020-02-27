#import "OpenglPocPlugin.h"
#if __has_include(<opengl_poc_plugin/opengl_poc_plugin-Swift.h>)
#import <opengl_poc_plugin/opengl_poc_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "opengl_poc_plugin-Swift.h"
#endif

@implementation OpenglPocPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftOpenglPocPlugin registerWithRegistrar:registrar];
}
@end

//
//  PinterestPlugin.h

#import <Cordova/CDV.h>
#import "PinterestSDK.h"

@interface PinterestPlugin : CDVPlugin
{
    PDKClient*    pinterest;
    NSString*     clientId;
}

@property (nonatomic, retain) PDKClient* pinterest;
@property (nonatomic, retain) NSString*     clientId;

- (void)initPinterest:(CDVInvokedUrlCommand*)command;;
- (void)pin:(CDVInvokedUrlCommand*)command;
- (void)canPinWithSDK:(CDVInvokedUrlCommand*)command;


@end

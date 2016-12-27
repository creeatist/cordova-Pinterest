//
//  Pinterest.m

#import "PinterestPlugin.h"
#include "PDKPin.h"

@implementation PinterestPlugin

@synthesize pinterest;
@synthesize clientId;

- (void) initPinterest:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;

    self.clientId = [[NSString alloc] initWithString:[command.arguments objectAtIndex:0]];
    if (self.pinterest == nil){
        [PDKClient configureSharedInstanceWithAppId:self.clientId];
        self.pinterest = [PDKClient sharedInstance];
	}
    if (self.pinterest != nil) {
        NSLog(@"Pinterest Plugin initalized with clientID: %@", self.clientId);
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    } else{
        NSLog(@"Pinterest Plugin failed to initalize.");
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Arg was null"];
    }
     [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void) pin:(CDVInvokedUrlCommand*)command {
    NSURL *sourceURL = [NSURL URLWithString:[command.arguments objectAtIndex:0]];
    NSURL *imageURL = [NSURL URLWithString:[command.arguments objectAtIndex:1]];
	NSString* description =[NSString stringWithString:[command.arguments objectAtIndex:2]];

    [PDKPin pinWithImageURL:imageURL link:sourceURL suggestedBoardName:"" note:description fromViewController:[self viewController] withSuccess:^{
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } andFailure:^(NSError *error) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[error description]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}
     
- (void)canPinWithSDK:(CDVInvokedUrlCommand*)command {
	CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end

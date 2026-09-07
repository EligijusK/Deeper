import UIKit
import GoogleMaps

class AppDelegate: NSObject, UIApplicationDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        guard let apiKey = Bundle.main.infoDictionary?["MAPS_API_KEY"] as? String else {
            fatalError("Maps API key not set in Info.plist")
        }
        print("Maps key loaded:", apiKey.count > 20)
        print("key: ", apiKey)
        let accepted = GMSServices.provideAPIKey(apiKey)

        print("Google Maps accepted key:", accepted)
        return true
    }
    
    
}

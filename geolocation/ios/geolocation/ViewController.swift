import UIKit
import WebKit

class ViewController: UIViewController, WKNavigationDelegate {

    var webView: WKWebView!
    override func viewDidLoad() {
    super.viewDidLoad()
        let request = URLRequest(url: URL(string: "customscheme://localhost/index.html")!)
        _ = webView?.load(request)
    }

    override func loadView() {
        let webViewConfiguration = WKWebViewConfiguration()
        webViewConfiguration.setURLSchemeHandler(WebViewHander(), forURLScheme: "customscheme")
        webView = WKWebView(frame: .zero, configuration: webViewConfiguration)
        webView.navigationDelegate = self
        view = webView
    }

}


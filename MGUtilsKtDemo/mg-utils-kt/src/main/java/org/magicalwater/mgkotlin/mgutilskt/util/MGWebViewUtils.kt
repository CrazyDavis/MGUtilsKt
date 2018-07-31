package org.magicalwater.mgkotlin.mgutilskt.util

import android.webkit.WebView

class MGWebViewUtils {

    companion object {
        //向前一頁, 返回是否成功
        fun forwardPage(webView: WebView): Boolean {
            val canGoForward = webView.canGoForward()
            if (webView.canGoForward()) {
                webView.goForward()
            }
            return canGoForward
        }
    }
}
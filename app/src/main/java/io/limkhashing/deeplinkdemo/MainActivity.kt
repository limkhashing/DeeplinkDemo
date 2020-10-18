package io.limkhashing.deeplinkdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var deeplink: String = ""

    companion object {
        const val URL_PARAM_DEEPLINK = "url"
        private const val EXTRA_DEEPLINK = "EXTRA_DEEPLINK"

        fun getStartIntent(context: Context, deeplink: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_DEEPLINK, deeplink)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getInputData()
        initView()
    }

    private fun getInputData() {
        val intent = this.intent
        if (intent.hasExtra(EXTRA_DEEPLINK)) deeplink = intent.getStringExtra(EXTRA_DEEPLINK)!!
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        if (deeplink.startsWith("demo", true)) {
            web_view.visibility = View.GONE
            tv_deeplink_url.text = deeplink
        } else {
            web_view.visibility = View.VISIBLE
            web_view.settings.javaScriptEnabled = true
            web_view.settings.domStorageEnabled = true
            web_view.webViewClient = WebViewClient()
            web_view.webChromeClient = WebChromeClient()
            web_view.loadUrl(deeplink)
        }
    }

    override fun onBackPressed() {
        if (web_view.canGoBack())
            web_view.goBack()
        else
            super.onBackPressed()
    }
}

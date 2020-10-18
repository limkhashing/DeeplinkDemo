package io.limkhashing.deeplinkdemo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.NavUtils
import io.limkhashing.deeplinkdemo.MainActivity.Companion.URL_PARAM_DEEPLINK


class DeeplinkHandler {
    companion object {
        const val HOME = "/home"
        const val PROMO = "/promo"
        const val ACTIVATE = "/activate"
        const val ACCOUNT = "/account"
        const val WEBLINK = "/weblink"
        const val EXTERNAL = "/external"
        const val INTERNAL = "/internal"
    }

    private fun isLoginRequired(path: String?, uri: Uri): Boolean {
        if (path.isNullOrEmpty()) return false
        return when (path) {
            HOME, PROMO, ACTIVATE, ACCOUNT, WEBLINK -> false
            else -> false
        }
    }

    fun getIntentForDeepLink(context: Context, path: String?, uri: Uri): Intent {
        val intent: Intent
        if (isLoginRequired(path, uri)) throw LoginRequiredException()

        when (path) {
            HOME -> intent = MainActivity.getStartIntent(context, "demo://io.github.limkhashing/home")
            PROMO -> intent = MainActivity.getStartIntent(context, "demo://io.github.limkhashing/promo")
            ACTIVATE -> intent = MainActivity.getStartIntent(context, "demo://io.github.limkhashing/activate")
            ACCOUNT -> intent = MainActivity.getStartIntent(context, "demo://io.github.limkhashing/account")
            WEBLINK -> {
                val url = uri.getQueryParameter(URL_PARAM_DEEPLINK)
                intent = if (url.isNullOrEmpty()) // fallback
                    MainActivity.getStartIntent(context, "https://limkhashing.github.io")
                else
                    MainActivity.getStartIntent(context, url)
            }
//                EXTERNAL -> {
//                    uri.getQueryParameter(URL_PARAM_DEEPLINK)?.let { url ->
//                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    }
//                }
//                INTERNAL -> {
//                    uri.getQueryParameter(URL_PARAM_DEEPLINK)?.let { url ->
//                        WebViewActivity.getIntent(context, url)
//                    }
//                }
            else -> intent = Intent(context, MainActivity::class.java) // fallback
        }
        return intent
//        return Pair(intent, getParentActivityIntent(context, intent?.component?.className ?: ""))
    }

//    private fun getBottomNavigationIndex(path: String?): Int {
//        return when (path) {
//            HOME -> ManagerBottomNavigation.HOME.ordinal
//            DATA -> ManagerBottomNavigation.DATA.ordinal
//            FAVESERVICES -> ManagerBottomNavigation.SERVICES.ordinal
//            TRANSACTION -> ManagerBottomNavigation.TRANSACTION.ordinal
//            ACCOUNT -> ManagerBottomNavigation.ACCOUNT.ordinal
//            else -> ManagerBottomNavigation.HOME.ordinal // fallback to home tab
//        }
//    }

    private fun getParentActivityIntent(context: Context, className: String): Intent? {
        try {
            val act = Class.forName(className)
            return NavUtils.getParentActivityIntent(context, act)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    class InvalidDeepLinkException internal constructor() : Exception()
    class LoginRequiredException internal constructor() : Exception()
}


/*
To Test Deep Link, you can use ADB to send the intent:
adb shell am start -a android.intent.action.VIEW -d "DEEP_LINK_HERE"

demo://io.github.limkhashing/home
demo://io.github.limkhashing/promo
demo://io.github.limkhashing/activate
demo://io.github.limkhashing/acccount
demo://io.github.limkhashing/weblink?www.google.com
demo://io.github.limkhashing/external?url=https://www.google.com
demo://io.github.limkhashing/internal?url=https://www.google.com

*/

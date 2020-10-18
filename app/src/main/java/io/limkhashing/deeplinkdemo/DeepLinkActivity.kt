package io.limkhashing.deeplinkdemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.TaskStackBuilder

class DeepLinkActivity : AppCompatActivity() {
    private var createNewTask = true

    companion object {
        private const val EXTRA_CREATE_NEW_TASK = "EXTRA_CREATE_NEW_TASK"

        fun start(context: Context, uri: String) {
            val intent = Intent(context, DeepLinkActivity::class.java)
            intent.putExtra(EXTRA_CREATE_NEW_TASK, false)
            intent.data = Uri.parse(uri)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deeplinkHandler = DeeplinkHandler()
        val uri = intent.data
        createNewTask = intent.getBooleanExtra(EXTRA_CREATE_NEW_TASK, true)
        var path: String? = null
        uri?.let { path = it.path }

        try {
            val targetActivity = deeplinkHandler.getIntentForDeepLink(this, path, uri!!)
            startTargetActivity(targetActivity, null)

        } catch (e: DeeplinkHandler.LoginRequiredException) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    private fun startTargetActivity(intent: Intent?, parentActivityIntent: Intent?) {
        if (intent != null) {
            if (createNewTask && parentActivityIntent != null) {
                // this is to add parent activity to the target deepklink intent
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(intent)
                    .startActivities()
            } else
                startActivity(intent)
        } else {
            // Do nothing if it is call from activity, means it is not valid and to prevent recreate task stack
            if (createNewTask) startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

package de.danielglaser.himbeerheim.model

import android.preference.PreferenceManager
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.view.BaseActivity

/**
 * Created by Daniel on 29.07.2017.
 */
class Util {
    companion object {
        var defaultPort = 22
        var defaultUsername = "pi"
        var defaultPath = "sudo ./raspberry-remote/send"

        var hostNotSet = "hostNotSet"
        var usernameNotSet = "usernameNotSet"
        var connectTimeout = "connectTimeout"

        fun setTheme(theme: Int) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(BaseActivity.appContext.applicationContext)
            prefs.edit().putInt(BaseActivity.appContext.applicationContext.getString(R.string.current_theme_key), theme).apply()
        }

        fun getTheme(): Int {
            val prefs = PreferenceManager.getDefaultSharedPreferences(BaseActivity.appContext.applicationContext)
            return prefs.getInt(BaseActivity.appContext.applicationContext.getString(R.string.current_theme_key), -1)
        }
    }
}
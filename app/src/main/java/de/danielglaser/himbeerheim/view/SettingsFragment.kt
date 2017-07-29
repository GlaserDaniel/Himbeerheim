package de.danielglaser.himbeerheim.view

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.Data
import de.danielglaser.himbeerheim.model.Util
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity



/**
 * Created by Daniel on 28.07.2017.
 */
class SettingsFragment() : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    var TAG = "SettingsFragment"

    lateinit var m_data: Data
    lateinit var prefs: SharedPreferences

    constructor(data: Data): this() {
        retainInstance = true
        this.m_data = data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        initIpPref()
        initUsernamePref()
        initPasswordPref()
        initThemePref()
    }

    override fun onDestroy() {
        super.onDestroy()
        m_data.save()
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (preference is Preference && newValue is String) {
            when (preference.key) {
                getString(R.string.ip_key) -> {
                    updateIpPref(preference, newValue)
                    return true
                }
                getString(R.string.username_key) -> {
                    updateUsernamePref(preference, newValue)
                    return true
                }
                getString(R.string.password_key) -> {
                    updatePasswordPref(preference, newValue)
                    return true
                }
                else -> return false
            }
        } else if (preference is Preference && newValue is Boolean) {
            when (preference.key) {
                getString(R.string.dark_theme_key) -> {
                    updateThemePref(preference, newValue)
                    return true
                }
                else -> return false
            }
        } else {
            Toast.makeText(activity, "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "OnPrefenceChange(), preference no Preference or newValue no String")
            return false
        }
    }

    fun initIpPref() {
        val preference = findPreference(getString(R.string.ip_key))
        preference.onPreferenceChangeListener = this
        preference.summary = m_data.host

        /*preference.setOnPreferenceChangeListener { preference, any ->
            if (preference is Preference) {
                Toast.makeText(activity, preference.toString() + " : " + any, Toast.LENGTH_SHORT).show()
                true
            } else {
                Toast.makeText(activity, "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show()
                false
            }
        }*/
    }

    fun updateIpPref(preference: Preference, value: String) {
        preference.summary = value
        m_data.host = value
    }

    fun initUsernamePref() {
        val preference = findPreference(getString(R.string.username_key))
        preference.onPreferenceChangeListener = this
        preference.summary = m_data.username
    }

    fun updateUsernamePref(preference: Preference, value: String) {
        preference.summary = value
        m_data.username = value
    }

    fun initPasswordPref() {
        val preference = findPreference(getString(R.string.password_key))
        preference.onPreferenceChangeListener = this
        preference.summary = m_data.password
    }

    fun updatePasswordPref(preference: Preference, value: String) {
        preference.summary = value
        m_data.password = value
    }

    fun initThemePref() {
        findPreference(getString(R.string.dark_theme_key)).onPreferenceChangeListener = this
        //prefs.edit().putBoolean(getString(R.string.dark_theme_key), false).commit()
    }

    fun updateThemePref(preference: Preference, value: Boolean) {
        prefs.edit().putBoolean(getString(R.string.dark_theme_key), value).commit()

        if (value) {
            // Dark Theme
            Util.setTheme(2)
        } else {
            // Light Theme
            Util.setTheme(1)
        }
        //activity.recreate()

        activity.finish()

        activity.startActivity(Intent(activity, activity.javaClass))
    }
}
package de.danielglaser.himbeerheim.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.DataSingleton
import de.danielglaser.himbeerheim.model.Util


/**
 * Created by Daniel on 28.07.2017.
 */
class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    private val mTAG = "SettingsFragment"

    private lateinit var prefs: SharedPreferences

    @SuppressLint("ValidFragment")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        addPreferencesFromResource(R.xml.preferences)

        prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        initIpPref()
        initPortPref()
        initUsernamePref()
        initPasswordPref()
        initPathPref()
        initThemePref()
    }

    override fun onDestroy() {
        super.onDestroy()
        DataSingleton.mData.save()
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
                getString(R.string.port_key) -> {
                    updatePortPref(preference, newValue.toInt())
                    return true
                }
                getString(R.string.path_key) -> {
                    updatePathPref(preference, newValue)
                    return true
                }
                else -> return false
            }
        } else if (preference is Preference && newValue is Boolean) {
            return when (preference.key) {
                getString(R.string.dark_theme_key) -> {
                    updateThemePref(newValue)
                    true
                }
                else -> false
            }
        } else {
            Toast.makeText(activity, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            Log.d(mTAG, "onPreferenceChange(), preference no Preference or newValue no String")
            return false
        }
    }

    private fun initIpPref() {
        val preference = findPreference(getString(R.string.ip_key))
        preference.onPreferenceChangeListener = this
        preference.summary = DataSingleton.mData.host

        /*preference.setOnPreferenceChangeListener { preference, any ->
            if (preference is Preference) {
                Toast.makeText(activity, preference.toString() + " : " + any, Toast.LENGTH_SHORT).show()
                true
            } else {
                Toast.makeText(activity, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                false
            }
        }*/
    }

    private fun updateIpPref(preference: Preference, value: String) {
        preference.summary = value
        DataSingleton.mData.host = value
    }

    private fun initPortPref() {
        val preference = findPreference(getString(R.string.port_key))
        preference.onPreferenceChangeListener = this
        preference.summary = DataSingleton.mData.port.toString()
    }

    private fun updatePortPref(preference: Preference, value: Int) {
        preference.summary = value.toString()
        DataSingleton.mData.port = value
    }

    private fun initUsernamePref() {
        val preference = findPreference(getString(R.string.username_key))
        preference.onPreferenceChangeListener = this
        preference.summary = DataSingleton.mData.username
    }

    private fun updateUsernamePref(preference: Preference, value: String) {
        preference.summary = value
        DataSingleton.mData.username = value
    }

    private fun initPasswordPref() {
        val preference = findPreference(getString(R.string.password_key))
        preference.onPreferenceChangeListener = this
        preference.summary = DataSingleton.mData.password
    }

    private fun updatePasswordPref(preference: Preference, value: String) {
        preference.summary = value
        DataSingleton.mData.password = value
    }

    private fun initPathPref() {
        val preference = findPreference(getString(R.string.path_key))
        preference.onPreferenceChangeListener = this
        preference.summary = DataSingleton.mData.getPath()
    }

    private fun initThemePref() {
        findPreference(getString(R.string.dark_theme_key)).onPreferenceChangeListener = this
        //prefs.edit().putBoolean(getString(R.string.dark_theme_key), false).commit()
    }

    private fun updateThemePref(value: Boolean) {
        prefs.edit().putBoolean(getString(R.string.dark_theme_key), value).apply()

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

    private fun updatePathPref(preference: Preference, value: String) {
        preference.summary = value
        DataSingleton.mData.updatePath(value)
    }
}
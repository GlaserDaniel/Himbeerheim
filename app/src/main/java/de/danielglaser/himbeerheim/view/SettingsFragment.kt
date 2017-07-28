package de.danielglaser.himbeerheim.view

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.util.Log
import android.widget.Toast
import de.danielglaser.himbeerheim.R

/**
 * Created by Daniel on 28.07.2017.
 */
class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    var TAG = "SettingsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        initIpPreference()
        initUsernamePref()
        initPasswordPref()
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (preference is Preference && newValue is String) {
            when (preference.key) {
                getString(R.string.ip_key) -> {
                    updateIpPreference(preference, newValue)
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
        } else {
            Toast.makeText(activity, "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "OnPrefenceChange(), preference no Preference or newValue no String")
            return false
        }
    }

    fun initIpPreference() {
        val ipPreference = findPreference(getString(R.string.ip_key))
        ipPreference.onPreferenceChangeListener = this
        updateIpPreference(ipPreference, "")

        /*ipPreference.setOnPreferenceChangeListener { preference, any ->
            if (preference is Preference) {
                Toast.makeText(activity, preference.toString() + " : " + any, Toast.LENGTH_SHORT).show()
                true
            } else {
                Toast.makeText(activity, "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show()
                false
            }
        }*/
    }

    fun updateIpPreference(preference: Preference, value: String) {
        //preference.summary = value
        // TODO Data
    }

    fun initUsernamePref() {
        val preference = findPreference(getString(R.string.username_key))
        preference.onPreferenceChangeListener = this
        updateUsernamePref(preference, "")
    }

    fun updateUsernamePref(preference: Preference, value: String) {

    }

    fun initPasswordPref() {
        val preference = findPreference(getString(R.string.password_key))
        preference.onPreferenceChangeListener = this
        updatePasswordPref(preference, "")
    }

    fun updatePasswordPref(preference: Preference, value: String) {

    }
}
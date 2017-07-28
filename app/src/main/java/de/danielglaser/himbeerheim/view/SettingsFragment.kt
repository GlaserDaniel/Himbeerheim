package de.danielglaser.himbeerheim.view

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
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

        var ipPreference = findPreference(getString(R.string.ip_key))

    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (preference is Preference) {
            Toast.makeText(activity.applicationContext, preference.toString() + " : " + newValue, Toast.LENGTH_SHORT).show()
            return true
        } else {
            Toast.makeText(activity.applicationContext, "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}
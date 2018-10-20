package de.danielglaser.himbeerheim.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.ButtonCommand
import de.danielglaser.himbeerheim.model.DataSingleton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainActivityFragment.MainActivityFragmentListener, EditCommandFragment.EditCommandListener {

    companion object {
        private lateinit var appContext: Context

        fun getContext() : Context {
            return appContext
        }
    }

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        appContext = this

        if (savedInstanceState != null) {
            return
        }

        /*// Wenn App das erste mal startet

        if (DataSingleton.mData.m_buttonCommands.size == 0 && DataSingleton.mData.host.isBlank()) {
            // Erste Buttons hinzufügen
            val code = ArrayList<Any>()
            code.add(0)
            code.add(1)
            code.add(1)
            code.add(1)
            code.add(0)
            DataSingleton.mData.m_buttonCommands.add(ButtonCommand("Licht Wohnstube", code, 2, DataSingleton.mData.getPath()))
            DataSingleton.mData.m_buttonCommands.add(ButtonCommand("TV", code, 3, DataSingleton.mData.getPath()))

            code[4] = 1
            DataSingleton.mData.m_buttonCommands.add(ButtonCommand("Nachtlicht", code, 4, DataSingleton.mData.getPath()))
        }*/

        loadMainActivityFragment()

        firstStart()
    }

    override fun onPause() {
        super.onPause()
        DataSingleton.mData.save()
    }

    private fun firstStart() {
        val firstStart = preferences.getBoolean("first_start", true)

        if (firstStart) {
            showFirstStartDialog()
        }

        preferences.edit().putBoolean("first_start", false).apply()
    }

    private fun showFirstStartDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.welcome_dialog_title)
                .setMessage(R.string.welcome_dialog_text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // make nothing, just close the dialog
                }

        val alert = builder.create()
        alert.show()
    }

    private fun loadMainActivityFragment() {
        val manager = fragmentManager
        manager.popBackStack()
        val newFragment = MainActivityFragment()

        val trans = manager.beginTransaction()
        trans.replace(R.id.fragment_container, newFragment)
        trans.commit()
    }

    private fun loadSettingsFragment() {
        val manager = fragmentManager
        manager.popBackStack()
        val newFragment = SettingsFragment()

        val trans = manager.beginTransaction()
        trans.addToBackStack(null)
        trans.replace(R.id.fragment_container, newFragment)
        trans.commit()
    }

    private fun loadEditCommandFragment(buttonCommand: ButtonCommand) {
        val manager = fragmentManager
        val newFragment = EditCommandFragment(buttonCommand)

        val trans = manager.beginTransaction()
        trans.addToBackStack(null)
        trans.replace(R.id.fragment_container, newFragment)
        trans.commit()
    }

    override fun onButtonCommandSelected(buttonCommand: ButtonCommand) {
        loadEditCommandFragment(buttonCommand)
    }

    override fun onSettingsSelected() {
        loadSettingsFragment()
    }

    override fun onSaveSelectedListener() {
        DataSingleton.mData.save()
        loadMainActivityFragment()
    }

    override fun onCancelSelectedListener() {
        DataSingleton.mData.save()
        loadMainActivityFragment()
    }

    override fun onDeleteSelectedListener(buttonCommand: ButtonCommand) {
        DataSingleton.mData.m_buttonCommands.remove(buttonCommand)
        DataSingleton.mData.save()
        loadMainActivityFragment()
    }
}

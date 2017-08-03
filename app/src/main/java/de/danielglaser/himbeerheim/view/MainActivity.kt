package de.danielglaser.himbeerheim.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.ButtonCommand
import de.danielglaser.himbeerheim.model.Data

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainActivityFragment.MainActivityFragmentListener, EditCommandFragment.EditCommandListener {

    companion object {
        private lateinit var appContext: Context

        fun getContext() : Context {
            return appContext
        }
    }

    lateinit var data: Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        appContext = this
        data = Data()

        if (savedInstanceState != null) {
            return
        }

        // Wenn App das erste mal startet

        if (data.m_buttonCommands.size == 0 && data.host.isNullOrBlank()) {
            // Erste Buttons hinzufügen
            val code = ArrayList<Any>()
            code.add(0)
            code.add(1)
            code.add(1)
            code.add(1)
            code.add(0)
            data.m_buttonCommands.add(ButtonCommand("Licht Wohnstube", code, 2, data.getPath()))
            data.m_buttonCommands.add(ButtonCommand("TV", code, 3, data.getPath()))

            code[4] = 1
            data.m_buttonCommands.add(ButtonCommand("Nachtlicht", code, 4, data.getPath()))
        }

        loadMainActivityFragment()
    }

    override fun onPause() {
        super.onPause()
        data.save()
    }

    private fun loadMainActivityFragment() {
        val manager = fragmentManager
        manager.popBackStack()
        val newFragment = MainActivityFragment(data)

        val trans = manager.beginTransaction()
        trans.replace(R.id.fragment_container, newFragment)
        trans.commit()
    }

    private fun loadSettingsFragment() {
        val manager = fragmentManager
        manager.popBackStack()
        val newFragment = SettingsFragment(data)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onButtonCommandSelected(buttonCommand: ButtonCommand) {
        loadEditCommandFragment(buttonCommand)
    }

    override fun onSettingsSelected() {
        loadSettingsFragment()
    }

    override fun onSaveSelectedListener() {
        data.save()
        loadMainActivityFragment()
    }

    override fun onCancelSelectedListener() {
        data.save()
        loadMainActivityFragment()
    }

    override fun onDeleteSelectedListener(buttonCommand: ButtonCommand) {
        data.m_buttonCommands.remove(buttonCommand)
        data.save()
        loadMainActivityFragment()
    }
}

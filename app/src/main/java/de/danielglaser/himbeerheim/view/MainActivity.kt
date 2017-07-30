package de.danielglaser.himbeerheim.view

import android.os.Bundle
import android.view.Menu
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.ButtonCommand
import de.danielglaser.himbeerheim.model.Data

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainActivityFragment.MainActivityFragmentListener, EditCommandFragment.EditCommandListener {

    lateinit var data: Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        data = Data()

        if (savedInstanceState != null) {
            return
        }

        // Wenn App das erste mal startet

        if (data.buttonCommands.size == 0) {
            // Erste Buttons hinzufügen
            val code = ArrayList<Any>()
            code.add(0)
            code.add(1)
            code.add(1)
            code.add(1)
            code.add(1)
            data.buttonCommands.add(ButtonCommand("Licht", code, 4, data.getCommand()))
            code[4] = 0
            data.buttonCommands.add(ButtonCommand("TV", code, 3, data.getCommand()))
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
        data.buttonCommands.remove(buttonCommand)
        data.save()
        loadMainActivityFragment()
    }
}

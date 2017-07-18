package de.danielglaser.himbeerheim.view

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Button
import android.widget.Toast
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.SSHConnection
import de.danielglaser.himbeerheim.model.ButtonCommand
import de.danielglaser.himbeerheim.model.Data

import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    lateinit var data: Data

    lateinit var sshConnection: SSHConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        data = Data()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }
        //R.id.search -> consume { MenuItemCompat.expandActionView(item) }
        //R.id.settings -> consume { navigateToSettings() }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater!!.inflate(R.layout.fragment_main, container, false)

        sshConnection = SSHConnection(hostname = "192.168.178.43", port = 22, username = "pi", password = "Roter!Weg!3")

        for (buttonCommand: ButtonCommand in data.buttonCommands) {
            view.buttonsLinearLayout.addView(makeButton(buttonCommand))
        }

        view.fab.setOnClickListener {
            var newButtonCommand = ButtonCommand("Licht An", "sudo ./send 01111 4 1")

            data.buttonCommands.add(newButtonCommand)
            data.save()

            view.buttonsLinearLayout.addView(makeButton(newButtonCommand))
        }


        view.buttonLightOn.setOnClickListener {
            sshConnection.sendSSHCommand(command = "sudo ./send 01111 4 1")
        }

        view.buttonLightOff.setOnClickListener {
            sshConnection.sendSSHCommand(command = "sudo ./send 01111 4 0")
        }

        view.buttonTVOn.setOnClickListener {
            sshConnection.sendSSHCommand(command = "sudo ./send 01110 3 1")
        }

        view.buttonTVOff.setOnClickListener {
            sshConnection.sendSSHCommand(command = "sudo ./send 01110 3 0")
        }

        view.buttonPowerOff.setOnClickListener {
            sshConnection.sendSSHCommand(command = "sudo shutdown -h 0")
        }

        return view
    }

    private fun makeButton(buttonCommand: ButtonCommand) : Button {
        val button = Button(context)
        button.text = buttonCommand.title
        button.layoutParams = Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.setOnClickListener {
            sshConnection.sendSSHCommand(command = buttonCommand.command)
        }
        button.setOnLongClickListener{
            Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show()

            val manager = activity.supportFragmentManager
            val newFragment = EditCommandFragment()
            /*
            val args = Bundle()
            args.putInt(ArticleFragment.ARG_POSITION, position)
            newFragment.setArguments(args)
            */

            val trans = manager.beginTransaction()
            trans.addToBackStack(null)
            trans.replace(R.id.fragment_container, newFragment)
            trans.commit()

            true
        }
        return button
    }

}

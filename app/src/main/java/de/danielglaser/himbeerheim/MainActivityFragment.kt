package de.danielglaser.himbeerheim

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater!!.inflate(R.layout.fragment_main, container, false)

        var sshConnection = SSHConnection(hostname = "192.168.178.43", port = 22, username = "pi", password = "Roter!Weg!3")

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
}

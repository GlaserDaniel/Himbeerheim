package de.danielglaser.himbeerheim;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Created by Daniel on 16.07.2017.
 */

public class SSHConnection {
    String hostname;
    int port;
    String username;
    String password;

    public SSHConnection(String hostname, int port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String sendSSHCommand(String command) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(username, hostname, port);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        try {
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        // SSH Channel
        ChannelExec channelssh = null;
        try {
            channelssh = (ChannelExec)
                    session.openChannel("exec");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand(command);
        try {
            channelssh.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        channelssh.disconnect();

        return baos.toString();
    }
}

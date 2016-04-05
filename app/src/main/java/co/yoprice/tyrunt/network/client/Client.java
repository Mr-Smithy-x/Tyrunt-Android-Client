package co.yoprice.tyrunt.network.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.CharBuffer;

/**
 * Created by Charl on 3/25/2016.
 */
public class Client {
    protected Socket socket;
    protected String ip;
    protected int port = 4711;

    protected Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    protected Client() {
    }

    protected boolean connect() {
        if (socket == null) socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket.isConnected();
    }

    public boolean isConnected() {
        if (socket == null) return false;
        return socket.isConnected();
    }

    public boolean close() {
        if (!isConnected()) return true;
        else {
            try {
                socket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean sendData(String data) {
        if (!isConnected()) return false;
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println(data);
            pw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String recvData() {
        if (!isConnected()) return null;
        try {
            char[] buffer = new char[1024];
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            isr.read(buffer);
            return new String(buffer);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

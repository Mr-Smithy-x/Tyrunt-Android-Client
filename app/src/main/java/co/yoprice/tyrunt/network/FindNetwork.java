package co.yoprice.tyrunt.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class FindNetwork {

    public interface OnNetworkListener {
        void OnNetworkFound(InetAddress inetAddress);

        void OnNetworkFound(String ipAddress);

        void OnNetworkNotFound();

    }

    public static void getReachableHosts(InetAddress inetAddress, OnNetworkListener onNetworkListener) throws SocketException {
        String ipAddress = inetAddress.toString();
        ipAddress = ipAddress.substring(1, ipAddress.lastIndexOf('.')) + ".";
        int size = 0;
        for (int i = 0; i < 256; i++) {
            String otherAddress = ipAddress + String.valueOf(i);
            try {
                if (reachable(InetAddress.getByName(otherAddress).getHostName())) {
                    InetAddress inet = InetAddress.getByName(otherAddress);
                    onNetworkListener.OnNetworkFound(inet);
                    size++;
                }
            } catch (UnknownHostException e) {
                continue;
            } catch (IOException e) {
                continue;
            }
        }
        if(size == 0) onNetworkListener.OnNetworkNotFound();
    }

    private static boolean reachable(String hostName) {
        SocketAddress sockAddr = new InetSocketAddress(hostName, 4711);
        Socket sock = new Socket();
        try {
            sock.connect(sockAddr, 50);
            sock.getOutputStream().write("TYRUNT?".getBytes(), 0, "TYRUNT?".getBytes().length);
            sock.getOutputStream().flush();
            byte[] b = new byte[32];
            if (sock.getInputStream().read(b, 0, b.length) > 0) {
                String s = new String(b).toLowerCase();
                if (s.equals("yes")) {
                    sock.close();
                    return true;
                }
            } else {
                sock.close();
                return false;
            }
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static InetAddress getWLANipAddress(String protocolVersion) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            if (netint.isUp() && !netint.isLoopback() && !netint.isVirtual()) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (protocolVersion.equals("IPv4")) {
                        if (inetAddress instanceof Inet4Address) {
                            return inetAddress;
                        }
                    } else {
                        if (inetAddress instanceof Inet6Address) {
                            return inetAddress;
                        }
                    }
                }
            }
        }
        return null;
    }

}
package co.yoprice.tyrunt.network;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import co.yoprice.tyrunt.models.Request;
import co.yoprice.tyrunt.models.Torrent;
import co.yoprice.tyrunt.models.TorrentList;
import co.yoprice.tyrunt.network.client.Client;
import co.yoprice.tyrunt.singleton.TorrentSingleton;
import co.yoprice.tyrunt.utils.PhoneUtils;

/**
 * Created by Charl on 3/25/2016.
 */
public class TyruntClient extends Client {
    private OnSyncListener onSyncListener;

    public TyruntClient(String ip, int port, OnSyncListener onSyncListener) {
        super(ip, port);
        this.onSyncListener = onSyncListener;
        startOnRecieve();
        connect();
    }

    public TyruntClient(OnSyncListener onSyncListener) {
        super();
        this.onSyncListener = onSyncListener;
        try {
            findNetwork();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void findNetwork() throws SocketException {
        FindNetwork.getReachableHosts(FindNetwork.getWLANipAddress("IPv4"), new FindNetwork.OnNetworkListener() {
            @Override
            public void OnNetworkFound(InetAddress inetAddress) {
                TyruntClient.this.ip = inetAddress.getHostName();
                TyruntClient.this.port = 4711;
                startOnRecieve();
                connect();
            }

            @Override
            public void OnNetworkFound(String ipAddress) {

            }

            @Override
            public void OnNetworkNotFound() {

            }
        });
    }

    public static void FindTysyncNetwork(FindNetwork.OnNetworkListener onNetworkListener) throws SocketException {
        FindNetwork.getReachableHosts(FindNetwork.getWLANipAddress("IPv4"), onNetworkListener);
    }

    public boolean connect() {
        if (super.connect()) {
            if (sendDeviceInfo(PhoneUtils.getDevice())) {
                return requestPCInfo();
            }
            return false;
        }
        return false;
    }

    public boolean sendDeviceInfo(PhoneUtils.DeviceInfo device) {
        return super.sendData(new Request<PhoneUtils.DeviceInfo>(0, "CONNECTION_ESTABLISHED", device).toString());
    }

    public boolean requestPCInfo() {
        return super.sendData(new Request<String>(100, "REQUEST_PC_INFO", "device_info").toString());
    }


    public void setConnection(String ip, int port) {
        if (super.close()) {
            super.ip = ip;

            super.port = port;
            connect();
        }
    }


    public interface OnSyncListener {
        public void OnTorrentResponse(String data);
    }

    public void addSyncListener(OnSyncListener onSyncListener) {
        this.onSyncListener = onSyncListener;
    }

    public boolean sendTorrent(Torrent torrent) {
        return super.sendData(new Request<Torrent>(2, "DOWNLOAD_TORRENT", torrent).toString());
    }

    public boolean sendPong() {
        return super.sendData(new Request<String>(10, "SENDING_PONG", "pong").toString());
    }

    public boolean saveTorrentList(TorrentList torrents) {
        return super.sendData(new Request<TorrentList>(3, "SAVE TORRENTS", torrents).toString());
    }

    public boolean saveTorrent(Torrent torrent) {
        return super.sendData(new Request<Torrent>(4, "SAVE TORRENT", torrent).toString());
    }

    public boolean sendTorrentList(TorrentList torrents) {
        return super.sendData(new Request<TorrentList>(1, "DOWNLOAD TORRENTS", torrents).toString());
    }

    public void startOnRecieve() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        while (isConnected()) {
                            onSyncListener.OnTorrentResponse(recvData());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

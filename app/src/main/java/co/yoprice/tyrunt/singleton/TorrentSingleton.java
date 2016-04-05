package co.yoprice.tyrunt.singleton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.yoprice.tyrunt.models.PCInfo;
import co.yoprice.tyrunt.models.Response;
import co.yoprice.tyrunt.network.TyruntClient;

/**
 * Created by Charl on 3/25/2016.
 */
public class TorrentSingleton implements TyruntClient.OnSyncListener {
    private static TorrentSingleton ourInstance = new TorrentSingleton();
    private OnPCInfoListener onPCInfoListener;
    private List<PCInfo> pcInfoList = new ArrayList<>();
    private int index = -1;


    public PCInfo getConnectedPCInfo(){
        if(index == -1) return null;
        else return pcInfoList.get(index);
    }

    public interface  OnPCInfoListener{
        void OnRecievedPCInfo(PCInfo pcInfo);
    }

    public void addOnPCInfoListener(OnPCInfoListener onPCInfoListener){
        this.onPCInfoListener = onPCInfoListener;
    }
    public static TorrentSingleton getInstance() {
        return ourInstance;
    }

    public TyruntClient tyruntClient;

    public TyruntClient getTyruntClient() {
        return tyruntClient;
    }

    public void initTyruntClient(){
        if(tyruntClient == null){
            tyruntClient = new TyruntClient(this);
        }
    }

    public void initTyruntClient(String ip){
        if(tyruntClient == null){
            tyruntClient = new TyruntClient(ip, 4711, this);
        }else{
            tyruntClient.setConnection(ip, 4711);
        }
    }



    private TorrentSingleton() {

    }

    @Override
    public void OnTorrentResponse(String data) {
        try {
            JSONObject json = new JSONObject(data);
            switch (json.getInt("code")){
                case 10:
                    if(json.getString("data").equals("ping")){
                        getTyruntClient().sendPong();
                    }
                    break;
                case 100:
                    PCInfo pcInfoResponse = new Gson().fromJson(json.getJSONObject("data").toString(),PCInfo.class);
                    pcInfoList.add(pcInfoResponse);
                    index = pcInfoList.indexOf(pcInfoResponse);
                    if(onPCInfoListener != null){
                        onPCInfoListener.OnRecievedPCInfo(pcInfoResponse);
                    }
                    break;
                case 200:
                    break;
                case 500:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

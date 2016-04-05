package co.yoprice.tyrunt.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Charl on 3/25/2016.
 */
public class TorrentList extends ArrayList<Torrent> implements Serializable {

    public ArrayList<Torrent> findTorrentsByUser(String user){
        ArrayList<Torrent> torrents =  new ArrayList<>();
        for(Torrent t : this){
            if(t.getAuthor().toLowerCase().equals(user.toLowerCase())) {
                torrents.add(t);
            }
        }
        return torrents;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this, getClass());
    }

}

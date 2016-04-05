package co.yoprice.tyrunt.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

import co.yoprice.tyrunt.R;
import co.yoprice.tyrunt.activities.TorrentActivity_;
import co.yoprice.tyrunt.adapters.recycler.TorrentAdapter;
import co.yoprice.tyrunt.adapters.recycler.views.TorrentItemView;
import co.yoprice.tyrunt.utils.TParser;
import co.yoprice.tyrunt.models.Torrent;

/**
 * Created by Charl on 3/24/2016.
 */
@EFragment(R.layout.search_result)
public class SearchResultFragment extends Fragment implements TorrentAdapter.OnTorrentClickedListener {
    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

    TorrentAdapter torrentAdapter;

    @AfterViews
    public void after() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(torrentAdapter = new TorrentAdapter().addTorrentAdapter(this));
    }


    @Override
    public void OnTorrentClicked(TorrentItemView torrentItemView, int position) {
        TorrentActivity_.intent(this).torrents(torrentAdapter.getAllTorrents()).index(position).start();
    }

    @Override
    public boolean OnTorrentLongClicked(TorrentItemView torrentItemView, int position) {
        return false;
    }

    @Background
    public void search(String newText) {
        try {
            List<Torrent> t = TParser.Search(newText);
            onresult(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void onresult(List<Torrent> torrents){
        torrentAdapter.clear();
        for(Torrent t : torrents){
            torrentAdapter.addItem(t);
        }
    }

}

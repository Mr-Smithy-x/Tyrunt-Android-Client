package co.yoprice.tyrunt.adapters.recycler;

import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import co.yoprice.tyrunt.adapters.recycler.base.RecyclerViewBaseAdapter;
import co.yoprice.tyrunt.adapters.recycler.base.holders.ViewWrapper;
import co.yoprice.tyrunt.adapters.recycler.views.TorrentItemView;
import co.yoprice.tyrunt.adapters.recycler.views.TorrentItemView_;
import co.yoprice.tyrunt.models.Torrent;

/**
 * Created by Charl on 3/24/2016.
 */
@EBean
public class TorrentAdapter extends RecyclerViewBaseAdapter<Torrent, TorrentItemView> {


    private OnTorrentClickedListener onTorrentClickedListener;

    public TorrentAdapter addTorrentAdapter(OnTorrentClickedListener torrentClickedListener){
        this.onTorrentClickedListener = torrentClickedListener;
        return this;
    }

    @Override
    protected TorrentItemView onCreateItemView(ViewGroup parent, int viewType) {
        return TorrentItemView_.build(parent.getContext());
    }

    public void addItem(Torrent torrent){
        super.items.add(torrent);
        notifyItemInserted(getItemCount());
    }

    public Torrent getItemAtPosition(int index){
        return super.items.get(index);
    }

    public void clear(){
        int size = getItemCount();
        super.items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public ArrayList<Torrent> getAllTorrents(){
        return super.items;
    }

    public interface OnTorrentClickedListener{
        void OnTorrentClicked(TorrentItemView torrentItemView, int position);
        boolean OnTorrentLongClicked(TorrentItemView torrentItemView, int position);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<TorrentItemView> viewHolder, final int position) {
        final TorrentItemView view = viewHolder.getView();
        Torrent person = items.get(position);
        view.bind(person);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onTorrentClickedListener != null)
                onTorrentClickedListener.OnTorrentClicked(view, position);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onTorrentClickedListener.OnTorrentLongClicked(view,position);
            }
        });
    }

}

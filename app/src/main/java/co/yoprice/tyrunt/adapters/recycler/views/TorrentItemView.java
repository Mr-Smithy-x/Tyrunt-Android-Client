package co.yoprice.tyrunt.adapters.recycler.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import co.yoprice.tyrunt.R;
import co.yoprice.tyrunt.models.Torrent;

/**
 * Created by Charl on 3/24/2016.
 */
@EViewGroup(R.layout.torrent_item)
public class TorrentItemView extends CardView {

    @ViewById
    AppCompatTextView torrent_title;
    @ViewById
    AppCompatTextView torrent_age;
    @ViewById
    AppCompatTextView torrent_size;
    @ViewById
    AppCompatTextView torrent_author;

    public TorrentItemView(Context context) {
        super(context);
    }

    public void bind(Torrent person) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        torrent_title.setText(person.getTitle());
        torrent_size.setText(person.getSize());
        torrent_age.setText(person.getTime());
        torrent_author.setText(person.getAuthor());
    }
}
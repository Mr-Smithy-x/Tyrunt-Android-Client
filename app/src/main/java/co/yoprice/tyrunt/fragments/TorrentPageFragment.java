package co.yoprice.tyrunt.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.TimerTask;

import co.yoprice.tyrunt.R;
import co.yoprice.tyrunt.activities.TorrentActivity;
import co.yoprice.tyrunt.callback.OnToolbarCallBack;
import co.yoprice.tyrunt.models.Torrent;
import co.yoprice.tyrunt.singleton.TorrentSingleton;
import co.yoprice.tyrunt.utils.TParser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Charl on 3/25/2016.
 */
@EFragment(R.layout.fragment_torrent_page)
public class TorrentPageFragment extends Fragment implements View.OnClickListener {

    @FragmentArg("torrent")
    Torrent torrent;

    @FragmentArg("index")
    int index;


    @ViewById(R.id.torrent_page_appbar)
    AppBarLayout appBarLayout;

    @ViewById(R.id.torrent_page_coord)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @ViewById(R.id.torrent_page_header)
    ImageView imageHeader;

    @ViewById(R.id.torrent_page_user)
    AppCompatTextView torrent_page_user;

    @ViewById(R.id.torrent_page_date)
    AppCompatTextView torrent_page_date;

    @ViewById(R.id.torrent_page_files)
    AppCompatTextView torrent_page_files;

    @ViewById(R.id.torrent_page_leech)
    AppCompatTextView torrent_page_leech;

    @ViewById(R.id.torrent_page_size)
    AppCompatTextView torrent_page_size;

    @ViewById(R.id.torrent_page_seeds)
    AppCompatTextView torrent_page_seeds;

    @ViewById(R.id.torrent_page_title)
    AppCompatTextView torrent_page_title;

    @ViewById(R.id.torrent_page_user_img)
    CircleImageView circleImageView;


    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onToolbarCallBack = (TorrentActivity)context;
    }
    OnToolbarCallBack onToolbarCallBack;

    @ViewById(R.id.torrent_page_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.torrent_page_btn_save)
    FloatingActionButton fab;


    @Click(R.id.torrent_page_btn_sync)
    public void OnSyncClicked(AppCompatButton syncBtn){
        doSync();
    }

    @Background
    public void doSync(){
        String value = String.valueOf(TorrentSingleton.getInstance().getTyruntClient().isConnected());
        String ret = String.valueOf(TorrentSingleton.getInstance().getTyruntClient().sendTorrent(torrent));
        onReturn(value, ret);
    }

    @UiThread
    public void onReturn(String value, String sent){
        System.err.println("Connected: " + value);
        System.err.println("Send Status: " + sent);
    }

    @Click(R.id.torrent_page_btn_download)
    public void OnDownloadClicked(AppCompatButton syncBtn){

    }

    @Click(R.id.torrent_page_btn_save)
    public void OnFavoriteClicked(){
        doFavorite();
    }

    @Background
    public void doFavorite() {
        String value = String.valueOf(TorrentSingleton.getInstance().getTyruntClient().isConnected());
        String ret = String.valueOf(TorrentSingleton.getInstance().getTyruntClient().saveTorrent(torrent));
        onReturn(value, ret);
    }

    @AfterViews
    public void init(){
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getActivity(),android.R.color.transparent));

        getToolbar().setTitle(torrent.getAuthorPostedIn());
        onToolbarCallBack.OnToolbarLoaded(toolbar,index);
        torrent_page_title.setText(torrent.getTitle());
        torrent_page_user.setText(torrent.getAuthor());
        torrent_page_date.setText(torrent.getTime());
        torrent_page_files.setText(String.format(torrent_page_files.getText().toString(),torrent.getFiles()));
        torrent_page_leech.setText(String.format(torrent_page_leech.getText().toString(),torrent.getPeers()));
        torrent_page_seeds.setText(String.format(torrent_page_seeds.getText().toString(),torrent.getSeeds()));
        torrent_page_size.setText(torrent.getSize());
        getUserImage(torrent);
    }


    @Background
    public void getUserImage(Torrent torrent) {
        try {
            String url = TParser.getUserImage(torrent);
            if (url.endsWith(".gif")) {
                load(Glide.with(this).load(Uri.parse(url)).asGif().animate(new ViewPropertyAnimation.Animator() {
                    @Override
                    public void animate(View view) {

                    }
                }).centerCrop(), circleImageView);
                load(Glide.with(this).load(Uri.parse(url)).asBitmap().centerCrop(), imageHeader);
            }else{
                load(Glide.with(this).load(Uri.parse(url)).asBitmap().centerCrop(),circleImageView);
                load(Glide.with(this).load(Uri.parse(url)).asBitmap().centerCrop(), imageHeader);

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @UiThread
    public void load(BitmapRequestBuilder<Uri, Bitmap> uriBitmapBitmapRequestBuilder, ImageView imageView) {
        uriBitmapBitmapRequestBuilder.dontAnimate().listener(new RequestListener<Uri, Bitmap>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(appBarLayout !=null)
                        appBarLayout.setExpanded(true, true);
                    }
                },1000);
                return false;
            }
        }).into(imageView);

    }

    @UiThread
    public void load(GifRequestBuilder<Uri> uriGifRequestBuilder, ImageView imageView) {
        uriGifRequestBuilder.crossFade().listener(new RequestListener<Uri, GifDrawable>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Uri model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(appBarLayout!=null)
                        appBarLayout.setExpanded(true, true);
                    }
                },1000);
                return false;
            }
        }).into(imageView);

    }




    public void setTorrent(@FragmentArg("torrent") Torrent myDateArgument){
        // do something with anotherStringArgument and myDateArgument
    }
    public void setIndex(@FragmentArg("index") int index){
        // do something with anotherStringArgument and myDateArgument
    }

    @Override
    public void onClick(View v) {

    }
}

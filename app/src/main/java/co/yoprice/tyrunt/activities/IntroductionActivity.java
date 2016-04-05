package co.yoprice.tyrunt.activities;

import android.animation.Animator;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.net.InetAddress;
import java.net.SocketException;

import co.yoprice.tyrunt.MainActivity_;
import co.yoprice.tyrunt.R;
import co.yoprice.tyrunt.models.Torrent;
import co.yoprice.tyrunt.network.FindNetwork;
import co.yoprice.tyrunt.network.TyruntClient;
import co.yoprice.tyrunt.singleton.TorrentSingleton;

/**
 * Created by Charl on 3/27/2016.
 */
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_introduction)
public class IntroductionActivity extends AppCompatActivity {

    @ViewById(R.id.activity_intro_img)
    AppCompatImageView appCompatImageView;

    Animation fadeInAnimation;
    @Click(R.id.activity_intro_img)
    public void OnClicked(){
        appCompatImageView.setEnabled(false);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        appCompatImageView.startAnimation(fadeInAnimation);
        initTyrunt();
    }

    @AfterViews
    public void init(){

    }

    @Background
    public void initTyrunt() {
        try {
            TyruntClient.FindTysyncNetwork(new FindNetwork.OnNetworkListener() {
                @Override
                public void OnNetworkFound(InetAddress inetAddress) {
                    TorrentSingleton.getInstance().initTyruntClient(inetAddress.getHostName());
                    OnInitialized();
                }

                @Override
                public void OnNetworkFound(String ipAddress) {
                    appCompatImageView.setImageTintList(ContextCompat.getColorStateList(IntroductionActivity.this, R.color.md_green_700));
                    TorrentSingleton.getInstance().initTyruntClient(ipAddress);
                    OnInitialized();
                }

                @Override
                public void OnNetworkNotFound() {
                    fadeInAnimation.cancel();
                    ReEnable();
                }
            });
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    @UiThread
    public void ReEnable(){
        appCompatImageView.setEnabled(true);
        Snackbar.make(getWindow().getDecorView(),"Could not find your PC,",Snackbar.LENGTH_LONG).show();
        fadeInAnimation.cancel();
    }


    @UiThread
    public void OnInitialized(){

        appCompatImageView.setEnabled(true);
        appCompatImageView.setImageTintList(ContextCompat.getColorStateList(IntroductionActivity.this, R.color.md_green_700));
        appCompatImageView.setBackgroundDrawable(ContextCompat.getDrawable(IntroductionActivity.this, R.drawable.ic_cloud_done_black_24dp));
        // Get the background, which has been compiled to an AnimationDrawable object.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity_.intent(IntroductionActivity.this).start();
                finish();
            }
        }, 3000);
    }
}

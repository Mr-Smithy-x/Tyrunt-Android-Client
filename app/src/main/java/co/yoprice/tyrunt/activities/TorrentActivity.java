package co.yoprice.tyrunt.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;

import co.yoprice.tyrunt.R;
import co.yoprice.tyrunt.adapters.pager.TorrentPagerAdapter;
import co.yoprice.tyrunt.callback.OnToolbarCallBack;
import co.yoprice.tyrunt.fragments.TorrentPageFragment;
import co.yoprice.tyrunt.fragments.TorrentPageFragment_;
import co.yoprice.tyrunt.models.Torrent;

@Fullscreen
@EActivity(R.layout.activity_torrent)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class TorrentActivity extends AppCompatActivity implements OnToolbarCallBack {

    @ViewById
    ViewPager torrent_pager;

    TorrentPagerAdapter torrentPagerAdapter;

    @Extra("torrents")
    ArrayList<Torrent> torrents;

    @Extra("index")
    int index;

    @AfterViews
    public void init(){
        torrentPagerAdapter = new TorrentPagerAdapter(getSupportFragmentManager());
        for(Torrent t : torrents){
            torrentPagerAdapter.add(TorrentPagerAdapter.TorrentFragmentInfo.Builder().setTorrent(t).setTitle(t.getTitle()).setFragment(TorrentPageFragment_.builder().setTorrent(t).setIndex(torrents.indexOf(t)).build()));
        }
        torrent_pager.setAdapter(torrentPagerAdapter);
        torrent_pager.setOffscreenPageLimit(torrents.size());
        torrent_pager.setPageTransformer(false, new ParallaxPagerTransformer(R.id.contentImageView));
        torrent_pager.setCurrentItem(index);
        torrent_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onToolbarSelected(torrentPagerAdapter.getTorrentFragmentInfo().get(position).getFragment().getToolbar());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onToolbarSelected(Toolbar toolbar){
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                    finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean OnToolbarLoaded(Toolbar toolbar, int position) {
        if(position == torrent_pager.getCurrentItem()){
            onToolbarSelected(toolbar);
        }
        return false;
    }
}

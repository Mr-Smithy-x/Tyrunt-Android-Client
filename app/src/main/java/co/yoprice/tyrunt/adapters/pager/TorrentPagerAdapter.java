package co.yoprice.tyrunt.adapters.pager;

import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;

import co.yoprice.tyrunt.fragments.TorrentPageFragment;
import co.yoprice.tyrunt.models.Torrent;

/**
 * Created by Charl on 3/25/2016.
 */
public class TorrentPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<TorrentFragmentInfo> fragments = new ArrayList<TorrentFragmentInfo>();

    public ArrayList<TorrentFragmentInfo> getTorrentFragmentInfo() {
        return fragments;
    }

    public void setTorrentFragmentInfo(ArrayList<TorrentFragmentInfo> fragments){
        this.fragments = fragments;
    }

    public static class TorrentFragmentInfo {
        private String title;
        private TorrentPageFragment fragment;
        private Torrent torrent;

        public static TorrentFragmentInfo Builder() {
            return new TorrentFragmentInfo();
        }

        public String getTitle() {
            return title;
        }

        public TorrentFragmentInfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public TorrentPageFragment getFragment() {
            return fragment;
        }

        public TorrentFragmentInfo setFragment(TorrentPageFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public Torrent getTorrent() {
            return torrent;
        }

        public TorrentFragmentInfo setTorrent(Torrent torrent) {
            this.torrent = torrent;
            return this;
        }
    }

    public TorrentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(TorrentFragmentInfo fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

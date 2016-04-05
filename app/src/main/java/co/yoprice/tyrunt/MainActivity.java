package co.yoprice.tyrunt;

import android.content.DialogInterface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lapism.searchview.view.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.net.InetAddress;
import java.net.SocketException;

import co.yoprice.tyrunt.fragments.SearchResultFragment;
import co.yoprice.tyrunt.fragments.SearchResultFragment_;
import co.yoprice.tyrunt.models.PCInfo;
import co.yoprice.tyrunt.network.FindNetwork;
import co.yoprice.tyrunt.network.TyruntClient;
import co.yoprice.tyrunt.singleton.TorrentSingleton;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TorrentSingleton.OnPCInfoListener, SearchView.SearchMenuListener, SearchView.OnQueryTextListener, SearchView.SearchViewListener {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.fab)
    FloatingActionButton mFab;
    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @ViewById(R.id.nav_view)
    NavigationView mNavView;
    @ViewById(R.id.searchView)
    com.lapism.searchview.view.SearchView mSearchView;
    @ViewById(R.id.contentCoord)
    CoordinatorLayout mCoords;
    @ViewById(R.id.contentAppBar)
    AppBarLayout mAppBar;
    @ViewById(R.id.app_bar_connected_to)
    AppCompatTextView connected;
    @ViewById(R.id.app_bar_pc_os)
    AppCompatTextView os;
    @ViewById(R.id.app_bar_pc_proc)
    AppCompatTextView proc;
    @ViewById(R.id.app_bar_pc_user)
    AppCompatTextView user;

    @AfterViews
    void init(){
        TorrentSingleton.getInstance().addOnPCInfoListener(this);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.setNavigationItemSelectedListener(this);
        mSearchView.setOnSearchMenuListener(this);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchViewListener(this);
        os.setText(TorrentSingleton.getInstance().getConnectedPCInfo().getPc_version());
        user.setText(TorrentSingleton.getInstance().getConnectedPCInfo().getPc_user());
        proc.setText(TorrentSingleton.getInstance().getConnectedPCInfo().getPc_processor());
        connected.setText(String.format(connected.getText().toString(),TorrentSingleton.getInstance().getConnectedPCInfo().getPc_name()));
    }


    public void changeFragment(android.support.v4.app.Fragment frag, String tag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(fm.getFragments() != null){
            for(android.support.v4.app.Fragment f : fm.getFragments()){
                if(f != frag)
                ft.hide(f);
            }
        }
        android.support.v4.app.Fragment f = fm.findFragmentByTag(tag);
        if(f != null){
            ft.show(f);
        }else{
            ft.add(R.id.fragment_holder, frag, tag);
        }
        ft.commit();
    }

    @Click(R.id.fab)
    public void OnFabClicked(FloatingActionButton fab){

    }

    @Override
    public void onBackPressed() {
        if(mSearchView.isSearchOpen()){
            mSearchView.hide(true);
        }
        else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_search:
                mSearchView.show(true);
                onNavigationItemSelected(mNavView.getMenu().findItem(R.id.nav_search));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_search) {
            changeFragment(SearchResultFragment_.builder().build(),"search");
        } else if (id == R.id.nav_about) {
            MsgBox("About","This desktop application allows you to search torrents on kat.cr and download them straight to your pc.").show();
        } else if (id == R.id.nav_credits) {
            MsgBox("Credits","This application was developed by Mr Smithy x");
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public AlertDialog.Builder MsgBox(String title, String message){
        AlertDialog.Builder ab = new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return ab;
    }

    @Override
    public void onMenuClick() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.hide(true);
        ((SearchResultFragment)getSupportFragmentManager().findFragmentByTag("search")).search(query);
        return false;
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {

    }

    @Override
    public void OnRecievedPCInfo(PCInfo pcInfo) {
        OnRecievedPC(pcInfo);
    }

    @UiThread
    public void OnRecievedPC(PCInfo pcInfo) {
        Snackbar.make(getWindow().getDecorView(),pcInfo.getPc_version(), Snackbar.LENGTH_LONG).show();
    }
}

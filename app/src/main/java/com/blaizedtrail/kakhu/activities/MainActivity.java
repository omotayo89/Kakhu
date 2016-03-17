package com.blaizedtrail.kakhu.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.fragments.ActivitiesFragment;
import com.blaizedtrail.kakhu.fragments.CompanyInfoFragment;
import com.blaizedtrail.kakhu.fragments.HomeFragment;
import com.blaizedtrail.kakhu.fragments.OverviewFragment;
import com.blaizedtrail.kakhu.utils.Agent;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Handler handler;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        Company company= CompanyHandler.get().getCompany();
        if (company==null){
            Intent intent=new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0,0);
            return;
        }
        Agent agent=company.getAgent();
        if (agent==null){
            Intent intent=new Intent(this,AgentInfo.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0,0);
            return;
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(company.getCompany_name()+"- Dashboard");
        instantiateVariables(toolbar);
    }
    private void instantiateVariables(Toolbar toolbar){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        handler=new Handler();
        ViewPager viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragsAdapter(getSupportFragmentManager()));
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
    class FragsAdapter extends FragmentPagerAdapter{

        public FragsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:return new OverviewFragment();
                case 1:return new ActivitiesFragment();
                case 2:return new CompanyInfoFragment();
            }
            return null;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:return "Overview";
                case 1:return "Activities";
                case 2:return "Company info";
                default:return "No title";
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        final Intent[] intent = {null};
        switch (id){
            case R.id.nav_inventory:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(MainActivity.this, InventoryActivity.class);
                        startActivity(intent[0]);
                        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                    }
                }, 200);
                break;
            case R.id.nav_transaction:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] =new Intent(MainActivity.this,TransactionActivity.class);
                        startActivity(intent[0]);
                        overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left);
                    }
                }, 200);
                break;
            case R.id.nav_carts:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       intent[0]=new Intent(MainActivity.this,CartsActivity.class);
                        startActivity(intent[0]);
                        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
                    }
                },200);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_MENU:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }else {
                    drawer.openDrawer(GravityCompat.START);
                }
                return true;
            default:return super.onKeyDown(keyCode, event);
        }
    }
}

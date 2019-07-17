package wencongchio.com.ucsibustracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import wencongchio.com.ucsibustracker.Fragment.FeedbackFragment;
import wencongchio.com.ucsibustracker.Fragment.HomeFragment;
import wencongchio.com.ucsibustracker.Fragment.ScheduleFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragmentSelected = null;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        SystemClock.sleep(1500);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navigationHeaderView = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView)navigationHeaderView.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count == 7){
                    Toast.makeText(MainActivity.this, "Produced by WencongChio 2018 V ^.^ V", Toast.LENGTH_LONG).show();
                    count = 0;
                }
            }
        });

        backToHome();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentSelected instanceof HomeFragment){
                super.onBackPressed();
            }else{
                backToHome();
            }

        }
    }

    private void backToHome(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        fragmentSelected = new HomeFragment();

        if (fragmentSelected != null){
            getSupportActionBar().setTitle(getResources().getString(R.string.title_home));
            navigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentSelected).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        String title = null;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            fragmentSelected = new HomeFragment();
            title = getResources().getString(R.string.title_home);

        } else if (id == R.id.nav_location) {
            Intent intent = new Intent(this, LocationActivity.class);
            item.setCheckable(false);
            startActivity(intent);

            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_schedule) {
            fragmentSelected = new ScheduleFragment();
            title = getResources().getString(R.string.title_schedule);
        } else if (id == R.id.nav_feedback) {
            fragmentSelected = new FeedbackFragment();
            title = getResources().getString(R.string.title_feedback);
        }

        if (fragmentSelected != null){
            getSupportActionBar().setTitle(title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentSelected).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

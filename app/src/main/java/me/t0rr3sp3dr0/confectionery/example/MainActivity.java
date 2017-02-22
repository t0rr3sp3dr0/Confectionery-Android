package me.t0rr3sp3dr0.confectionery.example;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import java.util.HashMap;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.confectionery.example.databinding.ActivityMainBinding;
import me.t0rr3sp3dr0.confectionery.utilities.NumberPickerDialog;

public class MainActivity extends CandyActivity<ActivityMainBinding>
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().appBarMain.toolbar);

        getBinding().appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                startActivity(EmptyActivity.class, new HashMap<String, Object>());
                                new NumberPickerDialog(MainActivity.this, new NumberPickerDialog.OnValueSetListener() {
                                    @Override
                                    public void onNumberSet(NumberPicker view, int value) {
                                        Log.d("NumberPickerDialog", Integer.toString(value));
                                    }
                                }, 5, 0, Byte.MAX_VALUE).show();
                            }
                        }).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, getBinding().drawerLayout, getBinding().appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getBinding().drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        getBinding().navView.setNavigationItemSelectedListener(this);

//        Fragment fragment = fragmentManager.findFragmentById(R.id.content_main);
//        if (fragment == null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("str", "Olá Mundo!");
//            addFragment(R.id.content_main, new BlankFragment(map), true);
//
//            //noinspection unchecked
//            replaceFragment(R.id.content_main, new ItemFragment((Map) DummyContent.ITEM_MAP, 1, DummyContent.ITEMS, false), true, true);
//        }
    }

    @Override
    public void onBackPressed() {
        if (getBinding().drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getBinding().drawerLayout.closeDrawer(GravityCompat.START);
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
            startActivity(BasicActivity.class, new HashMap<String, Object>());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        getBinding().drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri, @Nullable Object object) {
        Log.d("#onFragmentInteraction", uri.toString());
        if (object != null)
            Log.d("#onFragmentInteraction", object.toString());
    }

    @Override
    public void onListFragmentInteraction(Class<?> clazz, @Nullable Object object) {
        Log.d("#onFragmentInteraction", clazz.toString());
        if (object != null)
            Log.d("#onFragmentInteraction", object.toString());

        Log.d("#onFragmentInteraction", (clazz.isAssignableFrom(ItemFragment.class)) ? "Is ItemFragment" : "Not ItemFragment");
    }
}

package com.up_coders.astan;

import android.app.*;
import android.support.v4.app.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.widget.AdapterView;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.up_coders.astan.model.Martyr;
import com.up_coders.astan.parser.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ShowFragment.onMartyrSelectedListener {

    FragmentManager fm;
    FragmentTransaction ft;

    TextView maintextview;
    //TextView output;
    ProgressBar pb;
    List<MyTask> tasks;

    List<Martyr> martyrList;

    public Integer martyrID;

//    public static final String baseURl = "http://192.168.43.85:13952/api/cms/";
    public static final String baseURl = "http://astan.sellonclouds.com/api/cms/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //Fragments


        //Custom List
        final String[] itemname = {};

        Integer[] itemicon = {};

        CustomList listAdapter = new CustomList(MainActivity.this, itemname, itemicon);

        ListView list = (ListView) findViewById(R.id.myList);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Initialize the text view for vertical scrolling
        maintextview = (TextView) findViewById(R.id.maintextview);
        maintextview.setMovementMethod(new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();

    }


    public void onMartyrSelected(int ID){
        martyrID = ID;
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.containerView, new TabFragment()).commit();

    }


    protected void updateDisplay() {
        //use MartyrAdapter to show data
        MartyrAdapter adapter = new MartyrAdapter(this, R.layout.mylist, martyrList);
        ListView list = (ListView) findViewById(R.id.myList);
        list.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            return true;

        } else {

            return false;
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
        if (isOnline()) {
            requestData(baseURl + "/find");
        } else {
            Toast.makeText(this, "Network isn't available.", Toast.LENGTH_LONG).show();
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 123) {
            maintextview = (TextView) findViewById(R.id.maintextview);
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");

                maintextview.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                maintextview.setText("There was an error. Try again later.");
            }
        }
    }//onActivityResult

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            // Call fragment
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.containerView, new ShowFragment()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show();
//            ContentFragment fragment = new ContentFragment();
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frame,fragment);
//            fragmentTransaction.commit();
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.containerView, new TabFragment()).commit();

        } else if (id == R.id.nav_scanner) {
            Log.d("call", "QRScanner called.");

            Intent intent = new Intent(MainActivity.this, QRreader.class);
            startActivityForResult(intent, 123);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class MyTask extends AsyncTask<String, String, List<Martyr>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay();
            super.onPreExecute();

            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Martyr> doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);

            martyrList = MartyrJSONParser.parseFeed(content);
//            Log.d("json", content);

            return martyrList;
        }

        @Override
        protected void onPostExecute(List<Martyr> result) {
            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                Toast.makeText(MainActivity.this, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }

            martyrList = result;
            updateDisplay();
        }

        @Override
        protected void onProgressUpdate(String... values) {
//            updateDisplay();
        }
    }

    Integer getMartyrID(){
        return martyrID;
    }
}

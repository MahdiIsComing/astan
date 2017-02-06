package ir.upcoders.sheydaee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.up_coders.astan.R;

import java.util.ArrayList;
import java.util.List;

import ir.upcoders.sheydaee.model.Martyr;
import ir.upcoders.sheydaee.parser.MartyrJSONParser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ShowFragment.onMartyrSelectedListener {

    FragmentManager fm;
    FragmentTransaction ft;

    private ShareActionProvider mShareActionProvider;


    TextView maintextview;
    //TextView output;
    ProgressBar pb;
    List<MyTask> tasks;

    List<Martyr> martyrList;

    public Integer martyrID;

    //    public static final String baseURl = "http://192.168.43.85:13952/api/cms/";
    public static final String baseURl = "http://martyr.sellonclouds.com/api/martyrs/";

    public static final String basePath = Environment.getExternalStorageDirectory().getPath() +
            "/Sheydaee";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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

        //Check for internet connection and Notify User.
        if (!isOnline(this))
            Toast.makeText(MainActivity.this, R.string.internetMessage, Toast.LENGTH_LONG).show();


    }


    public void onMartyrSelected(int ID) {
        martyrID = ID;
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.containerView, new TabFragment()).addToBackStack("TabFragment").commit();

    }


    protected void updateDisplay() {
        //use MartyrAdapter to show data
        MartyrAdapter adapter = new MartyrAdapter(this, R.layout.mylist, martyrList);
        ListView list = (ListView) findViewById(R.id.myList);
        list.setAdapter(adapter);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            return true;

        } else {

            return false;
        }
    }

    @Override
    public void onBackPressed() {

        //Going to last fragment
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (isOnline()) {
//            requestData(baseURl + "/find");
//        } else {
//            Toast.makeText(this, "Network isn't available.", Toast.LENGTH_LONG).show();
//        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.menu_item_share) {
            doShare();


        }
        return super.onOptionsItemSelected(item);
    }

    public void doShare() {
        // populate the share intent with data

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "thihsodi");
        mShareActionProvider.setShareIntent(intent);

    }
    public void requestData(String uri) {
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
                //TODO: mahdi: run TabFragment
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
            Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
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


    public class MyTask extends AsyncTask<String, String, List<Martyr>> {

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

    public Integer getMartyrID() {

        return martyrID;
    }

    public List<Martyr> getMartyr() {
        return martyrList;
    }

    public void setMartyrList(List<Martyr> martyrList) {
        this.martyrList = martyrList;
    }

}

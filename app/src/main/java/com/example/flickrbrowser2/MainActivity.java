package com.example.flickrbrowser2;

import static android.widget.Toast.*;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlikrJsonData.OnDownloadAvailable,RecyclerItemClickListerner.OnRecyclerClickListerner{
    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;
private SearchView searchView;
private String queryResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
        activateToolBar(false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       recyclerView.addOnItemTouchListener(new RecyclerItemClickListerner(this,recyclerView,this));
        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);

//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute("https://www.flickr.com/services/feeds/photos_public.gne?&format=json&nojsoncallback=1");


        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
//        Log.d(TAG, "onResume: starts");
        super.onResume();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String result=sharedPreferences.getString(FLICKR_QUERY,"");
        if(result.length()>0)
        {
            GetFlikrJsonData getFlickrJsonData = new GetFlikrJsonData(this, "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true);
//        getFlickrJsonData.executeOnSameThread("android, nougat");
            getFlickrJsonData.execute(result);
        }
//        GetFlikrJsonData getFlickrJsonData = new GetFlikrJsonData(this, "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true);
////        getFlickrJsonData.executeOnSameThread("android, nougat");
//        getFlickrJsonData.execute(queryResult);

//        Log.d(TAG, "onResume ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String s) {
        GetFlikrJsonData getFlickrJsonData = new GetFlikrJsonData(MainActivity.this, "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true);
//        getFlickrJsonData.executeOnSameThread("android, nougat");
        getFlickrJsonData.execute(s);
        queryResult=s;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
});



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
            Intent intent=new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent=new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned: returned");
        return super.onOptionsItemSelected(item);
    }

    public void onDownloadAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDownloadAvailable: starts");
        if (status == DownloadStatus.OK) {
            mFlickrRecyclerViewAdapter.loadNewData(data);
        } else {
            Log.e(TAG, "onDownloadComplete: failed with status" + status);
        }
        Log.d(TAG, "onDownloadAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
//        Toast.makeText(this, "Normal tap at "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
//        Toast.makeText(this, "long tap at "+position, Toast.LENGTH_SHORT).show();
Intent intent=new Intent(this,PhotoDetailActivity.class);
intent.putExtra(PHOTO_TRANSFER,mFlickrRecyclerViewAdapter.getPhoto(position));
    startActivity(intent);
    }
}


package com.example.latek.breakingnews;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsKeys>>, SharedPreferences.OnSharedPreferenceChangeListener {
//Sharedpreferences on destroy and onchanged codes were taken from the mentor Ahmet's comments.
    private static final String newsUrl =
"https://content.guardianapis.com/search?from-date=2018-08-01&to-date=2018-12-01&use-date=published&show-tags=contributor&page-size=100&api-key=3f88d598-113f-4974-9b5f-44661ce1722f";
    private static final int newsComing= 0;
    private NewsAdapter newsAdapter;
    private TextView noNews;
    private  SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsListView = (ListView) findViewById(R.id.list_projector);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        newsAdapter = new NewsAdapter(this, new ArrayList<NewsKeys>());
        newsListView.setAdapter(newsAdapter);
        noNews = (TextView) findViewById(R.id.no_news);
        newsListView.setEmptyView(noNews);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsKeys currentNews = newsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getnUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                websiteIntent.setData(newsUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(newsComing, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.circle);
            loadingIndicator.setVisibility(View.GONE);
            noNews.setText(R.string.there_must_be_smth_wrong_with_your_connection_or_host_or_i_dk);
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s){
        if (s.equals(getString(R.string.settings_section_by_key)) || s.equals(getString(R.string.settings_type_by_label))){ getLoaderManager().restartLoader(0,null,this); }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public Loader<List<NewsKeys>> onCreateLoader(int i, Bundle bundle) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String choSection = sharedPreferences.getString(getString(R.string.settings_section_by_key), "");
        String choType = sharedPreferences.getString(getString(R.string.settings_type_by_label),"");
        Uri baseUri = Uri.parse(newsUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if (!choSection.isEmpty()) {
                uriBuilder.appendQueryParameter("q", choSection);
        }
        if (!choType.isEmpty()) {
            uriBuilder.appendQueryParameter("q", choType);
        } else {
            return new BackgroundJob(this, newsUrl);
        }
        return new BackgroundJob(this, newsUrl);
    }
    @Override
    public void onLoadFinished(Loader<List<NewsKeys>> loader, List<NewsKeys> news) {
        View loadingIndicator = findViewById(R.id.circle);
        loadingIndicator.setVisibility(View.GONE);
        if (news != null && !news.isEmpty()) {
            newsAdapter.addAll(news);
        }else {
            noNews.setText(R.string.there_must_be_smth_wrong_with_your_connection_or_host_or_i_dk);
            noNews.setVisibility(View.VISIBLE);
            newsAdapter.clear();

        }
    }
    @Override
    public void onLoaderReset(Loader<List<NewsKeys>> loader) {
        newsAdapter.clear();
    }
    // lesson 7 codes
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if (id == R.id.action_reset) {
            sharedPreferences.edit().clear().apply();
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.getlosthere.ohmynews.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.getlosthere.ohmynews.R;
import com.getlosthere.ohmynews.adapters.ArticleArrayAdapter;
import com.getlosthere.ohmynews.clients.NewsAPIClient;
import com.getlosthere.ohmynews.fragments.FilterFragment;
import com.getlosthere.ohmynews.listeners.EndlessScrollListener;
import com.getlosthere.ohmynews.models.Article;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterFragment.FilterListener{
//    EditText etQuery;
    GridView gvResults;
//    Button btnSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    // variables to hold filters
    private String filterSortOrder = "Oldest";
    private long filterDate = -1;
    private boolean filterArts = false;
    private boolean filterFashion = false;
    private boolean filterSports = false;
    private final int REQUEST_CODE = 30;
    String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.news);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setupViews();
    }

    public void setupViews() {
//        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
//        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        // hook up listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create intent
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);

                // get article to display
                Article article = articles.get(position);

                // pass in article into the intent
                i.putExtra("article",article);

                // launch the activity
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                int articleCount = adapter.getCount();
                loadNewsPageFromAPI(page);
                return adapter.getCount() > articleCount;
            }
        });
    }

    public void loadNewsPageFromAPI(int page) {
        RequestParams params = setupParams(page);
        if (isNetworkAvailable() && isOnline()) {
            NewsAPIClient.get("articlesearch.json", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());

                    JSONArray articleJSONResults = null;
                    try {
                        articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                        if (articleJSONResults.length() > 0) {
                            adapter.addAll(Article.fromJSONArray(articleJSONResults));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;
                RequestParams params = setupParams(0);

                if (isNetworkAvailable() && isOnline()) {
                    NewsAPIClient.get("articlesearch.json", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", response.toString());

                            JSONArray articleJSONResults = null;

                            try {
                                articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                                adapter.clear();
                                adapter.addAll(Article.fromJSONArray(articleJSONResults));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

        if (id == R.id.miFilter) {
            FragmentManager fm = getSupportFragmentManager();
            FilterFragment filterDialogFragment = FilterFragment.newInstance("Filter Settings",filterSortOrder,filterDate,filterArts,filterFashion,filterSports);
            filterDialogFragment.show(fm, "fragment_filter");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            filterArts = data.getExtras().getBoolean("arts");
            filterSports = data.getExtras().getBoolean("sports");
            filterFashion = data.getExtras().getBoolean("fashion");
            filterSortOrder = data.getExtras().getString("sort_order");
            filterDate = data.getExtras().getLong("date",-1);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }


    public RequestParams setupParams(int page){
        RequestParams p = new RequestParams();
        p.put("page",page);
        p.put("q",currentQuery);
        p.put("sort",filterSortOrder.toLowerCase());
        if (filterDate != -1 ) {
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
            c.setTimeInMillis(filterDate);
            p.put("begin_date",sdFormat.format(c.getTime()));
        }
        String filterNewsDesk = "";
        if(filterArts) {
            filterNewsDesk += " \"Arts\"";
            p.put("fq", "news_desk:("+filterNewsDesk+")");
        }
        if(filterFashion) {
            filterNewsDesk += " \"Fashion & Style\"";
            p.put("fq", "news_desk:("+filterNewsDesk+")");
        }
        if(filterSports) {
            filterNewsDesk += " \"Sports\"";
            p.put("fq", "news_desk:("+filterNewsDesk+")");
        }
        return p;
    }

    @Override
    public void  onFinishedFilterDialog(String sortOrder, Long date, boolean arts, boolean fashion, boolean sports) {
        filterArts = arts;
        filterSports = sports;
        filterFashion = fashion;
        filterSortOrder = sortOrder;
        filterDate = date;
    }

}

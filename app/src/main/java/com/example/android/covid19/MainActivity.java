package com.example.android.covid19;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;

    private TextView mNoConnectionTextView;
    private ImageView mNoConnectionImageView;

    /**
     * Constant value for the covid loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int CORONA_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.swipeLayout);

        mNoConnectionTextView = (TextView) findViewById(R.id.no_connection_text);
        mNoConnectionImageView = (ImageView) findViewById(R.id.no_connection_image);

        mNoConnectionImageView.setVisibility(View.GONE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Data Source: https://ncov2019.live/data/global ", Snackbar.LENGTH_LONG).setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar = Snackbar.make(view, "An App by Allen Benny", Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.DKGRAY);
                                snackbar.setTextColor(Color.WHITE);
                                snackbar.show();
                            }
                        }).show();

            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ParseAdapter(parseItems, this);
        recyclerView.setAdapter(adapter);

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            loadItems();

        } else {

            progressBar.setVisibility(View.GONE);
            mNoConnectionImageView.setVisibility(View.VISIBLE);
            mNoConnectionTextView.setVisibility(View.VISIBLE);
            mNoConnectionTextView.setText(R.string.no_internet_connection);

        }
        Log.e("Allen_Benny", "Loader Manager was Successfully called");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });
    }

    private void loadItems() {

        Content content = new Content();
        content.execute();
    }


    private class Content extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNoConnectionImageView.setVisibility(View.GONE);
            mNoConnectionTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
            parseItems.clear();
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                    int count =1;
                    String url = "https://ncov2019.live/data/world";
                    Document doc = Jsoup.connect(url).get();

                    Log.d("items","Items 0" + doc);

                    Element data = doc.getElementById("sortable_table_world");
                    Log.d("items","Items 0: " + data);

                    Elements data1 = doc.select("tbody");
                    Log.d("items","Items 1: " + data1);

                    Elements rows = data1.select("tr:gt(2)");
                    Log.d("items","Items 2: " + rows);

                int country_total_count = 0;

                for (Element row : rows){

                        Log.d("items","Items 3: " + row);
                        Elements cells = row.select("td");
                        Log.d("items","Items 4: " + cells);

                            country_total_count++;
                            String imgUrl = null;
                            String country_name = null;
                            String total_count = null;
                            String total_deaths = null;

                            if(country_total_count > 50) break;

                            //Log.d("items","Items 5: " + cell);
/*
                            if (cell.className().equals("text--gray") ){
*/

                                Log.d("items","Items 6: " + cells.text());
                                country_name = cells.get(0).text();
                                Log.d("items","Items 6.1: " + country_name);
                                String[] parts = country_name.split("(?<=★ )");
                                Log.d("items","Items 6.1 : " + parts[0]);
                                country_name = parts[1];
                                Log.d("items","Items 6.2 : " + country_name);

                                String mod_url = country_name.replace(" ","+");
                                Log.d("items","Items 6.3 : " + mod_url);

                                String img_Url = "https://www.iconfinder.com/search/?q=iconset%3A195-flat-flag-psd-icons+" + mod_url + "&from=navbar";
                                Document img = Jsoup.connect(img_Url).get();

                                Elements img_data = img.select("div.icon-preview-bg");
                                Log.d("items","Items 6.4 : " + img_data);

                                String img_final_url = img_data.select("div.icon-preview-bg").select("img").attr("src");
                                Log.d("items","Items 6.5 : " + img_final_url);

                                if(img_final_url != null) {

                                    Log.d("items","Items 6.6 : " + img_final_url);
                                    imgUrl = img_final_url;
                                }

                                if(img_final_url.equals("")){

                                    Log.d("items","Items 6.7 : " + img_final_url);
                                    imgUrl = "http://icons.iconarchive.com/icons/hopstarter/sleek-xp-software/64/Windows-Close-Program-icon.png";
                                    Log.d("items","Items 6.8 : " + imgUrl);

                                }
                                Log.d("items","Items 6.9 : " + imgUrl);


                    /*        }*/

/*                            if(cell.className().equals("text--green") ){*/

                                Log.d("items","Items 7: " + cells.text());
                                total_count = cells.get(1).text() + " cases";
                                Log.d("items","Items 7.1: " + total_count);

                                Log.d("items","Items 8: " + cells.text());
                                total_deaths = cells.get(4).text();
                                Log.d("items","Items 8.1: " + total_deaths);


                    /*                            }*/

                            if( country_name != null && total_count != null && total_deaths != null) {
                                Log.d("items", "Count = " + count++);
                                Log.d("items", "Items 9: " + "Name: " + country_name + " Count: " + total_count + " Deaths: " + total_deaths);
                                parseItems.add(new ParseItem(imgUrl, country_name, total_count, total_deaths));
                            }

                        }
                        Log.d("items","Items 10");

/*
                try {

                    String url = "https://ncov2019.live/data/global";
                    Document doc = Jsoup.connect(url).get();

                    Log.d("items","Items 1");

                    Element data = doc.getElementById("table.sortable_table_global");
                    Elements rows = data.select("tr");

                    Log.d("items","Items 2" + data);

                    int size = data.size();
                    for (int i = 0; i < size; i++){

                        Log.d("items","Items 3");
                        String imgUrl = data.select("std.text--gray text--gray").select("span").eq(i).text();
                        String country_name = data.select("td.text--gray text--gray").text();
                        String total_count = data.select("td.text--green text--green sorting_1").text();
                        Log.d("items","Items 4");
                        parseItems.add(new ParseItem(imgUrl, country_name, total_count));
                        Log.d("items","Image" + imgUrl + ".Country Name: " + country_name + ".Total Count: " + total_count);
                    }
*/

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}

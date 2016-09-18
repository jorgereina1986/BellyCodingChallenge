package com.jorgereina.bellycodingchallenge.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.jorgereina.bellycodingchallenge.BuildConfig;
import com.jorgereina.bellycodingchallenge.R;
import com.jorgereina.bellycodingchallenge.adapter.YelpAdapter;
import com.jorgereina.bellycodingchallenge.ui.DividerItemDecorator;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity Jorge";
    private static final String CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    private static final String CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;
    private static final String TOKEN = BuildConfig.TOKEN;
    private static final String TOKEN_SECRET = BuildConfig.TOKEN_SECRET;
    double latitude;
    double longitude;
    private TextView locationTv;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private YelpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        coordinates();
        yelpConnection();

    }

    private void yelpConnection() {

        Map<String, String> params = new HashMap<>();
        params.put("term", "");
        params.put("sort", "1");

        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        YelpAPI yelpAPI = apiFactory.createAPI();

        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(40.6947417)
                .longitude(-73.9096657)
                .build();


        Call<SearchResponse> call = yelpAPI.search(coordinate, params);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                Log.d(TAG, "response: " + response.body()+"");

                SearchResponse searchResponse = response.body();
                ArrayList<Business> businesses = searchResponse.businesses();

                recyclerView.setAdapter(new YelpAdapter(getApplicationContext(), businesses));

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        });

    }


    private void coordinates() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }

    private void initializeViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecorator());

    }

}

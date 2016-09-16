package com.jorgereina.bellycodingchallenge.activities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jorgereina.bellycodingchallenge.BuildConfig;
import com.jorgereina.bellycodingchallenge.R;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity Jorge";
    private static final String CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    private static final String CONSUMER_SECRET =BuildConfig.CONSUMER_SECRET;
    private static final String TOKEN = BuildConfig.TOKEN;
    private static final String TOKEN_SECRET = BuildConfig.TOKEN_SECRET;
    private double latitude;
    private double longitude;
    private TextView locationTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationTv = (TextView) findViewById(R.id.location);

        currentLocation();
        networkConnection();




    }

    private void networkConnection(){


        Map<String,String> params = new HashMap<>();
        params.put("term","");

        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        YelpAPI yelpAPI = apiFactory.createAPI();

        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        Call<SearchResponse> call = yelpAPI.search(coordinate,params);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                Log.d(TAG,"response: " + response.body().businesses().get(0));
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        });

    }

    private void currentLocation(){

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                locationTv.setText(latitude+", "+longitude);



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

    }
}

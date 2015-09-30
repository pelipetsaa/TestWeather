package com.example.testapplication.activity;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.Constants;
import com.example.testapplication.R;
import com.example.testapplication.model.WeatherResponse;
import com.example.testapplication.net.NetworkClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    public static final String TAG = "MapActivity";

    private NetworkClient mNetworkClient;
    private GoogleMap mMap;
    private ImageView mIcon;
    private TextView mText;
    private boolean isReceived = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initViews();
        init();
        initMap();

    }

    private void init(){
        mNetworkClient = NetworkClient.getInstance();
    }

    private void initViews(){
        mIcon  = (ImageView)findViewById(R.id.icon);
        mText = (TextView)findViewById(R.id.text);
    }

    private void initMap() {
        if (mMap == null) {
            SupportMapFragment  mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setOnMyLocationChangeListener(this);
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    public void onMyLocationChange(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 14));
        if(!isReceived){
            mNetworkClient.getRestApi().getWeather(location.getLatitude(), location.getLongitude(), new Callback<WeatherResponse>() {
                @Override
                public void success(WeatherResponse weatherResponse, Response response) {
                    Log.d(TAG, "get Weather succes");
                    Picasso.with(MapActivity.this).load(String.format(Constants.IMAGE_TEMPLETE_URL, weatherResponse.getWeather().get(0).icon)).into(mIcon);
                    mText.setText(weatherResponse.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });
            isReceived = true;
        }

    }
}

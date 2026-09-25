package com.example.testapplication.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.testapplication.BuildConfig;
import com.example.testapplication.Constants;
import com.example.testapplication.R;
import com.example.testapplication.model.WeatherResponse;
import com.example.testapplication.net.NetworkClient;
import com.example.testapplication.utils.WeatherFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "MapActivity";

    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private NetworkClient mNetworkClient;
    private FusedLocationProviderClient mLocationClient;
    private GoogleMap mMap;
    private ImageView mIcon;
    private TextView mText;
    private Call<WeatherResponse> mWeatherCall;
    private boolean isReceived = false;

    private final ActivityResultLauncher<String[]> mPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (hasLocationPermission()) {
                    onLocationPermissionGranted();
                } else {
                    mText.setText(R.string.location_permission_denied);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initViews();
        init();
        initMap();
    }

    @Override
    protected void onDestroy() {
        if (mWeatherCall != null) {
            mWeatherCall.cancel();
        }
        super.onDestroy();
    }

    private void init() {
        mNetworkClient = NetworkClient.getInstance(BuildConfig.OWM_API_KEY);
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initViews() {
        mIcon = findViewById(R.id.icon);
        mText = findViewById(R.id.text);
        mText.setOnClickListener(v -> {
            if (!isReceived) {
                requestLocation();
            }
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(() -> {
            if (!isReceived) {
                requestLocation();
            }
            return false;
        });
        if (hasLocationPermission()) {
            onLocationPermissionGranted();
        } else {
            mPermissionLauncher.launch(LOCATION_PERMISSIONS);
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void onLocationPermissionGranted() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        requestLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        if (!hasLocationPermission()) {
            mPermissionLauncher.launch(LOCATION_PERMISSIONS);
            return;
        }
        mText.setText(R.string.weather_loading);
        mLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location == null) {
                        mText.setText(R.string.location_unavailable);
                    } else {
                        onLocationReceived(location);
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.w(TAG, "get location failed", e);
                    mText.setText(R.string.location_unavailable);
                });
    }

    private void onLocationReceived(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
        if (isReceived) {
            return;
        }
        if (BuildConfig.OWM_API_KEY.isEmpty()) {
            mText.setText(R.string.weather_no_api_key);
            return;
        }
        loadWeather(latLng);
    }

    private void loadWeather(LatLng latLng) {
        if (mWeatherCall != null) {
            mWeatherCall.cancel();
        }
        mText.setText(R.string.weather_loading);
        mWeatherCall = mNetworkClient.getRestApi().getWeather(latLng.latitude, latLng.longitude,
                Constants.UNITS, Locale.getDefault().getLanguage());
        mWeatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                WeatherResponse weatherResponse = response.body();
                if (!response.isSuccessful() || weatherResponse == null) {
                    Log.w(TAG, "get Weather failed, HTTP " + response.code());
                    mText.setText(R.string.weather_error);
                    return;
                }
                Log.d(TAG, "get Weather success");
                isReceived = true;
                showWeather(weatherResponse);
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                if (call.isCanceled() || isFinishing() || isDestroyed()) {
                    return;
                }
                Log.w(TAG, "get Weather failed", t);
                mText.setText(R.string.weather_error);
            }
        });
    }

    private void showWeather(WeatherResponse weatherResponse) {
        WeatherResponse.Weather weather = weatherResponse.getFirstWeather();
        if (weather != null && weather.icon != null) {
            Picasso.get().load(String.format(Constants.ICON_URL_TEMPLATE, weather.icon)).into(mIcon);
        }
        mText.setText(WeatherFormatter.format(weatherResponse, Locale.getDefault()));
    }
}

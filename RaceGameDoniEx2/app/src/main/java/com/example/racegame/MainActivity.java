package com.example.racegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private LocationRequest locationRequest;

    public static SharedPreferences sharedPreferences;
    public static String current_Location;
    public static double lati = 0.0, longi = 0.0;
    FusedLocationProviderClient mFusedLocationClient;
    public static WifiManager wifiManager;
    ProgressBar pg;
    TextView tv;
    Button igbut;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocPermissions();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ActionBar actionBar = getSupportActionBar();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        tv = findViewById(R.id.loctxt);
        pg = findViewById(R.id.pg);
        igbut = findViewById(R.id.igbut);
        igbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
                igbut.setVisibility(View.INVISIBLE);
                kickstart();
            }
        });
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                 startLocationUpdates();
                       Toast.makeText(getApplicationContext(), "Entered in BLE Permission", Toast.LENGTH_SHORT).show();
                callback();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, 1);
                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)// && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    {
                        return;
                    }
                    mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location loc = task.getResult();
                            if (loc == null) {
                                current_Location = "Not found";
                            } else {
                                current_Location = loc.getLatitude() + "," + loc.getLongitude();
                                lati = loc.getLatitude();
                                longi = loc.getLongitude();
                                pg.setVisibility(View.INVISIBLE);
                                tv.setVisibility(View.INVISIBLE);
                                igbut.setVisibility(View.INVISIBLE);
                                kickstart();
                            }
                        }
                    });

                } else
                    for (Location loc : locationResult.getLocations()) {

                        current_Location = loc.getLatitude() + "," + loc.getLongitude();
                        lati = loc.getLatitude();
                        longi = loc.getLongitude();
                        pg.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.INVISIBLE);
                        igbut.setVisibility(View.INVISIBLE);
                        kickstart();
                    }

            }
        };

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


    }

    Handler handler = new Handler();
    Boolean isFirstTime = true;
    Task task;
    PendingIntent pendingIntent;

    private void callback() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }


        if (isFirstTime) {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location loc = task.getResult();

                    if (loc == null) {
                        current_Location = "Not found";
                    } else {
                        current_Location = loc.getLatitude() + "," + loc.getLongitude();
                        lati = loc.getLatitude();
                        longi = loc.getLongitude();
                        pg.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.INVISIBLE);
                        igbut.setVisibility(View.INVISIBLE);
                        kickstart();
                    }
                }
            });
        }
        task = mFusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        isFirstTime = false;
    }

    private static final String[] LOC_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private boolean checkPermission(String permissions[]) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void checkLocPermissions() {
        boolean permissionsGranted;
        permissionsGranted = checkPermission(LOC_PERMISSIONS);
        if (!permissionsGranted)
            ActivityCompat.requestPermissions(this, LOC_PERMISSIONS, 20);
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            15);
                } else {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                             kickstart();
                        }
                    }, 900);
                }
            } else {
                handler.postDelayed(new Runnable() {
                    public void run() {
                             kickstart();
                    }
                }, 900);
            }
        }
    }

    FragmentManager fragmentManager;

    private void kickstart() {
        if (isLocationEnabled()) {
            if (!wifiManager.isWifiEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Intent mIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                    WiFiResultLauncher.launch(mIntent);

                } else
                    wifiManager.setWifiEnabled(true);
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainMenuFragment myFragment = new MainMenuFragment();
                fragmentTransaction.add(R.id.fragment_container, myFragment);
                fragmentTransaction.commit();
            } else
                fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MainMenuFragment myFragment = new MainMenuFragment();
            fragmentTransaction.add(R.id.fragment_container, myFragment);
            fragmentTransaction.commit();
        } else {
            enableGPS();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 20) {
            boolean hasGrantedPermissions = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false;
                    break;
                }
            }

            if (!hasGrantedPermissions) {
                Toast.makeText(this, "Location Permissions not granted", Toast.LENGTH_SHORT);

                finish();
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            15);
                }
                mFusedLocationClient.removeLocationUpdates(locationCallback);
                task = mFusedLocationClient.requestLocationUpdates(locationRequest,
                        locationCallback,
                        Looper.getMainLooper());
            }

        } else if (requestCode == 15) {
            handler.postDelayed(new Runnable() {
                public void run() {

                    kickstart();
                }
            }, 900);
        }


    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void enableGPS() {


        try {
            new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {

                    if (isGPSEnable) {
                        if (!wifiManager.isWifiEnabled()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            } else
                                wifiManager.setWifiEnabled(true);
                            kickstart();
                        } else
                            kickstart();
                    }
                }


            });
        } catch (Exception e) {
            Log.i(e.getMessage(), e.getLocalizedMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (!wifiManager.isWifiEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    } else
                        wifiManager.setWifiEnabled(true);
                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    task = mFusedLocationClient.requestLocationUpdates(locationRequest,
                            locationCallback,
                            Looper.getMainLooper());

                } else
                {   mFusedLocationClient.removeLocationUpdates(locationCallback);
                    task = mFusedLocationClient.requestLocationUpdates(locationRequest,
                            locationCallback,
                            Looper.getMainLooper());}

            }
            }
        }

}
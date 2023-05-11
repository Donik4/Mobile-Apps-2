package com.example.racegame;

import static com.example.racegame.MainActivity.lati;
import static com.example.racegame.MainActivity.longi;
import static com.example.racegame.MainActivity.sharedPreferences;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GoogleMap mMap;

    private String mParam1;
    private String mParam2;

    public MapFragment() {
    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(lati, longi);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
    @SuppressLint("SuspiciousIndentation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);

        SupportMapFragment mapFragment = (SupportMapFragment)  this.getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        TextView  score_1 =view.findViewById(R.id.score_1);
        TextView  score_2 =view.findViewById(R.id.score_2);
        TextView  score_3=view.findViewById(R.id.score_3);
        TextView  score_4 =view.findViewById(R.id.score_4);
        TextView  score_5 =view.findViewById(R.id.score_5);
        TextView  score_6 =view.findViewById(R.id.score_6);
        TextView  score_7 =view.findViewById(R.id.score_7);
        TextView  score_8 =view.findViewById(R.id.score_8);
        TextView  score_9 =view.findViewById(R.id.score_9);
        TextView  score_10 =view.findViewById(R.id.score_10);

        RecordManager recordManager = new RecordManager(getContext());

        List<Record> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (recordManager.hasRecord(i)) {
                int scored = recordManager.getScore(i);
                float longi = recordManager.getlongi(i);
                float lati = recordManager.getlati(i);
                Record record = new Record(scored, lati, longi);
                records.add(record);
            }
        }

      if(records.size()>=1)  score_1.setText("Score " + records.get(0).getScore());
        if(records.size()>=2)    score_2.setText("Score " + records.get(1).getScore());
        if(records.size()>=3)     score_3.setText("Score " + records.get(2).getScore());
        if(records.size()>=4)    score_4.setText("Score " + records.get(3).getScore());
        if(records.size()>=5)    score_5.setText("Score " + records.get(4).getScore());
        if(records.size()>=6)    score_6.setText("Score " + records.get(5).getScore());
        if(records.size()>=7)     score_8.setText("Score " + records.get(6).getScore());
        if(records.size()>=8)   score_7.setText("Score " + records.get(7).getScore());
        if(records.size()>=9)    score_9.setText("Score " + records.get(8).getScore());
        if(records.size()>=10)    score_10.setText("Score " + records.get(9).getScore());
        if(records.size()>=1)    score_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(0).getlati(),records.get(0).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(0).getScore()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=2)    score_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(1).getlati(),records.get(1).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(1).getScore()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=3)     score_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(2).getlati(),records.get(2).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(2).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=4)      score_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(3).getlati(),records.get(3).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(3).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=5)    score_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(4).getlati(),records.get(4).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(4).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=6)    score_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(5).getlati(),records.get(5).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(5).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=7)    score_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(6).getlati(),records.get(6).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(6).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=8)    score_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(7).getlati(),records.get(7).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(7).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=9)    score_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(8).getlati(),records.get(8).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(8).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        if(records.size()>=10)     score_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(records.get(9).getlati(),records.get(9).getlongi());
                mMap.addMarker(new MarkerOptions().position(location).title("Score "+records.get(9).getScore()));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });

        return  view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
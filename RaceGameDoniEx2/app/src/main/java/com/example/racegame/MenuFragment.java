package com.example.racegame;

import static com.example.racegame.MainActivity.sharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class MenuFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MenuFragment() {
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        Button bt_map = view.findViewById(R.id.bt_map);

        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MapFragment anotherFragment = new MapFragment();
                fragmentTransaction.replace(R.id.fragment_container, anotherFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        RadioButton radio_slow = view.findViewById(R.id.radio_slow);
        RadioButton radio_fast = view.findViewById(R.id.radio_fast);
        RadioButton radio_reg = view.findViewById(R.id.radio_Regular);
        RadioButton radio_sensor = view.findViewById(R.id.radio_Sensor);

        radio_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("mode", 2);
                editor.apply();
            }
        });
        radio_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("mode", 1);
                editor.apply();
            }
        });
        radio_slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("speed", false);
                editor.apply();
            }
        });

        radio_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("speed", true);
                editor.apply();
            }
        });



        boolean speed = sharedPreferences.getBoolean("speed", false);
        if (speed) {
            radio_fast.setChecked(true);
            radio_slow.setChecked(false);
        } else {
            radio_slow.setChecked(true);
            radio_fast.setChecked(false);
        }

        int mode = sharedPreferences.getInt("mode", 1);
        if (mode == 1) {
            radio_reg.setChecked(true);
            radio_sensor.setChecked(false);
        } else {
            radio_sensor.setChecked(true);
            radio_reg.setChecked(false);
        }


        return view;
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
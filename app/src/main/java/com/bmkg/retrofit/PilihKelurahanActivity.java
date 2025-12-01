package com.bmkg.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bmkg.retrofit.model.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PilihKelurahanActivity extends AppCompatActivity {

    RecyclerView rv;
    List<Location> allLocations = new ArrayList<>();
    String kecCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kelurahan);

        kecCode = getIntent().getStringExtra("code");

        rv = findViewById(R.id.rvLokasi);
        rv.setLayoutManager(new LinearLayoutManager(this));

        loadJSON();
        showKelurahan();
    }

    void loadJSON() {
        try {
            InputStream is = getAssets().open("lokasi.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            allLocations = new Gson().fromJson(json, new TypeToken<List<Location>>(){}.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showKelurahan() {
        List<Location> kel = new ArrayList<>();

        for (Location loc : allLocations) {
            if (loc.getCode().startsWith(kecCode) &&
                    loc.getCode().split("\\.").length == 4) {
                kel.add(loc);
            }
        }

        rv.setAdapter(new LocationAdapter(kel, selected -> {
            // DI SINI NANTI LANJUT ACTION MISAL: buka cuaca
        }));
    }
}

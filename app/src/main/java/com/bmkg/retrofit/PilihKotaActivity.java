package com.bmkg.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.bmkg.retrofit.model.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PilihKotaActivity extends AppCompatActivity {

    RecyclerView rv;
    List<Location> allLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kota);

        rv = findViewById(R.id.rvLokasi);
        rv.setLayoutManager(new LinearLayoutManager(this));

        loadJSON();
        showKota();
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

    void showKota() {
        List<Location> kota = new ArrayList<>();

        for (Location loc : allLocations) {
            if (loc.getCode().split("\\.").length == 2) {
                kota.add(loc);
            }
        }

        rv.setAdapter(new LocationAdapter(kota, selected -> {
            Intent i = new Intent(this, PilihKecamatanActivity.class);
            i.putExtra("code", selected.getCode());
            startActivity(i);
        }));
    }
}

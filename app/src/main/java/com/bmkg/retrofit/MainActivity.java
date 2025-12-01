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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvLokasi;
    List<Location> allLocations = new ArrayList<>();

    int level = 1; // 1 = kota, 2 = kecamatan, 3 = kelurahan
    String selectedKota = null;
    String selectedKecamatan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvLokasi = findViewById(R.id.rvLokasi);
        rvLokasi.setLayoutManager(new LinearLayoutManager(this));

        allLocations = loadLocations();
        showKota();
    }

    // -------------------------
    // STEP 1 : TAMPILKAN KOTA
    // -------------------------
    private void showKota() {
        level = 1;

        List<Location> list = new ArrayList<>();
        for (Location loc : allLocations) {
            if (loc.getCode().split("\\.").length == 2) {
                list.add(loc);
            }
        }

        rvLokasi.setAdapter(new LocationAdapter(list, loc -> {
            selectedKota = loc.getCode();
            showKecamatan();   // lanjut ke step kecamatan
        }));
    }

    // -------------------------
    // STEP 2 : TAMPILKAN KECAMATAN
    // -------------------------
    private void showKecamatan() {
        level = 2;

        List<Location> list = new ArrayList<>();
        for (Location loc : allLocations) {
            if (loc.getCode().startsWith(selectedKota) &&
                    loc.getCode().split("\\.").length == 3) {
                list.add(loc);
            }
        }

        rvLokasi.setAdapter(new LocationAdapter(list, loc -> {
            selectedKecamatan = loc.getCode();
            showKelurahan();   // lanjut ke step kelurahan
        }));
    }

    // -------------------------
    // STEP 3 : TAMPILKAN KELURAHAN
    // -------------------------
    private void showKelurahan() {
        level = 3;

        List<Location> list = new ArrayList<>();
        for (Location loc : allLocations) {
            if (loc.getCode().startsWith(selectedKecamatan) &&
                    loc.getCode().split("\\.").length == 4) {
                list.add(loc);
            }
        }

        rvLokasi.setAdapter(new LocationAdapter(list, loc -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            intent.putExtra("kode_wilayah", loc.getCode());
            startActivity(intent);
        }));
    }

    // -------------------------
    // LOAD JSON
    // -------------------------
    private List<Location> loadLocations() {
        List<Location> list = new ArrayList<>();

        try {
            InputStream is = getAssets().open("lokasi.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Location>>() {}.getType();
            list = gson.fromJson(json, listType);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

package com.mstztrk.j10_4menuveayarislemleri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity
        extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    int sayac = 0;
    Button btnSayac;
    SharedPreferences preferences, ayarlar;
    boolean sesAcikmi = false, titresimAcikMi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSayac = (Button) findViewById(R.id.btnSay);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlar.registerOnSharedPreferenceChangeListener(this);
        ayarlariYukle(ayarlar);

        sayac = preferences.getInt("sayac", 0);
        btnSayac.setText(String.format("%d", sayac));

        final Vibrator titresimci = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final MediaPlayer player = MediaPlayer.create(this, R.raw.buttonclick);

        btnSayac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSayac.setText(String.format("%d", ++sayac));
                if (titresimAcikMi)
                    titresimci.vibrate(500);
                if (sesAcikmi)
                    player.start();
            }
        });
        btnSayac.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sayac = 0;
                btnSayac.setText(String.format("%d", sayac));
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_ayarlar) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_cikis)
            finish();
        return true;
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("sayac", sayac);
        editor.commit();
        super.onPause();
    }

    private void ayarlariYukle(SharedPreferences ayar) {
        sesAcikmi = ayar.getBoolean("sesdurum", false);
        titresimAcikMi = ayar.getBoolean("titresim", false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Toast.makeText(this, "Ayarlar değişti", Toast.LENGTH_SHORT).show();
        ayarlariYukle(sharedPreferences);
    }
}

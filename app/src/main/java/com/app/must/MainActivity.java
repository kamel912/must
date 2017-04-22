package com.app.must;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    Button abt;
    Button album;
    Button contact;
    Button mp;
    Button news;
    Button stdzone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        setupScreen();
    }

    public void setupScreen() {
        this.abt = (Button) findViewById(R.id.about_btn);
        this.mp = (Button) findViewById(R.id.map_btn);
        this.stdzone = (Button) findViewById(R.id.std_btn);
        this.news = (Button) findViewById(R.id.news_btn);
        this.contact = (Button) findViewById(R.id.contact_btn);
        this.album = (Button) findViewById(R.id.photo_btn);
        this.abt.setOnClickListener(this);
        this.mp.setOnClickListener(this);
        this.stdzone.setOnClickListener(this);
        this.news.setOnClickListener(this);
        this.contact.setOnClickListener(this);
        this.album.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_btn:
                if (isNetworkConnected()) {
                    startActivity(new Intent(this, MustMap.class));
                } else {
                    Show_Toast("Please check Your internet Service");
                }
            case R.id.news_btn:
                startActivity(new Intent(this, News.class));
            case R.id.std_btn:
                startActivity(new Intent(this, StudentLogin.class));
            case R.id.about_btn:
                startActivity(new Intent(this, About.class));
            case R.id.photo_btn:
                startActivity(new Intent(this, PhotoAlbum.class));
            case R.id.contact_btn:
                startActivity(new Intent(this, Contact.class));
            default:
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        new Builder(this).setIcon(17301543).setTitle("Close Application").setMessage("Do you Want to Exit?").setNegativeButton("NO", null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.moveTaskToBack(true);
            }
        }).show();
        return true;
    }

    private boolean isNetworkConnected() {
        if (((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }

    public void Show_Toast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, 0);
        toast.setGravity(49, 0, 20);
        toast.show();
    }
}

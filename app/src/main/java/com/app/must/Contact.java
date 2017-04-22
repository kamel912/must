package com.app.must;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Contact extends Activity implements OnClickListener {
    Button face;
    Button google;
    Button instgram;
    Button pinterest;
    Button twitter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        setupScreen();
    }

    public void setupScreen() {
        this.face = (Button) findViewById(R.id.face);
        this.twitter = (Button) findViewById(R.id.twitter);
        this.instgram = (Button) findViewById(R.id.instagram);
        this.google = (Button) findViewById(R.id.google);
        this.pinterest = (Button) findViewById(R.id.pinterest);
        this.face.setOnClickListener(this);
        this.twitter.setOnClickListener(this);
        this.google.setOnClickListener(this);
        this.instgram.setOnClickListener(this);
        this.pinterest.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/mustuni")));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "No application can handle this request, Please install a webbrowser", 1).show();
                    e.printStackTrace();
                }
            case R.id.twitter:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://twitter.com/must_university")));
                } catch (ActivityNotFoundException e2) {
                    Toast.makeText(this, "No application can handle this request, Please install a webbrowser", 1).show();
                    e2.printStackTrace();
                }
            case R.id.pinterest:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.pinterest.com/mustuni/")));
                } catch (ActivityNotFoundException e22) {
                    Toast.makeText(this, "No application can handle this request, Please install a webbrowser", 1).show();
                    e22.printStackTrace();
                }
            case R.id.google:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/105406820605474276288")));
                } catch (ActivityNotFoundException e222) {
                    Toast.makeText(this, "No application can handle this request, Please install a webbrowser", 1).show();
                    e222.printStackTrace();
                }
            case R.id.instagram:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://instagram.com/mustuni")));
                } catch (ActivityNotFoundException e2222) {
                    Toast.makeText(this, "No application can handle this request, Please install a webbrowser", 1).show();
                    e2222.printStackTrace();
                }
            default:
        }
    }
}

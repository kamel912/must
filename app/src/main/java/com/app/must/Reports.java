package com.app.must;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import java.lang.reflect.Field;

public class Reports extends Activity implements OnClickListener {
    public static final String std_pref = "std_Pref";
    Button courses_btn;
    Button grades_btn;
    Button progress_btn;
    Button schedule_btn;
    SharedPreferences sharedpreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        getOverflowMenu();
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        setupScreen();
    }

    protected void onResume() {
        checksession();
        super.onResume();
    }

    public void checksession() {
        this.sharedpreferences = getSharedPreferences(std_pref, 0);
        if (!this.sharedpreferences.contains("std_id") || !this.sharedpreferences.contains("password")) {
            finish();
        }
    }

    public void setupScreen() {
        this.courses_btn = (Button) findViewById(R.id.courses_btn);
        this.schedule_btn = (Button) findViewById(R.id.schedule_btn);
        this.grades_btn = (Button) findViewById(R.id.grades_btn);
        this.progress_btn = (Button) findViewById(R.id.progress_btn);
        this.courses_btn.setOnClickListener(this);
        this.schedule_btn.setOnClickListener(this);
        this.grades_btn.setOnClickListener(this);
        this.progress_btn.setOnClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.logout) {
            return super.onOptionsItemSelected(item);
        }
        logout();
        return true;
    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        Intent action = new Intent(this, ReportsAction.class);
        switch (v.getId()) {
            case R.id.courses_btn:
                action.putExtra("type", "courses");
                startActivity(action);
            case R.id.schedule_btn:
                action.putExtra("type", "schedule");
                startActivity(action);
            case R.id.grades_btn:
                action.putExtra("type", "grades");
                startActivity(action);
            case R.id.progress_btn:
                action.putExtra("type", "progress");
                startActivity(action);
            default:
        }
    }

    public void logout() {
        Editor editor = getSharedPreferences(std_pref, 0).edit();
        editor.clear();
        editor.commit();
        ((NotificationManager) getSystemService("notification")).cancelAll();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

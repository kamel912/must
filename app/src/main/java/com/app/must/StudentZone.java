package com.app.must;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import java.lang.reflect.Field;

public class StudentZone extends Activity implements OnClickListener {
    public static final String std_pref = "std_Pref";
    Button inbox_btn;
    Button info_btn;
    String pass;
    Button reg_btn;
    Button report_btn;
    SharedPreferences sharedpreferences;
    String std_id;

    public StudentZone() {
        this.std_id = "";
        this.pass = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_zone);
        getOverflowMenu();
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        setupScreen();
        SharedPreferences sharedpreferences = getSharedPreferences(std_pref, 0);
        this.std_id = sharedpreferences.getString("std_id", "0").toString();
        this.pass = sharedpreferences.getString("password", "0").toString();
    }

    protected void onResume() {
        checksession();
        startService(new Intent(this, PushNotifications.class));
        super.onResume();
    }

    public void checksession() {
        this.sharedpreferences = getSharedPreferences(std_pref, 0);
        if (!this.sharedpreferences.contains("std_id") || !this.sharedpreferences.contains("password")) {
            finish();
        }
    }

    public void setupScreen() {
        this.report_btn = (Button) findViewById(R.id.reports);
        this.info_btn = (Button) findViewById(R.id.info);
        this.inbox_btn = (Button) findViewById(R.id.inbox);
        this.reg_btn = (Button) findViewById(R.id.register);
        this.report_btn.setOnClickListener(this);
        this.info_btn.setOnClickListener(this);
        this.inbox_btn.setOnClickListener(this);
        this.reg_btn.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.reports:
                startActivity(new Intent(this, Reports.class));
            case R.id.info:
                startActivity(new Intent(this, Info.class));
            case R.id.inbox:
                startActivity(new Intent(this, Inbox.class));
            case R.id.register:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("javascript:var to = 'http://www.must.edu.eg/studentszone/android/reg_land.php';var p = {std_id:'" + this.std_id + "',password:'" + this.pass + "'};" + "var myForm = document.createElement('form');" + "myForm.method='post' ;" + "myForm.action = to;" + "for (var k in p) {" + "var myInput = document.createElement('input') ;" + "myInput.setAttribute('type', 'text');" + "myInput.setAttribute('name', k) ;" + "myInput.setAttribute('value', p[k]);" + "myForm.appendChild(myInput) ;" + "}" + "document.body.appendChild(myForm) ;" + "myForm.submit() ;" + "document.body.removeChild(myForm) ;")));
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

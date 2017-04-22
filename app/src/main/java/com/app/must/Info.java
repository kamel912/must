package com.app.must;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class Info extends Activity {
    private static final String std_pref = "std_Pref";
    private ProgressDialog pd;
    SharedPreferences sharedpreferences;
    String std_id;
    String std_name;
    TextView stdadvisor;
    TextView stdcgpa;
    TextView stdcollege;
    TextView stdearnhrs;
    TextView stdid;
    TextView stdlevel;
    TextView stdmajor;
    TextView stdname;

    class LoadInfo extends AsyncTask<String, String, String[]> {
        String advisor;
        String cgpa;
        String college;
        String earn_hrs;
        String level;
        String major;

        LoadInfo() {
            this.college = "";
            this.major = "";
            this.cgpa = "";
            this.level = "";
            this.earn_hrs = "";
            this.advisor = "";
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Info.this.pd = new ProgressDialog(Info.this);
            Info.this.pd.setMessage("Please wait, Loading Your Information...");
            Info.this.pd.setIndeterminate(false);
            Info.this.pd.setCancelable(true);
            Info.this.pd.show();
        }

        protected String[] doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("std_id", args[0]));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.must.edu.eg/studentszone/android/info.php");
                httppost.setEntity(new UrlEncodedFormEntity(params));
                JSONObject object = new JSONObject(Info.this.inputStreamToString(httpclient.execute(httppost).getEntity().getContent()).toString());
                if (Integer.parseInt(object.getString("success")) == 1) {
                    this.college = object.getString("college");
                    this.major = object.getString("major");
                    this.level = object.getString("level");
                    this.cgpa = object.getString("cgpa");
                    this.earn_hrs = object.getString("earn_hrs");
                    this.advisor = object.getString("advisor");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return new String[]{this.college, this.cgpa};
        }

        protected void onPostExecute(String[] msgs) {
            Info.this.pd.dismiss();
            String st_college = msgs[0].toString();
            String st_cgpa = msgs[1].toString();
            Info.this.stdid.setText(Info.this.std_id);
            Info.this.stdname.setText(Info.this.std_name);
            Info.this.stdcollege.setText(st_college);
            Info.this.stdcgpa.setText(st_cgpa);
            Info.this.stdmajor.setText(this.major);
            Info.this.stdearnhrs.setText(this.earn_hrs);
            Info.this.stdadvisor.setText(this.advisor);
            Info.this.stdlevel.setText(this.level);
        }
    }

    public Info() {
        this.std_id = "";
        this.std_name = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        getOverflowMenu();
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        setupScreen();
        SharedPreferences sharedpreferences = getSharedPreferences(std_pref, 0);
        this.std_id = sharedpreferences.getString("std_id", "0");
        this.std_name = sharedpreferences.getString("name", "0");
        new LoadInfo().execute(new String[]{this.std_id});
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
        this.stdid = (TextView) findViewById(R.id.student_id);
        this.stdname = (TextView) findViewById(R.id.student_name);
        this.stdcollege = (TextView) findViewById(R.id.student_college);
        this.stdcgpa = (TextView) findViewById(R.id.student_cgpa);
        this.stdmajor = (TextView) findViewById(R.id.student_major);
        this.stdearnhrs = (TextView) findViewById(R.id.student_earned_hrs);
        this.stdadvisor = (TextView) findViewById(R.id.student_advisor);
        this.stdlevel = (TextView) findViewById(R.id.student_level);
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

    public void logout() {
        Editor editor = getSharedPreferences(std_pref, 0).edit();
        editor.clear();
        editor.commit();
        ((NotificationManager) getSystemService("notification")).cancelAll();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                rLine = rd.readLine();
                if (rLine == null) {
                    break;
                }
                answer.append(rLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return answer;
    }
}

package com.app.must;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class StudentLogin extends Activity implements OnClickListener {
    public static final String std_pref = "std_Pref";
    final String empty_id;
    final String empty_password;
    TextView error;
    EditText id;
    final String internet;
    Button login;
    EditText pass;
    private ProgressDialog pd;
    SharedPreferences sharedpreferences;

    class CheckLogin extends AsyncTask<String, String, String[]> {
        String msg;
        String name;

        CheckLogin() {
            this.msg = "0";
            this.name = "0";
        }

        protected void onPreExecute() {
            super.onPreExecute();
            StudentLogin.this.pd = new ProgressDialog(StudentLogin.this);
            StudentLogin.this.pd.setMessage("Please wait, Checking your ID and Password..");
            StudentLogin.this.pd.setIndeterminate(false);
            StudentLogin.this.pd.setCancelable(true);
            StudentLogin.this.pd.show();
        }

        protected String[] doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("std_id", args[0]));
            params.add(new BasicNameValuePair("password", args[1]));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.must.edu.eg/studentszone/android/check_login.php");
                httppost.setEntity(new UrlEncodedFormEntity(params));
                JSONObject object = new JSONObject(StudentLogin.this.inputStreamToString(httpclient.execute(httppost).getEntity().getContent()).toString());
                String success = object.getString("success");
                this.msg = object.getString("Message");
                if (Integer.parseInt(success) == 1) {
                    this.name = object.getString("name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return new String[]{this.msg, this.name};
        }

        protected void onPostExecute(String[] msgs) {
            StudentLogin.this.pd.dismiss();
            String msg = msgs[0].toString();
            String std_name = msgs[1].toString();
            if (msg.equalsIgnoreCase("Welcome")) {
                Log.e("session", "begin");
                StudentLogin.this.createSession(std_name);
                Log.e("session", "Done");
                StudentLogin.this.startActivity(new Intent(StudentLogin.this, StudentZone.class));
                StudentLogin.this.finish();
                Log.e("insideif", msg);
                return;
            }
            Log.e("insideelse", msg);
            StudentLogin.this.Show_Toast(msg);
            StudentLogin.this.error.setText(msg);
            StudentLogin.this.error.setVisibility(0);
        }
    }

    public StudentLogin() {
        this.internet = "Please Check Your Internet Service";
        this.empty_id = "Please Enter your ID";
        this.empty_password = "Please Enter your Password";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);
        checksession();
        setupScreen();
    }

    protected void onResume() {
        checksession();
        super.onResume();
    }

    public void setupScreen() {
        this.id = (EditText) findViewById(R.id.std_id);
        this.pass = (EditText) findViewById(R.id.password);
        this.login = (Button) findViewById(R.id.login);
        this.login.setOnClickListener(this);
        this.error = (TextView) findViewById(R.id.error_msg);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (!isNetworkConnected()) {
                    Show_Toast("Please Check Your Internet Service");
                    this.error.setText("Please Check Your Internet Service");
                    this.error.setVisibility(0);
                } else if (TextUtils.isEmpty(this.id.getText().toString())) {
                    Show_Toast("Please Enter your ID");
                    this.error.setText("Please Enter your ID");
                    this.error.setVisibility(0);
                } else if (TextUtils.isEmpty(this.pass.getText().toString())) {
                    Show_Toast("Please Enter your Password");
                    this.error.setText("Please Enter your Password");
                    this.error.setVisibility(0);
                } else {
                    this.error.setVisibility(8);
                    new CheckLogin().execute(new String[]{this.id.getText().toString(), this.pass.getText().toString()});
                }
            default:
        }
    }

    public void createSession(String std_name) {
        Editor editor = getSharedPreferences(std_pref, 0).edit();
        editor.putString("std_id", this.id.getText().toString());
        editor.putString("password", this.pass.getText().toString());
        editor.putString("name", std_name);
        editor.commit();
    }

    public void checksession() {
        this.sharedpreferences = getSharedPreferences(std_pref, 0);
        if (this.sharedpreferences.contains("std_id") && this.sharedpreferences.contains("password")) {
            startActivity(new Intent(this, StudentZone.class));
            finish();
        }
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

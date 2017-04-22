package com.app.must;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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

public class MsgDetails extends Activity implements OnClickListener {
    private static final String std_pref = "std_Pref";
    TextView bod;
    String body;
    String date;
    String emp_code;
    final String empty_bod;
    final String empty_sub;
    final String internet;
    int msg_id;
    private ProgressDialog pd;
    Button reply;
    EditText reply_body;
    LinearLayout reply_section;
    EditText reply_subject;
    Button send;
    SharedPreferences sharedpreferences;
    TextView subj;
    String subject;

    class SendMsg extends AsyncTask<String, String, String> {
        SendMsg() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MsgDetails.this.pd = new ProgressDialog(MsgDetails.this);
            MsgDetails.this.pd.setMessage("Please wait, Loading News and Events...");
            MsgDetails.this.pd.setIndeterminate(false);
            MsgDetails.this.pd.setCancelable(true);
            MsgDetails.this.pd.show();
        }

        protected String doInBackground(String... args) {
            String status = "0";
            Log.e("Service", "start async");
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("msg_id", args[0]));
            params.add(new BasicNameValuePair("subject", args[1]));
            params.add(new BasicNameValuePair("body", args[2]));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.must.edu.eg/studentszone/android/reply_msg.php");
                httppost.setEntity(new UrlEncodedFormEntity(params));
                status = new JSONObject(MsgDetails.this.inputStreamToString(httpclient.execute(httppost).getEntity().getContent()).toString()).getString("success");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            MsgDetails.this.pd.dismiss();
            Builder adb = new Builder(MsgDetails.this);
            adb.setTitle("Reply Message");
            if (Integer.parseInt(status) == 1) {
                adb.setMessage(" Your Message has been sent Succefully ");
            } else {
                adb.setMessage(" Error Sending Your Message, please try again Later ");
            }
            adb.setNegativeButton("OK", null);
            adb.show();
        }
    }

    public MsgDetails() {
        this.empty_sub = "Please Enter Message Subject";
        this.empty_bod = "Please Enter Message Body";
        this.internet = "Please Check Your Internet Service";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_details);
        getOverflowMenu();
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        setupScreen();
        this.subject = getIntent().getStringExtra("subject").toString();
        this.body = getIntent().getStringExtra("body").toString();
        this.date = getIntent().getStringExtra("date").toString();
        this.emp_code = getIntent().getStringExtra("emp_code").toString();
        this.msg_id = Integer.parseInt(getIntent().getStringExtra("msg_id").toString());
        this.subj.setText(this.subject);
        this.bod.setText(this.body);
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
        this.subj = (TextView) findViewById(R.id.subject);
        this.bod = (TextView) findViewById(R.id.body);
        this.reply_subject = (EditText) findViewById(R.id.reply_subject);
        this.reply_body = (EditText) findViewById(R.id.reply_body);
        this.reply = (Button) findViewById(R.id.reply);
        this.send = (Button) findViewById(R.id.send_reply);
        this.reply.setOnClickListener(this);
        this.send.setOnClickListener(this);
        this.reply_section = (LinearLayout) findViewById(R.id.reply_section);
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

    public void Show_Toast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, 0);
        toast.setGravity(49, 0, 20);
        toast.show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reply:
                this.reply_section.setVisibility(0);
                this.reply_subject.setText("Re: " + this.subject);
                this.reply.setVisibility(8);
            case R.id.send_reply:
                if (!isNetworkConnected()) {
                    Show_Toast("Please Check Your Internet Service");
                } else if (TextUtils.isEmpty(this.reply_subject.getText().toString())) {
                    Show_Toast("Please Enter Message Subject");
                } else if (TextUtils.isEmpty(this.reply_body.getText().toString())) {
                    Show_Toast("Please Enter Message Body");
                } else {
                    String re_sub = this.reply_subject.getText().toString();
                    String re_bod = this.reply_body.getText().toString();
                    new SendMsg().execute(new String[]{String.valueOf(this.msg_id), re_sub, re_bod});
                    Log.e("msgdetails", ":" + String.valueOf(this.msg_id) + "-" + re_sub + "-" + re_bod);
                }
            default:
        }
    }

    private boolean isNetworkConnected() {
        if (((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
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

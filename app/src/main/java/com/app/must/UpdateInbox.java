package com.app.must;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
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

public class UpdateInbox extends Service {
    String msg_id;
    String std_id;

    class InboxUpdate extends AsyncTask<String, String, String> {
        InboxUpdate() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            Log.e("Service", "start async");
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("msg_id", args[0]));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.must.edu.eg/studentszone/android/update_inbox.php");
                httppost.setEntity(new UrlEncodedFormEntity(params));
                Log.e("result:", new JSONObject(UpdateInbox.this.inputStreamToString(httpclient.execute(httppost).getEntity().getContent()).toString()).getString("success"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            UpdateInbox.this.stopSelf();
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        Log.e("insideservice", "Service Created");
    }

    public void onStart(Intent intent, int startId) {
        this.msg_id = intent.getStringExtra("msg_id").toString();
        new InboxUpdate().execute(new String[]{this.msg_id});
        Log.e("insideservice", "Service Started:" + this.msg_id);
    }

    public void onDestroy() {
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

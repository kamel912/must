package com.app.must;

import android.app.NotificationManager;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.app.must.buisness.InboxData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushNotifications extends Service {
    Intent resultIntent;
    SharedPreferences sharedpreferences;
    String std_id;

    class InboxNotifications extends AsyncTask<String, String, String> {
        private final ArrayList<InboxData> msgs_list_as;

        InboxNotifications() {
            this.msgs_list_as = new ArrayList();
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            Log.e("InboxNotifications", "async:start");
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("std_id", args[0]));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.must.edu.eg/studentszone/android/notifications.php");
                httppost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpclient.execute(httppost);
                JSONArray jSONArray = new JSONArray(PushNotifications.this.inputStreamToString(response.getEntity().getContent()).toString());
                Log.e("resultsize", jSONArray.length());
                if (jSONArray.length() > 0) {
                    Log.e("resultsize", jSONArray.length());
                    for (int i = 0; i < jSONArray.length(); i++) {
                        boolean read;
                        JSONObject object = jSONArray.getJSONObject(i);
                        String id = object.getString("id");
                        int msg_id = Integer.parseInt(id);
                        Log.e("in-idx", id);
                        String dept = object.getString("dept_id");
                        int dept_id = Integer.parseInt(dept);
                        Log.e("in-dept_id", dept);
                        String subject = object.getString("subject").toString();
                        String body = object.getString("body").toString();
                        String send_date = object.getString("send_date").toString();
                        PushNotifications.this.send_notification(subject, body, msg_id);
                        if (object.getString("read").equalsIgnoreCase("f")) {
                            read = false;
                        } else {
                            read = true;
                        }
                        String emp_code = object.getString("emp_code");
                        InboxData nd = new InboxData();
                        nd.setMsgID(msg_id);
                        nd.setDeptID(dept_id);
                        nd.setSubject(subject);
                        nd.setBody(body);
                        nd.setSendDate(send_date);
                        nd.setRead(read);
                        nd.setEmpcode(emp_code);
                        this.msgs_list_as.add(nd);
                    }
                } else {
                    Log.e("servicestop", "size:0");
                    PushNotifications.this.stopSelf();
                }
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
            Log.e("beforefor", "sizex:" + this.msgs_list_as.size());
            for (int i = 0; i < this.msgs_list_as.size(); i++) {
                InboxData nd = (InboxData) this.msgs_list_as.get(i);
                Log.e("insidefor", "size:" + this.msgs_list_as.size());
                PushNotifications.this.send_notification(nd.getSubject(), nd.getBody(), nd.getMsgID());
            }
            Log.e("service", "stoped after for");
        }
    }

    public PushNotifications() {
        this.std_id = "";
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onStart(Intent intent, int startId) {
        this.std_id = getSharedPreferences(StudentZone.std_pref, 0).getString("std_id", "0");
        new InboxNotifications().execute(new String[]{this.std_id});
        Log.e("insideservice", "Notification Service Started:");
    }

    public void onDestroy() {
    }

    public void send_notification(String subject, String body, int not_id) {
        Log.e("send_notification", "start");
        Builder mBuilder = new Builder(this).setSmallIcon(R.drawable.notify).setContentTitle(subject).setContentText(body).setSound(RingtoneManager.getDefaultUri(2));
        this.resultIntent = new Intent(this, Inbox.class);
        this.resultIntent.putExtra("subject", subject);
        this.resultIntent.putExtra("body", body);
        this.resultIntent.setData(Uri.parse("custom://" + System.currentTimeMillis()));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(StudentZone.class);
        stackBuilder.addNextIntent(this.resultIntent);
        mBuilder.setContentIntent(stackBuilder.getPendingIntent(0, 134217728));
        ((NotificationManager) getSystemService("notification")).notify(not_id, mBuilder.build());
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

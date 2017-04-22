package com.app.must;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.app.must.buisness.InboxData;
import com.google.android.gms.plus.PlusShare;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
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

public class Inbox extends Activity {
    private static final String std_pref = "std_Pref";
    Inbox_Adapter inbox_adapter;
    ListView inbox_listview;
    LinearLayout internet;
    private ArrayList<InboxData> msgs_list;
    private ProgressDialog pd;
    SharedPreferences sharedpreferences;
    String std_id;

    public class Inbox_Adapter extends ArrayAdapter<InboxData> {
        Activity activity;
        ArrayList<InboxData> data;
        int layoutResourceId;
        InboxData user;

        class UserHolder {
            TextView date;
            ImageView msg;
            TextView subject;

            UserHolder() {
            }
        }

        public Inbox_Adapter(Activity act, int layoutResourceId, ArrayList<InboxData> data) {
            super(act, layoutResourceId, data);
            this.data = new ArrayList();
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            UserHolder holder;
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(this.activity).inflate(this.layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.subject = (TextView) row.findViewById(R.id.msg_subject);
                holder.date = (TextView) row.findViewById(R.id.msg_date);
                holder.msg = (ImageView) row.findViewById(R.id.msg_image);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            this.user = (InboxData) this.data.get(position);
            String subj = "";
            if (this.user.getSubject().equalsIgnoreCase("Placement")) {
                subj = "English Placement Test Result";
            } else if (this.user.getSubject().equalsIgnoreCase("Midterm")) {
                subj = "Mid-Term Exam Results";
            } else if (this.user.getSubject().equalsIgnoreCase("Final")) {
                subj = "Final Exam Results";
            } else if (this.user.getSubject().equalsIgnoreCase("Registration")) {
                subj = "Your Registration for This Semester";
            } else if (this.user.getSubject().equalsIgnoreCase("Location")) {
                subj = "Timetable Course Location Changed";
            } else {
                subj = this.user.getSubject();
            }
            holder.subject.setTag(Integer.valueOf(this.user.getMsgID()));
            holder.subject.setText(subj);
            holder.date.setText(this.user.getSendDate().substring(0, 10));
            if (this.user.getRead()) {
                holder.msg.setBackgroundResource(R.drawable.read);
            } else {
                holder.msg.setBackgroundResource(R.drawable.unread);
            }
            Inbox.this.inbox_listview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Inbox_Adapter.this.user = (InboxData) Inbox_Adapter.this.data.get(position);
                    String type = "";
                    Intent mIntent;
                    Intent redirect;
                    if (Inbox_Adapter.this.user.getSubject().equalsIgnoreCase("Placement")) {
                        Log.e("service", "before strt");
                        mIntent = new Intent(Inbox.this, UpdateInbox.class);
                        mIntent.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        Inbox.this.startService(mIntent);
                        Log.e("service", "after strt:" + String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        redirect = new Intent(Inbox.this, ReportsAction.class);
                        redirect.putExtra("type", "Placement");
                        Inbox.this.startActivity(redirect);
                    } else if (Inbox_Adapter.this.user.getSubject().equalsIgnoreCase("Midterm")) {
                        mIntent = new Intent(Inbox.this, UpdateInbox.class);
                        mIntent.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        Inbox.this.startService(mIntent);
                        redirect = new Intent(Inbox.this, ReportsAction.class);
                        redirect.putExtra("type", "grades");
                        Inbox.this.startActivity(redirect);
                    } else if (Inbox_Adapter.this.user.getSubject().equalsIgnoreCase("Final")) {
                        mIntent = new Intent(Inbox.this, UpdateInbox.class);
                        mIntent.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        Inbox.this.startService(mIntent);
                        redirect = new Intent(Inbox.this, ReportsAction.class);
                        redirect.putExtra("type", "grades");
                        Inbox.this.startActivity(redirect);
                    } else if (Inbox_Adapter.this.user.getSubject().equalsIgnoreCase("Registration")) {
                        mIntent = new Intent(Inbox.this, UpdateInbox.class);
                        mIntent.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        Inbox.this.startService(mIntent);
                        redirect = new Intent(Inbox.this, ReportsAction.class);
                        redirect.putExtra("type", "courses");
                        Inbox.this.startActivity(redirect);
                    } else if (Inbox_Adapter.this.user.getSubject().equalsIgnoreCase("Location")) {
                        mIntent = new Intent(Inbox.this, UpdateInbox.class);
                        mIntent.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        Inbox.this.startService(mIntent);
                        redirect = new Intent(Inbox.this, ReportsAction.class);
                        redirect.putExtra("type", "schedule");
                        Inbox.this.startActivity(redirect);
                    } else if (Inbox_Adapter.this.user.getBody().startsWith("http://www.must.edu.eg/")) {
                        Log.e("inbox", "Body:" + Inbox_Adapter.this.user.getBody());
                        mIntent = new Intent(Inbox.this, UpdateInbox.class);
                        mIntent.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        Inbox.this.startService(mIntent);
                        redirect = new Intent(Inbox.this, ReportsAction.class);
                        redirect.putExtra("type", "link");
                        redirect.putExtra(PlusShare.KEY_CALL_TO_ACTION_URL, Inbox_Adapter.this.user.getBody().toString());
                        Inbox.this.startActivity(redirect);
                    } else {
                        mIntent = new Intent(Inbox.this, UpdateInbox.class);
                        mIntent.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        Inbox.this.startService(mIntent);
                        redirect = new Intent(Inbox.this, MsgDetails.class);
                        redirect.putExtra("msg_id", String.valueOf(Inbox_Adapter.this.user.getMsgID()));
                        redirect.putExtra("subject", Inbox_Adapter.this.user.getSubject().toString());
                        redirect.putExtra("body", Inbox_Adapter.this.user.getBody().toString());
                        redirect.putExtra("emp_code", Inbox_Adapter.this.user.getEmpcode().toString());
                        redirect.putExtra("date", Inbox_Adapter.this.user.getSendDate().toString());
                        Inbox.this.startActivity(redirect);
                    }
                }
            });
            return row;
        }
    }

    class LoadMsgs extends AsyncTask<String, String, String> {
        private final ArrayList<InboxData> msgs_list_as;

        LoadMsgs() {
            this.msgs_list_as = new ArrayList();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Inbox.this.pd = new ProgressDialog(Inbox.this);
            Inbox.this.pd.setMessage("Please wait, Loading Inbox Messages...");
            Inbox.this.pd.setIndeterminate(false);
            Inbox.this.pd.setCancelable(true);
            Inbox.this.pd.show();
        }

        protected String doInBackground(String... args) {
            Log.e("inside do", "start");
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("std_id", args[0]));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.must.edu.eg/studentszone/android/inbox.php");
                httppost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpclient.execute(httppost);
                JSONArray jSONArray = new JSONArray(Inbox.this.inputStreamToString(response.getEntity().getContent()).toString());
                Log.e("result size", jSONArray.length());
                for (int i = 0; i < jSONArray.length(); i++) {
                    boolean read;
                    JSONObject object = jSONArray.getJSONObject(i);
                    String id = object.getString("id");
                    int msg_id = Integer.parseInt(id);
                    Log.e("in-idx", id);
                    String dept = object.getString("dept_id");
                    int dept_id = Integer.parseInt(dept);
                    Log.e("in-dept_id", dept);
                    String subject = object.getString("subject");
                    String body = object.getString("body").toString();
                    String send_date = object.getString("send_date");
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
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            Inbox.this.pd.dismiss();
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.e("Sizx", this.msgs_list_as.size());
            Inbox.this.setupScreen(this.msgs_list_as);
        }
    }

    public Inbox() {
        this.msgs_list = new ArrayList();
        this.std_id = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getOverflowMenu();
        this.internet = (LinearLayout) findViewById(R.id.internet_error);
        this.std_id = getSharedPreferences(std_pref, 0).getString("std_id", "0");
    }

    protected void onResume() {
        checksession();
        if (isNetworkConnected()) {
            new LoadMsgs().execute(new String[]{this.std_id});
            this.internet.setVisibility(8);
        } else {
            this.internet.setVisibility(0);
        }
        super.onResume();
    }

    public void checksession() {
        this.sharedpreferences = getSharedPreferences(std_pref, 0);
        if (!this.sharedpreferences.contains("std_id") || !this.sharedpreferences.contains("password")) {
            finish();
        }
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

    public void setupScreen(ArrayList<InboxData> news) {
        Log.e("test", news.size());
        Log.e("msgs_list", this.msgs_list.size());
        this.inbox_listview = (ListView) findViewById(R.id.inbox_list);
        this.inbox_listview.setItemsCanFocus(false);
        Log.e("InsideTest", "after4msgs_list:" + this.msgs_list.size());
        this.inbox_adapter = new Inbox_Adapter(this, R.layout.inbox_list_view, news);
        this.inbox_listview.setAdapter(this.inbox_adapter);
        this.inbox_adapter.notifyDataSetChanged();
        Log.e("InsideTest", "last:inbox_adapter");
    }

    private boolean isNetworkConnected() {
        if (((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }
}

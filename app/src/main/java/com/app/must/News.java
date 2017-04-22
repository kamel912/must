package com.app.must;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.app.must.buisness.NewsData;
import com.google.android.gms.plus.PlusShare;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class News extends Activity {
    LinearLayout internet;
    News_Adapter news_adapter;
    private ArrayList<NewsData> news_list;
    ListView news_listview;
    private ProgressDialog pd;

    class LoadNews extends AsyncTask<String, String, String> {
        private final ArrayList<NewsData> news_list_as;

        LoadNews() {
            this.news_list_as = new ArrayList();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            News.this.pd = new ProgressDialog(News.this);
            News.this.pd.setMessage("Please wait, Loading News and Events...");
            News.this.pd.setIndeterminate(false);
            News.this.pd.setCancelable(true);
            News.this.pd.show();
        }

        protected String doInBackground(String... args) {
            Log.e("inside do", "start");
            try {
                JSONArray res = new JSONArray(News.this.inputStreamToString(new DefaultHttpClient().execute(new HttpPost("http://www.must.edu.eg/studentszone/android/news.php")).getEntity().getContent()).toString());
                Log.e("result size", res.length());
                for (int i = 0; i < res.length(); i++) {
                    JSONObject object = res.getJSONObject(i);
                    String id = object.getString("id");
                    Log.e("in-idx", id);
                    int news_id = Integer.parseInt(id);
                    String title = object.getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE);
                    Log.e("in-titlex", title);
                    this.news_list_as.add(new NewsData(news_id, title, object.getString("path")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            News.this.pd.dismiss();
            return null;
        }

        protected void onPostExecute(String file_url) {
            News.this.setupScreen(this.news_list_as);
        }
    }

    public class News_Adapter extends ArrayAdapter<NewsData> {
        Activity activity;
        ArrayList<NewsData> data;
        int layoutResourceId;
        NewsData user;

        class UserHolder {
            TextView news_title;

            UserHolder() {
            }
        }

        public News_Adapter(Activity act, int layoutResourceId, ArrayList<NewsData> data) {
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
                holder.news_title = (TextView) row.findViewById(R.id.news_title);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            this.user = (NewsData) this.data.get(position);
            holder.news_title.setTag(Integer.valueOf(this.user.getID()));
            holder.news_title.setText(this.user.getTitle());
            News.this.news_listview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    News_Adapter.this.user = (NewsData) News_Adapter.this.data.get(position);
                    Intent update_user = new Intent(News.this.getApplicationContext(), NewsDetails.class);
                    update_user.putExtra("news_id", String.valueOf(News_Adapter.this.user.getID()));
                    update_user.putExtra("news_path", News_Adapter.this.user.getPath());
                    News.this.startActivity(update_user);
                }
            });
            return row;
        }
    }

    public News() {
        this.news_list = new ArrayList();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        this.internet = (LinearLayout) findViewById(R.id.internet_error);
        if (isNetworkConnected()) {
            new LoadNews().execute(new String[0]);
            this.internet.setVisibility(8);
            return;
        }
        this.internet.setVisibility(0);
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

    public void setupScreen(ArrayList<NewsData> news) {
        Log.e("test", news.size());
        Log.e("news_list", this.news_list.size());
        this.news_listview = (ListView) findViewById(R.id.news_list);
        this.news_listview.setItemsCanFocus(false);
        for (int i = 0; i < news.size(); i++) {
            int idno = ((NewsData) news.get(i)).getID();
            Log.e("InsideTest", "ID:" + idno);
            String title = ((NewsData) news.get(i)).getTitle();
            title.replaceAll(System.getProperty("line.separator"), "");
            Log.e("InsideTest", "title:" + title);
            String path = ((NewsData) news.get(i)).getPath();
            Log.e("InsideTest", "path:" + path);
            NewsData nd = new NewsData();
            nd.setID(idno);
            nd.setTitle(title);
            nd.setPath(path);
            this.news_list.add(nd);
        }
        Log.e("InsideTest", "after4news_list:" + this.news_list.size());
        this.news_adapter = new News_Adapter(this, R.layout.news_list_view, this.news_list);
        this.news_listview.setAdapter(this.news_adapter);
        this.news_adapter.notifyDataSetChanged();
        Log.e("InsideTest", "last:news_adapter");
    }

    private boolean isNetworkConnected() {
        if (((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }
}

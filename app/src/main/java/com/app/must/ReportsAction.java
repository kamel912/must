package com.app.must;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.plus.PlusShare;
import java.lang.reflect.Field;
import org.apache.http.util.EncodingUtils;

public class ReportsAction extends Activity {
    static final String MyPREFERENCES = "std_Pref";
    private static final String TAG = "Main";
    public static final String std_pref = "std_Pref";
    LinearLayout internet;
    WebView mWebview;
    String postData;
    private ProgressDialog progressBar;
    String report_title;
    SharedPreferences sharedpreferences;
    String std_id;
    String type;
    String url;

    /* renamed from: com.app.must.ReportsAction.1 */
    class AnonymousClass1 extends WebViewClient {
        private final /* synthetic */ Activity val$activity;

        AnonymousClass1(Activity activity) {
            this.val$activity = activity;
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(ReportsAction.TAG, "Processing webview url click...");
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            Log.i(ReportsAction.TAG, "Finished loading URL: " + url);
            if (ReportsAction.this.progressBar.isShowing()) {
                ReportsAction.this.progressBar.dismiss();
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(this.val$activity, description, 0).show();
        }
    }

    public ReportsAction() {
        this.type = "";
        this.url = "";
        this.postData = "";
        this.std_id = "";
        this.report_title = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_action);
        getOverflowMenu();
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        this.std_id = getSharedPreferences(std_pref, 0).getString("std_id", "0");
        this.postData = "std_id=" + this.std_id;
        this.internet = (LinearLayout) findViewById(R.id.internet_error);
        if (isNetworkConnected()) {
            this.internet.setVisibility(8);
            this.mWebview = new WebView(this);
            this.type = getIntent().getStringExtra("type").toString();
            if (this.type.equalsIgnoreCase("courses")) {
                this.url = "http://www.must.edu.eg/studentszone/android/reg_report.php";
                this.report_title = "Registered Courses";
            } else if (this.type.equalsIgnoreCase("schedule")) {
                this.url = "http://www.must.edu.eg/studentszone/android/Time_Table.php";
                this.report_title = "Registered Courses Schedule";
            } else if (this.type.equalsIgnoreCase("grades")) {
                this.url = "http://www.must.edu.eg/studentszone/android/results.php";
                this.report_title = "Semester Grades";
            } else if (this.type.equalsIgnoreCase("progress")) {
                this.url = "http://www.must.edu.eg/studentszone/android/progress_report.php";
                this.report_title = "Progress Report";
            } else if (this.type.equalsIgnoreCase("Placement")) {
                this.url = "http://www.must.edu.eg/studentszone/android/placement.php";
                this.report_title = "Placement Test";
            } else if (this.type.equalsIgnoreCase("link")) {
                this.url = getIntent().getStringExtra(PlusShare.KEY_CALL_TO_ACTION_URL).toString();
                this.report_title = "Loading Message";
            }
            this.mWebview.getSettings().setJavaScriptEnabled(true);
            this.mWebview.getSettings().setBuiltInZoomControls(true);
            this.progressBar = ProgressDialog.show(this, this.report_title, "Loading...");
            this.progressBar.setCancelable(true);
            this.mWebview.setWebViewClient(new AnonymousClass1(this));
            this.mWebview.postUrl(this.url, EncodingUtils.getBytes(this.postData, "BASE64"));
            setContentView(this.mWebview);
            return;
        }
        this.internet.setVisibility(0);
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

    private boolean isNetworkConnected() {
        if (((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }
}

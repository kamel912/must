package com.app.must;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsDetails extends Activity {
    private static final String TAG = "Main";
    WebView mWebview;
    private ProgressDialog progressBar;
    String url;

    /* renamed from: com.app.must.NewsDetails.1 */
    class AnonymousClass1 extends WebViewClient {
        private final /* synthetic */ Activity val$activity;

        AnonymousClass1(Activity activity) {
            this.val$activity = activity;
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(NewsDetails.TAG, "Processing webview url click...");
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            Log.i(NewsDetails.TAG, "Finished loading URL: " + url);
            if (NewsDetails.this.progressBar.isShowing()) {
                NewsDetails.this.progressBar.dismiss();
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(this.val$activity, description, 0).show();
        }
    }

    public NewsDetails() {
        this.url = "";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        this.mWebview = new WebView(this);
        this.url = getIntent().getStringExtra("news_path").toString();
        this.url = "http://www.must.edu.eg/" + this.url;
        WebSettings settings = this.mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        this.progressBar = ProgressDialog.show(this, "News & Events", "Loading...");
        this.mWebview.setWebViewClient(new AnonymousClass1(this));
        this.mWebview.loadUrl(this.url);
        setContentView(this.mWebview);
    }
}

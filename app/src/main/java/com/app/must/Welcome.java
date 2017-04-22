package com.app.must;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Welcome.this.finish();
                Welcome.this.startActivity(new Intent(Welcome.this, MainActivity.class));
            }
        }, 3000);
    }
}

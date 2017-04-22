package com.app.must;

import android.app.Activity;
import android.os.Bundle;

public class About extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
    }
}

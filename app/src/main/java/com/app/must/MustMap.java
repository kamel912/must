package com.app.must;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MustMap extends FragmentActivity {
    static final LatLng MUST;
    private GoogleMap map;

    static {
        MUST = new LatLng(29.996766d, 30.965141d);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mustmap);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        try {
            this.map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            this.map.addMarker(new MarkerOptions().position(MUST).title("Misr University For Science & Technology"));
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(MUST, 15.0f));
            this.map.animateCamera(CameraUpdateFactory.zoomTo(15.0f), GamesStatusCodes.STATUS_REQUEST_UPDATE_PARTIAL_SUCCESS, null);
        } catch (Exception e) {
        }
    }
}

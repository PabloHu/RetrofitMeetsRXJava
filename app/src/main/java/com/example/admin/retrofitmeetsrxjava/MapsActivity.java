package com.example.admin.retrofitmeetsrxjava;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.admin.retrofitmeetsrxjava.data.modelhomeworklocation.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double passLat;
    private Double passLon;
    private List<Place> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        //passLat = getIntent().getDoubleExtra("lat",33);
         //passLon = getIntent().getDoubleExtra("lon",88);

       // Intent i = getIntent();
       placeList = (List<Place>) getIntent().getSerializableExtra("passList");
    //   placeList = (List<Place>) getIntent().getExtras().getSerializable("passList");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < placeList.size(); i++) {



        // Add a marker in Sydney and move the camera
  //      LatLng sydney = new LatLng(-34, 151);
            LatLng sydney = new LatLng(placeList.get(i).getLatitude(), placeList.get(i).getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Location:" + (i+1)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        
        }
    }
}

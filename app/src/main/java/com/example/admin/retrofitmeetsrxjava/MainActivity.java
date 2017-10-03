package com.example.admin.retrofitmeetsrxjava;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.retrofitmeetsrxjava.data.model.GeocodeResponse;
import com.example.admin.retrofitmeetsrxjava.data.modelhomeworklocation.Place;
import com.example.admin.retrofitmeetsrxjava.data.remote.ApiProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements LocationListener {

    String finishedSaved = "";

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 12;
    private static final String TAG = "MainActivityTag";

    FusedLocationProviderClient fusedLocationProviderClient;

    Location currentLocation;
    private TextView tvCurrentLocation;
    private EditText etStreet, etZipcode;
    Spinner spinner1;

    //===========
    Double currentLat = 0.0;
    Double currentLon = 0.0;
    //==============


    List<Place> placeList = new ArrayList<>();
    RecyclerView rvPersonList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCurrentLocation = (TextView) findViewById(R.id.tvLocation);
        etStreet = (EditText) findViewById(R.id.etStreet);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        etZipcode = (EditText) findViewById(R.id.etZipcode);


        //   tvCurrentLocation = (TextView) findViewById(R.id.tvLocation);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
        } else {
            getLocation();
        }

        addSpinner();
    }

    public void addSpinner() {


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, spinner1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {

                    Toast.makeText(this, "Need this location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    public void getLocation() {

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        Log.d(TAG, "onSuccess: " + location.toString());
                        tvCurrentLocation.setText(location.toString());
                        currentLocation = location;


                        getAddressUsingGeocoding(location);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 100, this);
    }


    private void getAddressUsingGeocoding(Location location) {

        //create an observable that will emit the response from the network call
        Observable<GeocodeResponse> responseObservable = ApiProvider.getGeocodeObs(location);

        //create an observer that is going to read the emitted values
        Observer<GeocodeResponse> responseObserver = new Observer<GeocodeResponse>() {
            String address;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");

            }

            @Override
            public void onNext(@NonNull GeocodeResponse geocodeResponse) {
                address = geocodeResponse.getResults().get(0).getFormattedAddress();
                Log.d(TAG, "onNext: " + address);

                Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.toString());

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
                finishedSaved = address;
            }
        };

        //subscribe the oberver to the observable
        responseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObserver);
        responseObservable.unsubscribeOn(Schedulers.io());

    }

    private void getAddressUsingGeocodingAddress(String Address) {

        //create an observable that will emit the response from the network call
        Observable<GeocodeResponse> responseObservableAddress = ApiProvider.getGeocodeAddress(Address);

        //create an observer that is going to read the emitted values
        Observer<GeocodeResponse> responseObserverAddress = new Observer<GeocodeResponse>() {
            //  String address;
            Double retroLan = 0.0;
            Double retroLon = 0.0;
            String latLon = "";

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");

            }

            @Override
            public void onNext(@NonNull GeocodeResponse geocodeResponse) {
                //  address = geocodeResponse.getResults().get(0).getFormattedAddress();
                // Log.d(TAG, "onNext: " + address);

                latLon = geocodeResponse.getResults().get(0).getGeometry().getLocation().getLat()
                        + ", "
                        + geocodeResponse.getResults().get(0).getGeometry().getLocation().getLng();
                //Toast.makeText(MainActivity.this, address, Toast.LENGTH_SHORT).show();

                retroLan = geocodeResponse.getResults().get(0).getGeometry().getLocation().getLat();
                retroLon = geocodeResponse.getResults().get(0).getGeometry().getLocation().getLng();

            }

            @Override
            public void onError(@NonNull Throwable e) {

                Log.d(TAG, "onError: " + e.toString());

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
                finishedSaved = latLon;
                currentLat = retroLan;
                currentLon = retroLon;
                showEditDialog(currentLat, currentLon);
            }
        };

        //subscribe the oberver to the observable
        responseObservableAddress.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObserverAddress);

        responseObservableAddress.unsubscribeOn(Schedulers.io());

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void btnClickSelections(View view) {
        switch (view.getId()) {
            case R.id.btnGoMap:
                Toast.makeText(this, finishedSaved, Toast.LENGTH_SHORT).show();
                /*
                String a = showLat;
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("lat", currentLocation.getLatitude());
                intent.putExtra("lon", currentLocation.getLongitude());
                intent.putExtra("location", currentLocation);
                startActivity(intent);
                */

                if (placeList != null) {
                    Intent intent = new Intent(this, MapsActivity.class);
                    intent.putExtra("passList", (Serializable) placeList);

                    startActivity(intent);
                }
                break;
            case R.id.btnConvert:

                //   Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                // showEditDialog();
                //  getLocation();
//getAddressUsingGeocoding("5481 Williams rd, tampa 33610, fl");

                String addy = etStreet.getText() + " ," + spinner1.getSelectedItem().toString() + ", " + etZipcode.getText();
                getAddressUsingGeocodingAddress(addy);
                //getAddressUsingGeocodingAddress("5481 Williams rd, tampa 33610, fl");
                //showEditDialog(currentLat, currentLon);
                //Toast.makeText(this, finishedSaved, Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private void showEditDialog(Double lat, Double lon) {

        FragmentManager fm = getSupportFragmentManager();
        //      Bundle args = new Bundle();
        //  args.putString("lat", showLat);
//args.putString("lon", showLon);


        // String a = address;
        MyAlertDialogFragment alertDialog = MyAlertDialogFragment.newInstance("Some title", lat, lon);
        //  alertDialog.setArguments(args);
        alertDialog.show(fm, "fragment_alert");


    }
    /*
    public void showMyAlert(View view) {
        MyAlertDialogFragment myAlert = new MyAlertDialogFragment(this);
        myAlert.show(getFragmentManager(), "My New Alert");
    }
*/

    public void onUserSelectValue(Double passLati, Double passLongi) {
        Toast.makeText(this, "from dialog: " + passLati + ", " + passLongi, Toast.LENGTH_SHORT).show();
        Place place = new Place(passLati, passLongi);
        placeList.add(place);


        rvPersonList = (RecyclerView) findViewById(R.id.rvPersonList);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(placeList);
        rvPersonList.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        itemAnimator = new DefaultItemAnimator();
        rvPersonList.setLayoutManager(layoutManager);
        rvPersonList.setItemAnimator(itemAnimator);

    }

}

package com.example.kiat.a1553015_1553033_doan;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class map_looking_for extends AppCompatActivity implements OnMapReadyCallback {

//    private GoogleMap mMap;
//    LatLng src= new LatLng(10.762593, 106.682526),dest=new LatLng(10.758198, 106.681657);
//    private String serverKey = "AIzaSyClVauEbTnKOH95kI8oTHuRYt293y6hHz4";
    ImageButton send;

    private GoogleMap mMap;
    LatLng src,dest;
    private String serverKey = "AIzaSyClVauEbTnKOH95kI8oTHuRYt293y6hHz4";
    private LatLng origin;
    private LatLng destination;
    Direction dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_looking_for);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id._map_lf);
        mapFragment.getMapAsync(this);
        anhxa();

    }

    private void anhxa() {

        send= (ImageButton) findViewById(R.id._send);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.clear();
                origin=place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });
        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment_2);

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.clear();
                destination=place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleDirection.withServerKey(serverKey)
                        .from(origin)
                        .to(destination)
                        .transportMode(TransportMode.DRIVING)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                if(direction.isOK()) {
                                    // Do something
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 17));
                                    mMap.addMarker(new MarkerOptions().position(origin));
                                    mMap.addMarker(new MarkerOptions().position(destination));
                                    ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                    mMap.addPolyline(DirectionConverter.createPolyline(map_looking_for.this, directionPositionList, 5, Color.RED));
                                } else {
                                    // Do something
                                }

                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {

                            }
                        });

            }
        });

//        ActivityCompat.requestPermissions(map_looking_for.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},123);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
    }

}

package com.example.kiat.a1553015_1553033_doan;

import android.*;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import im.delight.android.location.SimpleLocation;

import static java.lang.Math.sqrt;

public class use_map_master extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference mdata;
    ArrayList<user> _list;
    ArrayList<String> key_user;
    SimpleLocation.Listener  _listener;
    SimpleLocation getPos;
    LatLng currnetPos;
    Intent _getI;
    Bundle _getB;
    FloatingActionButton bt_search,bt_focus;
    ArrayList<LatLong_direction> _list_direction;
    LatLong_direction _tem_derection;
    private String serverKey = "AIzaSyClVauEbTnKOH95kI8oTHuRYt293y6hHz4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_map_master);
        mdata= FirebaseDatabase.getInstance().getReference();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id._map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(use_map_master.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},123);
        anhxa();
        _list=new ArrayList<>();
        key_user=new ArrayList<>();
        _list_direction=new ArrayList<>();
        _getI=getIntent();
        _getB=_getI.getBundleExtra("e_n");
        _listener = new SimpleLocation.Listener() {
            @Override
            public void onPositionChanged() {
                currnetPos = new LatLng(getPos.getLatitude(), getPos.getLongitude());
                //Log.e("_test",currnetPos.latitude+" "+currnetPos.longitude);
                //Toast.makeText(use_map_master.this, currnetPos.latitude+","+currnetPos.longitude, Toast.LENGTH_LONG).show();
                mdata.child("user").child(_getB.getString("key")).child("x").setValue(currnetPos.latitude);
                mdata.child("user").child(_getB.getString("key")).child("y").setValue(currnetPos.longitude);
            }
        };

        // Init Simple
        final boolean fineLocation = true;
        final boolean passiveMode = false;
        int updatePeriod = 60*1000;
        boolean newLocation;
        getPos = new SimpleLocation(this, fineLocation, passiveMode, updatePeriod);

        // Kiểm tra đã cấp quyền truy cập chưa
        if (!getPos.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        // Lấy vị trí hiện tại khi mới start app
        currnetPos = new LatLng(getPos.getLatitude(), getPos.getLongitude());
//
//        //Toast.makeText(this, currnetPos.toString(), Toast.LENGTH_SHORT).show();
        mdata.child("user").child(_getB.getString("key")).child("x").setValue(currnetPos.latitude);
        mdata.child("user").child(_getB.getString("key")).child("y").setValue(currnetPos.longitude);
        getPos.setListener(_listener);
        bt_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdata.child("user").child(_getB.getString("key")).child("focus").setValue(true);
                for(String i:key_user)
                {
                    if(!i.equals(_getB.getString("key")))
                    {
                        mdata.child("user").child(i).child("focus").setValue(true);
                    }
                }
            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _move=new Intent(use_map_master.this,map_looking_for.class);
                startActivity(_move);
            }
        });

    }

    private void anhxa() {
        bt_search= (FloatingActionButton) findViewById(R.id._search);
        bt_focus= (FloatingActionButton) findViewById(R.id._Focus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPos.beginUpdates();
    }

    @Override
    protected void onPause() {
        getPos.endUpdates();
        super.onPause();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mdata.child("group").child(_getB.getString("keygroup")).child("key_users")
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                mdata.child("user").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot2, String s) {
                        if(dataSnapshot2.getKey().equals(dataSnapshot.getValue()))
                        {
                           // Toast.makeText(use_map_master.this, dataSnapshot2.getKey(), Toast.LENGTH_SHORT).show();
                            key_user.add(dataSnapshot2.getKey());// lấy key để tạo danh sách tập trung
                            user _user=dataSnapshot2.getValue(user.class);
                            _list.add(_user);

                            // add direction to list
                            LatLong_direction _derection=new LatLong_direction(_user.x,_user.y,false,_user.email);
                            _list_direction.add(_derection);

                            //add nguoi cam đt
                            if(_user.email.equals(_getB.getString("email")))
                            {
                                _tem_derection=new LatLong_direction(_user.x,_user.y,false,_user.email);
                            }
                            if(_user.email.equals(_getB.getString("email")))
                            {
                                LatLng sydney = new LatLng(_user.x, _user.y);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(_user.Nickname)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.newicon))
                                );
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                            }
                            else {


                                LatLng sydney = new LatLng(_user.x, _user.y);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(_user.Nickname));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                            }

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        //Toast.makeText(use_map.this, "change", Toast.LENGTH_SHORT).show();
                        mMap.clear();
                        user _tem=dataSnapshot.getValue(user.class);
                        if(_tem.focus==true)
                        {
                            mdata.child("user").child(_getB.getString("key")).child("focus").setValue(false);
                        }
                        for(user i:_list)
                        {
                            if(i.email.equals(_tem.email))
                            {
                                i.x=_tem.x;
                                i.y=_tem.y;
                                //Toast.makeText(use_map_master.this, i.x+","+i.y, Toast.LENGTH_SHORT).show();
                            }
                            double path=sqrt((_tem_derection.x-i.x)*(_tem_derection.x-i.x)+(_tem_derection.y-i.y)*(_tem_derection.y-i.y));
                            if(path*1000>3.0)
                            {
                                Toast.makeText(use_map_master.this,"Đang ở xa "+ i.Nickname, Toast.LENGTH_SHORT).show();
                            }
                            if(i.email.equals(_getB.getString("email")))
                            {
                                LatLng sydney = new LatLng(i.x, i.y);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(i.Nickname)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.newicon))
                                );
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                            }
                            else {


                                LatLng sydney = new LatLng(i.x, i.y);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(i.Nickname));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                            }
                        }

                        //update vị trí thằng cằm đt
                        if(_tem.email.equals(_tem_derection.email))
                        {
                            _tem_derection.x=_tem.x;
                            _tem_derection.y=_tem.y;
                        }
                        //update List derection
                        for(int j=0;j<_list_direction.size();j++)
                        {
                            if(_list_direction.get(j).email.equals(_tem.email))
                            {
                                _list_direction.get(j).x=_tem.x;
                                _list_direction.get(j).y=_tem.y;

                            }
                            // vẽ lại các đường đi
                            if(_list_direction.get(j).check==true)
                            {
                                GoogleDirection.withServerKey(serverKey)
                                        .from(new LatLng(_tem_derection.x,_tem_derection.y))
                                        .to(new LatLng(_list_direction.get(j).x,_list_direction.get(j).y))
                                        .transportMode(TransportMode.DRIVING)
                                        .execute(new DirectionCallback() {
                                            @Override
                                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                                if(direction.isOK()) {
                                                    // Do something
                                                    ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                                    mMap.addPolyline(DirectionConverter.createPolyline(use_map_master.this, directionPositionList, 5, Color.RED));
                                                } else {
                                                    // Do something
                                                }

                                            }

                                            @Override
                                            public void onDirectionFailure(Throwable t) {

                                            }
                                        });
                            }

                        }

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (LatLong_direction i:_list_direction)
                {
                    LatLng tem=new LatLng(i.x,i.y);
                    if(marker.getPosition().equals(tem))
                    {
                        Toast.makeText(use_map_master.this, i.email, Toast.LENGTH_SHORT).show();
                        if(i.check==false)
                        {

                            for(LatLong_direction j:_list_direction)
                            {
                                if(j.check==true)
                                    j.check=false;
                            }
                            i.check=true;
                            mMap.clear();
                            for(user z:_list)
                            {
                                if(z.email.equals(_getB.getString("email")))
                                {
                                    LatLng sydney = new LatLng(z.x, z.y);
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(z.Nickname)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.newicon))
                                    );
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                                }
                                else {
                                    LatLng sydney = new LatLng(z.x, z.y);
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(z.Nickname));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                                }
                            }
                            GoogleDirection.withServerKey(serverKey)
                                    .from(new LatLng(_tem_derection.x,_tem_derection.y))
                                    .to(new LatLng(i.x,i.y))
                                    .transportMode(TransportMode.DRIVING)
                                    .execute(new DirectionCallback() {
                                        @Override
                                        public void onDirectionSuccess(Direction direction, String rawBody) {
                                            if(direction.isOK()) {
                                                // Do something
                                                ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                                mMap.addPolyline(DirectionConverter.createPolyline(use_map_master.this, directionPositionList, 5, Color.RED));
                                            } else {
                                                // Do something
                                            }

                                        }

                                        @Override
                                        public void onDirectionFailure(Throwable t) {

                                        }
                                    });
                        }
                        else
                        {
                            i.check=false;
                            mMap.clear();
                            for(user j:_list)
                            {
                                if(j.email.equals(_getB.getString("email")))
                                {
                                    LatLng sydney = new LatLng(j.x, j.y);
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(j.Nickname)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.newicon))
                                    );
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                                }
                                else {


                                    LatLng sydney = new LatLng(j.x, j.y);
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(j.Nickname));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });

    }
}

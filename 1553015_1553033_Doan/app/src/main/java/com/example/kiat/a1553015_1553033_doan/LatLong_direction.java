package com.example.kiat.a1553015_1553033_doan;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kiat on 23/12/2017.
 */

public class LatLong_direction {

    public double x;
    public  double y;
    public boolean check;
    public String email;
    public LatLong_direction(double x,double y, boolean check,String email) {
        this.x=x;
        this.y=y;
        this.check = check;
        this.email=email;
    }

}

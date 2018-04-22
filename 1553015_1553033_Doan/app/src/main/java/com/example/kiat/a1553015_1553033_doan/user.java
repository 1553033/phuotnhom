package com.example.kiat.a1553015_1553033_doan;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Kiat on 30/11/2017.
 */

public class user implements Serializable {
    public String email;
    public String Nickname;
    public double x,y;
    public String keyGroup;
    public boolean focus;
    public user() {
    }

    public user(String email, String nickname, double x, double y, String keyGroup, boolean focus) {
        this.email = email;
        Nickname = nickname;
        this.x = x;
        this.y = y;
        this.keyGroup = keyGroup;
        this.focus = focus;
    }
}

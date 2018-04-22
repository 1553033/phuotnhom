package com.example.kiat.a1553015_1553033_doan;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kiat on 01/12/2017.
 */

public class group implements Serializable{
    public String name_group;
    public ArrayList<String> key_users;
    public String key_master;
    public group() {

    }
    public group(String name_group, ArrayList<String> key_user, String key_master) {
        this.name_group = name_group;
        this.key_users = key_user;
        this.key_master=key_master;
    }
}

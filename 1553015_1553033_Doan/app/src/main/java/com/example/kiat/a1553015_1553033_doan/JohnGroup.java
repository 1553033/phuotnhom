package com.example.kiat.a1553015_1553033_doan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JohnGroup extends AppCompatActivity {

    DatabaseReference mdata;
    TextView jg_email,jg_nickname;
    EditText ed_jg;
    Button bt_jg,bt_create,bt_dktg;

    String key,email,key_group;
    ArrayList<String> name_group;
    ArrayAdapter adapter;
    ListView _list_name_group;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_john_group);
        mdata= FirebaseDatabase.getInstance().getReference();
        anhxa();
        //lấy dữ lieu bênh màn hình kia
        final Intent _getI=getIntent();
        final Bundle _getB=_getI.getBundleExtra("user");
        name_group=new ArrayList<>();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,name_group);
        _list_name_group.setAdapter(adapter);

        _list_name_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ed_jg.setText(name_group.get(position));
            }
        });
        mdata.child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user _tem=dataSnapshot.getValue(user.class);
                if(_tem.email.equals(_getB.getString("string")))
                {
                    key=dataSnapshot.getKey();
                    email=_tem.email;
                    jg_email.setText(_tem.email);
                    if(!_tem.keyGroup.equals("x"))
                    {
                        ed_jg.setText(_tem.keyGroup);
                    }

                }
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
        mdata.child("group").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                group _group=dataSnapshot.getValue(group.class);
                name_group.add(_group.name_group);
                adapter.notifyDataSetChanged();
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

        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> _temarry=new ArrayList<String>();
                _temarry.add(key);
                group _tem=new group(ed_jg.getText().toString(),_temarry,key);
                mdata.child("group").push().setValue(_tem);
                mdata.child("user").child(key).child("keyGroup").setValue(_tem.name_group);
            }
        });
        bt_jg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdata.child("group").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        group _temjon=dataSnapshot.getValue(group.class);
                        if(_temjon.name_group.equals(ed_jg.getText().toString()))
                        {
                            key_group = dataSnapshot.getKey();
                            Toast.makeText(JohnGroup.this,key, Toast.LENGTH_SHORT).show();
                            if(!dataSnapshot.child("key_master").getValue().equals(key))
                            {
                                Intent _i=new Intent(JohnGroup.this,use_map.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("email",jg_email.getText().toString());
                                bundle.putString("key",key);
                                bundle.putString("keygroup",key_group);
                                _i.putExtra("e_n",bundle);
                                startActivity(_i);
                            }
                            else {
                                Intent _i=new Intent(JohnGroup.this,use_map_master.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("email",jg_email.getText().toString());
                                bundle.putString("key",key);
                                bundle.putString("keygroup",key_group);
                                _i.putExtra("e_n",bundle);
                                startActivity(_i);
                            }
                            //mdata.child("group").child(key_group).

                        }
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
            }
        });
        bt_dktg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdata.child("group").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        group _temjon1=dataSnapshot.getValue(group.class);
                        if(_temjon1.name_group.equals(ed_jg.getText().toString()))
                        {
                            _temjon1.key_users.add(key);
                                mdata.child("group").child(dataSnapshot.getKey()).child("key_users")
                                        .setValue(_temjon1.key_users);
                                mdata.child("user").child(key).child("keyGroup").setValue(_temjon1.name_group);
                        }
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
            }
        });
    }

    private int dangky() {
        
        mdata.child("group").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                group _tem_check=dataSnapshot.getValue(group.class);
                if(_tem_check.name_group.equals(ed_jg.getText().toString()))
                {

                    i=1;
                    Toast.makeText(JohnGroup.this, i+"", Toast.LENGTH_SHORT).show();
                }
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
        return i;
    }

    private void anhxa() {
        jg_email= (TextView) findViewById(R.id.jg_email);
        ed_jg= (EditText) findViewById(R.id.ed_jg);
        bt_jg= (Button) findViewById(R.id.bt_jg);

        bt_create= (Button) findViewById(R.id.bt_create);
        bt_dktg= (Button) findViewById(R.id.jg_thamgia);
        _list_name_group= (ListView) findViewById(R.id._list_view);
    }
}

package com.example.kiat.a1553015_1553033_doan;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference mdata;
    Button bt_dn,bt_dk;
    EditText ed_email,ed_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mdata= FirebaseDatabase.getInstance().getReference();
        anhxa();
        bt_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ed_email.getText().toString()))
                {
                    ed_email.setError("thiếu email");
                }
                else if(TextUtils.isEmpty(ed_pass.getText().toString()))
                {
                    ed_pass.setError("thiếu pass");
                }
                else {
                    dangnhap();
                }
            }
        });
        bt_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setTitle("Hợp thoại đăng ký");
                dialog.setContentView(R.layout.dialog);
                final EditText dk_email= (EditText) dialog.findViewById(R.id._dk_email);
                final EditText dk_pass= (EditText) dialog.findViewById(R.id._dk_pass);
                final EditText dk_nickname= (EditText) dialog.findViewById(R.id._dk_nickname);
                Button bt_huy= (Button) dialog.findViewById(R.id.dk_huy);
                Button bt_dongy= (Button) dialog.findViewById(R.id._dk_dongy);
                bt_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                bt_dongy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(dk_email.getText().toString()))
                        {
                            dk_email.setError("Thiếu Email");
                        }
                        else  if(TextUtils.isEmpty(dk_pass.getText().toString()))
                        {
                            dk_pass.setError("Thiếu Pass");
                        }
                        else if(TextUtils.isEmpty(dk_nickname.getText().toString()))
                        {
                            dk_nickname.setError("Thiếu Nick name");
                        }
                        else
                        {

                            dangky(dk_email.getText().toString(),dk_pass.getText().toString(),dk_nickname.getText().toString());
                            dialog.cancel();
                        }
                    }
                });
                dialog.show();;
            }
        });

    }

    private void dangnhap() {
        mAuth.signInWithEmailAndPassword(ed_email.getText().toString(),
                ed_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent user=new Intent(MainActivity.this,JohnGroup.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("string",ed_email.getText().toString());
                            user.putExtra("user",bundle);
                            startActivity(user);
                        } else {

                            Toast.makeText(MainActivity.this, "đăng nhâp fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void anhxa() {
        bt_dn= (Button) findViewById(R.id._bt_dn);
        bt_dk= (Button) findViewById(R.id._bt_dk);
        ed_email= (EditText) findViewById(R.id._login_email);
        ed_pass= (EditText) findViewById(R.id._login_pass);
    }

    private void dangky(final String _email, final String _pass,final String nickname) {
        mAuth.createUserWithEmailAndPassword(_email, _pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ed_email.setText(_email);
                            ed_pass.setText(_pass);

                            mdata.child("user").push().setValue(new user(_email,nickname,1.0,1.0,"x",false));
                            Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "no", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

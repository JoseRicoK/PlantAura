package com.example.plantaura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {


    TextView t1;
    FirebaseAuth firebaseAuth;
    DatabaseReference userReference;
    Button btnHS,btnHN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        t1 = findViewById(R.id.txtProfile);
        btnHS = findViewById(R.id.btnHubSi);
        btnHN = findViewById(R.id.btnHubNo);

        firebaseAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference("Users");


        btnHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cambiarEstado("SI");
            }
        });

        btnHN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cambiarEstado("NO");
            }
        });
    }

    private void cambiarEstado(String status) {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            userReference.child(uid).child("hub").setValue(status);
            Toast.makeText(Profile.this,"Estado del Hub actualizado: "+ status,Toast.LENGTH_SHORT).show();
        }
    }


    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            t1.setText(user.getEmail());
        }else{
            Intent intent = new Intent(Profile.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
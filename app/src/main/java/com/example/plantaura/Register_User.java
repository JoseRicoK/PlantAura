package com.example.plantaura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Register_User extends AppCompatActivity {


    EditText EdEmail, EdPass;
    Button btnReg;
    ProgressDialog progressDialog;
    TextView mHaveAccountTv;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        EdEmail = findViewById(R.id.EditEmail);
        EdPass = findViewById(R.id.EditPassword);
        btnReg = findViewById(R.id.btnRegister);
        mHaveAccountTv = findViewById(R.id.have_accountTv);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario....");
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email  = EdEmail.getText().toString().trim();
                String pass = EdPass.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    EdEmail.setError("Email invalido...");
                    EdEmail.setFocusable(true);

                }else if (pass.length()<6){
                    EdPass.setError("Password 6 caracteres");
                    EdPass.setFocusable(true);
                }else{
                    regiterUser(email,pass);
                }

            }
        });

        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_User.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void regiterUser(String email, String pass) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();


                            String email =user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("lastname","");
                            hashMap.put("image","");
                            hashMap.put("hub","");
                            hashMap.put("plantas","");


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(Register_User.this,"Registrando"+user.getEmail(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register_User.this, Profile.class));
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register_User.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register_User.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
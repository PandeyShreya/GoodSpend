package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText email_id , password;
    TextView forgot_password, create_newacc;
    Button login_button;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        forgot_password = findViewById(R.id.forgot_password);
        create_newacc = findViewById(R.id.create_newacc);
        login_button = findViewById(R.id.login_button);

        firebaseAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_id.getText().toString().trim();
                String Password= password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    email_id.setError("Email is Required!");
                    return;
                }
                if(TextUtils.isEmpty(Password)) {
                    password.setError("Password is Required!");
                    return;
                }

                //Authenticate the user
                firebaseAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Some Error Occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setMessage("Enter your Email to receive reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link

                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "ERROR!  Reset Link is Not sent" +e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
                passwordResetDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });



    }

    public void signup(View view) {
        startActivity(new Intent(getApplicationContext(), Registration.class));
    }



}
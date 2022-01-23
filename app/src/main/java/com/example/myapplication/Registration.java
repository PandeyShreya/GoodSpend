package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    private static final String TAG ="MyActivity" ;
    public static String userPassword;
    TextInputEditText full_name, email_id, mobile_no, username, password, confirm_password;
    RadioButton male, female, other;
    Button submit_button;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        full_name = findViewById(R.id.full_name);
        email_id = findViewById(R.id.email_id);
        mobile_no = findViewById(R.id.mobile_no);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm_password= findViewById(R.id.confirm_password);
        submit_button = findViewById(R.id.submit_button);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //If user is already logged in
        /*if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = full_name.getText().toString();
                String email = email_id.getText().toString().trim();
                String MobileNo= mobile_no.getText().toString();
                String UserName = username.getText().toString().trim();
                String Password= password.getText().toString().trim();
                String ConfirmPassword = confirm_password.getText().toString().trim();
                String gender = "";

                userPassword = ConfirmPassword;

                if(TextUtils.isEmpty(name)){
                    full_name.setError("Name is Required!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    email_id.setError("Email is Required!");
                    return;
                }
                if(TextUtils.isEmpty(MobileNo)){
                    mobile_no.setError("Mobile number is Required!");
                    return;
                }
                if(TextUtils.isEmpty(UserName)){
                    mobile_no.setError("UserName is Required!");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Password is Required!");
                    return;
                }
                if(password.length() < 6){
                    password.setError("Password must contain atleast 6 characters");
                    return;
                }
                if(TextUtils.isEmpty(ConfirmPassword)){
                    confirm_password.setError("Password Confirmation Required!");
                    return;
                }
                if(!(TextUtils.equals(Password, ConfirmPassword))){
                    password.setError("Passwords Do not Match");
                    return;
                }
                if(male.isChecked()){
                    gender = "Male";
                }
                else if(female.isChecked()){
                    gender = "Female";
                }
                else if(other.isChecked()){
                    gender = "Other";
                }
                String finalGender = gender;
                String userType = "Admin";
                //registering the user in firebase
                firebaseAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Registration.this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("full_name", name);
                            user.put("email_id", email);
                            user.put("mobile_no", MobileNo);
                            user.put("username", UserName);
                            user.put("password",Password);
                            user.put("gender", finalGender);
                            user.put("user_type", userType);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG ,"onSuccess: User Profile is created for"+userID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(Registration.this, "Some Error Occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });


    }
}
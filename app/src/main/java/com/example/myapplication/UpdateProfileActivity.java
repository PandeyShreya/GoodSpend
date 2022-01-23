package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    private static final String TAG ="MyActivity" ;
    public static String userPassword;
    TextInputEditText full_name, email_id, mobile_no, username, password, confirm_password;
    RadioButton male, female, other;
    Button update_button, delete_button;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private String onlineUserID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference incomeRef,expenseRef,balanceRef,autopayRef,reminderRef;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        full_name = findViewById(R.id.updatefull_name);
        email_id = findViewById(R.id.updateemail_id);
        mobile_no = findViewById(R.id.updatemobile_no);
        username = findViewById(R.id.updateusername);
        password = findViewById(R.id.updatepassword);
        confirm_password= findViewById(R.id.updateconfirm_password);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        male = findViewById(R.id.updatemale);
        female = findViewById(R.id.updatefemale);
        other = findViewById(R.id.updateother);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());

        String oldEmail=email_id.getText().toString().trim();
        String oldPassword = password.getText().toString().trim();

       update_button.setOnClickListener(new View.OnClickListener() {
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

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(oldEmail!=email) {
                    user.updateEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                    }
                                }
                            });
                }
                if(oldPassword!=ConfirmPassword) {
                    user.updatePassword(ConfirmPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });
                }


                //updation of user info
                final DocumentReference sfDocRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());

                db.runTransaction(new Transaction.Function<Void>() {

                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);


                        transaction.update(sfDocRef, "full_name", name);
                        transaction.update(sfDocRef, "email_id", email);
                        transaction.update(sfDocRef, "mobile_no", MobileNo);
                        transaction.update(sfDocRef, "username", UserName);
                        transaction.update(sfDocRef, "password", Password);
                        transaction.update(sfDocRef, "gender", finalGender);

                        return null;
                    }

                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });



    }

    protected void onStart(){
        super.onStart();
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String name = task.getResult().getString("full_name");
                    String email = task.getResult().getString("email_id");
                    String phone = task.getResult().getString("mobile_no");
                    String gender = task.getResult().getString("gender");
                    String password1 = task.getResult().getString("password");
                    String username1= task.getResult().getString("username");
                    
                    username.setText(username1);
                    full_name.setText(name);
                    email_id.setText(email);
                    mobile_no.setText(phone);
                    password.setText(password1);
                    confirm_password.setText(password1);
                    if(gender.equals("Male")){
                        male.setChecked(true);
                        female.setChecked(false);
                        other.setChecked(false);
                    }
                    else if(gender.equals("Female")){
                        male.setChecked(false);
                        female.setChecked(true);
                        other.setChecked(false);
                    }
                    else if(gender.equals("Other")){
                        male.setChecked(false);
                        female.setChecked(false);
                        other.setChecked(true);
                    }
                }
                else{
                    Toast.makeText(UpdateProfileActivity.this,"No Profile Exists", Toast.LENGTH_SHORT);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void deleteProfile(View view) {
        ShowDialog();
    }
    private void ShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete Profile?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                documentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                deleteuser(email_id.getText().toString(), password.getText().toString());

//                                Toast.makeText(UpdateProfileActivity.this, "Profile Deleted", Toast.LENGTH_SHORT);
//                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateProfileActivity.this, "Profile Deletion Failed", Toast.LENGTH_SHORT);

                            }
                        });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void deleteuser(String email, String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        incomeRef=FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);
        expenseRef=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        reminderRef=FirebaseDatabase.getInstance().getReference("reminders").child(onlineUserID);
        autopayRef=FirebaseDatabase.getInstance().getReference("autopay").child(onlineUserID);
        balanceRef=FirebaseDatabase.getInstance().getReference("balance").child(onlineUserID);


        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            incomeRef.removeValue();
                            expenseRef.removeValue();
                            balanceRef.removeValue();
                            autopayRef.removeValue();
                            reminderRef.removeValue();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User account deleted.");
                                                Toast.makeText(UpdateProfileActivity.this, "Profile Deleted", Toast.LENGTH_SHORT);
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                        }
                    });
        }
    }
}
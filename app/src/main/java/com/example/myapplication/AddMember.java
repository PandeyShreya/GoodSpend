package com.example.myapplication;

import static com.example.myapplication.Registration.userPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMember extends AppCompatActivity {
    private static final String TAG ="MyActivity" ;
    EditText memberName, memberEmail, memberPhone, memberRelation;
    RadioButton male, female, other;
    Switch adminAccess;
    Button saveButton;
    String newUserID="1";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DatabaseReference newMemberReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        memberName = findViewById(R.id.MemberName);
        memberEmail = findViewById(R.id.MemberEmail);
        memberPhone = findViewById(R.id.MemberPhone);
        memberRelation = findViewById(R.id.Relation);
        saveButton = findViewById(R.id.save_button);
        male = findViewById(R.id.Membermale);
        female = findViewById(R.id.Memberfemale);
        other = findViewById(R.id.Memberother);
        adminAccess = findViewById(R.id.AdminAccess);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        newMemberReference = FirebaseDatabase.getInstance().getReference().child("Members").child(firebaseAuth.getCurrentUser().getUid());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = memberName.getText().toString();
                String email = memberEmail.getText().toString().trim();
                String phone= memberPhone.getText().toString();
                String relation = memberRelation.getText().toString().trim();
                String gender = "";
                String adminaccess= "";
                String id = newMemberReference.push().getKey();
                String password = userPassword;
                if(male.isChecked()){
                    gender = "Male";
                }
                else if(female.isChecked()){
                    gender = "Female";
                }
                else if(other.isChecked()){
                    gender = "Other";
                }
                if(adminAccess.isChecked()){
                    adminaccess = "Allowed";
                }
                else{
                    adminaccess = "NotAllowed";
                }

                if(TextUtils.isEmpty(name)){
                    memberName.setError("Name is Required!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    memberEmail.setError("Email is Required!");
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    memberPhone.setError("Mobile number is Required!");
                    return;
                }
                if(TextUtils.isEmpty(relation)){
                    memberRelation.setError("Relation is Required!");
                    return;
                }

                String finalGender = gender;
                String finalAdminaccess = adminaccess;
//                firebaseAuth.createUserWithEmailAndPassword(email, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Toast.makeText(AddMember.this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
//                            DocumentReference documentReference = firestore.collection("members").document(newUserID);
//                            Map<String,Object> member = new HashMap<>();
//                            member.put("Name", name);
//                            member.put("Email", email);
//                            member.put("Mobile No", phone);
//                            member.put("Relation", relation);
//                            member.put("Gender", finalGender);
//                            member.put("Admin Access", finalAdminaccess);
//
//                            documentReference.set(member).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Log.d(TAG ,"onSuccess: User Profile is created for"+newUserID);
//                                    int id = Integer.parseInt(newUserID);
//                                    newUserID = String.valueOf(++id);
//                                }
//                            });
//
//                        }
//                        else{
//                            Toast.makeText(AddMember.this, "Some Error Occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                NewMemberData newMemberData = new NewMemberData(name, id, email, phone, relation, finalGender, finalAdminaccess);
                newMemberReference.child(id).setValue(newMemberData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddMember.this, "New Member added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddMember.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));


                    }
                });


            }
        });




    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.ProfileAdapter;
import com.example.myapplication.Adapters.ReminderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    TextView username, userType, userPhone, userGender, userEmail,noMember, Uname;
    LinearLayout profileLinearLayout;
    RecyclerView profileRecyclerView;
    ProfileAdapter profileAdapter;
    ArrayList<NewMemberData> arrayList;
    DatabaseReference memberReference;
    FirebaseAuth firebaseAuth;
    private String onlineUserID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.userName);
        userType = findViewById(R.id.userType);
        userPhone = findViewById(R.id.user_phone);
        userGender = findViewById(R.id.user_gender);
        userEmail = findViewById(R.id.user_email);
        Uname = findViewById(R.id.username);
//        noMember = findViewById(R.id.no_member);
//        profileLinearLayout = findViewById(R.id.profile_linear_layout);
//        profileRecyclerView = findViewById(R.id.profile_recycle);

        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        memberReference = FirebaseDatabase.getInstance().getReference("Members").child(onlineUserID);
        documentReference = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());

//        profileRecyclerView.setHasFixedSize(true);
//        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        arrayList = new ArrayList<>();
//        profileAdapter = new ProfileAdapter(this, arrayList);
//        profileRecyclerView.setAdapter(profileAdapter);
//
//        getRecyclerView();
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
                    String usertype = task.getResult().getString("user_type");
                    String uname = task.getResult().getString("username");

                    username.setText(name);
                    Uname.setText(uname);
                    userType.setText(usertype);
                    userEmail.setText(email);
                    userPhone.setText(phone);
                    userGender.setText(gender);
                }
                else{
                    Toast.makeText(Profile.this,"No Profile Exists", Toast.LENGTH_SHORT);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void getRecyclerView() {
        noMember.setText(" ");
        memberReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NewMemberData memberData = dataSnapshot.getValue(NewMemberData.class);
                    arrayList.add(memberData);
                }
                profileAdapter.notifyDataSetChanged();
            }

            @Override  
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
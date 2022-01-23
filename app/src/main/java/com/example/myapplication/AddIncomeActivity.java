package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddIncomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText Date, Amount, Note;
    Spinner Category;
    Button done_button,autopay_button;
    String income_category;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DatabaseReference incomeReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);


        Date = findViewById(R.id.date);
        Amount = findViewById(R.id.amount);
        Note = findViewById(R.id.note);
        Category = findViewById(R.id.category);
        done_button = findViewById(R.id.done_button);
//        autopay_button = findViewById(R.id.autopay_button);

        String[] incomeList = getResources().getStringArray(R.array.income_list);


        firebaseAuth = FirebaseAuth.getInstance();
        incomeReference = FirebaseDatabase.getInstance().getReference().child("income").child(firebaseAuth.getCurrentUser().getUid());

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,incomeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(adapter);

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = Amount.getText().toString().trim();
                String note = Note.getText().toString().trim();
                String income_category = Category.getSelectedItem().toString();

//                if(TextUtils.isEmpty(date)){
//                    Date.setError("Date is a required field");
//                    return;
//                }
                if(TextUtils.isEmpty(amount)){
                    Amount.setError("Amount is a required field");
                    return;
                }
//                if(TextUtils.isEmpty(category)){
//                    Category.setError("Category is a required field");
//                    return;
//                }
                String id = incomeReference.push().getKey();
                String date = Date.getText().toString().trim();
                if(date.isEmpty()){
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar calendar = Calendar.getInstance();
                    date = dateFormat.format(calendar.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                }


                IncomeData incomeData = new IncomeData(date,id, note, income_category,Integer.parseInt(amount));
                incomeReference.child(id).setValue(incomeData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddIncomeActivity.this, "Income Item added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddIncomeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }
                });


            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.category)
        {
            income_category= parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
//    public void income_autopay(View view) {
//        startActivity(new Intent(getApplicationContext(), AutopayActivity.class));
//    }
}



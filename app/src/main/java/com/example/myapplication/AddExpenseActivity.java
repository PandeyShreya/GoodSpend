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

public class AddExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText Date, Amount, Note;
    Spinner Category,PaymentType;
    Button done_button,autopay_button;
    String expense_category;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DatabaseReference expenseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Date = findViewById(R.id.date);
        Amount = findViewById(R.id.amount);
        Note = findViewById(R.id.note);
        Category = findViewById(R.id.expense_category);
        PaymentType = findViewById(R.id.payment_type);
        done_button = findViewById(R.id.done_button);
//        autopay_button = findViewById(R.id.autopay_button);

        String[] expenseList = getResources().getStringArray(R.array.expense_list);
        String[] paymentList = getResources().getStringArray(R.array.payment_mode);

        firebaseAuth = FirebaseAuth.getInstance();
        expenseReference = FirebaseDatabase.getInstance().getReference().child("expenses").child(firebaseAuth.getCurrentUser().getUid());

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,expenseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(adapter);

        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,paymentList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PaymentType.setAdapter(adapter1);

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = Amount.getText().toString().trim();
                String note = Note.getText().toString().trim();
                String expense_category = Category.getSelectedItem().toString();
                String payment_type = PaymentType.getSelectedItem().toString();

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
                String id = expenseReference.push().getKey();
                String date = Date.getText().toString().trim();
                if(date.isEmpty()){
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar calendar = Calendar.getInstance();
                    date = dateFormat.format(calendar.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                }


                ExpenseData expenseData = new ExpenseData(date,id, note, expense_category,payment_type,Integer.parseInt(amount));
                expenseReference.child(id).setValue(expenseData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddExpenseActivity.this, "Expense Item added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
        if(parent.getId() == R.id.expense_category)
        {
            expense_category= parent.getItemAtPosition(position).toString();
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
//    public void expense_autopay(View view) {
//        startActivity(new Intent(getApplicationContext(), AutopayActivity.class));
//    }

}

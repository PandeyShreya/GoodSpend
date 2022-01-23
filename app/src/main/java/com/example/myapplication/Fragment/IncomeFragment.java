package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.IncomeData;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView transaction_salary,transaction_saving,transaction_deposit,transaction_others;
    private String onlineUserID;
    private DatabaseReference incomeRef;
    FirebaseAuth firebaseAuth;

    private  double totalSavingsAmount = 0, totalSalaryAmount = 0, totalDepositAmount = 0, totalOtherAmount = 0;


    public IncomeFragment() {
        // Required empty public constructor
    }

    /*  /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        transaction_salary = view.findViewById(R.id.transaction_salary);
        transaction_saving = view.findViewById(R.id.transaction_saving);
        transaction_deposit = view.findViewById(R.id.transaction_deposit);
        transaction_others = view.findViewById(R.id.transaction_others);

        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        incomeRef = FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);

        getIncomeAmount();


        //return inflater.inflate(R.layout.fragment_income, container, false);
        return view;
    }
    private void getIncomeAmount() {
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object Category = map.get("category");
                        String incomeCategory = String.valueOf(Category);

                        if(incomeCategory.equals("Savings") || incomeCategory.equals("Savings(Income)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalSavingsAmount += pAmount;
                            transaction_saving.setText((String.valueOf(totalSavingsAmount)));
                        }
                        else if(incomeCategory.equals("Salary") || incomeCategory.equals("Salary(Income)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalSalaryAmount += pAmount;
                            transaction_salary.setText((String.valueOf(totalSalaryAmount)));
                        }
                        else if(incomeCategory.equals("Deposit") || incomeCategory.equals("Deposit(Income)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalDepositAmount += pAmount;
                            transaction_deposit.setText((String.valueOf(totalDepositAmount)));
                        }
                        else if(incomeCategory.equals("Others") || incomeCategory.equals("Others(Income)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalOtherAmount += pAmount;
                            transaction_others.setText((String.valueOf(totalOtherAmount)));
                        }


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
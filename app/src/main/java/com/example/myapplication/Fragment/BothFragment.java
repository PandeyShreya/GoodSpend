package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Use the {@link BothFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BothFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView transaction_salary, transaction_saving, transaction_deposit, transaction_others, grocery, entertainment,
            kids, savings, shopping, bills, travel, vehicle, fuels, medical, eatingOut, others;
    private String onlineUserID;
    private DatabaseReference incomeRef, expensesRef;
    FirebaseAuth firebaseAuth;

    private double totalSavingsAmount = 0, totalSalaryAmount = 0, totalDepositAmount = 0, totalOtherAmount = 0,
            totalGroceryAmount = 0, totalEntertainmentAmount = 0, totalKidsAmount = 0, totalexpense_SavingsAmount = 0,
            totalShoppingAmount = 0, totalBillsAmount = 0, totalTravelAmount = 0, totalVehicleAmount = 0,
            totalFuelsAmount = 0, totalMedicalAmount = 0, totalEatingOutAmount = 0, totalOthersAmount = 0;

    public BothFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BothFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BothFragment newInstance(String param1, String param2) {
        BothFragment fragment = new BothFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_both, container, false);
        View view = inflater.inflate(R.layout.fragment_both, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        transaction_salary = view.findViewById(R.id.both_salary);
        transaction_saving = view.findViewById(R.id.both_savings);
        transaction_deposit = view.findViewById(R.id.both_deposit);
        transaction_others = view.findViewById(R.id.income_others);

        grocery = view.findViewById(R.id.both_grocery);
        entertainment = view.findViewById(R.id.both_entertainment);
        kids = view.findViewById(R.id.both_kids);
        savings = view.findViewById(R.id.expense_savings);
        shopping = view.findViewById(R.id.both_shopping);
        bills = view.findViewById(R.id.both_bills);
        travel = view.findViewById(R.id.both_travel);
        vehicle = view.findViewById(R.id.both_vehicle);
        fuels = view.findViewById(R.id.both_fuels);
        medical = view.findViewById(R.id.both_medical);
        eatingOut = view.findViewById(R.id.both_eatingout);
        others = view.findViewById(R.id.expense_others);

        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        incomeRef = FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);

        getIncomeAmount();
        getExpenseAmount();
        return view;

    }

    private void getExpenseAmount() {
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object Category = map.get("expense_category");
                        String expenseCategory = String.valueOf(Category);

                        if(expenseCategory.equals("Grocery")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalGroceryAmount += pAmount;
                            grocery.setText((String.valueOf(totalGroceryAmount)));
                        }
                        else if(expenseCategory.equals("Entertainment")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalEntertainmentAmount += pAmount;
                            entertainment.setText((String.valueOf(totalEntertainmentAmount)));
                        }
                        else if(expenseCategory.equals("Kids")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalKidsAmount += pAmount;
                            kids.setText((String.valueOf(totalKidsAmount)));
                        }
                        else if(expenseCategory.equals("Savings/Investment")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalexpense_SavingsAmount+= pAmount;
                            savings.setText((String.valueOf(totalexpense_SavingsAmount)));
                        }
                        else if(expenseCategory.equals("Shopping")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalShoppingAmount+= pAmount;
                            shopping.setText((String.valueOf(totalShoppingAmount)));
                        }
                        else if(expenseCategory.equals("Bills")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalBillsAmount+= pAmount;
                            bills.setText((String.valueOf(totalBillsAmount)));
                        }
                        else if(expenseCategory.equals("Travel")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalTravelAmount+= pAmount;
                            travel.setText((String.valueOf(totalTravelAmount)));
                        }
                        else if(expenseCategory.equals("Vehicle")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalVehicleAmount+= pAmount;
                            vehicle.setText((String.valueOf(totalVehicleAmount)));
                        }
                        else if(expenseCategory.equals("Fuels")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalFuelsAmount+= pAmount;
                            fuels.setText((String.valueOf(totalFuelsAmount)));
                        }
                        else if(expenseCategory.equals("Medical")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalMedicalAmount+= pAmount;
                            medical.setText((String.valueOf(totalMedicalAmount)));
                        }
                        else if(expenseCategory.equals("Eating Out")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalEatingOutAmount+= pAmount;
                            eatingOut.setText((String.valueOf(totalEatingOutAmount)));
                        }
                        else if(expenseCategory.equals("Others")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalOthersAmount+= pAmount;
                            others.setText((String.valueOf(totalOthersAmount)));
                        }


                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                        if(incomeCategory.equals("Savings")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalSavingsAmount += pAmount;
                            transaction_saving.setText((String.valueOf(totalSavingsAmount)));
                        }
                        else if(incomeCategory.equals("Salary")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalSalaryAmount += pAmount;
                            transaction_salary.setText((String.valueOf(totalSalaryAmount)));
                        }
                        else if(incomeCategory.equals("Deposit")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalDepositAmount += pAmount;
                            transaction_deposit.setText((String.valueOf(totalDepositAmount)));
                        }
                        else if(incomeCategory.equals("Others")){
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
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
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView grocery, entertainment, kids, savings, shopping, bills, travel, vehicle, fuels, medical, eatingOut, others;
    private String onlineUserID;
    private DatabaseReference expensesRef;
    FirebaseAuth firebaseAuth;

    private  double totalGroceryAmount = 0, totalEntertainmentAmount = 0, totalKidsAmount = 0, totalSavingsAmount = 0,
            totalShoppingAmount = 0, totalBillsAmount = 0, totalTravelAmount = 0, totalVehicleAmount = 0,
            totalFuelsAmount = 0, totalMedicalAmount = 0, totalEatingOutAmount = 0, totalOthersAmount = 0;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
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
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);

        grocery = view.findViewById(R.id.transaction_grocery);
        entertainment = view.findViewById(R.id.transaction_entertainment);
        kids = view.findViewById(R.id.transaction_kids);
        savings = view.findViewById(R.id.transaction_save);
        shopping = view.findViewById(R.id.transaction_shopping);
        bills = view.findViewById(R.id.transaction_bill);
        travel = view.findViewById(R.id.transaction_travel);
        vehicle = view.findViewById(R.id.transaction_vehicle);
        fuels = view.findViewById(R.id.transaction_fuels);
        medical = view.findViewById(R.id.transaction_medical);
        eatingOut = view.findViewById(R.id.transaction_food);
        others = view.findViewById(R.id.transaction_others_expense);

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

                        if(expenseCategory.equals("Grocery") || expenseCategory.equals("Grocery(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalGroceryAmount += pAmount;
                            grocery.setText((String.valueOf(totalGroceryAmount)));
                        }
                        else if(expenseCategory.equals("Entertainment") || expenseCategory.equals("Entertainment(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalEntertainmentAmount += pAmount;
                            entertainment.setText((String.valueOf(totalEntertainmentAmount)));
                        }
                        else if(expenseCategory.equals("Kids") || expenseCategory.equals("Kids(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalKidsAmount += pAmount;
                            kids.setText((String.valueOf(totalKidsAmount)));
                        }
                        else if(expenseCategory.equals("Savings/Investment") || expenseCategory.equals("Savings/Investment(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalSavingsAmount+= pAmount;
                            savings.setText((String.valueOf(totalSavingsAmount)));
                        }
                        else if(expenseCategory.equals("Shopping") || expenseCategory.equals("Shopping(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalShoppingAmount+= pAmount;
                            shopping.setText((String.valueOf(totalShoppingAmount)));
                        }
                        else if(expenseCategory.equals("Bills") || expenseCategory.equals("Bills(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalBillsAmount+= pAmount;
                            bills.setText((String.valueOf(totalBillsAmount)));
                        }
                        else if(expenseCategory.equals("Travel") || expenseCategory.equals("Travel(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalTravelAmount+= pAmount;
                            travel.setText((String.valueOf(totalTravelAmount)));
                        }
                        else if(expenseCategory.equals("Vehicle") || expenseCategory.equals("Vehicle(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalVehicleAmount+= pAmount;
                            vehicle.setText((String.valueOf(totalVehicleAmount)));
                        }
                        else if(expenseCategory.equals("Fuels") || expenseCategory.equals("Fuels(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalFuelsAmount+= pAmount;
                            fuels.setText((String.valueOf(totalFuelsAmount)));
                        }
                        else if(expenseCategory.equals("Medical") || expenseCategory.equals("Medical(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalMedicalAmount+= pAmount;
                            medical.setText((String.valueOf(totalMedicalAmount)));
                        }
                        else if(expenseCategory.equals("Eating Out") || expenseCategory.equals("Eating Out(Expense)")){
                            Object Amount = map.get("amount");
                            double pAmount = Double.parseDouble(String.valueOf(Amount));
                            totalEatingOutAmount+= pAmount;
                            eatingOut.setText((String.valueOf(totalEatingOutAmount)));
                        }
                        else if(expenseCategory.equals("Others") || expenseCategory.equals("Others(Expense)")){
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
}
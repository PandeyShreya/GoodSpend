package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AnyChartView anyChartView;
    private String onlineUserID;
    private DatabaseReference expensesRef;
    FirebaseAuth firebaseAuth;

    private  int totalGroceryAmount = 0, totalEntertainmentAmount = 0, totalKidsAmount = 0, totalSavingsAmount = 0,
            totalShoppingAmount = 0, totalBillsAmount = 0, totalTravelAmount = 0, totalVehicleAmount = 0,
            totalFuelsAmount = 0, totalMedicalAmount = 0, totalEatingOutAmount = 0, totalOthersAmount = 0;


    public ExpenseChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseChartFragment newInstance(String param1, String param2) {
        ExpenseChartFragment fragment = new ExpenseChartFragment();
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
        View view = inflater.inflate(R.layout.fragment_expense_chart, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);

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
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalGroceryAmount += pAmount;
                        }
                        else if(expenseCategory.equals("Entertainment") || expenseCategory.equals("Entertainment(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalEntertainmentAmount += pAmount;

                        }
                        else if(expenseCategory.equals("Kids") || expenseCategory.equals("Kids(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalKidsAmount += pAmount;

                        }
                        else if(expenseCategory.equals("Savings/Investment") || expenseCategory.equals("Savings/Investment(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalSavingsAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Shopping") || expenseCategory.equals("Shopping(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalShoppingAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Bills") || expenseCategory.equals("Bills(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalBillsAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Travel") || expenseCategory.equals("Travel(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalTravelAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Vehicle") || expenseCategory.equals("Vehicle(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalVehicleAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Fuels") || expenseCategory.equals("Fuels(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalFuelsAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Medical") || expenseCategory.equals("Medical(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalMedicalAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Eating Out") || expenseCategory.equals("Eating Out(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalEatingOutAmount+= pAmount;

                        }
                        else if(expenseCategory.equals("Others") || expenseCategory.equals("Others(Expense)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalOthersAmount+= pAmount;

                        }
                        anyChartView = view.findViewById(R.id.expense_chart_view);
                        APIlib.getInstance().setActiveAnyChartView(anyChartView);
                        Pie pie = AnyChart.pie();

                        List<DataEntry> data = new ArrayList<>();
                        data.add(new ValueDataEntry("Grocery", totalGroceryAmount));
                        data.add(new ValueDataEntry("Entertainment", totalEntertainmentAmount));
                        data.add(new ValueDataEntry("Kids", totalKidsAmount));
                        data.add(new ValueDataEntry("Savings/Investment", totalSavingsAmount));
                        data.add(new ValueDataEntry("Shopping", totalShoppingAmount));
                        data.add(new ValueDataEntry("Bills", totalBillsAmount));
                        data.add(new ValueDataEntry("Travel", totalTravelAmount));
                        data.add(new ValueDataEntry("Vehicle", totalVehicleAmount));
                        data.add(new ValueDataEntry("Fuels", totalFuelsAmount));
                        data.add(new ValueDataEntry("Medical", totalMedicalAmount));
                        data.add(new ValueDataEntry("Eating Out", totalEatingOutAmount));
                        data.add(new ValueDataEntry("Others", totalOthersAmount));

//                        anyChartView.setBackgroundColor(Color.GREEN);
//                        anyChartView.setBackground(android.graphics.drawable.shapes.OvalShape);
                        pie.data(data);
                        pie.title("Expense Data");

                        pie.labels().position("outside");

                        pie.legend().title().enabled(true);
                        pie.legend().title()
                                .text("Items Spent on")
                                .padding(0d, 0d, 10d, 0d);

                        pie.legend()
                                .position("center-bottom")
                                .itemsLayout(LegendLayout.HORIZONTAL)
                                .align(Align.CENTER);

                        anyChartView.setChart(pie);

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
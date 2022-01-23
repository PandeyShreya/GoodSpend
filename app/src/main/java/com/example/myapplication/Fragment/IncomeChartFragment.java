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
 * Use the {@link IncomeChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AnyChartView anyChartView1;
    private String onlineUserID;
    private DatabaseReference incomeRef;
    FirebaseAuth firebaseAuth;

    private  int totalSavingsAmount = 0, totalSalaryAmount = 0, totalDepositAmount = 0, totalOtherAmount = 0;

    public IncomeChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeChartFragment newInstance(String param1, String param2) {
        IncomeChartFragment fragment = new IncomeChartFragment();
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
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        incomeRef = FirebaseDatabase.getInstance().getReference("income").child(onlineUserID);

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
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalSavingsAmount += pAmount;
                        }
                        else if(incomeCategory.equals("Salary") || incomeCategory.equals("Salary(Income)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalSalaryAmount += pAmount;
                        }
                        else if(incomeCategory.equals("Deposit") || incomeCategory.equals("Deposit(Income)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalDepositAmount += pAmount;
                        }
                        else if(incomeCategory.equals("Others") || incomeCategory.equals("Others(Income)")){
                            Object Amount = map.get("amount");
                            int pAmount = Integer.parseInt(String.valueOf(Amount));
                            totalOtherAmount += pAmount;
                        }
                        anyChartView1 = view.findViewById(R.id.income_chart_view);
                        APIlib.getInstance().setActiveAnyChartView(anyChartView1);
                        Pie pie1 = AnyChart.pie();

                        List<DataEntry> data1 = new ArrayList<>();
                        data1.add(new ValueDataEntry("Savings", totalSavingsAmount));
                        data1.add(new ValueDataEntry("Salary", totalSalaryAmount));
                        data1.add(new ValueDataEntry("Deposit", totalDepositAmount));
                        data1.add(new ValueDataEntry("Others", totalOtherAmount));

//                        anyChartView.setBackgroundColor(Color.GREEN);
//                        anyChartView.setBackground(android.graphics.drawable.shapes.OvalShape);
                        pie1.data(data1);
                        pie1.title("Income Data");

                        pie1.labels().position("outside");

                        pie1.legend().title().enabled(true);
                        pie1.legend().title()
                                .text("Items Spent on")
                                .padding(0d, 0d, 10d, 0d);

                        pie1.legend()
                                .position("center-bottom")
                                .itemsLayout(LegendLayout.HORIZONTAL)
                                .align(Align.CENTER);

                        anyChartView1.setChart(pie1);

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
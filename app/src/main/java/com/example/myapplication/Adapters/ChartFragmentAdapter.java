package com.example.myapplication.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.Fragment.BothFragment;
import com.example.myapplication.Fragment.ExpenseChartFragment;
import com.example.myapplication.Fragment.ExpenseFragment;
import com.example.myapplication.Fragment.IncomeChartFragment;
import com.example.myapplication.Fragment.IncomeFragment;

public class ChartFragmentAdapter extends FragmentPagerAdapter {
    public ChartFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new IncomeChartFragment();
            case 1: return new ExpenseChartFragment();
            default: return new IncomeChartFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==0){
            title = "INCOME";
        }
        if (position==1){
            title = "EXPENSES";
        }
        return title;
    }
}

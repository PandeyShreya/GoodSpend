package com.example.myapplication.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.Fragment.BothFragment;
import com.example.myapplication.Fragment.ExpenseFragment;
import com.example.myapplication.Fragment.IncomeFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new IncomeFragment();
            case 1: return new ExpenseFragment();
            case 2: return new BothFragment();
            default: return new IncomeFragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==0){
            title = "INCOME";
        }
        if (position==1){
            title = "EXPENSES";
        }
        if (position==2){
            title = "ALL";
        }
        return title;
    }
}

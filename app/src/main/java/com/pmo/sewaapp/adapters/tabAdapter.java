package com.pmo.sewaapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class tabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private  final List<String> stringListtitle = new ArrayList<>();


    public tabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    public void addFragment(Fragment fragment, String title) {
       fragmentList.add(fragment);
        stringListtitle.add(title);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringListtitle.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

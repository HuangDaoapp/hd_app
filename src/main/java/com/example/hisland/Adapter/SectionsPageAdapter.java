package com.example.hisland.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;
public class SectionsPageAdapter extends FragmentPagerAdapter {
    private  List<Fragment> fragments;
    public  SectionsPageAdapter (FragmentManager fragmentManager, List<Fragment> fragments){
        super(fragmentManager);
        this.fragments=fragments;
    }
    @Override
    public Fragment getItem(int i){
        return fragments.get(i);
    }
    @Override
    public int getCount(){
        return fragments.size();
    }
}

package com.database.android_2022_internshiptask.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.database.android_2022_internshiptask.fragments.BodyFragment;
import com.database.android_2022_internshiptask.fragments.HeadersFragment;
import com.database.android_2022_internshiptask.fragments.ParamsFragment;

public class TapAdapter extends FragmentStateAdapter {


    public TapAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }



    @Override
    public Fragment createFragment(int position) {
        if(position ==0){
            return new HeadersFragment();
        }else if(position==1){
            return new ParamsFragment();
        }
        return new BodyFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}

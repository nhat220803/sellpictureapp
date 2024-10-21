package com.example.sellpicture.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.sellpicture.fragment.CartFragment;
import com.example.sellpicture.fragment.HomeFragment;
import com.example.sellpicture.fragment.MoreFragment;
import com.example.sellpicture.fragment.ProFileFragment;
import com.example.sellpicture.fragment.TimkiemFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new HomeFragment();
        } else if (position == 1) {
            fragment = new TimkiemFragment();
        } else if (position == 2) {
            fragment = new CartFragment();
        } else if (position == 3) {
            fragment = new ProFileFragment();
        } else if (position == 4) {
            fragment = new MoreFragment();
        } else {
            fragment = new HomeFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

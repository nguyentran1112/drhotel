package com.example.drhotel;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPaperAdapter extends FragmentStateAdapter {

    public ViewPaperAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentHome();
            case 1:
                return new FragmentDashboard();
            case 2:
                return new TranslateFragment();
            default:
                return new FragmentHome();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

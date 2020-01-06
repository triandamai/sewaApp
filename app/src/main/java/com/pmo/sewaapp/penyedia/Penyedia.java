package com.pmo.sewaapp.penyedia;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pmo.sewaapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Penyedia extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyedia);
        ButterKnife.bind(this);
        loadFragment(new fragment_home());
        bottomNav.setOnNavigationItemSelectedListener(this);
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.bottom_navigation_item_home:
                fragment = new fragment_home();
                break;
            case R.id.bottom_navigation_item_progress:
                fragment = new fragment_history();
                break;
            case R.id.bottom_navigation_item_toko:
                fragment = new fragment_toko();
                break;
            case R.id.bottom_navigation_item_profile:
                fragment = new fragment_profile();
                break;
        }
        return loadFragment(fragment);
    }
}

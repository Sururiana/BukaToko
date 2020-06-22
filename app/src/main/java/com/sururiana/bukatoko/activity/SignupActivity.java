package com.sururiana.bukatoko.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.adapter.TabAdapter;
import com.sururiana.bukatoko.fragment.SigninFragment;
import com.sururiana.bukatoko.fragment.SignupFragment;

public class SignupActivity extends AppCompatActivity {

    public static TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        addTab(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setTitle("Pengguna Baru");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void addTab(ViewPager viewPager){
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignupFragment(),"Daftar");
        adapter.addFragment(new SigninFragment(),"Masuk");
        viewPager.setAdapter(adapter);

    }
}

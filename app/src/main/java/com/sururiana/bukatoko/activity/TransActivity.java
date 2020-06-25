package com.sururiana.bukatoko.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.adapter.TabAdapter;
import com.sururiana.bukatoko.fragment.trans.PaidFragment;
import com.sururiana.bukatoko.fragment.trans.UnpaidFragment;

public class TransActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        addTab(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pembelian");

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return super.onSupportNavigateUp();
    }

    private void addTab(ViewPager viewPager){
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new UnpaidFragment(),"Tertunda");
        adapter.addFragment(new PaidFragment(),"Di Proses");
        viewPager.setAdapter(adapter);
    }
}

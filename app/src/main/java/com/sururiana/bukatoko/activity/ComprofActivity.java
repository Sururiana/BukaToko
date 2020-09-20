package com.sururiana.bukatoko.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.sururiana.bukatoko.R;

import java.util.ArrayList;
import java.util.List;

public class ComprofActivity extends AppCompatActivity {
    ImageSlider imageSlider;
    Button btnMap, btnWa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprof);

        imageSlider = findViewById(R.id.slider);
        btnMap = findViewById(R.id.btnMap);
        btnWa = findViewById(R.id.btnWa);

        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.testi1));
        slideModels.add(new SlideModel(R.drawable.testi2));
        slideModels.add(new SlideModel(R.drawable.testi3));
        slideModels.add(new SlideModel(R.drawable.testi4));
        imageSlider.setImageList(slideModels,true);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ComprofActivity.this,MapsActivity.class));
            }
        });

        btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.apiWa)));
                startActivity(intent);
            }
        });
    }
}

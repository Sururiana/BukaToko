package com.sururiana.bukatoko.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.sururiana.bukatoko.App;
import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.data.model.Detail;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;
import com.sururiana.bukatoko.dialog.CartDialog;
import com.sururiana.bukatoko.utils.Converter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    TextView txtName, txtPrice, txtDescription;

    SliderLayout sliderLayout;
    ImageButton btnAddCart;
    Button btnCheckout;

    Bundle bundle;

    int detailPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bundle = getIntent().getExtras();
        Log.e("_LogImgIntent", bundle.getString("PRODUCT_IMAGE"));

        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescription);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnCheckout = findViewById(R.id.btnCheckout);


        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (App.sqLiteHelper.checkExists(bundle.getInt("PRODUCT_ID")) == 0){

                    Long cart_id = App.sqLiteHelper.addToCart(bundle.getInt("PRODUCT_ID"), txtName.getText().toString(),
                            bundle.getString("PRODUCT_IMAGE"), detailPrice);

                    Log.e("_log_cartId", String.valueOf(cart_id));

                }
                new CartDialog().showChartDialog(DetailActivity.this);
            }
        });


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        getDetails();
        getSupportActionBar().setTitle("Detail barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void getDetails() {
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        Call<Detail> call = apiInterface.getDetail(bundle.getInt("PRODUCT_ID"));
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {

                Detail.Data data = response.body().getData();
                txtName.setText(data.getProduct());

                detailPrice = data.getPrice();
                txtPrice.setText(Converter.rupiah(data.getPrice()));

                if (data.getDescription() != null){
                    txtDescription.setText(data.getDescription());
                }

                Detail detail = response.body();
                List<Detail.Data.Images> images = detail.getData().getImages();

                ArrayList<String> arrayList = new ArrayList<>();
                for (Detail.Data.Images img : images){
                    arrayList.add(img.getImage());
                    Log.e("_LogImages", img.getImage());
                }

                setSlider(arrayList);
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {

            }
        });
    }

    private void setSlider(ArrayList<String> urlImgs){
        sliderLayout = findViewById(R.id.slider);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        requestOptions.placeholder(R.drawable.ic_load);
        requestOptions.error(R.drawable.ic_close);


        for (int i = 0; i < urlImgs.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(urlImgs.get(i))
                    .setRequestOption(requestOptions)
                    .setBackgroundColor(Color.TRANSPARENT)
                    .setProgressBarVisible(false)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderLayout.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

package com.sururiana.bukatoko.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.adapter.CityAdapter;
import com.sururiana.bukatoko.data.Constant;
import com.sururiana.bukatoko.data.model.rajaongkir.City;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.editText)
    EditText editText;

    @OnClick(R.id.imgCancel) void clickCancel(){
        finish();
    }

    CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getCity();
    }

    private void getCity(){
        ApiInterface apiInterface = Api.getUrlRajaOngkir(Constant.BASE_URL_RAJAONGKIR_STARTER).create(ApiInterface.class);
        Call<City> call = apiInterface.getCities(Constant.KEY_RAJAONGKIR);
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {

                City.RajaOngkir rajaOngkir = response.body().getRajaOngkir();
                List<City.RajaOngkir.Results> results = rajaOngkir.getResults();

                adapter = new CityAdapter(CityActivity.this, results);
                recyclerView.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);



                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String search = editable.toString();

                        adapter.getFilter().filter(search);
                    }
                });
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {

            }
        });

    }
}

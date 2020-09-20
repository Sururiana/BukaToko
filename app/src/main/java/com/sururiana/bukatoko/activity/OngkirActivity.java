package com.sururiana.bukatoko.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sururiana.bukatoko.App;
import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.data.Constant;
import com.sururiana.bukatoko.data.database.PrefsManager;
import com.sururiana.bukatoko.data.model.Cart;
import com.sururiana.bukatoko.data.model.rajaongkir.Cost;
import com.sururiana.bukatoko.data.model.transaction.TransPost;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;
import com.sururiana.bukatoko.utils.Converter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OngkirActivity extends AppCompatActivity {
    //butterknife
    @BindView(R.id.linearSave) LinearLayout linearSave;
    @BindView(R.id.linearTrans) LinearLayout linearTrans;

    @BindView(R.id.edtDestination) EditText edtDestination;
    @BindView(R.id.edtAddress) EditText edtAddress;
    @BindView(R.id.edtPhone) EditText edtPhone;

    @BindView(R.id.spnService) Spinner spnService;
    @BindView(R.id.txtOngkir) TextView txtOngkir;

    @BindView(R.id.progressBar) ProgressBar progressBar;


    @OnClick(R.id.btnSave) void clickSave(){

        if (edtDestination.length() > 0 || edtAddress.length() > 0){
            linearSave.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            ArrayList<TransPost.Detail> arrayList = new ArrayList<>();
            for (int i = 0; i < CartActivity.cartList.size(); i++){
                TransPost.Detail detail = new TransPost().new Detail();
                detail.setProduct_id(CartActivity.cartList.get(i).getProduct_id());
                detail.setQty(CartActivity.cartList.get(i).getQty());
                detail.setPrice(CartActivity.cartList.get(i).getPrice());
                detail.setTotal(CartActivity.cartList.get(i).getTotal());

                arrayList.add(detail);
            }

            TransPost transPost = new TransPost();
            transPost.setUser_id( Integer.parseInt(App.prefsManager.getUserDetail().get(PrefsManager.SESS_ID)) );
            transPost.setPhone( edtPhone.getText().toString()); ;
            transPost.setDestination( edtDestination.getText().toString() + " - " + edtAddress.getText().toString()) ;
            transPost.setOngkir(ongkirValue);
            transPost.setGrandtotal(CartActivity.adapter.getTotal() + ongkirValue);
            transPost.setDetailList( arrayList );


            postTransaction(transPost);

        } else {
            Toast.makeText(getApplicationContext(),"Lengkapi alamat pengiriman", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnTrans) void clickTrans(){
        startActivity(new Intent(this,TransActivity.class));
        finish();
    }

    @OnClick(R.id.txtDismiss) void clickDismiss(){
        finish();
    }

    @OnClick(R.id.txtCancel) void clickCancel(){
        finish();
    }

    @OnClick(R.id.edtDestination) void clickDestination(){
        startActivity(new Intent(this,CityActivity.class));
    }

    List<String> listService;
    List<Integer> listValue;

    private int ongkirValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongkir);

        ButterKnife.bind(this);
        listService = new ArrayList<>();
        listValue = new ArrayList<>();


        getSupportActionBar().setTitle("Cek Ongkos Kirim");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (!Constant.DESTINATION.equals("")){
            edtDestination.setText(Constant.DESTINATION_NAME);

            getServices();
        }
    }

    private void getServices(){
        listValue.clear();
        listService.clear();
        //agar tidak ketumpuk



        ApiInterface apiInterface = Api.getUrlRajaOngkir(Constant.BASE_URL_RAJAONGKIR_STARTER).create(ApiInterface.class);
        Call<Cost> call = apiInterface.getCost(Constant.KEY_RAJAONGKIR,"343",Constant.DESTINATION,"1000","pos");
        call.enqueue(new Callback<Cost>() {
            @Override
            public void onResponse(Call<Cost> call, Response<Cost> response) {

                Cost.RajaOngkir ongkir = response.body().getRajaOngkir();

                List<Cost.RajaOngkir.Results> results = ongkir.getResults();
                for (int i = 0; i<results.size(); i++){
                    Log.e("_serviceCode", results.get(i).getCode());

                    List<Cost.RajaOngkir.Results.Costs> costs = results.get(i).getCosts();
                    for (int j = 0; j < costs.size(); j++) {
                        Log.e("_serviceDesc", costs.get(j).getDescription());

                        List<Cost.RajaOngkir.Results.Costs.Data> data = costs.get(j).getCost();
                        for (int k = 0; k < data.size(); k++) {
                            Log.e("_serviceValue", String.valueOf(data.get(k).getValue() ));

                            listService.add("Rp "+ Converter.rupiah(data.get(k).getValue() ) +
                                    " (POS " + costs.get(j).getService() + ")");
                            listValue.add(data.get(k).getValue());
                        }
                    }

                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(OngkirActivity.this,
                        android.R.layout.simple_list_item_1, listService);

                spnService.setAdapter(arrayAdapter);
                spnService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        txtOngkir.setText("Rp " + Converter.rupiah(listValue.get(i) ));
                        ongkirValue = listValue.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            @Override
            public void onFailure(Call<Cost> call, Throwable t) {

            }
        });
    }

    private void postTransaction(TransPost transPost){
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        Call<TransPost> call = apiInterface.insertTrans(transPost);
        call.enqueue(new Callback<TransPost>() {
            @Override
            public void onResponse(Call<TransPost> call, Response<TransPost> response) {
                if (response.isSuccessful()){
                    linearTrans.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(),"Transaksi berhasil di buat", Toast.LENGTH_SHORT).show();
                    App.sqLiteHelper.clearTable();
                } else {
                    Toast.makeText(getApplicationContext(),"Stock habis, Mohon hapus dari keranjang",Toast.LENGTH_SHORT).show();
                    linearSave.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TransPost> call, Throwable t) {
            }
        });
    }
}

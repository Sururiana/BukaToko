package com.sururiana.bukatoko.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.adapter.TransDetailAdapter;
import com.sururiana.bukatoko.data.Constant;
import com.sururiana.bukatoko.data.model.TransDetail;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransDetActivity extends AppCompatActivity {

    Switch switchTagihan, switchPengiriman;
    LinearLayout linearTagihan, linearPengiriman;


    TextView txtCode, txtPrice, txtName, txtAddress, txtStatus;
    Button btnTrack;

    ProgressBar progressBar;
    RecyclerView recyclerView;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_det);
        //cast switch
        switchTagihan = findViewById(R.id.switchTagihan);
        switchPengiriman = findViewById(R.id.switchPengiriman);

        linearTagihan = findViewById(R.id.linearTagihan);
        linearPengiriman = findViewById(R.id.linearPengiriman);

        //cast view
        txtCode = findViewById(R.id.txtCode);
        txtPrice = findViewById(R.id.txtPrice);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtStatus = findViewById(R.id.txtStatus);

        btnTrack = findViewById(R.id.btnTrack);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        bundle = getIntent().getExtras();




        switchTagihan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    linearTagihan.setVisibility(View.VISIBLE);
                } else {
                    linearTagihan.setVisibility(View.GONE);
                }
            }
        });

        switchPengiriman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    linearPengiriman.setVisibility(View.VISIBLE);
                } else {
                    linearPengiriman.setVisibility(View.GONE);
                }
            }
        });

        txtCode.setText(bundle.getString("TRANSACTION_CODE"));
        getDetTrans();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Transaksi");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void getDetTrans(){
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        Call<TransDetail> call = apiInterface.getTransDetail(bundle.getString("TRANSACTION_CODE"));
        call.enqueue(new Callback<TransDetail>() {
            @Override
            public void onResponse(Call<TransDetail> call, Response<TransDetail> response) {

                TransDetail.Data data = response.body().getData();

                txtPrice.setText( data.getGrandtotal() );
                txtName.setText( data.getUser() );
                txtAddress.setText( data.getDestination() );
                txtStatus.setText( data.getStatus_transaction() );

                if (data.getStatus_transaction().equals("sent")){

                    btnTrack.setVisibility(View.VISIBLE);
                    btnTrack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                } else {
                    btnTrack.setVisibility(View.GONE);
                }

                List<TransDetail.Data.DetailTransaction> transactions = data.getDetail_transaction();
                TransDetailAdapter adapter = new TransDetailAdapter(TransDetActivity.this, transactions);
                recyclerView.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TransDetail> call, Throwable t) {

            }
        });
    }
}

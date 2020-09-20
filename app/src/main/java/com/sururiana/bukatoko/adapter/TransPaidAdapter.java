package com.sururiana.bukatoko.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sururiana.bukatoko.App;
import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.activity.TransDetActivity;
import com.sururiana.bukatoko.data.database.PrefsManager;
import com.sururiana.bukatoko.data.model.Upload;
import com.sururiana.bukatoko.data.model.transaction.TransUser;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransPaidAdapter extends RecyclerView.Adapter<TransPaidAdapter.ViewHolder> {

    private List<TransUser.Data> transPaid;
    Context context;

    public TransPaidAdapter (Context context, List<TransUser.Data> transaction){
        this.context = context;
        this.transPaid = transaction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_trans, viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final TransUser.Data data = transPaid.get(i);

        if (data.getStatus_transaction().equals("send")){
            viewHolder.txtInfo.setVisibility(View.GONE);
        }

        if (data.getStatus_transaction().equals("process")){
            viewHolder.txtInfo.setText(context.getString(R.string.info_pross));
            viewHolder.btnAction.setText("TERIMA");
            viewHolder.btnDp.setVisibility(View.GONE);
            viewHolder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);

                    builder.setTitle("Konfirmasi");
                    builder.setMessage("Apakah barang sudah diterima?");

                    builder.setPositiveButton("Diterima", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
                            Call<Upload> call = apiInterface.send(data.getTransaction_code());
                            call.enqueue(new Callback<Upload>() {
                                @Override
                                public void onResponse(Call<Upload> call, Response<Upload> response) {
                                    if (response.code() == 202){
                                        Toast.makeText(context,"Terimakasih",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context,"Terjadi Kesalahan",Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<Upload> call, Throwable t) {

                                }
                            });
                        }
                    });

                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });

        } else {
            viewHolder.btnAction.setVisibility(View.GONE);
            viewHolder.btnDp.setVisibility(View.GONE);
        }

        viewHolder.txtTitle.setText( data.getTransaction_code() );
        viewHolder.txtStatus.setText( data.getStatus_transaction() );

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TransDetActivity.class);
                intent.putExtra("TRANSACTION_CODE", data.getTransaction_code());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transPaid.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtStatus, txtInfo, btnDp;
        Button btnAction;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            btnDp = itemView.findViewById(R.id.btnDp);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnAction = itemView.findViewById(R.id.btnAction);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

    public void getSend(){

    }
}
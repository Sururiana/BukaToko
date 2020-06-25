package com.sururiana.bukatoko.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.activity.CartActivity;
import com.sururiana.bukatoko.data.Constant;

public class CartDialog {

    public void showChartDialog (final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_cart, null);
        builder.setView(view);

        view.findViewById(R.id.btnCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CartActivity.class));
                //finis detail activty
                ((Activity) context).finish();

            }
        });

        view.findViewById(R.id.btnPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.SHOP_NOW = true;
                context.startActivity(new Intent(context, CartActivity.class));
                ((Activity) context).finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

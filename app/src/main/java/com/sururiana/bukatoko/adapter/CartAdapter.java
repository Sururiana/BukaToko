package com.sururiana.bukatoko.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sururiana.bukatoko.App;
import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.activity.CartActivity;
import com.sururiana.bukatoko.data.model.Cart;
import com.sururiana.bukatoko.utils.Converter;
import com.sururiana.bukatoko.utils.GlideImage;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Cart> carts;

    public CartAdapter(Context context, List<Cart> carts){
        this.context = context;
        this.carts = carts;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_cart,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder viewHolder, final int i) {
        final Cart cart = carts.get(i);

        cart.setQty( 1 );
        cart.setTotal( cart.getTotal() );

        viewHolder.txtName.setText(cart.getProduct());
        viewHolder.txtPrice.setText(Converter.rupiah(cart.getPrice()));
        GlideImage.get(context,cart.getImage(), viewHolder.imgProd);



        viewHolder.spnQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total = Integer.valueOf( adapterView.getItemAtPosition(i).toString() ) * cart.getPrice();
                viewHolder.txtTotal.setText(Converter.rupiah( total ));

                cart.setQty( Integer.valueOf( adapterView.getItemAtPosition(i).toString() ) );
                cart.setTotal( total );

                getTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewHolder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);

                builder.setTitle("Konfirmasi");
                builder.setMessage("Yakin menghapus "+ cart.getProduct() + "dari keranjang?");

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
//                        Toast.makeText(context, "Hapus", Toast.LENGTH_SHORT).show();

                        //hapus dari database
                        App.sqLiteHelper.removeItem(String.valueOf(cart.getCart_id() ) );

                        //hapus dari recyclerview
                        carts.remove(i);
                        notifyDataSetChanged();
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

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProd;
        TextView txtName, txtPrice, txtTotal;
        ImageButton btnDel;
        Spinner spnQty;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgProd = view.findViewById(R.id.imgProd);
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtPrice);
            btnDel = view.findViewById(R.id.btnDel);

            txtTotal = view.findViewById(R.id.txtTotal);
            spnQty = view.findViewById(R.id.spnQty);

            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 1; i <= 10; i++){
                arrayList.add(String.valueOf(i));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayList);
            spnQty.setAdapter(adapter);

        }
    }

    public int getTotal(){
        int priceTotal =0;

        for (int i = 0; i < carts.size(); i++){
            priceTotal += carts.get(i).getTotal();
        }

        CartActivity.txtPriceTotal.setText("TOTAL Rp " + Converter.rupiah(priceTotal));
        return priceTotal;
    }
}

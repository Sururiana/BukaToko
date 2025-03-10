package com.sururiana.bukatoko.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sururiana.bukatoko.App;
import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.activity.CartActivity;
import com.sururiana.bukatoko.activity.SignupActivity;
import com.sururiana.bukatoko.data.model.User;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;
import com.sururiana.bukatoko.utils.AuthState;
import com.sururiana.bukatoko.utils.Converter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDialog {

    public void showLoginDialog (final Context context, final Menu menu){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(view);

        final EditText edtEmail = view.findViewById(R.id.edtEmail);
        final EditText edtPass = view.findViewById(R.id.edtPass);

        final AlertDialog alertDialog = builder.create();


        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtEmail.length() > 0 && edtPass.length() > 0) {
                    String email = edtEmail.getText().toString();
                    final String pass = edtPass.getText().toString();

                    if (Converter.isValidEmailId(email)){

                        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
                        Call<User> call = apiInterface.authLogin(email, pass);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Log.e("_logResponse",response.toString());

                                if (response.isSuccessful()){

                                    User.Data data = response.body().getData();
                                    AuthState.isLoggedIn(menu);

                                    //menyimpan ke dalam prefsmanager
                                    App.prefsManager.createLoginSession(String.valueOf(data.getId()), data.getName(),
                                            data.getEmail(), pass);

                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Salah", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });


                    }
                }


            }
        });

        view.findViewById(R.id.txtRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SignupActivity.class));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}

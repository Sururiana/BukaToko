package com.sururiana.bukatoko.fragment.trans;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sururiana.bukatoko.App;
import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.adapter.TransPaidAdapter;
import com.sururiana.bukatoko.adapter.TransUnpaidAdapter;
import com.sururiana.bukatoko.data.database.PrefsManager;
import com.sururiana.bukatoko.data.model.transaction.TransUser;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaidFragment extends Fragment {
    RecyclerView recyclerView;
    TextView textView;



    public PaidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paid, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.textView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        getTrans();

        return view;
    }

    private void getTrans(){
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        Call<TransUser> call = apiInterface.getTransPaid(App.prefsManager.getUserDetail().get(PrefsManager.SESS_ID));
        call.enqueue(new Callback<TransUser>() {
            @Override
            public void onResponse(Call<TransUser> call, Response<TransUser> response) {
                if (response.isSuccessful()){
                    TransUser transUser = response.body();
                    List<TransUser.Data> data = transUser.getData();

                    recyclerView.setAdapter(new TransPaidAdapter(getContext(), data));
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TransUser> call, Throwable t) {

            }
        });
    }

}

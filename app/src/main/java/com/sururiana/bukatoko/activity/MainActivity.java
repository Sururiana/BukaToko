package com.sururiana.bukatoko.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sururiana.bukatoko.App;
import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.adapter.ProductAdapter;
import com.sururiana.bukatoko.data.model.Product;
import com.sururiana.bukatoko.data.model.User;
import com.sururiana.bukatoko.data.retrofit.Api;
import com.sururiana.bukatoko.data.retrofit.ApiInterface;
import com.sururiana.bukatoko.dialog.LoginDialog;
import com.sururiana.bukatoko.utils.AuthState;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    Menu menu;

    private void getProducts(){

        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        Call<Product> call = apiInterface.getProducts();
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = response.body();
                List<Product.Data> products = product.getProducts();
                Collections.sort(products, Product.Data.BY_STOCK);

                Log.e("_logSizeProducts", String.valueOf(products.size()));


                recyclerView.setAdapter(new ProductAdapter(MainActivity.this, products));


                for (int i =0; i < products.size(); i++){
                    Log.e("logNameProducts", products.get(i).getProduct());
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        menu = navigationView.getMenu();

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setAdapter(null);
                getProducts();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProducts();

        if (menu != null){
            if (App.prefsManager.isLoggedIn()){
                AuthState.isLoggedIn(menu);
            } else {
                AuthState.isLoggedOut(menu);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.custom_menu, menu);

//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            if (App.prefsManager.isLoggedIn()){
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            } else {
                new LoginDialog().showLoginDialog(MainActivity.this, menu);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this,SignupActivity.class));
        } else if (id == R.id.nav_notif) {
            startActivity(new Intent(MainActivity.this,NotifActivity.class));
        } else if (id == R.id.nav_trans) {
            startActivity(new Intent(MainActivity.this,TransActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        } else if (id == R.id.nav_logout) {
            App.prefsManager.logoutUser();
            AuthState.isLoggedOut(menu);
        } else if (id == R.id.nav_profusaha){
            startActivity(new Intent(MainActivity.this,ComprofActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

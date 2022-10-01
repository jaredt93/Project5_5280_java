package com.group3.project4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.project4.R;
import com.example.project4.databinding.ActivityMainBinding;
import com.group3.project4.cart.CartFragment;
import com.group3.project4.login.LoginFragment;
import com.group3.project4.login.LoginResult;
import com.group3.project4.profile.User;
import com.group3.project4.profile.UserProfileFragment;
import com.group3.project4.shop.Item;
import com.group3.project4.shop.ShopFragment;
import com.group3.project4.signup.SignupFragment;
import com.group3.project4.util.Globals;
import com.group3.project4.util.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
    LoginFragment.IListener, SignupFragment.IListener, UserProfileFragment.IListener, ShopFragment.IListener, CartFragment.IListener {
    ActivityMainBinding binding;
    User user;
    ArrayList<Item> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        if (user == null) {
//            binding.bottomNavigationView.setVisibility(View.INVISIBLE);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.layoutView, new LoginFragment(), "LoginFragment")
//                    .commit();
//        } else {
            Log.d("JWT", "onCreate: " + user);
            binding.bottomNavigationView.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layoutView, new ShopFragment())
                    .commit();
//        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.shopFragment:
                    replaceFragment(new ShopFragment());
                    break;
                case R.id.cartFragment:
                    replaceFragment(CartFragment.newInstance(cartItems));
                    break;
                case R.id.userProfileFragment:
                    replaceFragment(UserProfileFragment.newInstance(user));
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutView, fragment).commit();
    }

    @Override
    public void signup() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutView, new SignupFragment(), "SignupFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registerCancelled() {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LoginFragment");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void loginSuccess(String email, String password) {
        setUser(email, password);
    }

    private void setUser(String email, String password) {
        RetrofitInterface retrofitInterface;
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(Globals.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);

        Call<LoginResult> call = retrofitInterface.login(data);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    LoginResult result = response.body();
                    user = new User(result.getId(), result.getEmail(), result.getFirstName(),
                            result.getLastName(), result.getCity(),
                            result.getGender(), "", result.getToken(), result.getAge(), result.getWeight(),
                            result.getAddress());

                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layoutView, new ShopFragment())
                            .commit();
                } else {
                    Toast.makeText(getApplicationContext(), "you were not found   ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void chooseProfileImage() {

    }

    @Override
    public void signOut() {
        finishAffinity();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void updateUserProfile() {

    }

    @Override
    public void addItemToCart(Item item) {
        int index = cartItems.indexOf(item);

        if(index != -1) {
            Item tempItem = cartItems.get(index);
            tempItem.setQuantity(tempItem.getQuantity() + 1);
            cartItems.set(index, tempItem);
        } else {
            cartItems.add(item);
        }

        Log.d("JWT", "addItemToCart: " + cartItems.toString());
    }

    @Override
    public void deleteItemFromCart(Item cartItem) {

    }
}
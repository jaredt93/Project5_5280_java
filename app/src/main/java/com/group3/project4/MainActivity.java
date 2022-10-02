package com.group3.project4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeClient;
//import com.braintreepayments.api.DropInClient;
//import com.braintreepayments.api.DropInListener;
//import com.braintreepayments.api.DropInRequest;
//import com.braintreepayments.api.DropInResult;
import com.braintreepayments.api.UserCanceledException;
import com.example.project4.R;
import com.example.project4.databinding.ActivityMainBinding;
import com.group3.project4.cart.CartFragment;
import com.group3.project4.cart.Order;
import com.group3.project4.history.OrderHistoryFragment;
import com.group3.project4.login.LoginFragment;
import com.group3.project4.login.LoginResult;
import com.group3.project4.profile.UpdateUserResult;
import com.group3.project4.profile.User;
import com.group3.project4.profile.UserProfileFragment;
import com.group3.project4.shop.Item;
import com.group3.project4.shop.ShopFragment;
import com.group3.project4.signup.SignupFragment;
import com.group3.project4.util.BraintreeClientTokenProvider;
import com.group3.project4.util.Globals;
import com.group3.project4.util.RetrofitInterface;
import com.group3.project4.util.UserResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
    LoginFragment.IListener, SignupFragment.IListener, UserProfileFragment.IListener, ShopFragment.IListener, CartFragment.IListener, OrderHistoryFragment.IListener {
    ActivityMainBinding binding;
    RetrofitInterface retrofitInterface;
    Retrofit retrofit;
    User user;
    private static String SHARED_PREF_JWT_TOKEN = "JWT_TOKEN";
    private static String SHARED_PREF_EMAIL = "EMAIL";

    // Braintree
    private BraintreeClient braintreeClient;
    //private DropInClient dropInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofit = new Retrofit.Builder()
                .baseUrl(Globals.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        createUserViaToken();

        if (user == null) {
            binding.bottomNavigationView.setVisibility(View.INVISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layoutView, new LoginFragment(), "LoginFragment")
                    .commit();
        } else {
            Log.d("JWT", "onCreate: " + user);
            binding.bottomNavigationView.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layoutView, new ShopFragment())
                    .commit();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.shopFragment:
                    replaceFragment(new ShopFragment());
                    break;
                case R.id.cartFragment:
                    replaceFragment(CartFragment.newInstance(user.getOrder()));
                    break;
                case R.id.userProfileFragment:
                    replaceFragment(UserProfileFragment.newInstance(user));
                    break;
            }

            return true;
        });

        braintreeClient = new BraintreeClient(this, new BraintreeClientTokenProvider());
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
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);

        MainActivity mainActivity = this;
        Call<UserResult> call = retrofitInterface.login(data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    user = new User(result.getId(), result.getEmail(), result.getFirstName(),
                            result.getLastName(), result.getCity(),
                            result.getGender(), result.getToken(), result.getAge(), result.getWeight(),
                            result.getAddress(), result.getOrder(), result.getOrderHistory());

                    SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SHARED_PREF_JWT_TOKEN, result.getToken());
                    editor.putString(SHARED_PREF_EMAIL, result.getEmail());
                    editor.apply();

                    String token = sharedPref.getString(SHARED_PREF_JWT_TOKEN, null);
                    String email = sharedPref.getString(SHARED_PREF_EMAIL, null);

                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layoutView, new ShopFragment())
                            .commit();
                } else {
                    Toast.makeText(getApplicationContext(), "you were not found   ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
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
                .replace(R.id.layoutView, new LoginFragment(), "LoginFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void updateUserProfile(HashMap<String, Object> data, User user) {
        this.user = user;
        Call<UserResult> call = retrofitInterface.updateUser(user.getToken(), data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Logout and log back in.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void showOrderHistory() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutView, OrderHistoryFragment.newInstance(user))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addItemToCart(Item item) {
        HashMap<String, Object> data = new HashMap<>();
        data = putUserData();

        if(user.getOrder() != null) {
            int index = user.getOrder().getCartItems().indexOf(item);

            if (index != -1) {
                Item tempItem = user.getOrder().getCartItems().get(index);
                tempItem.setQuantity(tempItem.getQuantity() + 1);
                user.getOrder().getCartItems().set(index, tempItem);
            } else {
                user.getOrder().getCartItems().add(item);
            }

            user.getOrder().setOrderTotal();
        } else {
            ArrayList<Item> cartItems = new ArrayList<>();
            cartItems.add(item);
            user.setOrder(new Order(new ArrayList<Item>(cartItems)));
        }

        data.put("order", user.getOrder());

        Call<UserResult> call = retrofitInterface.updateUser(user.getToken(), data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    Toast.makeText(getApplicationContext(), "Item added to cart.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private HashMap<String, Object> putUserData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("firstName", user.getFirst_name());
        data.put("lastName", user.getLast_name());
        data.put("city", user.getCity());
        data.put("gender", user.getGender());
        data.put("age", user.getAge());
        data.put("weight", user.getWeight());
        data.put("address", user.getAddress());
        data.put("customerId", user);
        data.put("order", user.getOrder());
        data.put("orderHistory", user.getOrderHistory());

        return data;
    }

    @Override
    public void deleteItemFromCart(Item cartItem) {
        HashMap<String, Object> data = new HashMap<>();
        data = putUserData();

        int index = user.getOrder().getCartItems().indexOf(cartItem);

        if (index != -1) {
            user.getOrder().getCartItems().remove(index);
        }

        user.getOrder().setOrderTotal();

        data.put("order", user.getOrder());

        Call<UserResult> call = retrofitInterface.updateUser(user.getToken(), data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    Toast.makeText(getApplicationContext(), "Item removed from cart.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        replaceFragment(CartFragment.newInstance(user.getOrder()));
    }

    @Override
    public void emptyCart() {
        user.setOrder(new Order());
        HashMap<String, Object> data = new HashMap<>();
        data = putUserData();

        data.put("order", null);

        Call<UserResult> call = retrofitInterface.updateUser(user.getToken(), data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    Toast.makeText(getApplicationContext(), "Cart emptied.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        replaceFragment(CartFragment.newInstance(user.getOrder()));
    }

    @Override
    public void checkout() {
        HashMap<String, Object> data = new HashMap<>();
        data = putUserData();

        if(user.getOrderHistory() != null) {
            user.getOrderHistory().add(user.getOrder());
        } else {
            ArrayList<Order> orderHistory = new ArrayList<>();
            orderHistory.add(user.getOrder());
            user.setOrderHistory(orderHistory);
        }

        data.put("orderHistory", user.getOrderHistory());
        data.put("order", null);
        user.setOrder(new Order());

        Call<UserResult> call = retrofitInterface.updateUser(user.getToken(), data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    Toast.makeText(getApplicationContext(), "Cart emptied.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        replaceFragment(CartFragment.newInstance(user.getOrder()));
    }

//    public void onBraintreeSubmit() {
//        DropInRequest dropInRequest = new DropInRequest();
//        dropInClient = new DropInClient(this, dropInRequest, new BraintreeClientTokenProvider());
//    }

//    private void launchDropIn() {
//        dropInClient.launchDropIn();
//    }
//
//    @Override
//    public void onDropInSuccess(@NonNull DropInResult dropInResult) {
//        // send dropInResult.getPaymentMethodNonce().getString() to server
//    }
//
//    @Override
//    public void onDropInFailure(@NonNull Exception error) {
//        if (error instanceof UserCanceledException) {
//            // user canceled
//        } else {
//            // handle error
//        }
//    }

    private void createUserViaToken() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String savedToken = sharedPref.getString(SHARED_PREF_JWT_TOKEN, null);
        String savedEmail = sharedPref.getString(SHARED_PREF_EMAIL, null);

        if (savedToken == null || savedEmail == null) return;

        HashMap<String, String> data = new HashMap<>();
        data.put("email", savedEmail);

        Call<UserResult> call = retrofitInterface.getUserByToken(savedToken, data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    user = new User(result.getId(), result.getEmail(), result.getFirstName(),
                            result.getLastName(), result.getCity(),
                            result.getGender(), result.getToken(), result.getAge(), result.getWeight(),
                            result.getAddress(), result.getOrder(), result.getOrderHistory());

                    sharedPref.edit().putString(SHARED_PREF_JWT_TOKEN, result.getToken());
                    sharedPref.edit().putString(SHARED_PREF_EMAIL, result.getEmail());

                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layoutView, new ShopFragment())
                            .commit();
                } else {
                    Toast.makeText(getApplicationContext(), "Token expired!!! Login again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
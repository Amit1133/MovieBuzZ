package com.example.amit.moviebuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    //k=XJfS1PS4ar5j4icB5NTqPSCK3fc=
    //id= 329791527753706
    public static int APP_REQUEST_CODE = 999;
    Button phoneBtn, emailBtn;
    AccessToken accessToken;
    LoginButton fbBtn;
    CallbackManager callbackManager;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        phoneBtn = findViewById(R.id.phone_btn);
        emailBtn = findViewById(R.id.email_btn);
        fbBtn = findViewById(R.id.fb_btn);
        //printHash();

        accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null || com.facebook.AccessToken.getCurrentAccessToken() != null) {
            //Handle Returning User
            goToMyLoggedInActivity();
        } else {
            //Handle new or logged out user
            emailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLogin(LoginType.EMAIL);
                }
            });


            phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLogin(LoginType.PHONE);
                }
            });

            fbBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fbBtn.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
                    fbBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
                    fbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            dialog = new ProgressDialog(LoginActivity.this);
                            dialog.setMessage("Gathering data........");
                            dialog.show();

                            GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    dialog.dismiss();
                                    Log.d("Response", response.toString());
                                    getData(object);

                                }
                            });
                            //graph api
                            Bundle parameter = new Bundle();
                            parameter.putString("fields", "id,email,first_name");
                            graphRequest.setParameters(parameter);
                            graphRequest.executeAsync();

                        }

                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login unSuccessful", Toast.LENGTH_LONG).show();


                        }

                        @Override
                        public void onError(FacebookException error) {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
        }
    }

    private void getData(JSONObject object) {
        try {
            String name = object.getString("first_name");
            String email = object.getString("email");

            SharedPreferences.Editor editor = getSharedPreferences("myInformation", MODE_PRIVATE).edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.apply();


            //Toast.makeText(this, "" + name + " " + email, Toast.LENGTH_SHORT).show();
            goToMyLoggedInActivity();
           /* Intent m=new Intent(LoginActivity.this,Homepage.class);
            m.putExtra("username",name);
            //i.putExtra("useremail",email);
            startActivity(m);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void startLogin(LoginType loginType) {
        if (loginType == LoginType.EMAIL) {
            final Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(
                            LoginType.EMAIL,
                            AccountKitActivity.ResponseType.TOKEN);
            // ... perform additional configuration ...
            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build());
            startActivityForResult(intent, APP_REQUEST_CODE);
        } else if (loginType == LoginType.PHONE) {
            final Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(
                            LoginType.PHONE,
                            AccountKitActivity.ResponseType.TOKEN);
            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build());
            startActivityForResult(intent, APP_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                goToMyLoggedInActivity();
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void goToMyLoggedInActivity() {
        Intent i = new Intent(this, Homepage.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void showErrorActivity(AccountKitError error) {
        Toast.makeText(this, "Got error" + error.toString(), Toast.LENGTH_SHORT).show();
    }


    private void printHash() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(
                    "com.example.amit.moviebuzz",
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature signature : info.signatures) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            Toast.makeText(this, Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
        }


    }
}

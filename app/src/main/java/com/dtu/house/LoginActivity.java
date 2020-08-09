package com.dtu.house;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {

    private String email = "lol", first_name = "lol", last_name = "lol";
    private static final int RC_SIGN_IN = 1;
    private SignInButton mgoogle;
    private LoginButton mfacebook;
    private TextView mtext_view;
    private LinearLayout mlinear;
    private FirebaseAuth mauth;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private ProgressBar mprogress;

    //new Check method
    private AccessTokenTracker accessTokenTracker;
    private AccessToken maccess;

    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!isConnected(LoginActivity.this)) {
            Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }


        mauth = FirebaseAuth.getInstance();

        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        mgoogle = findViewById(R.id.google_btn);
        mfacebook = findViewById(R.id.facebook_login);
        mtext_view = findViewById(R.id.facebook_text_view);
        mlinear = findViewById(R.id.linear_layout);

        mprogress = findViewById(R.id.progressBar);

        callbackManager = CallbackManager.Factory.create();
        mfacebook.setPermissions("email", "public_profile");


        mfacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                mprogress.setVisibility(View.VISIBLE);
                mgoogle.setVisibility(View.GONE);
                mfacebook.setVisibility(View.GONE);
                mtext_view.setVisibility(View.GONE);
                mlinear.setVisibility(View.GONE);

                AccessToken accessToken = loginResult.getAccessToken();


                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        maccess = currentAccessToken;


                    }
                };


                accessTokenTracker.startTracking();

                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                    }
                };
                mProfileTracker.startTracking();

                // setFacebookData(accessToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                                try {
                                    email = object.getString("email");
                                    first_name = object.getString("first_name");
                                    last_name = object.getString("last_name");
                                    String id = object.getString("id");

                                    if (last_name == null) {
                                        last_name = " ";
                                    }


                                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                                    SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                    final SharedPreferences.Editor editor = preferences.edit();

                                    editor.putString("first_name", first_name + " " + last_name);
                                    editor.putString("email", email);
                                    editor.putString("image", image_url);
                                    editor.commit();
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.dontAnimate();

                                    if (Profile.getCurrentProfile() == null && first_name.equals("")) {
                                        mProfileTracker = new ProfileTracker() {
                                            @Override
                                            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {


                                                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                                final SharedPreferences.Editor editor = preferences.edit();

                                                editor.putString("first_name", currentProfile.getFirstName() + " " + currentProfile.getLastName());
                                                editor.putString("email", currentProfile.getLinkUri().toString());
                                                editor.putString("image", currentProfile.getProfilePictureUri(200, 200).toString());
                                                editor.commit();

                                                mProfileTracker.stopTracking();
                                            }
                                        };


                                    }

                                    mProfileTracker.startTracking();


                                } catch (JSONException error) {


                                    Log.d("ERROR", error.getMessage());

                                }


                            }
                        }


                );


                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name ,last_name , email, id");
                request.setParameters(parameters);
                request.executeAsync();


                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {

                    SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("first_name", profile.getFirstName() + " " + profile.getLastName());
                    // editor.putString("email", profile.getLinkUri().toString());
                    editor.putString("image", profile.getProfilePictureUri(200, 200).toString());
                    editor.commit();


                    //handlefacebookkAccessToken(maccess);

                    AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                    mauth.signInWithCredential(credential)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                } else {

                    SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = preferences.edit();


                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                            editor.putString("first_name", currentProfile.getFirstName() + " " + currentProfile.getLastName());
                            // editor.putString("email", profile.getLinkUri().toString());
                            editor.putString("image", currentProfile.getProfilePictureUri(200, 200).toString());
                            editor.commit();
                            mProfileTracker.stopTracking();
                        }
                    };

                    AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                    mauth.signInWithCredential(credential)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onCancel() {

                mprogress.setVisibility(View.GONE);

                Toasty.info(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT, true).show();

            }

            @Override
            public void onError(FacebookException error) {

                mprogress.setVisibility(View.GONE);

                Toasty.info(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


                        Toasty.error(LoginActivity.this, connectionResult.getErrorMessage(), Toasty.LENGTH_LONG, false).show();

                    }
                })


                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });


    }


    private void setFacebookData(final AccessToken accessToken) {


        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {


                        try {
                            email = object.getString("email");
                            first_name = object.getString("first_name");
                            last_name = object.getString("last_name");
                            String id = object.getString("id");

                            if (last_name == null) {
                                last_name = " ";
                            }


                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = preferences.edit();

                            editor.putString("first_name", first_name + " " + last_name);
                            editor.putString("email", email);
                            editor.putString("image", image_url);
                            editor.commit();
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.dontAnimate();

                            if (Profile.getCurrentProfile() == null && first_name.equals("")) {
                                mProfileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {


                                        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = preferences.edit();

                                        editor.putString("first_name", currentProfile.getFirstName() + " " + currentProfile.getLastName());
                                        editor.putString("email", currentProfile.getLinkUri().toString());
                                        editor.putString("image", currentProfile.getProfilePictureUri(200, 200).toString());
                                        editor.commit();

                                        mProfileTracker.stopTracking();
                                    }
                                };


                            }

                            mProfileTracker.startTracking();


                        } catch (JSONException error) {


                            Log.d("ERROR", error.getMessage());

                        }


                    }
                }


        );


        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name ,last_name , email, id");
        request.setParameters(parameters);
        request.executeAsync();


    }


    private void handlefacebookkAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mprogress.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }


    }
/*

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {



            if(Profile.getCurrentProfile() == null)
            {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {


                        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("first_name", currentProfile.getFirstName() + " " + currentProfile.getLastName());
                        editor.putString("email",currentProfile.getLinkUri().toString());
                        editor.putString("image",currentProfile.getProfilePictureUri(200 , 200).toString());
                        editor.commit();

                            mProfileTracker.stopTracking();
                    }
                };

                mProfileTracker.startTracking();
            }

            SharedPreferences check = getSharedPreferences("prefs", Context.MODE_PRIVATE);

            if(check.getString("first_name" , "ghdkf").equals("")) {
              //kite
            }


            if (currentAccessToken != null) {

                // setFacebookData(currentAccessToken);

                // handlefacebookkAccessToken(currentAccessToken);
            }

        }
    };
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        callbackManager.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


            if (result.isSuccess()) {
                mfacebook.setEnabled(false);

                GoogleSignInAccount accoutn = result.getSignInAccount();

                GoogleSignInAccount details = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("first_name", details.getDisplayName());
                editor.putString("email", details.getEmail());
                editor.putString("image", details.getPhotoUrl().toString());
                editor.commit();

                firebaseAuthWithGoogle(accoutn);


            } else {

                mprogress.setVisibility(View.GONE);

                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();


            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount accoutn) {

        AuthCredential credential = GoogleAuthProvider.getCredential(accoutn.getIdToken(), null);
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            mfacebook.setEnabled(true);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                mprogress.setVisibility(View.GONE);
                mfacebook.setEnabled(true);

            }
        });


    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo moobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (moobile != null && moobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());

        } else {
            return false;
        }


    }

}


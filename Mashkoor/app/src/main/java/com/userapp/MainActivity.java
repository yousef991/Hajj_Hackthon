package com.userapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.userapp.com.userapp.utility.Singleton;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient googleApiClient;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);

        Button but1 = (Button) findViewById(R.id.uBtn);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DocAct.class);
                view.getContext().startActivity(intent);}
        });

        RelativeLayout loginBtn = (RelativeLayout) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (Singleton.getOurInstance().isNetworkAvailable(MainActivity.this)) {

                        signIn();

                    } else {

                        Singleton.getOurInstance().showToast(MainActivity.this,
                                getResources().getString(R.string.no_network));
                    }


                } catch (Exception e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        });

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading..");
        dialog.setTitle("Please Wait");
        dialog.setCancelable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (!dialog.isShowing())
                dialog.show();


            if (requestCode == RC_SIGN_IN) {

                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if (googleSignInResult.isSuccess()) {

                    GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                    FirebaseUserAuth(googleSignInAccount);
                } else {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) throws Exception {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(authCredential).addOnCanceledListener(MainActivity.this, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }).addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callHomePage();
                        } else {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void callHomePage() {

        Intent intent = new Intent(MainActivity.this, DecideAct.class);
        startActivity(intent);
//        customType(MainActivity.this, getString(R.string.left_to_right));
    }

    protected void signInwithGoogle() throws Exception {

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            signInwithGoogle();
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private void signIn() throws Exception {
        // Passing Google Api Client into Intent.
//        mFunctions.startProgress(mProgressDialog, getString(R.string.signing__in));
        dialog.show();
        dialog.setContentView(R.layout.progress_layout);
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
// An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("MainActivity", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog.isShowing())
            dialog.dismiss();
    }
}

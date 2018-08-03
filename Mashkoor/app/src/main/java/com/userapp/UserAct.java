package com.userapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.userapp.com.userapp.utility.LocationTracker;
import com.userapp.com.userapp.utility.Singleton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserAct extends Activity {

    private LocationTracker tracker;
    private EditText idTxt, nameID, cstMssg, ppID;
    private TextView fn;
    private String path;
    private Uri downloadURI;
    private Spinner spinnere;
    private String ill;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.user_act_layout);

        fn = (TextView) findViewById(R.id.fn);
        idTxt = (EditText) findViewById(R.id.idTxt);
        nameID = (EditText) findViewById(R.id.nameID);
        ppID = (EditText) findViewById(R.id.ppID);
        cstMssg = (EditText) findViewById(R.id.cstMssg);


        try {
            tracker = new LocationTracker(UserAct.this, UserAct.this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        RelativeLayout updBtn = (RelativeLayout) findViewById(R.id.updBtn);
        updBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singleton.getOurInstance().capturePic(UserAct.this, new Singleton.CallBackProfile() {
                    @Override
                    public void choosePhotoFromGallery() {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, Singleton.GALLERY);
                    }

                    @Override
                    public void takePhotoFromCamera() {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, Singleton.CAMERA);
                    }
                });


            }
        });

        RelativeLayout submitBtn = (RelativeLayout) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String idString = idTxt.getText().toString();
                String nameIDString = nameID.getText().toString();
                String ppIDString = ppID.getText().toString();
                String cstMssgString = cstMssg.getText().toString();


                if (!TextUtils.isEmpty(idString)) {
                } else {
                    Toast.makeText(UserAct.this, "Id field cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!TextUtils.isEmpty(nameIDString)) {
                } else {
                    Toast.makeText(UserAct.this, "Name field cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(ppIDString)) {
                } else {
                    Toast.makeText(UserAct.this, "Passport field cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(cstMssgString)) {
                } else {
                    Toast.makeText(UserAct.this, "Message field cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog();
            }
        });


        spinnere = (Spinner) findViewById(R.id.spinner);
        spinnere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ill = spinnere.getSelectedItem().toString();
                if (ill.equals("Midicend"))
                {
                    ill +=  "He Need Midicend";
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }


    void dialog() {


        final SweetAlertDialog pDialog = new SweetAlertDialog(UserAct.this,
                SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Confirm your details");
        pDialog.setContentText("Are you sure?");
        pDialog.setConfirmText("Yes");
        pDialog.show();
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismiss();
            }
        });
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                pDialog.dismiss();
                callService();


            }
        });


    }//


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Singleton.RESULT_CANCELED) {
            return;
        }
        if (requestCode == Singleton.GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserAct.this.getApplicationContext().
                            getContentResolver(), contentURI);
                    path = Singleton.getOurInstance().saveImage(UserAct.this, bitmap);
                    fn.setText(path);
                    uploadStorage();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UserAct.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Singleton.CAMERA) {
            try {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                path = Singleton.getOurInstance().saveImage(UserAct.this, thumbnail);
                fn.setText(path);
                uploadStorage();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT > 22) {
            checkAndRequestPermissions();
        }
    }

    private void uploadStorage() {

        if (Singleton.getOurInstance().isNetworkAvailable(UserAct.this)) {

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            Uri file = Uri.fromFile(new File(path));
            final StorageReference ref = mStorageRef.child("images/" + path);
            UploadTask uploadTask = ref.putFile(file);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadURI = task.getResult();
                        Toast.makeText(UserAct.this, "" + downloadURI, Toast.LENGTH_LONG);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            Singleton.getOurInstance().showToast(UserAct.this, getString(R.string.no_network));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Singleton.REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                perms.put(android.Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(UserAct.this,
                                android.Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(UserAct.this,
                                android.Manifest.permission.WRITE_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(UserAct.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(UserAct.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(UserAct.this,
                                android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                            checkAndRequestPermissions();
                        } else {
                            //~
                        }
                    }
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }


    public boolean checkAndRequestPermissions() {

        int re_Ex = ContextCompat.checkSelfPermission(UserAct.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);

        int wr_Ex = ContextCompat.checkSelfPermission(UserAct.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        //

        int acc_c = ContextCompat.checkSelfPermission(UserAct.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);

        int acc_F = ContextCompat.checkSelfPermission(UserAct.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int acc_N = ContextCompat.checkSelfPermission(UserAct.this,
                android.Manifest.permission.ACCESS_NETWORK_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (re_Ex != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (wr_Ex != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (acc_c != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (acc_F != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (acc_N != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(UserAct.this, listPermissionsNeeded.toArray(new
                    String[listPermissionsNeeded.size()]), Singleton.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }//


    private void callService() {


//        if (downloadUrl == null && !fn.getText().toString().trim().equalsIgnoreCase("")) {
//            Toast.makeText(UserAct.this, "Upload Storage First!", Toast.LENGTH_SHORT).show();
//            return;
//        }


        String idString = idTxt.getText().toString();
        String nameIDString = nameID.getText().toString();
        String ppIDString = ppID.getText().toString();
        //~~~~~~~~~~~~~~~~~~~~~~
        Singleton.getOurInstance().creatUser(UserAct.this, ill, downloadURI,
                idString, nameIDString, ppIDString,
                new Singleton.ServiceListener() {
                    @Override
                    public void onSuccess(String message) {


                        final SweetAlertDialog pDialog = new SweetAlertDialog(UserAct.this,
                                SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText("Successful");
                        pDialog.setContentText("Your request is successfully submitted. We will contact you soon");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                pDialog.dismiss();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {


                    }
                });
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    }//
}

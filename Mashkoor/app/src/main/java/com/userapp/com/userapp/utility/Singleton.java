package com.userapp.com.userapp.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.userapp.DocAct;
import com.userapp.R;
import com.userapp.UserAct;
import com.userapp.UserFirbase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by "Megha" on 09-03-2018.
 */

public class Singleton {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final Singleton ourInstance = new Singleton();
    public static final int RESULT_CANCELED = 0, GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/hajjdoc";
    public static String token = "";

    public static Singleton getOurInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    /*Generic method to display toast messages*/
    public void showToast(Context applicationContext, String message) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show();
    }//


    /*Check internet connection*/
    public boolean isNetworkAvailable(Context context) {
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            return true;
        }
    }

    public void capturePic(Context mContext, final CallBackProfile callBack) {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(mContext);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                callBack.choosePhotoFromGallery();
                                break;
                            case 1:
                                callBack.takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();


    }//


    public String saveImage(Context context, Bitmap myBitmap) {


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY);

//        File wallpaperDirectory = new File(
//                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            String filename = f.getName();
            String mPath = f.getAbsolutePath();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void creatUser(final Context mContext, String ill, Uri downloadUrl, String idString,
                          String nameIDString, String ppIDString,
                          final ServiceListener lS) {


        try {
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                    .child(mContext.getResources().getString(R.string.user_table));
            final String key = database.push().getKey();
            final UserFirbase _user = new UserFirbase(Singleton.token, ill,
                    CurrentDateTime(), downloadUrl,
                    key,
                    idString, nameIDString, ppIDString,
                    "" + LocationTracker.latitude, "" + LocationTracker.longitude,
                    Singleton.getOurInstance().getCompleteAddressString(
                            mContext, LocationTracker.latitude, LocationTracker.longitude));
            database.child(key).setValue(_user);
            lS.onSuccess("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String CurrentDateTime() {

        String formatted = "";
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            formatted = format1.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatted;
    }

    public String getCompleteAddressString(Context mContext, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            String address = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
//                strAdd = strReturnedAddress.toString();
                strAdd = cityName + ", " + stateName;
                Log.v("My Current loction ", strReturnedAddress.toString());
            } else {
                Log.v("My Current loction ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("My Current loction ", "Canont get Address!");
        }
        return strAdd;
    }

    public List<UserFirbase> getUsers(Context mContext, final SL ls) {

        try {
            FirebaseDatabase.getInstance().getReference(mContext.getString(R.string.user_table))
                    .orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<HashMap<String, UserFirbase>> t = new GenericTypeIndicator<HashMap<String, UserFirbase>>() {
                    };
                    HashMap<String, UserFirbase> map = dataSnapshot.getValue(t);
                    ArrayList<UserFirbase> topicsList = new ArrayList<UserFirbase>(map.values());
                    Collections.reverse(topicsList);
                    ls.onSuccess(topicsList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    ls.onFailure(databaseError.getMessage().toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ServiceListener {
        void onSuccess(String message);

        void onFailure(String message);
    }

    public interface SL {
        void onSuccess(List<UserFirbase> message);

        void onFailure(String message);
    }

    public interface CallBackProfile {
        void choosePhotoFromGallery();

        void takePhotoFromCamera();
    }

    public static final int PERMISSION_REQUEST_CODE = 121;
}

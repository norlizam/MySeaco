package jasonpaulraj.myseaco;

/**
 * Created by GPH on 10/18/2016.
 */

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonGeometry;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.kml.KmlLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jasonpaulraj.myseaco.module.GisInfo;
import jasonpaulraj.myseaco.module.MySQLiteHelper;

public class MapsEditOnline extends FragmentActivity implements OnMapReadyCallback {

    public final String TAG = "MapsEditOnline";

    public GoogleMap mMap;

    private GeoJsonGeometry mGeometry;

    protected LocationManager locationManager;

    private String JSON_STRING;

    public GeoJsonLayer layer1;
    public GeoJsonLayer layer2;
    public GeoJsonLayer layer3;
    public GeoJsonLayer layer4;
    public LatLng currentLocation, currentLocationOnMarker;

    public KmlLayer klayer1;
    public KmlLayer klayer2;
    public KmlLayer klayer3;
    public KmlLayer klayer4;

    private MySQLiteHelper db;
    // public CommonRadioCheck commonRadioCheck;
    private GoogleApiClient client;
    //qr code scanner object
    private IntentIntegrator qrScan;

    Context context;
    Spinner spinnerHouseStreetType, spinnerHouseAreaType, spinnerHouseMukim, spinnerHouseStatus;

    EditText editTxtBarcode, editTxtLat, editTxtLon, editTxtAddress, editTxtHouseNo, editTxtHouseStreetName, editTxtHouseAreaName, editTxtHouseBatu;
    RadioGroup radGrpHouseStreetType, radGrpHouseAreaType, radGrpHouseMukim;
    RadioButton radBtnHouseStreetType_Jln, radBtnHouseStreetType_Lrg, radBtnHouseStreetType_NA, radBtnHouseAreaType_Tmn, radBtnHouseAreaType_Kg, radBtnHouseAreaType_Felda,
            radBtnHouseAreaType_NA, radBtnHouseMukim_Bekok, radBtnHouseMukim_Chaah, radBtnHouseMukim_Gemereh, radBtnHouseMukim_Segamat, radBtnHouseMukim_Jabi;
    Button btnSave, btnBarScanner;
    TextView txtViewStatus;
    String mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName,
            mapsHouseBatu, mapsHouseMukim, mapsFullAddress2, mapInsertBy,mapsModifiedBy, mapsHouseStatus, checkLatLonIsExist, isEdit, id, barcode, fullAddress, fullAddress2, address2_no, address2_streetType,
            address2_streetName, address2_areaType, address2_areaName, address2_batu, address2_mukim, checkLatLonNotDup,  testLatitude, testLongitude, userID, userRegtype, radModeType, status;


    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    public static final float HUE_GREEN = 120; //Agree
    public static final float HUE_RED = 0; //Reject
    public static final float HUE_YELLOW =  60; //empty
    public static final float HUE_CYAN = 180; //New
    public static final float HUE_VIOLET = 270; //Not at home

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get Intent
        userID = getIntent().getExtras().getString("userID");
        userRegtype = getIntent().getExtras().getString("userRegtype");
        radModeType = getIntent().getExtras().getString("radModeType");

        if (mMap == null){
            Log.d("MapsActivity","if not null");
        }
        //        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        // mMap.setMyLocationEnabled(true);
        // mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //        }

        db = new MySQLiteHelper(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //STEP GET CURRENT LOCATION 5: start permission get current location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //end permission current location

        //STEP GET CURRENT LOCATION 4:start request current location
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MapsEditOnline.MyLocationListener()
        );
        //end request current location

		/*LatLng segamat = new LatLng(6,130);
        mMap.getMaxZoomLevel();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(segamat));*/

        //STEP GET CURRENT LOCATION 3:Add a marker in Current location and move the camera
        showCurrentLocation();

        refreshMaps();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                currentLocation = marker.getPosition();
                Log.d("Current Location", "onMarkerClick currentLocation:"+currentLocation);

                final CustomInfo customInfo = new CustomInfo(getLayoutInflater(), marker.getId());
                double lngT = marker.getPosition().longitude;
                double latT = marker.getPosition().latitude;
                String s_lat = "";
                String s_lon = "";
                String s_nam = "";
                String s_add = "";

                final String slngT = String.valueOf(lngT).trim();
                final String slatT = String.valueOf(latT).trim();
                Log.d("Untrimmed Coordinates", "" + slngT + "" + slatT);

                Log.d("CHECK", "Lat: [" + "" + slngT + "] , " + "Lon: [" + "" + slatT + "]");

                //Test
                //start set progress dialog
                final ProgressDialog progressDialog = new ProgressDialog(MapsEditOnline.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                retrieveMarkerInfo(slatT, slngT, customInfo,marker, progressDialog);

                            }
                        }, 500);
                return false;
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latlng) {

                Log.d(TAG,"MapLongClickListener["+latlng+"]");
                //remove (
                //StringBuilder str1 = new StringBuilder(String.valueOf(latlng));
                StringBuilder str1 = new StringBuilder(String.valueOf(latlng));
                str1.delete(0, 10);
                //remove )
                StringBuilder str2 = new StringBuilder(str1);
                str2.deleteCharAt(str2.length() - 1);
                //split ","
                String[] partsLatLng = str2.toString().split(",");
                String latitude = partsLatLng[0]; // 004
                String longitude = partsLatLng[1]; // 034556

                //start get address
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try
                {
                    addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                    if (addresses != null && addresses.size() > 0)
                    {
                        int id = 0;
                        String address = addresses.get(0).getAddressLine(0);
                        String address11 = addresses.get(0).getAddressLine(1);
                        String city = addresses.get(0).getLocality();
                        String barcode = "";
                        String isEdit = "N";

                        String address2_no = "";
                        String address2_streetType = "";
                        String address2_streetName = "";
                        String address2_areaType = "";
                        String address2_areaName = "";
                        String address2_batu = "";
                        String address2_mukim = "";

                        String fullAddress = address +" "+address11;
                        Log.d(TAG,"address["+address+"]");
                        Log.d(TAG,"address["+address11+"]");
                        Log.d(TAG,"address["+city+"]");

                        viewEditDialog(id, barcode, latitude, longitude, fullAddress, address2_no, address2_streetType, address2_streetName, address2_areaType, address2_areaName, address2_batu, address2_mukim, isEdit, status);
                    }
                }
                catch (IOException e) {
                }
                //end get address
            }
        });

        //start click on marker-edit
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d(TAG,"Edit Tricky["+ marker.getPosition()+"]");
                //remove (
                currentLocationOnMarker = marker.getPosition();
                StringBuilder str1 = new StringBuilder(String.valueOf(currentLocationOnMarker));
                str1.delete(0, 10);
                //remove )
                StringBuilder str2 = new StringBuilder(str1);
                str2.deleteCharAt(str2.length() - 1);
                //split ","
                String[] partsLatLng = str2.toString().split(",");
                String latitude = partsLatLng[0]; // 004
                String longitude = partsLatLng[1]; // 034556


                retrieveNEditPoints(latitude, longitude);
            }
        });
        //end click on marker-edit

    }

    //view edit maps dialog
    public void viewEditDialog(final int id, String barcode, String latitude, String longitude, String fullAddress, String address2_no, String address2_streetType,
                               String address2_streetName, String address2_areaType, String address2_areaName, String address2_batu, String address2_mukim, final String isEdit, String status){

        // custom dialog
        final Dialog dialog = new Dialog(MapsEditOnline.this);
        //dialog.setContentView(R.layout.dialog_custom);
        dialog.setContentView(R.layout.dialog_custom_location);
        dialog.setTitle("Add Location");

        // set the custom dialog components - text, image and button
        editTxtBarcode = (EditText) dialog.findViewById(R.id.editTxtBarcode);
        editTxtLat = (EditText) dialog.findViewById(R.id.editTxtLat);
        editTxtLon = (EditText) dialog.findViewById(R.id.editTxtLon);
        editTxtAddress = (EditText) dialog.findViewById(R.id.editTxtAddress);
        editTxtHouseNo = (EditText) dialog.findViewById(R.id.editTxtHouseNo);
        editTxtHouseStreetName = (EditText) dialog.findViewById(R.id.editTxtHouseStreetName);
        editTxtHouseAreaName = (EditText) dialog.findViewById(R.id.editTxtHouseAreaName);
        editTxtHouseBatu = (EditText) dialog.findViewById(R.id.editTxtHouseBatu);
        mapsFullAddress2 ="";
        editTxtAddress.setEnabled(false);
        txtViewStatus = (TextView) dialog.findViewById(R.id.txtViewStatus);
        spinnerHouseStatus = (Spinner) dialog.findViewById(R.id.spinnerStatus);

        spinnerHouseStreetType = (Spinner) dialog.findViewById(R.id.spinnerHouseStreetType);
        spinnerHouseAreaType = (Spinner) dialog.findViewById(R.id.spinnerHouseAreaType);
        spinnerHouseMukim = (Spinner) dialog.findViewById(R.id.spinnerHouseMukim);
        btnBarScanner = (Button) dialog.findViewById(R.id.btnBarScanner);

        //intializing scan object
        qrScan = new IntentIntegrator(this);


        if(isEdit.equals("N")){
            //set invisible
            txtViewStatus.setVisibility(View.GONE);
            spinnerHouseStatus.setVisibility(View.GONE);
        }else{
            //start spinner
            getStatusList();
            //end spinner
        }

        //start spinner
        getStreetTypeList();
        getAreaTypeList();
        getMukimList();

        //start spinner listener
        spinnerHouseStreetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapsHouseStreetType = parent.getItemAtPosition(position).toString();

                //set parameter in value type
                Log.d(TAG,"mapsHouseStreetType Listener: ["+mapsHouseStreetType+"]");
                //subValues = getParamSubValue(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        spinnerHouseAreaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int mapsHouseAreaTypes = position;
                mapsHouseAreaType = parent.getItemAtPosition(position).toString();

                //set parameter in value type
                Log.d(TAG,"mapsHouseStreetType Listener:["+mapsHouseAreaTypes+"]["+mapsHouseAreaType+"]");

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        spinnerHouseMukim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapsHouseMukim = parent.getItemAtPosition(position).toString();

                //set parameter in value type
                Log.d(TAG,"mapsHouseStreetType Listener: ["+mapsHouseMukim+"]");
                //subValues = getParamSubValue(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        //start spinner listener
        spinnerHouseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapsHouseStatus = parent.getItemAtPosition(position).toString();

                //set parameter in value type
                Log.d(TAG,"mapsHouseStatus Listener: ["+mapsHouseStatus+"]");
                //subValues = getParamSubValue(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
        //end spinner listener

        btnSave = (Button) dialog.findViewById(R.id.btnSave);

        //empty first
        if(isEdit.equalsIgnoreCase("N")){

            editTxtBarcode.setText("");
            mapInsertBy = userID; //temporary hardcoded
            mapsModifiedBy ="";
            mapsHouseStatus = "New";

        }else{

            //CommonRadioCheck commonRadioCheck = new CommonRadioCheck();

            editTxtBarcode.setText(barcode);
            editTxtHouseNo.setText(address2_no);
            editTxtHouseStreetName.setText(address2_streetName);
            editTxtHouseAreaName.setText(address2_areaName);
            editTxtHouseBatu.setText(address2_batu);

            //start set spinner the default according to value
            //String streetTypeVal = "Not applicable"; //the value you want the position for
            ArrayAdapter myAdapStreetType = (ArrayAdapter) spinnerHouseStreetType.getAdapter(); //cast to an ArrayAdapter
            int spinnerStreetPosition = myAdapStreetType.getPosition(address2_streetType);
            spinnerHouseStreetType.setSelection(spinnerStreetPosition);

            //String areaTypeVal = "Felda"; //the value you want the position for
            ArrayAdapter myAdapAreaType = (ArrayAdapter) spinnerHouseAreaType.getAdapter(); //cast to an ArrayAdapter
            int spinnerAreaPosition = myAdapAreaType.getPosition(address2_areaType);
            spinnerHouseAreaType.setSelection(spinnerAreaPosition);

            //String mukimVal = "Sg Segamat"; //the value you want the position for
            ArrayAdapter myAdapMukim = (ArrayAdapter) spinnerHouseMukim.getAdapter(); //cast to an ArrayAdapter
            int spinnerMukimPosition = myAdapMukim.getPosition(address2_mukim);
            spinnerHouseMukim.setSelection(spinnerMukimPosition);
            //end set spinner the default according to value

            ArrayAdapter myAdapStatus = (ArrayAdapter) spinnerHouseStatus.getAdapter(); //cast to an ArrayAdapter
            int spinnerHouseStatusPosition = myAdapStatus.getPosition(status);
            spinnerHouseStatus.setSelection(spinnerHouseStatusPosition);
            //end set spinner the default according to value

            mapsFullAddress2 ="";

            mapsModifiedBy = userID;

            mapInsertBy = "";

            mapsHouseStatus = status;

        }

        //then set
        editTxtLat.setText(latitude);
        editTxtLon.setText(longitude);
        editTxtAddress.setText(fullAddress);

        //scan barcode function
        btnBarScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiating the qr code scan
                qrScan.initiateScan();
            }
        });

        //save function
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //start get input
                mapsBarcode = editTxtBarcode.getText().toString();
                mapsLatitude = editTxtLat.getText().toString();
                mapsLongitude = editTxtLon.getText().toString();
                mapsAddress = editTxtAddress.getText().toString();
                mapsHouseNo = editTxtHouseNo.getText().toString();
                mapsHouseStreetName = editTxtHouseStreetName.getText().toString();
                mapsHouseAreaName = editTxtHouseAreaName.getText().toString();
                mapsHouseBatu = editTxtHouseBatu.getText().toString();
                //mapsFullAddress2 ="Full Address";
                //mapInsertBy = userID; //temporary hardcoded
                //mapsModifiedBy = "";//temporary hardcoded

                //start set fullAddress
                mapsFullAddress2 = "";
                String [] mapsFullAddress2Split = {mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName,mapsHouseBatu,mapsHouseMukim};
                for( String address : mapsFullAddress2Split ) {
                    if(address!=null )
                        mapsFullAddress2+=address+" ";
                }
                //end set fullAddress

                Log.d(TAG,"isEdit: ["+isEdit+"] ID: ["+id+"] barcode: ["+mapsBarcode+"] latitude: ["+mapsLatitude+"] longitude: ["+mapsLongitude+"] streetType: ["+mapsHouseStreetType+"] " +
                        "streetName:["+mapsHouseStreetName+"] areaType: ["+mapsHouseAreaType+"] areaName: ["+mapsHouseAreaName+"] batu: ["+mapsHouseBatu+"] mukim: ["+mapsHouseMukim+"] address1: ["+mapsAddress+"] address2: ["+mapsFullAddress2+"] " +
                        "createdBy["+mapInsertBy+"] modifiedBy ["+mapsModifiedBy+"] mapsHouseStatus["+mapsHouseStatus+"]");

                //start set progress dialog
                final ProgressDialog progressDialog = new ProgressDialog(MapsEditOnline.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Saving...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                checkNSavePoints(isEdit, id, mapsBarcode,  mapsLatitude,mapsLongitude, mapsAddress,
                                        mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu, mapsHouseMukim, mapsFullAddress2,
                                        mapInsertBy, mapsModifiedBy, mapsHouseStatus, progressDialog, dialog);

                            }
                        }, 3000);
            }
        });

        dialog.show();
    }
    //end edit maps dialog

    //STEP 1: retrieve and edit points
    private void retrieveMarkerInfo(final String latitude, final String longitude, final CustomInfo customInfo, final Marker marker, final ProgressDialog progressDialog){

        class RetrieveMarkerInfo extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {

                Log.d(TAG,"onPreExecute retrieve");
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG,"onPostExecute retrieve");

                JSON_STRING = s;

                JSONObject jsonObject = null;

                String s_lat = "";
                String s_lon = "";
                String s_nam = "";
                String s_add = "";
                String s_add2 = "Not Available";

                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray gisInfoList = jsonObject
                            .getJSONArray(Config.TAG_JSON_ARRAY);

                    for(int i=0;i<gisInfoList.length();i++){

                        JSONObject jo = gisInfoList.getJSONObject(i);

                        checkLatLonIsExist = jo.getString(Config.TAG_CHECK_LOCATION);

                        id = jo.getString(Config.TAG_ID);
                        barcode = jo.getString(Config.TAG_BARCODE);
                        fullAddress = jo.getString(Config.TAG_ADDRESS1);
                        fullAddress2 = jo.getString(Config.TAG_ADDRESS2);
                        address2_no = jo.getString(Config.TAG_ADDRESS2_NO);
                        address2_streetType= jo.getString(Config.TAG_ADDRESS2_STREETTYPE);
                        address2_streetName= jo.getString(Config.TAG_ADDRESS2_STREETNAME);
                        address2_areaType= jo.getString(Config.TAG_ADDRESS2_AREATYPE);
                        address2_areaName= jo.getString(Config.TAG_ADDRESS2_AREANAME);
                        address2_batu= jo.getString(Config.TAG_ADDRESS2_BATU);
                        address2_mukim= jo.getString(Config.TAG_ADDRESS2_MUKIM);
                        isEdit = "Y";

                        testLatitude =  jo.getString(Config.TAG_LATITUDE);
                        testLongitude =  jo.getString(Config.TAG_LONGITUDE);

                        if(checkLatLonIsExist.equalsIgnoreCase("true")){

                            Log.d(TAG,"retrieveMarkerInfo: ["+checkLatLonIsExist+"]");


                            // Get the latitude and  Get the longitude
                            double lat = Double.parseDouble(testLatitude);
                            double lng = Double.parseDouble(testLongitude);
                            s_nam = barcode;//gisInfoPerMarker.getBarcode();
                            s_add = fullAddress;//gisInfoPerMarker.getAddress();
                            s_add2 = fullAddress2;

                            Log.d(TAG,"barcode: ["+s_nam+"]");
                            Log.d(TAG,"s_add: ["+s_add+"]");

                            customInfo.latitude = String.valueOf(lat);
                            customInfo.longitude = String.valueOf(lng);
                            customInfo.name = s_nam;
                            customInfo.address = s_add;//s_add;
                            customInfo.address2=s_add2;

                            mMap.setInfoWindowAdapter(customInfo);
                            marker.showInfoWindow();

                            progressDialog.dismiss();


                            // View edit dioalog
                            //viewEditDialog(Integer.parseInt(id), barcode, latitude, longitude, fullAddress, address2_no, address2_streetType, address2_streetName, address2_areaType, address2_areaName, address2_batu, address2_mukim, isEdit);
                        }else{
                            Log.d(TAG,"retrieveMarkerInfo: ["+checkLatLonIsExist+"]");

                            //Layer 1
                            for (GeoJsonFeature feature : layer1.getFeatures()) {
                                Log.d("Check Code 1", "Does GeoJsonFeature pass through here? Yes!");

                                if (feature.hasProperty("LATITUDE")) {
                                    s_lat = feature.getProperty("LATITUDE").toString().trim();
                                    //Log.d("Check s_lat", "" + s_lat);
                                }

                                if (feature.hasProperty("LONGITUDE")) {
                                    s_lon = feature.getProperty("LONGITUDE").toString().trim();
                                    // Log.d("Check s_lon", "" + s_lon);
                                }

                                if (feature.hasProperty("NAME")) {
                                    s_nam = feature.getProperty("NAME").toString().trim();
                                    //Log.d("Check s_nam", "" + s_nam);

                                }

                                if (feature.hasProperty("ADDRESS")) {
                                    s_add = feature.getProperty("ADDRESS").toString().trim();
                                    //Log.d("Check s_add", "" + s_add);

                                }

                                String sGeom = "";
                                if (feature.hasProperty("geometry")) {

                                    sGeom = feature.getProperty("geometry").toString().trim();
                                    //Log.d("Check sGeom", "" + sGeom);
                                }

                                if (feature.hasGeometry()) {

                                    mGeometry = feature.getGeometry();
                                    sGeom = mGeometry.toString();

                                    String[] split = sGeom.split("\\(");

                                    String open_bracket = split[1];

                                    String[] split2 = open_bracket.split("\\)");

                                    String close_bracket = split2[0];
//                                    Log.d("Split #1", "Split the brackets");
                                    String[] s_geom = close_bracket.split(",");
//                                    Log.d("Split #2", "Split the Latitude and Longitude");
                                    s_lat = s_geom[0];
                                    s_lon = s_geom[1];

                                    //Log.d("Split Result Check", "" + s_lat + " " + s_lon);
                                    if (s_lat.equals(latitude) && s_lon.equals(longitude)) {
                                        customInfo.latitude = latitude;
                                        customInfo.longitude = longitude;
                                        customInfo.name = s_nam;
                                        customInfo.address = s_add;
                                        customInfo.address2 = s_add2;
                                    }
                                }
                            }

                            //Layer2
                            for (GeoJsonFeature feature : layer2.getFeatures()) {
                                Log.d("Check Code 2", "Does GeoJsonFeature pass through here? Yes!");

                                if (feature.hasProperty("LATITUDE")) {
                                    s_lat = feature.getProperty("LATITUDE").toString().trim();
                                    //Log.d("Check s_lat", "" + s_lat);
                                }

                                if (feature.hasProperty("LONGITUDE")) {
                                    s_lon = feature.getProperty("LONGITUDE").toString().trim();
                                    //Log.d("Check s_lon", "" + s_lon);
                                }

                                if (feature.hasProperty("NAME")) {
                                    s_nam = feature.getProperty("NAME").toString().trim();
                                    //Log.d("Check s_nam", "" + s_nam);

                                }

                                if (feature.hasProperty("ADDRESS")) {
                                    s_add = feature.getProperty("ADDRESS").toString().trim();
//                                    Log.d("Check s_add", "" + s_add);

                                }

                                String sGeom = "";
                                if (feature.hasProperty("geometry")) {

                                    sGeom = feature.getProperty("geometry").toString().trim();
//                                    Log.d("Check sGeom", "" + sGeom);
                                }

                                if (feature.hasGeometry()) {

                                    mGeometry = feature.getGeometry();
                                    sGeom = mGeometry.toString();

                                    String[] split = sGeom.split("\\(");

                                    String open_bracket = split[1];

                                    String[] split2 = open_bracket.split("\\)");

                                    String close_bracket = split2[0];
//                                    Log.d("Split #1", "Split the brackets");
                                    String[] s_geom = close_bracket.split(",");
//                                    Log.d("Split #2", "Split the Latitude and Longitude");
                                    s_lat = s_geom[0];
                                    s_lon = s_geom[1];

//                                    Log.d("Split Result Check", "" + s_lat + " " + s_lon);
                                    if (s_lat.equals(latitude) && s_lon.equals(longitude)) {

                                        customInfo.latitude = latitude;
                                        customInfo.longitude = longitude;
                                        customInfo.name = s_nam;
                                        customInfo.address = s_add;
                                        customInfo.address2 = s_add2;
                                    }
                                }
                            }

                            //Layer 3
                            for (GeoJsonFeature feature : layer3.getFeatures()) {
//                                Log.d("Check Code 3", "Does GeoJsonFeature pass through here? Yes!");

                                if (feature.hasProperty("LATITUDE")) {
                                    s_lat = feature.getProperty("LATITUDE").toString().trim();
//                                    Log.d("Check s_lat", "" + s_lat);
                                }

                                if (feature.hasProperty("LONGITUDE")) {
                                    s_lon = feature.getProperty("LONGITUDE").toString().trim();
//                                    Log.d("Check s_lon", "" + s_lon);
                                }

                                if (feature.hasProperty("NAME")) {
                                    s_nam = feature.getProperty("NAME").toString().trim();
//                                    Log.d("Check s_nam", "" + s_nam);

                                }

                                if (feature.hasProperty("ADDRESS")) {
                                    s_add = feature.getProperty("ADDRESS").toString().trim();
//                                    Log.d("Check s_add", "" + s_add);

                                }

                                String sGeom = "";
                                if (feature.hasProperty("geometry")) {

                                    sGeom = feature.getProperty("geometry").toString().trim();
//                                    Log.d("Check sGeom", "" + sGeom);
                                }

                                if (feature.hasGeometry()) {

                                    mGeometry = feature.getGeometry();
                                    sGeom = mGeometry.toString();

                                    String[] split = sGeom.split("\\(");

                                    String open_bracket = split[1];

                                    String[] split2 = open_bracket.split("\\)");

                                    String close_bracket = split2[0];
//                                    Log.d("Split #1", "Split the brackets");
                                    String[] s_geom = close_bracket.split(",");
//                                    Log.d("Split #2", "Split the Latitude and Longitude");
                                    s_lat = s_geom[0];
                                    s_lon = s_geom[1];

//                                    Log.d("Split Result Check", "" + s_lat + " " + s_lon);
                                    if (s_lat.equals(latitude) && s_lon.equals(longitude)) {

                                        customInfo.latitude = latitude;
                                        customInfo.longitude = longitude;
                                        customInfo.name = s_nam;
                                        customInfo.address = s_add;
                                        customInfo.address2 = s_add2;
                                    }
                                }
                            }

                            //Layer4
                            for (GeoJsonFeature feature : layer4.getFeatures()) {
                                Log.d("Check Code 4", "Does GeoJsonFeature pass through here? Yes!");

                                if (feature.hasProperty("LATITUDE")) {
                                    s_lat = feature.getProperty("LATITUDE").toString().trim();
//                                    Log.d("Check s_lat", "" + s_lat);
                                }

                                if (feature.hasProperty("LONGITUDE")) {
                                    s_lon = feature.getProperty("LONGITUDE").toString().trim();
//                                    Log.d("Check s_lon", "" + s_lon);
                                }

                                if (feature.hasProperty("NAME")) {
                                    s_nam = feature.getProperty("NAME").toString().trim();
//                                    Log.d("Check s_nam", "" + s_nam);

                                }

                                if (feature.hasProperty("ADDRESS")) {
                                    s_add = feature.getProperty("ADDRESS").toString().trim();
//                                    Log.d("Check s_add", "" + s_add);

                                }

                                String sGeom = "";
                                if (feature.hasProperty("geometry")) {

                                    sGeom = feature.getProperty("geometry").toString().trim();
//                                    Log.d("Check sGeom", "" + sGeom);
                                }

                                if (feature.hasGeometry()) {

                                    mGeometry = feature.getGeometry();
                                    sGeom = mGeometry.toString();

                                    String[] split = sGeom.split("\\(");

                                    String open_bracket = split[1];

                                    String[] split2 = open_bracket.split("\\)");

                                    String close_bracket = split2[0];
//                                    Log.d("Split #1", "Split the brackets");
                                    String[] s_geom = close_bracket.split(",");
//                                    Log.d("Split #2", "Split the Latitude and Longitude");
                                    s_lat = s_geom[0];
                                    s_lon = s_geom[1];

//                                    Log.d("Split Result Check", "" + s_lat + " " + s_lon);
                                    if (s_lat.equals(latitude) && s_lon.equals(longitude)) {

                                        customInfo.latitude = latitude;
                                        customInfo.longitude = longitude;
                                        customInfo.name = s_nam;
                                        customInfo.address = s_add;
                                        customInfo.address2 = s_add2;
                                    }
                                }
                            }
                            mMap.setInfoWindowAdapter(customInfo);
                            marker.showInfoWindow();

                            progressDialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... v) {

                Log.d(TAG,"doInBackground retrieve");

                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.KEY_LATITUDE,latitude);
                params.put(Config.KEY_LONGITUDE,longitude);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_PERMARKER_LOCATION, params);
                return s;
            }
        }

        RetrieveMarkerInfo retrieveMarkerInfo = new RetrieveMarkerInfo();
        retrieveMarkerInfo.execute();
    }

    //STEP 2: retrieve and edit points - checking duplicate and save / update
    private void checkNSavePoints(final String isEdit, final int id, final String mapsBarcode, final String mapsLatitude, final String mapsLongitude, final String mapsAddress,
                                  final String mapsHouseNo, final String mapsHouseStreetType, final String mapsHouseStreetName, final String mapsHouseAreaType, final String mapsHouseAreaName, final String mapsHouseBatu, final String mapsHouseMukim, final String mapsFullAddress2,
                                  final String mapInsertBy, final String mapsModifiedBy, final String mapsHouseStatus, final ProgressDialog progressDialog, final Dialog dialog){

        class CheckNSavePoints extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG,"onPostExecute retrieve");

                JSON_STRING = s;

                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray gisInfoList = jsonObject
                            .getJSONArray(Config.TAG_JSON_ARRAY);

                    Log.d(TAG,"checkNSavePoints@@@@ InfoList Size: ["+gisInfoList.length()+"] latitude: ["+mapsLatitude+"] longitude: ["+mapsLongitude+"] mapsHouseStatus["+mapsHouseStatus+"]");


                    for(int i=0;i<gisInfoList.length();i++){

                        JSONObject jo = gisInfoList.getJSONObject(i);

                        checkLatLonNotDup = jo.getString(Config.TAG_CHECKDUP_LOCATION);

                        Log.d(TAG,"checkLatLonNotDup ["+checkLatLonNotDup+"]");

                        if(checkLatLonNotDup.equalsIgnoreCase("true")){
                            Log.d(TAG,"Can save");
                            progressDialog.dismiss();
                            dialog.dismiss();
                            refreshMaps();
                            Toast.makeText(getBaseContext(), "Save Successfully", Toast.LENGTH_LONG).show();
                        }else{
                            Log.d(TAG,"This location already in db");
                            Toast.makeText(getBaseContext(), "Information cannot be save because the location already exist.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            progressDialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... v)
            {

                Log.d(TAG,"doInBackground retrieve");

                HashMap<String,String> params = new HashMap<String, String>();

				/*params.put(Config.KEY_LATITUDE,latitude);
                params.put(Config.KEY_LONGITUDE,longitude);
                params.put(Config.KEY_ID, String.valueOf(id));*/

                params.put(Config.KEY_ID,String.valueOf(id));
                params.put(Config.KEY_BARCODE,mapsBarcode);
                params.put(Config.KEY_LATITUDE,mapsLatitude);
                params.put(Config.KEY_LONGITUDE,mapsLongitude);
                params.put(Config.KEY_ADDRESS1,mapsAddress);
                params.put(Config.KEY_ADDRESS2,mapsFullAddress2);
                params.put(Config.KEY_ADDRESS2_NO,mapsHouseNo);
                params.put(Config.KEY_ADDRESS2_STREETTYPE,mapsHouseStreetType);
                params.put(Config.KEY_ADDRESS2_STREETNAME,mapsHouseStreetName);
                params.put(Config.KEY_ADDRESS2_AREATYPE,mapsHouseAreaType);
                params.put(Config.KEY_ADDRESS2_AREANAME,mapsHouseAreaName);
                params.put(Config.KEY_ADDRESS2_BATU,mapsHouseBatu);
                params.put(Config.KEY_ADDRESS2_MUKIM,mapsHouseMukim);
                params.put(Config.KEY_ADDRESS2_CREATEDBY,mapInsertBy);
                params.put(Config.KEY_ADDRESS2_MODIFIEDBY,mapsModifiedBy);
                params.put(Config.KEY_STATUS,mapsHouseStatus);
                params.put(Config.KEY_ISEDIT,isEdit);

                Log.d(TAG,"id: ["+String.valueOf(id)+"]");
                Log.d(TAG,"mapsBarcode: ["+mapsBarcode+"]");
                Log.d(TAG,"mapsLatitude: ["+mapsLatitude+"]");
                Log.d(TAG,"mapsLongitude: ["+mapsLongitude+"]");
                Log.d(TAG,"mapsAddress: ["+mapsAddress+"]");
                Log.d(TAG,"mapsFullAddress2: ["+mapsFullAddress2+"]");
                Log.d(TAG,"mapsHouseNo: ["+mapsHouseNo+"]");
                Log.d(TAG,"mapsHouseStreetType: ["+mapsHouseStreetType+"]");
                Log.d(TAG,"mapsHouseStreetName: ["+mapsHouseStreetName+"]");
                Log.d(TAG,"mapsHouseAreaType: ["+mapsHouseAreaType+"]");
                Log.d(TAG,"mapsHouseAreaName: ["+mapsHouseAreaName+"]");
                Log.d(TAG,"mapsHouseBatu: ["+mapsHouseBatu+"]");
                Log.d(TAG,"mapsHouseMukim: ["+mapsHouseMukim+"]");
                Log.d(TAG,"createdBy: ["+mapInsertBy+"]");
                Log.d(TAG,"ismapsHouseStatusEdit: ["+mapsHouseStatus+"]");
                Log.d(TAG,"isEdit: ["+isEdit+"]");


                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_ADD_UPDATE_LOCATION, params);
                return s;
            }
        }

        CheckNSavePoints checkNSavePoints = new CheckNSavePoints();
        checkNSavePoints.execute();
    }

    //STEP 1: retrieve and edit points
    private void retrieveNEditPoints(final String latitude, final String longitude){

        class RetrieveNEditPoints extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG,"onPostExecute retrieve");

                JSON_STRING = s;

                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray gisInfoList = jsonObject
                            .getJSONArray(Config.TAG_JSON_ARRAY);

                    for(int i=0;i<gisInfoList.length();i++){

                        JSONObject jo = gisInfoList.getJSONObject(i);

                        checkLatLonIsExist = jo.getString(Config.TAG_CHECK_LOCATION);

                        id = jo.getString(Config.TAG_ID);
                        barcode = jo.getString(Config.TAG_BARCODE);
                        fullAddress = jo.getString(Config.TAG_ADDRESS1);
                        address2_no = jo.getString(Config.TAG_ADDRESS2_NO);
                        address2_streetType= jo.getString(Config.TAG_ADDRESS2_STREETTYPE);
                        address2_streetName= jo.getString(Config.TAG_ADDRESS2_STREETNAME);
                        address2_areaType= jo.getString(Config.TAG_ADDRESS2_AREATYPE);
                        address2_areaName= jo.getString(Config.TAG_ADDRESS2_AREANAME);
                        address2_batu= jo.getString(Config.TAG_ADDRESS2_BATU);
                        address2_mukim= jo.getString(Config.TAG_ADDRESS2_MUKIM);
                        status= jo.getString(Config.TAG_STATUS);
                        isEdit = "Y";
                        //status = "New";

                        if(checkLatLonIsExist.equalsIgnoreCase("true")){

                            // View edit dioalog
                            viewEditDialog(Integer.parseInt(id), barcode, latitude, longitude, fullAddress, address2_no, address2_streetType, address2_streetName, address2_areaType, address2_areaName, address2_batu, address2_mukim, isEdit, status);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... v) {

                Log.d(TAG,"doInBackground retrieve");

                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Config.KEY_LATITUDE,latitude);
                params.put(Config.KEY_LONGITUDE,longitude);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_PERMARKER_LOCATION, params);
                return s;
            }
        }

        RetrieveNEditPoints retrieveNEditPoints = new RetrieveNEditPoints();
        retrieveNEditPoints.execute();
    }

    // STEP 1: start display all the location as marker on google maps
    private void getJSON(){

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                getAllLocation();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> paramSearch = new HashMap<String, String>();

                String s = rh.sendPostRequest(Config.URL_ALL_LOCATION,paramSearch);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    // STEP 2: start display all the location as marker on google maps
    public void getAllLocation(){
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray gisInfoList = jsonObject
                    .getJSONArray(Config.TAG_JSON_ARRAY);

            //Log.d(TAG,"InfoList Size: ["+gisInfoList.length()+"]");


            for(int i=0;i<gisInfoList.length();i++){

                JSONObject jo = gisInfoList.getJSONObject(i);

                // Get the latitude
                double lat = jo.getDouble(Config.TAG_LATITUDE);

                // Get the longitude
                double lng = jo.getDouble(Config.TAG_LONGITUDE);

                //Get the status
                String stat = jo.getString(Config.TAG_STATUS);

                //Log.d(TAG,"Latitude ["+lat+"] and Longitude ["+lng+"]");

                LatLng location = new LatLng(lat, lng);

                // Drawing the marker in the Google Maps
                drawMarker(lat, lng, stat);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawMarker(double lat, double lng, String stat){
        // Creating an instance of MarkerOptions
        //MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        //String fullLatLon = "3.0626802710852146,101.60144817084074";
        //String status = "Empty";
        final LatLng position = new LatLng(lat, lng);

        if (stat.equalsIgnoreCase("Agree")){

            final MarkerOptions options = new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(options);

        }else if (stat.equalsIgnoreCase("Reject")){

            final MarkerOptions options = new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(options);

        }else if (stat.equalsIgnoreCase("Empty")){

            final MarkerOptions options = new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(options);

        }else if (stat.equalsIgnoreCase("Not at home")){

            final MarkerOptions options = new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            mMap.addMarker(options);

        }else {

            final MarkerOptions options = new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            mMap.addMarker(options);
        }

    }
    //end display all the location as marker on google maps

    //start put all location display into one method
    public void refreshMaps(){
        mMap.clear(); //fix cannot close info popup after refresh
        try {

            layer1 = new GeoJsonLayer(mMap, R.raw.cg, getApplicationContext());
            layer2 = new GeoJsonLayer(mMap, R.raw.cp, getApplicationContext());
            layer3 = new GeoJsonLayer(mMap, R.raw.bk, getApplicationContext());
            layer4 = new GeoJsonLayer(mMap, R.raw.bo, getApplicationContext());

            layer1.addLayerToMap();
            Log.d("Layer", "Layer 1 Loaded");
            layer2.addLayerToMap();
            Log.d("Layer", "Layer 2 Loaded");
            layer3.addLayerToMap();
            Log.d("Layer", "Layer 3 Loaded");
            layer4.addLayerToMap();
            Log.d("Layer", "Layer 4 Loaded");


            klayer1 = new KmlLayer(mMap, R.raw.ab, getApplicationContext());
            klayer2 = new KmlLayer(mMap, R.raw.ac, getApplicationContext());
            klayer3 = new KmlLayer(mMap, R.raw.ap, getApplicationContext());
            klayer4 = new KmlLayer(mMap, R.raw.as, getApplicationContext());

            klayer1.addLayerToMap();
            klayer2.addLayerToMap();
            klayer3.addLayerToMap();
            klayer4.addLayerToMap();

            Log.d("KML Layer", "KMLS Layer loaded");

        } catch (Exception e) {
            Log.d("Marker Error", "Marker failed to retrieve information from GeoJsonLayer");
        }

        //start Load points from local db
        getJSON();
        //end Load points from local db
    }
    //end put all location display into one method

    //STEP GET CURRENT LOCATION 2:start method show current location
    protected void showCurrentLocation() {
        Log.d(TAG, "ENTER CURRENT LOCATION 1");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "ENTER CURRENT LOCATION  2");
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.d(TAG, "ENTER CURRENT LOCATION 3");
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            // mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location"));
            float zoomLevel = 16; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, zoomLevel));
        }

    }

    //STEP GET CURRENT LOCATION 1:start class myLocationListener
    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(MapsEditOnline.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(MapsEditOnline.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(MapsEditOnline.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(MapsEditOnline.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }
    }
    //end class myLocationListener

    //load Street Type
    public void getStreetTypeList(){

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("streetType.txt"), "UTF-8"));

            List<String> listStreetType = new ArrayList<String>();

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //. ...
                listStreetType.add(mLine);
                ArrayAdapter<String> dataAdapterStreetType = new ArrayAdapter<String>(this,
                        R.layout.spinner_item, listStreetType);
                dataAdapterStreetType.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerHouseStreetType.setAdapter(dataAdapterStreetType);
                Log.d(TAG,"StreetTypeCollection: ["+mLine+"]");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    //load Area Type
    public void getAreaTypeList(){

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("areaType.txt"), "UTF-8"));

            List<String> listAreaType = new ArrayList<String>();

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //. ...
                listAreaType.add(mLine);
                ArrayAdapter<String> dataAdapterAreaType = new ArrayAdapter<String>(this,
                        R.layout.spinner_item, listAreaType);
                dataAdapterAreaType.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerHouseAreaType.setAdapter(dataAdapterAreaType);
                Log.d(TAG,"AreaTypeCollection: ["+mLine+"]");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    //load Mukim
    public void getMukimList(){

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("mukim.txt"), "UTF-8"));

            List<String> listMukim = new ArrayList<String>();

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //. ...
                listMukim.add(mLine);
                ArrayAdapter<String> dataAdapterMukim = new ArrayAdapter<String>(this,
                        R.layout.spinner_item, listMukim);
                dataAdapterMukim.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerHouseMukim.setAdapter(dataAdapterMukim);
                Log.d(TAG,"MukimCollection: ["+mLine+"]");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    //load Status
    public void getStatusList(){

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("status.txt"), "UTF-8"));

            List<String> listStatus = new ArrayList<String>();

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                //. ...
                listStatus.add(mLine);
                ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>(this,
                        R.layout.spinner_item, listStatus);
                dataAdapterStatus.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerHouseStatus.setAdapter(dataAdapterStatus);
                Log.d(TAG,"StatusCollection: ["+mLine+"]");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                //txtViewBarcode.setText(result.getContents());
                editTxtBarcode.setText(result.getContents());
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

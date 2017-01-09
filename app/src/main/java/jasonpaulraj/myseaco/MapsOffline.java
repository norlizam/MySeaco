package jasonpaulraj.myseaco;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jasonpaulraj.myseaco.module.MySQLiteHelper;

public class MapsOffline extends Activity implements OnClickListener {

    private static final String TAG = "MapsOffline";

    private MySQLiteHelper db;

    private Button btnViewOff, btnEditOff;
    private EditText editTxtBarcode, editTxtLat, editTxtLon, editTxtAddress, editTxtHouseNo, editTxtHouseStreetName, editTxtHouseAreaName, editTxtHouseBatu;
    //private RadioGroup radGrpHouseStreetType, radGrpHouseAreaType, radGrpHouseMukim;
    //private RadioButton radBtnHouseStreetType_Jln, radBtnHouseStreetType_Lrg, radBtnHouseStreetType_NA, radBtnHouseAreaType_Tmn, radBtnHouseAreaType_Kg, radBtnHouseAreaType_Felda,
            //radBtnHouseAreaType_NA, radBtnHouseMukim_Bekok, radBtnHouseMukim_Chaah, radBtnHouseMukim_Gemereh, radBtnHouseMukim_Segamat, radBtnHouseMukim_Jabi;
    private Button btnSave;
    private Spinner spinnerHouseStreetType, spinnerHouseAreaType, spinnerHouseMukim;
    private TextView txtViewAddress;
    String mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName,
            mapsHouseBatu, mapsHouseMukim, mapsFullAddress2, mapInsertBy,mapsModifiedBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offline);

        btnViewOff =  (Button) findViewById(R.id.button_viewOff);
        btnEditOff =  (Button) findViewById(R.id.button_editOff);

        btnViewOff.setOnClickListener(this);
        btnEditOff.setOnClickListener(this);

        //use for offline.
        db = new MySQLiteHelper(this);
        db.getAllGisInfo();


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {

            case R.id.button_viewOff:
                Intent intent = new Intent(MapsOffline.this, MapsOfflineList.class);
                startActivity(intent);
                Log.d(TAG,"Enter view offline form");
                break;

            case R.id.button_editOff:
                viewEditDialog();
                Log.d(TAG,"Enter add offline form");
                break;
        }

    }

    //view edit maps dialog
    public void viewEditDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MapsOffline.this);
        dialog.setContentView(R.layout.dialog_custom_location);
        dialog.setTitle("Add Location");

        // set the custom dialog components - text, image and button
        txtViewAddress = (TextView) dialog.findViewById(R.id.txtViewAddress);
        editTxtBarcode = (EditText) dialog.findViewById(R.id.editTxtBarcode);
        editTxtLat = (EditText) dialog.findViewById(R.id.editTxtLat);
        editTxtLon = (EditText) dialog.findViewById(R.id.editTxtLon);
        editTxtAddress = (EditText) dialog.findViewById(R.id.editTxtAddress);
        editTxtHouseNo = (EditText) dialog.findViewById(R.id.editTxtHouseNo);
        editTxtHouseStreetName = (EditText) dialog.findViewById(R.id.editTxtHouseStreetName);
        //radGrpHouseStreetType = (RadioGroup) dialog.findViewById(R.id.radGrpHouseStreetType);
        //radGrpHouseAreaType = (RadioGroup) dialog.findViewById(R.id.radGrpHouseAreaType);
        editTxtHouseAreaName = (EditText) dialog.findViewById(R.id.editTxtHouseAreaName);
        editTxtHouseBatu = (EditText) dialog.findViewById(R.id.editTxtHouseBatu);
        //radGrpHouseMukim = (RadioGroup) dialog.findViewById(R.id.radGrpHouseMukim);
        //mapsFullAddress2 ="";

//        radBtnHouseStreetType_Jln = (RadioButton) dialog.findViewById(R.id.radBtnHouseStreetType_Jln);
//        radBtnHouseStreetType_Lrg = (RadioButton) dialog.findViewById(R.id.radBtnHouseStreetType_Lrg);
//        radBtnHouseStreetType_NA = (RadioButton) dialog.findViewById(R.id.radBtnHouseStreetType_NA);

        spinnerHouseStreetType = (Spinner) dialog.findViewById(R.id.spinnerHouseStreetType);
        spinnerHouseAreaType = (Spinner) dialog.findViewById(R.id.spinnerHouseAreaType);
        spinnerHouseMukim = (Spinner) dialog.findViewById(R.id.spinnerHouseMukim);

        //start hide
        txtViewAddress.setVisibility(View.GONE);
        editTxtAddress.setVisibility(View.GONE);
        //end hide

        //start spinner
        getStreetTypeList();
        getAreaTypeList();
        getMukimList();
        //end spinner

        //start spinner listener
        spinnerHouseStreetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set parameter in value type
                mapsHouseStreetType = parent.getItemAtPosition(position).toString();
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
                //set parameter in value type
                mapsHouseAreaType = parent.getItemAtPosition(position).toString();
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
                //set parameter in value type
                mapsHouseMukim = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
        //end spinner listener

        /*radBtnHouseAreaType_Tmn = (RadioButton) dialog.findViewById(R.id.radBtnHouseAreaType_Tmn);
        radBtnHouseAreaType_Kg = (RadioButton) dialog.findViewById(R.id.radBtnHouseAreaType_Kg);
        radBtnHouseAreaType_Felda = (RadioButton) dialog.findViewById(R.id.radBtnHouseAreaType_Felda);
        radBtnHouseAreaType_NA = (RadioButton) dialog.findViewById(R.id.radBtnHouseAreaType_NA);

        radBtnHouseMukim_Bekok = (RadioButton) dialog.findViewById(R.id.radBtnHouseMukim_Bekok);
        radBtnHouseMukim_Chaah = (RadioButton) dialog.findViewById(R.id.radBtnHouseMukim_Chaah);
        radBtnHouseMukim_Gemereh = (RadioButton) dialog.findViewById(R.id.radBtnHouseMukim_Gemereh);
        radBtnHouseMukim_Segamat = (RadioButton) dialog.findViewById(R.id.radBtnHouseMukim_Segamat);
        radBtnHouseMukim_Jabi = (RadioButton) dialog.findViewById(R.id.radBtnHouseMukim_Jabi);

        //set radio button for street type
        radGrpHouseStreetType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radBtnHouseStreetType_Jln:

                        mapsHouseStreetType = "1";
                        Log.d(TAG,"mapsHouseStreetType: ["+mapsHouseStreetType+"]");

                        break;
                    case R.id.radBtnHouseStreetType_Lrg:

                        mapsHouseStreetType = "2";
                        Log.d(TAG,"mapsHouseStreetType: ["+mapsHouseStreetType+"]");

                        break;
                    case R.id.radBtnHouseStreetType_NA:

                        mapsHouseStreetType = "3";
                        Log.d(TAG,"mapsHouseStreetType: ["+mapsHouseStreetType+"]");

                        break;
                }
            }

        });

        //set radio button for Area type
        radGrpHouseAreaType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radBtnHouseAreaType_Tmn:

                        mapsHouseAreaType = "1";
                        Log.d(TAG,"mapsHouseAreaType: ["+mapsHouseAreaType+"]");

                        break;
                    case R.id.radBtnHouseAreaType_Kg:

                        mapsHouseAreaType = "2";
                        Log.d(TAG,"mapsHouseAreaType: ["+mapsHouseAreaType+"]");

                        break;
                    case R.id.radBtnHouseAreaType_Felda:

                        mapsHouseAreaType = "3";
                        Log.d(TAG,"mapsHouseAreaType: ["+mapsHouseAreaType+"]");

                        break;

                    case R.id.radBtnHouseAreaType_NA:

                        mapsHouseAreaType = "4";
                        Log.d(TAG,"mapsHouseAreaType: ["+mapsHouseAreaType+"]");

                        break;
                }
            }

        });

        //set radio button for mukim
        radGrpHouseMukim.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radBtnHouseMukim_Bekok:

                        mapsHouseMukim = "1";
                        Log.d(TAG,"mapsHouseMukim: ["+mapsHouseMukim+"]");

                        break;
                    case R.id.radBtnHouseMukim_Chaah:

                        mapsHouseMukim = "2";
                        Log.d(TAG,"mapsHouseMukim: ["+mapsHouseMukim+"]");

                        break;
                    case R.id.radBtnHouseMukim_Gemereh:

                        mapsHouseMukim = "3";
                        Log.d(TAG,"mapsHouseMukim: ["+mapsHouseMukim+"]");

                        break;

                    case R.id.radBtnHouseMukim_Segamat:

                        mapsHouseMukim = "4";
                        Log.d(TAG,"mapsHouseMukim: ["+mapsHouseMukim+"]");

                        break;

                    case R.id.radBtnHouseMukim_Jabi:

                        mapsHouseMukim = "5";
                        Log.d(TAG,"mapsHouseMukim: ["+mapsHouseMukim+"]");

                        break;
                }
            }

        });*/

        btnSave = (Button) dialog.findViewById(R.id.btnSave);

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
                mapInsertBy = "1"; //temporary hardcoded
                mapsModifiedBy = "";//temporary hardcoded

                //start set fullAddress
                mapsFullAddress2 = "";
                String [] mapsFullAddress2Split = {mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName,mapsHouseBatu,mapsHouseMukim};
                for( String address : mapsFullAddress2Split ) {
                    if(address!=null )
                        mapsFullAddress2+=address+" ";
                }
                //end set fullAddress

                Log.d(TAG,"latitude: ["+mapsLatitude+"] longitude: ["+mapsLongitude+"] streetType: ["+mapsHouseStreetType+"] " +
                        "streetName:["+mapsHouseStreetName+"] areaType: ["+mapsHouseAreaType+"] areaName: ["+mapsHouseAreaName+"] batu: ["+mapsHouseBatu+"] mukim: ["+mapsHouseMukim+"]");

                //start set progress dialog
                final ProgressDialog progressDialog = new ProgressDialog(MapsOffline.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Saving...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                db.addMapsPoint(mapsBarcode,  mapsLatitude,mapsLongitude, mapsAddress,
                                        mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu, mapsHouseMukim, mapsFullAddress2,
                                        mapInsertBy, mapsModifiedBy);
                                progressDialog.dismiss();
                                dialog.dismiss();
                                Toast.makeText(getBaseContext(), "Save Successfully", Toast.LENGTH_LONG).show();
                                //db.addMapsPoints(mapsBarcode,  mapsLatitude,mapsLongitude, mapsAddress,
                                //mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu, mapsHouseMukim, mapsFullAddress2,
                                //mapInsertBy, mapsModifiedBy, progressDialog, dialog);

                            }
                        }, 3000);
            }
        });

        dialog.show();
    }
    //end edit maps dialog

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

}

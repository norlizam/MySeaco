package jasonpaulraj.myseaco;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jasonpaulraj.myseaco.module.MySQLiteHelper;

public class MapsOfflineList extends Activity implements View.OnClickListener {

    private static final String TAG = "MapsOfflineList";
    public String action = "common";
    private TableLayout tableLayout;
    private LinearLayout linearLayout;
    private Spinner spinnerSub, spinnerHouseStreetType, spinnerHouseAreaType, spinnerHouseMukim;
    TextView textLblSearchSub, textPageNumber, txtViewAddress;
    EditText textIdhidden, editTxtBarcode, editTxtLat, editTxtLon, editTxtAddress, editTxtHouseNo, editTxtHouseStreetName, editTxtHouseAreaName, editTxtHouseBatu;
    private Button buttonPrev, buttonNext, buttonSearch, buttonAdd, btnSave;
    String selected, subValues, mapsId, mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsHouseNo,
            mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu,
            mapsHouseMukim, mapsFullAddress2, mapInsertBy, mapHouseCreatedDate, mapsModifiedBy,mapsModifiedDate, subValue, mapInsertByName, userID, userRegtype, radModeType;
    private Integer currentPage;
    private MySQLiteHelper db;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_mapsoffline_list_title);

        //get Intent
        userID = getIntent().getExtras().getString("userID");
        userRegtype = getIntent().getExtras().getString("userRegtype");
        radModeType = getIntent().getExtras().getString("radModeType");

        Log.d(TAG,"userID["+userID+"] userRegType["+userRegtype+"] radModeType["+radModeType+"]");

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        //label
        textLblSearchSub = (TextView) findViewById(R.id.mapsoffline_title_Search2);

        // set dropdown sub
        spinnerSub = (Spinner) findViewById(R.id.spinnerSub);
        //text field hideen to store Id
        textIdhidden = (EditText) findViewById(R.id.mapsoffline_txtIdhidden);


        //list item dropdown main
        List<String> list = new ArrayList<String>();
        list.add(0, "All");
        list.add(1, "Bekok");
        list.add(2, "Chaah");
        list.add(3, "Gemereh");
        list.add(4, "Sg Segamat");
        list.add(5, "Jabi");

        //adapter dropdown main
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_search_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_search_dropdown_item);

        //set Adapter
        spinnerSub.setAdapter(dataAdapter);

        spinnerSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();

                //set parameter in value type
                subValues = getParamSubValue(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        //button prev, next and search
        buttonPrev = (Button) findViewById(R.id.buttonPrevious);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        buttonSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                action = "common";
                currentPage = getCurrentPage(action);
                displayOfflineList(subValues, currentPage);
                getTotalPage(subValues, currentPage);

            }
        });


        buttonPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                action = "prev";
                currentPage = getCurrentPage(action);
                displayOfflineList(subValues, currentPage);
                getTotalPage(subValues, currentPage);

            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                action = "next";
                currentPage = getCurrentPage(action);
                displayOfflineList(subValues, currentPage);
                getTotalPage(subValues, currentPage);

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                viewEditDialog();
            }
        });

        //use for offline.
        db = new MySQLiteHelper(this);

        //first time load
        currentPage = getCurrentPage(action);
        subValues = "0";
        displayOfflineList(subValues, currentPage);
        getTotalPage(subValues, currentPage);
        //end first time load

		/* temporary using before implement server database
         *
			db = new MySQLiteHelper(this);
			final String[][] users = db.getUserResult();
			Log.d(TAG,"retrieve data users......"+users.length);
		 *
		 */
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {

    }

    //start display offline photo
    //public void displayOfflineList(String userID, Integer selectedMain, String subValues, Integer currentPage){
    public void displayOfflineList(String subValues, Integer currentPage){

        Log.d(TAG,"subValues ["+ subValues+"] currentPage["+currentPage+"]");

        String[][] gisInfo = db.getSingle_ListNotUpload(subValues, currentPage);

        int i = 0;

        //start refresh layout
        tableLayout.removeAllViews();
        tableLayout.refreshDrawableState();
        //end refresh layout

        textPageNumber = (TextView)findViewById(R.id.pageNumber);

        Log.d(TAG,"DISPLAY GIS OFFLINE"+gisInfo.length);
        while(i < gisInfo.length){

            mapsId = gisInfo[i][0];
            mapsBarcode = gisInfo[i][1];
            mapsLatitude = gisInfo[i][2];
            mapsLongitude =gisInfo[i][3];
            mapsAddress = gisInfo[i][4];
            mapsFullAddress2 = gisInfo[i][5];
            mapsHouseNo = gisInfo[i][6];
            mapsHouseStreetType = gisInfo[i][7];
            mapsHouseStreetName =gisInfo[i][8];
            mapsHouseAreaType =gisInfo[i][9];
            mapsHouseAreaName =gisInfo[i][10];
            mapsHouseBatu =gisInfo[i][11];
            mapsHouseMukim = gisInfo[i][12];
            mapInsertBy = gisInfo[i][13];
            mapHouseCreatedDate = gisInfo[i][14];
            mapsModifiedBy = gisInfo[i][15];
            mapsModifiedDate = gisInfo[i][16];
            mapInsertByName = gisInfo[i][18];

            //linearLayout = (LinearLayout) View.inflate(this,R.layout.table_mapsoffline_list_item, null);
            linearLayout = (LinearLayout) View.inflate(this,R.layout.table_mapsoffline_list_item, null);

            TextView mapsOffline_seacoBarcode  = (TextView) linearLayout.findViewById(R.id.mapsoffline_seacobarcode);
            TextView mapsOffline_latLon  = (TextView) linearLayout.findViewById(R.id.mapsoffline_LatLon);
            TextView mapsOffline_address1  = (TextView) linearLayout.findViewById(R.id.mapsoffline_address);
            ImageView mapsOffline_detail  = (ImageView) linearLayout.findViewById(R.id.mapsoffline_detail);
            Button mapsOffline_btnUpload = (Button) linearLayout.findViewById(R.id.mapsoffline_upload);

            mapsOffline_seacoBarcode.setText(mapsBarcode);
            mapsOffline_latLon.setText(mapsLatitude+","+mapsLongitude);
            mapsOffline_address1.setText(mapsFullAddress2);

            if(radModeType.equals("Y")){mapsOffline_btnUpload.setEnabled(true);}else{mapsOffline_btnUpload.setEnabled(false);}

            detailnUpload(mapsId, mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsFullAddress2, mapHouseCreatedDate, mapInsertBy,
            mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu, mapsHouseMukim,
            mapsModifiedBy, mapInsertByName, mapsOffline_detail, mapsOffline_btnUpload);

            i++;
            tableLayout.addView(linearLayout);
        }
    }
    //end display offline photo

    //Adding new points
    private void addOfflineList(final String id, final String mapsBarcode, final String mapsLatitude, final String mapsLongitude, final String mapsAddress,
                                final String mapsHouseNo, final String mapsHouseStreetType, final String mapsHouseStreetName, final String mapsHouseAreaType, final String mapsHouseAreaName, final String mapsHouseBatu, final String mapsHouseMukim, final String mapsFullAddress2,
                                final String mapInsertBy, final String mapsModifiedBy, final ProgressDialog progressDialog){

        class AddOfflineList extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG,"onPostExecute addOfflineList");

                db.updateListUpload(id);

                Toast.makeText(MapsOfflineList.this, "Successfully Save",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

                //first time load
                currentPage = getCurrentPage(action);
                subValues = "0";
                displayOfflineList(subValues, currentPage);
                getTotalPage(subValues, currentPage);
                //end first time load

                //Intent i = new Intent(MapsOfflineList.this, MapsOffline.class);
                //startActivity(i);
                //finish();
            }

            @Override
            protected String doInBackground(Void... v) {

                Log.d(TAG,"doInBackground addUser");

                HashMap<String,String> params = new HashMap<String, String>();
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

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_LOCATION, params);
                return res;
            }
        }

        AddOfflineList addOfflineList = new AddOfflineList();
        addOfflineList.execute();
    }

    //start display information detail
    public void informationDetail(String id, String mapsBarcode, String mapsLatitude, String mapsLongitude, String mapsAddress, String mapsFullAddress2, String mapHouseCreatedDate, String mapInsertBy, String mapInsertByName){

        // custom dialog
        final Dialog dialog = new Dialog(MapsOfflineList.this);
        dialog.setContentView(R.layout.dialog_custom_details);
        dialog.setTitle("Detail Information");

        // set the custom dialog components - text, image and button
        TextView mapsOffline_id_dialog  = (TextView) dialog.findViewById(R.id.mapsOffDetail_id_dialog);
        TextView mapsOffline_seacoBarcode_dialog  = (TextView) dialog.findViewById(R.id.mapsOffDetail_barcode_dialog);
        TextView mapsOffline_latlon_dialog  = (TextView) dialog.findViewById(R.id.mapsOffDetail_latlon_dialog);
        TextView mapsOffline_address_dialog  = (TextView) dialog.findViewById(R.id.mapsOffDetail_address_dialog);
        TextView mapsOffline_address2_dialog  = (TextView) dialog.findViewById(R.id.mapsOffDetail_address2_dialog);
        TextView mapsOffline_createDate_dialog  = (TextView) dialog.findViewById(R.id.mapsOffDetail_createdDate_dialog);
        TextView mapsOffline_createBy_dialog  = (TextView) dialog.findViewById(R.id.mapsOffDetail_createdBy_dialog);

        mapsOffline_id_dialog.setText(id);
        mapsOffline_seacoBarcode_dialog.setText(mapsBarcode);
        mapsOffline_latlon_dialog.setText(mapsLatitude+","+mapsLongitude);
        mapsOffline_address_dialog.setText(mapsAddress);
        mapsOffline_address2_dialog.setText(mapsFullAddress2);
        mapsOffline_createDate_dialog.setText(mapHouseCreatedDate);
        mapsOffline_createBy_dialog.setText(mapInsertByName);
        dialog.show();

    }
    //end display information detail

    //start display total page
    public void getTotalPage(String subValues, Integer currentPage){

        int pageCount = db.getSingle_ListNotUploadCount(subValues);

        textPageNumber.setText("Page "+(currentPage)+" of "+pageCount);

        // Disabled Button Next
        int pageCounts =pageCount;

        if(currentPage >= pageCounts){
            buttonNext.setEnabled(false);
        }else{
            buttonNext.setEnabled(true);
        }

        // Disabled Button Previous
        if(currentPage <= 1){
            buttonPrev.setEnabled(false);
        }else{
            buttonPrev.setEnabled(true);
        }
    }
    //end display total page

    //return current page
    public Integer getCurrentPage(String action){
        if(action.equalsIgnoreCase("common")){
            currentPage = 1;
        }
        if(action.equalsIgnoreCase("next")){
            currentPage++;
        }
        if(action.equalsIgnoreCase("prev")){
            currentPage--;
        }
        return currentPage;
    }
    //end return current page

    //view edit maps dialog
    public void viewEditDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MapsOfflineList.this);
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
        editTxtHouseAreaName = (EditText) dialog.findViewById(R.id.editTxtHouseAreaName);
        editTxtHouseBatu = (EditText) dialog.findViewById(R.id.editTxtHouseBatu);

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
                final ProgressDialog progressDialog = new ProgressDialog(MapsOfflineList.this);
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

                                //first time load
                                currentPage = getCurrentPage(action);
                                subValues = "0";
                                displayOfflineList(subValues, currentPage);
                                getTotalPage(subValues, currentPage);
                                //end first time load

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

    //start function on click detail and click upload
    public void detailnUpload(final String mapsId, final String mapsBarcode, final String mapsLatitude, final String mapsLongitude, final String mapsAddress, final String mapsFullAddress2, final String mapHouseCreatedDate, final String mapInsertBy,
                              final String mapsHouseNo, final String mapsHouseStreetType, final String mapsHouseStreetName, final String mapsHouseAreaType, final String mapsHouseAreaName, final String mapsHouseBatu, final String mapsHouseMukim,
                              final String mapsModifiedBy, final String mapInsertByName, ImageView mapsOffline_detail, Button mapsOffline_btnUpload){
        //click on detail image
        mapsOffline_detail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"i["+mapsId+"]");
                informationDetail(mapsId, mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsFullAddress2, mapHouseCreatedDate, mapInsertBy, mapInsertByName);
            }
        });

        //click on button upload
        mapsOffline_btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(MapsOfflineList.this,R.style.AppTheme);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Saving");
                progressDialog.show();

                // TODO: Implement your own authentication logic here.
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                addOfflineList(mapsId,  mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress,
                                        mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu, mapsHouseMukim, mapsFullAddress2,
                                        mapInsertBy, mapsModifiedBy, progressDialog);
                            }
                            //					}
                        }, 3000);
            }
        });
    }
    //end function on click detail and click upload

    //set paramater for searching
    public String getParamSubValue(String selected){
        if(selected.equalsIgnoreCase("All")){
            //set paramater All = 0,otherwise item
            subValue = "0";
            //SQL 1
        }else{
            subValue = selected;
        }
        return subValue;
    }

}


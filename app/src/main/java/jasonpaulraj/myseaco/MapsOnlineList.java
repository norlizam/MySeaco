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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jasonpaulraj.myseaco.module.MySQLiteHelper;

public class MapsOnlineList extends Activity implements View.OnClickListener {

    private static final String TAG = "MapsOnlineList";
    public String action = "common";
    private TableLayout tableLayout;
    private LinearLayout linearLayout;
    private Spinner spinnerSub;
    TextView textLblSearchSub, textPageNumber;
    private Button buttonPrev, buttonNext, buttonSearch, buttonAdd;
    String selected, subValues, mapsId, mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsHouseNo,
            mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu,
            mapsHouseMukim, mapsFullAddress2, mapInsertBy, mapHouseCreatedDate, mapsModifiedBy,mapsModifiedDate, JSON_STRING, userID, userRegtype, subValue, radModeType;
    Integer currentPage, totalLocationPerUser;
    MySQLiteHelper db;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_mapsonline_list_title);

        //get Intent
        userID = getIntent().getExtras().getString("userID");
        userRegtype = getIntent().getExtras().getString("userRegtype");
        radModeType = getIntent().getExtras().getString("radModeType");

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        //label
        textLblSearchSub = (TextView) findViewById(R.id.mapsoffline_title_Search2);

        // set dropdown sub
        spinnerSub = (Spinner) findViewById(R.id.spinnerSub);

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

        buttonSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                action = "common";
                currentPage = getCurrentPage(action);
                getJSON(subValues, currentPage);
                getTotalPage(subValues, currentPage);

            }
        });


        buttonPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                action = "prev";
                currentPage = getCurrentPage(action);
                getJSON(subValues, currentPage);
                getTotalPage(subValues, currentPage);

            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                action = "next";
                currentPage = getCurrentPage(action);
                getJSON(subValues, currentPage);
                getTotalPage(subValues, currentPage);

            }
        });


        //first time load
        currentPage = getCurrentPage(action);
        subValues = "0";
        getJSON(subValues, currentPage);
        getTotalPage(subValues, currentPage);
        //end first time load

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {

    }

    // To retrieve data from database
    private void getJSON(final String subValues, final Integer currentPage){
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                //showPhotosCommon(rootView, inflater, buttonPrev, buttonNext);
                displayOnlinePerUserList();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> paramSearch = new HashMap<String, String>();
                paramSearch.put(Config.KEY_SEARCH_SUB,subValues);
                paramSearch.put(Config.KEY_SEARCH_CURRPAGE,currentPage.toString());
                paramSearch.put(Config.KEY_USER_ID,userID);
                paramSearch.put(Config.KEY_USER_Regtype,userRegtype);

                Log.d(TAG,"subValues["+subValues+"] currentPage ["+currentPage.toString()+"] userID["+userID+"] userRegtype["+userRegtype+"]");

                String s = rh.sendPostRequest(Config.URL_ALL_LOCATION_PERUSER,paramSearch);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    // get total page from database
    private void getTotalPage(final String subValues, final Integer currentPage) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                JSON_STRING = s;

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject
                            .getJSONArray(Config.TAG_JSON_ARRAY);

                    JSONObject jo = result.getJSONObject(0);
                    String pageCount = jo.getString(Config.TAG_PAGE_COUNT);

                    textPageNumber.setText("Page "+(currentPage)+" of "+pageCount);

                    // Disabled Button Next
                    int pageCounts =Integer.parseInt(pageCount);

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

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                HashMap<String,String> paramSearch = new HashMap<String, String>();
                paramSearch.put(Config.KEY_SEARCH_SUB,subValues);
                paramSearch.put(Config.KEY_SEARCH_CURRPAGE,currentPage.toString());
                paramSearch.put(Config.KEY_USER_ID,userID);
                paramSearch.put(Config.KEY_USER_Regtype,userRegtype);

                String s = rh.sendPostRequest(Config.URL_ALL_LOCATION_PERUSER_PAGE,paramSearch);

                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    //show all data firstime enter this page
    private void displayOnlinePerUserList(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject
                    .getJSONArray(Config.TAG_JSON_ARRAY);

            //total data
            totalLocationPerUser =result.length();

            textPageNumber = (TextView)findViewById(R.id.pageNumber);

            int i = 0;

            //start refresh layout
            tableLayout.removeAllViews();
            tableLayout.refreshDrawableState();
            //end refresh layout

            while(i<result.length()){

                JSONObject jo = result.getJSONObject(i);
                mapsId = jo.getString(Config.TAG_ID);
                mapsBarcode = jo.getString(Config.TAG_BARCODE);
                mapsLatitude = jo.getString(Config.TAG_LATITUDE);
                mapsLongitude = jo.getString(Config.TAG_LONGITUDE);
                mapsAddress = jo.getString(Config.TAG_ADDRESS1);
                mapsFullAddress2 = jo.getString(Config.TAG_ADDRESS2);
                mapsHouseNo = jo.getString(Config.TAG_ADDRESS2_NO);
                mapsHouseStreetType = jo.getString(Config.TAG_ADDRESS2_STREETTYPE);
                mapsHouseStreetName = jo.getString(Config.TAG_ADDRESS2_STREETNAME);
                mapsHouseAreaType = jo.getString(Config.TAG_ADDRESS2_AREATYPE);
                mapsHouseAreaName = jo.getString(Config.TAG_ADDRESS2_AREANAME);
                mapsHouseBatu = jo.getString(Config.TAG_ADDRESS2_BATU);
                mapsHouseMukim = jo.getString(Config.TAG_ADDRESS2_MUKIM);
                mapInsertBy = jo.getString(Config.TAG_ADDRESS2_CREATEDBY);
                mapHouseCreatedDate = jo.getString(Config.TAG_ADDRESS2_CREATEDDATE);
                mapsModifiedBy = jo.getString(Config.TAG_ADDRESS2_MODIFIEDBY);
                mapsModifiedDate = jo.getString(Config.TAG_ADDRESS2_MODIFIEDDATE);

                linearLayout = (LinearLayout) View.inflate(this,R.layout.table_mapsonline_list_item, null);

                TextView mapsOffline_seacoBarcode  = (TextView) linearLayout.findViewById(R.id.mapsoffline_seacobarcode);
                TextView mapsOffline_latLon  = (TextView) linearLayout.findViewById(R.id.mapsoffline_LatLon);
                TextView mapsOffline_address1  = (TextView) linearLayout.findViewById(R.id.mapsoffline_address);
                TextView mapsOffline_address2  = (TextView) linearLayout.findViewById(R.id.mapsoffline_address2);
                Button mapsOffline_detail  = (Button) linearLayout.findViewById(R.id.mapsoffline_detail);

                mapsOffline_seacoBarcode.setText(mapsBarcode);
                mapsOffline_latLon.setText(mapsLatitude+","+mapsLongitude);
                mapsOffline_address1.setText(mapsAddress);
                mapsOffline_address2.setText(mapsFullAddress2);

                Log.d(TAG,"mapsId["+mapsId+"]");

                //click on detail image
                detail(mapsId, mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsFullAddress2, mapHouseCreatedDate, mapInsertBy,
                        mapsHouseNo, mapsHouseStreetType, mapsHouseStreetName, mapsHouseAreaType, mapsHouseAreaName, mapsHouseBatu, mapsHouseMukim,
                        mapsModifiedBy, mapsOffline_detail);

                i++;
                tableLayout.addView(linearLayout);
            }

        } catch (JSONException e) {
        }
    }

    //start function on click detail and click upload
    public void detail(final String mapsId, final String mapsBarcode, final String mapsLatitude, final String mapsLongitude, final String mapsAddress, final String mapsFullAddress2, final String mapHouseCreatedDate, final String mapInsertBy,
                              final String mapsHouseNo, final String mapsHouseStreetType, final String mapsHouseStreetName, final String mapsHouseAreaType, final String mapsHouseAreaName, final String mapsHouseBatu, final String mapsHouseMukim,
                              final String mapsModifiedBy, Button mapsOffline_detail){
        //click on detail image
        mapsOffline_detail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"i["+mapsId+"]");
                informationDetail(mapsId, mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsFullAddress2, mapHouseCreatedDate, mapInsertBy);
            }
        });
    }
    //end function on click detail and click upload

    //start display information detail
    public void informationDetail(String id, String mapsBarcode, String mapsLatitude, String mapsLongitude, String mapsAddress, String mapsFullAddress2, String mapHouseCreatedDate, String mapInsertBy){

        // custom dialog
        final Dialog dialog = new Dialog(MapsOnlineList.this);
        dialog.setContentView(R.layout.dialog_custom_detail);
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
        mapsOffline_address_dialog.setText(mapsFullAddress2);
        mapsOffline_address2_dialog.setText(mapsAddress);
        mapsOffline_createDate_dialog.setText(mapHouseCreatedDate);
        mapsOffline_createBy_dialog.setText(mapInsertBy);
        dialog.show();

    }
    //end display information detail

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


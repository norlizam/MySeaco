package jasonpaulraj.myseaco.module;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GPH on 10/13/2016.
 */



import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import jasonpaulraj.myseaco.UserDetail;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySQLiteHelper";

    private static final int DATABASE_VERSION = 1;

    private  static final String DATABASE_NAME = "Gis";

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_GIS_TABLE = "CREATE TABLE gis (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "seaco_barcode TEXT, " +
                "latitude TEXT, " +
                "longitude TEXT, " +
                "address1 TEXT, " +
                "address2 TEXT, " +
                "address2_no TEXT, " +
                "address2_streetType TEXT, " +
                "address2_streetName TEXT, " +
                "address2_areaType TEXT, " +
                "address2_areaName TEXT, " +
                "address2_batu TEXT, " +
                "address2_mukim TEXT, " +
                "createdBy TEXT, " +
                "createdDate DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "modifiedBy TEXT, " +
                "modifiedDate DATETIME DEFAULT CURRENT_TIMESTAMP, "+
                "flagUpload TEXT)";
        db.execSQL(CREATE_GIS_TABLE);

        String CREATE_GIS_USER_TABLE = "CREATE TABLE gisUser (" +
                "userId INTEGER, " +
                "userFullName TEXT, " +
                "userName TEXT, " +
                "userPassword TEXT, "+
                "userRegtype TEXT, "+
                "regDate DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_GIS_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS gis");
        this.onCreate(db);
    }

    private static final String TABLE_GIS = "gis";

    public static final String KEY_ID = "id";
    public static final String KEY_BARCODE = "seaco_barcode";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ADDRESS1 = "address1";
    public static final String KEY_ADDRESS2 = "address2";
    public static final String KEY_ADDRESS2_NO = "address2_no";
    public static final String KEY_ADDRESS2_STREETTYPE = "address2_streetType";
    public static final String KEY_ADDRESS2_STREETNAME = "address2_streetName";
    public static final String KEY_ADDRESS2_AREATYPE = "address2_areaType";
    public static final String KEY_ADDRESS2_AREANAME = "address2_areaName";
    public static final String KEY_ADDRESS2_BATU = "address2_batu";
    public static final String KEY_ADDRESS2_MUKIM = "address2_mukim";
    public static final String KEY_ADDRESS2_CREATEDBY = "createdBy";
    public static final String KEY_ADDRESS2_CREATEDDATE = "createdDate";
    public static final String KEY_ADDRESS2_MODIFIEDBY = "modifiedBy";
    public static final String KEY_ADDRESS2_MODIFIEDDATE = "modifiedDate";
    public static final String KEY_FLAGUPLOAD = "flagUpload";

    private static final String[] COLUMNS = {KEY_ID, KEY_BARCODE, KEY_LATITUDE, KEY_LONGITUDE, KEY_ADDRESS1, KEY_ADDRESS2, KEY_ADDRESS2_NO,
            KEY_ADDRESS2_STREETTYPE, KEY_ADDRESS2_STREETNAME, KEY_ADDRESS2_AREATYPE, KEY_ADDRESS2_AREANAME, KEY_ADDRESS2_BATU, KEY_ADDRESS2_MUKIM,
            KEY_ADDRESS2_CREATEDBY, KEY_ADDRESS2_CREATEDDATE, KEY_ADDRESS2_MODIFIEDBY, KEY_ADDRESS2_MODIFIEDDATE, KEY_FLAGUPLOAD};

    private static final String TABLE_GIS_USER = "gisUser";

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_FULLNAME =  "userFullName";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PASSWORD = "userPassword";
    public static final String KEY_USER_REGTYPE = "userRegtype";
    public static final String KEY_USER_REGDATE= "regDate";

    private static final String[] COLUMNS_USER = {KEY_USER_ID, KEY_USER_FULLNAME, KEY_USER_NAME, KEY_USER_PASSWORD, KEY_USER_REGTYPE, KEY_USER_REGDATE};

    //add new record
    public void addUser(UserDetail userDetail){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userDetail.getUserId());
        values.put(KEY_USER_FULLNAME, userDetail.getUserFullName());
        values.put(KEY_USER_NAME, userDetail.getUserName());
        values.put(KEY_USER_PASSWORD, userDetail.getUserPassword());
        values.put(KEY_USER_REGTYPE, userDetail.getUserRegtype());
        values.put(KEY_USER_REGDATE, userDetail.getRegDate());
        db.insert(TABLE_GIS_USER, null, values);

        db.close();
    }

    //public void addMapsPoints(String mapsBarcode, String mapsLatitude, String mapsLongitude, String mapInsertBy, String address) {
    public void addMapsPoint(String mapsBarcode, String mapsLatitude, String mapsLongitude,String mapsAddress,
                             String mapsHouseNo, String mapsHouseStreetType, String mapsHouseStreetName,
                             String mapsHouseAreaType, String mapsHouseAreaName, String mapsHouseBatu, String mapsHouseMukim,
                             String mapsFullAddress2, String mapInsertBy, String mapsModifiedBy) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, mapsBarcode);
        values.put(KEY_LATITUDE, mapsLatitude);
        values.put(KEY_LONGITUDE, mapsLongitude);
        values.put(KEY_ADDRESS1, mapsAddress);
        values.put(KEY_ADDRESS2, mapsFullAddress2);
        values.put(KEY_ADDRESS2_NO, mapsHouseNo);
        values.put(KEY_ADDRESS2_STREETTYPE, mapsHouseStreetType);
        values.put(KEY_ADDRESS2_STREETNAME, mapsHouseStreetName);
        values.put(KEY_ADDRESS2_AREATYPE, mapsHouseAreaType);
        values.put(KEY_ADDRESS2_AREANAME, mapsHouseAreaName);
        values.put(KEY_ADDRESS2_BATU, mapsHouseBatu);
        values.put(KEY_ADDRESS2_MUKIM, mapsHouseMukim);
        values.put(KEY_ADDRESS2_CREATEDBY, mapInsertBy);
        values.put(KEY_ADDRESS2_CREATEDDATE, getDateTime());
        values.put(KEY_ADDRESS2_MODIFIEDBY, mapsModifiedBy);
        values.put(KEY_ADDRESS2_MODIFIEDDATE, getDateTime());
        values.put(KEY_FLAGUPLOAD, "N");
        Log.d(TAG,"insert POINTS");
        db.insert(TABLE_GIS, null, values);

        db.close();
    }

    //update record
    public void updateMapsPoints(int id, String mapsBarcode, String mapsLatitude, String mapsLongitude, String mapInsertBy, String mapsAdress){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, mapsBarcode);
        values.put(KEY_LATITUDE, mapsLatitude);
        values.put(KEY_LONGITUDE, mapsLongitude);
        //values.put(KEY_INSERTDATE, getDateTime());
        //values.put(KEY_INSERTBY, mapInsertBy);
        //values.put(KEY_ADDRESS, mapsAdress);
        db.update(TABLE_GIS, values, "id = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    // Getting All Gis Info
    public List<GisInfo> getAllGisInfo() {

        List<GisInfo> gisInfoList = new ArrayList<GisInfo>();

        Log.d(TAG,"GET ALL GIS RECORD");

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GIS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GisInfo gisInfo = new GisInfo();
                gisInfo.setId(Integer.parseInt(cursor.getString(0)));
                gisInfo.setSeaco_barcode(cursor.getString(1));
                gisInfo.setLatitude(cursor.getString(2));
                gisInfo.setLongitude(cursor.getString(3));
                gisInfo.setAddress1(cursor.getString(4));
                gisInfo.setAddress2(cursor.getString(5));
                gisInfo.setAddress2_no(cursor.getString(6));
                gisInfo.setAddress2_streetType(cursor.getString(7));
                gisInfo.setAddress2_streetName(cursor.getString(8));
                gisInfo.setAddress2_areaType(cursor.getString(9));
                gisInfo.setAddress2_areaName(cursor.getString(10));
                gisInfo.setAddress2_batu(cursor.getString(11));
                gisInfo.setAddress2_mukim(cursor.getString(12));
                gisInfo.setCreatedBy(cursor.getString(13));
                gisInfo.setCreatedDate(cursor.getString(14));
                gisInfo.setModifiedBy(cursor.getString(15));
                gisInfo.setModifiedDate(cursor.getString(16));

                // Adding Gis Info to list
                gisInfoList.add(gisInfo);

                Log.i("ID:", cursor.getString(0));
                Log.i("BARCODE:", cursor.getString(1));
                Log.i("LAT:", cursor.getString(2));
                Log.i("LON:", cursor.getString(3));
                Log.i("ADDRESS 1:", cursor.getString(4));
                Log.i("ADDRESS 2:", cursor.getString(5));
                Log.i("ADDRESS 2_NO:", cursor.getString(6));
                Log.i("ADDRESS 2_STREET TYPE:", cursor.getString(7));
                Log.i("ADDRESS 2_STREET NAME:", cursor.getString(8));
                Log.i("ADDRESS 2_AREA TYPE:", cursor.getString(9));
                Log.i("ADDRESS 2_AREA NAME:", cursor.getString(10));
                Log.i("ADDRESS 2 BATU:", cursor.getString(11));
                Log.i("ADDRESS 2 MUKIM:", cursor.getString(12));
                Log.i("CREATED BY:", cursor.getString(13));
                Log.i("CREATED DATE:", cursor.getString(14));
                Log.i("MODIFIED BY:", cursor.getString(15));
                Log.i("MODIFIED DATE:", cursor.getString(16));
                Log.i("FLAG UPLOAD:", cursor.getString(17));

            } while (cursor.moveToNext());
        }

        // return contact list
        return gisInfoList;
    }

    //Getting All Gis Info List
    //public String[][] getSingle_ListNotUpload(String userId, Integer selectedMain, String subValues, int page){
    public String[][] getSingle_ListNotUpload(String subValues, int page){

        Log.d(TAG,"subValues ["+ subValues+"] currentPage["+page+"]");

        //set display per page
        int per_page = 16;

        //set start page
        int start = (page-1)*per_page;

        int i = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = null;

        if(subValues == "0"){
            //SQL 1
            query  = "SELECT gis.* , gisUser.userFullName" +
                    " FROM gis" +
                    " LEFT JOIN gisUser on gis.Id = gisUser.userId" +
                    " WHERE flagUpload = 'N'" +
                    " ORDER BY id desc limit "+start+","+per_page+"";
        }else{
            //SQL 2
            query  = "SELECT gis.* , gisUser.userFullName" +
                     "FROM gis" +
                     "LEFT JOIN gisUser on gis.Id = gisUser.userId" +
                     "WHERE address2_mukim = \"+subValues+\" AND flagUpload = 'N'" +
                     "ORDER BY id desc limit "+start+","+per_page+"";

        }

        Cursor cursor = db.rawQuery(query,null);

        Log.i("gisInfoResult", "After rawquery");

        String[][] gisInfo = new String[cursor.getCount()][19];

        Log.i("gisInfoResult", "After gisInfo[][] instatiation");

        if(cursor.moveToFirst()){

            while(!cursor.isAfterLast()){
                gisInfo[i][0] = cursor.getString(0);
                gisInfo[i][1] = cursor.getString(1);
                gisInfo[i][2] = cursor.getString(2);
                gisInfo[i][3] = cursor.getString(3);
                gisInfo[i][4] = cursor.getString(4);
                gisInfo[i][5] = cursor.getString(5);
                gisInfo[i][6] = cursor.getString(6);
                gisInfo[i][7] = cursor.getString(7);
                gisInfo[i][8] = cursor.getString(8);
                gisInfo[i][9] = cursor.getString(9);
                gisInfo[i][10] = cursor.getString(10);
                gisInfo[i][11] = cursor.getString(11);
                gisInfo[i][12] = cursor.getString(12);
                gisInfo[i][13] = cursor.getString(13);
                gisInfo[i][14] = cursor.getString(14);
                gisInfo[i][15] = cursor.getString(15);
                gisInfo[i][16] = cursor.getString(16);
                gisInfo[i][17] = cursor.getString(17);
                gisInfo[i][18] = cursor.getString(18);
                /*gisInfo[i][19] = cursor.getString(19);*/

                Log.i("insideCursor", cursor.getString(0));

                i++;
                cursor.moveToNext();
            }

        }

        return gisInfo;
    }

    //public int getSingle_ListNotUploadCount(String userId, Integer selectedMain, String subValues){
    public int getSingle_ListNotUploadCount(String subValues){

        Log.d(TAG,"subValues:["+subValues+"]");

        //set display per page
        int per_page = 16;

        int i = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = null;

        if(subValues == "0"){
            //SQL 1
            query  = "SELECT * FROM gis WHERE flagUpload = 'N'  ORDER BY id";
        }else{
            //SQL 2
            query  = "SELECT * FROM gis WHERE address2_mukim = "+subValues+" AND flagUpload = 'N'  ORDER BY id";
        }

        Cursor cursor = db.rawQuery(query,null);

        Log.i("getPhotosResult", "After rawquery");

        int count = cursor.getCount();
        int pages = 0;

        if(count % per_page == 0){
            pages =  count/per_page;
        }else{
            pages =  (count/per_page)+1;
        }

        Log.i("getGISOffResultPages", "Total ["+pages+"]");

        return pages;
    }

    public void updateListUpload(String id){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FLAGUPLOAD, "Y");
        db.update(TABLE_GIS, values, "id = ?", new String[]{id});

        db.close();
    }

    //start get current date
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    //end get current date

    //start get lat and lon already in local device
    public boolean checkLatLonInDB (String latitude, String longitude){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GIS, COLUMNS, "latitude = ? AND longitude = ?", new String[] {latitude,longitude}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }

    }
    //end start lat and lon already in local device

    //start checking lat and lon same in local device - persave
    public boolean checkLatLonSaveInDB (String latitude, String longitude, String isEdit, int id){

        List<GisInfo> gisInfoListPerMarker = new ArrayList<GisInfo>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GIS, COLUMNS, "latitude = ? AND longitude = ?", new String[] {latitude,longitude}, null, null, null, null);

        //save
        if(isEdit.equalsIgnoreCase("N")){
            if (cursor.getCount() <= 0) {
                Log.d(TAG,"TAG 1");
                cursor.close();
                return true;
            }else{
                Log.d(TAG,"TAG 2");
                cursor.close();
                return false;
            }
        }else{
            //edit
            if (cursor.getCount() <= 0) {
                Log.d(TAG,"TAG 3");
                cursor.close();
                return true;
            }else{
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        GisInfo gisInfo = new GisInfo();
                        gisInfo.setId(Integer.parseInt(cursor.getString(0)));
                        int ids = Integer.parseInt(cursor.getString(0));
                        // Adding contact to list
                        gisInfoListPerMarker.add(gisInfo);

                        if (ids == id){
                            Log.d(TAG,"TAG 4");
                            return true;
                        }else{
                            Log.d(TAG,"TAG 5");
                            return false;
                        }
                    } while (cursor.moveToNext());
                }
                Log.d(TAG,"TAG 6");
                cursor.close();
            }

        }
        Log.d(TAG,"TAG 7");
        return false;
    }
    //end checking lat and lon same in local device - persave

    // Getting All Gis Info on each click
    public List<GisInfo> getAllGisInfoPerMarker(String latitude, String longitude) {

        List<GisInfo> gisInfoListPerMarker = new ArrayList<GisInfo>();

        Log.d(TAG,"GET ALL GIS RECORD FOR PER MARKER");

        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_GIS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_GIS, COLUMNS, "latitude = ? AND longitude = ?", new String[] {latitude,longitude}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GisInfo gisInfo = new GisInfo();
                gisInfo.setId(Integer.parseInt(cursor.getString(0)));
                gisInfo.setSeaco_barcode(cursor.getString(1));
                gisInfo.setLatitude(cursor.getString(2));
                gisInfo.setLongitude(cursor.getString(3));
                gisInfo.setCreatedDate(cursor.getString(4));
                gisInfo.setCreatedBy(cursor.getString(5));
                gisInfo.setAddress1(cursor.getString(6));
                // Adding contact to list
                gisInfoListPerMarker.add(gisInfo);

                Log.i("ID:", cursor.getString(0));
                Log.i("BARCODE:", cursor.getString(1));
                Log.i("LAT:", cursor.getString(2));
                Log.i("LON:", cursor.getString(3));
                Log.i("INSERT DATE:", cursor.getString(4));
                Log.i("INSERT BY:", cursor.getString(5));
                Log.i("ADDRESS:", cursor.getString(6));
            } while (cursor.moveToNext());
        }

        // return contact list
        return gisInfoListPerMarker;
    }

    //start retrieve user data from local device
    public UserDetail getUserDetail(String userName, String userPassword){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GIS_USER, COLUMNS_USER, "userName = ? AND userPassword = ?", new String[] {userName,userPassword}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId((cursor.getString(0)));
        userDetail.setUserFullName((cursor.getString(1)));
        userDetail.setUserName(cursor.getString(2));
        userDetail.setUserPassword(cursor.getString(3));
        userDetail.setUserRegtype(cursor.getString(4));
        userDetail.setRegDate(cursor.getString(5));
        return userDetail;

    }
    //end retrieve user data from local device

    //start check if user id already exist in local device or not
    public boolean checkUserInDB (String userName, String userPassword){

        Log.d(TAG,"checkUserInDB1: and userPhoneNo " +userName+ "and userPassword: ["+userPassword+"]");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GIS_USER, COLUMNS_USER, "userName = ? AND userPassword = ?", new String[] {userName,userPassword}, null, null, null, null);
        Log.d(TAG,"checkUserInDB: 2");
        if (cursor.getCount() <= 0) {

            //IF RECORD NOT EXIST, INSERT
            //addUser(new UserDetail(userId,userName, userEmail, userPassword, userRegType, userPhoneNo, regDate));

            Log.d(TAG,"checkUserInDB: 3");

            cursor.close();
            return false;

        }else{
            Log.d(TAG,"checkUserInDB: 4");

            if(cursor.moveToNext()) {
                Log.d(TAG,"Data 1: "+cursor.getString(0));
                Log.d(TAG,"Data 2: "+cursor.getString(1));
                Log.d(TAG,"Data 3: "+cursor.getString(2));
                Log.d(TAG,"Data 4: "+cursor.getString(3));
                Log.d(TAG,"Data 5: "+cursor.getString(4));
                Log.d(TAG,"Data 6: "+cursor.getString(5));
            }


            cursor.close();
            return true;

        }

    }
    //end check if user id already exist in local device or not

    //start for the firstime login, need to be online. User record will be insert to local device
    public boolean checkUsernInsertInDB (String userId, String userFullName, String userName, String userPassword, String userRegtype, String regDate){

        Log.d(TAG,"1 and userName " +userName+ "and userPassword: ["+userPassword+"] and regDate: ["+regDate+"]");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GIS_USER, COLUMNS_USER, "userName = ? AND userPassword = ?", new String[] {userName,userPassword}, null, null, null, null);
        Log.d(TAG,"2");
        if (cursor.getCount() <= 0) {

            //IF RECORD NOT EXIST, INSERT
            addUser(new UserDetail(userId,userFullName, userName, userPassword, userRegtype, regDate));

            Log.d(TAG,"3");

            cursor.close();
            return false;

        }else{
            Log.d(TAG,"4");

            if(cursor.moveToNext()) {
                Log.d(TAG,"Data 1: "+cursor.getString(0));
                Log.d(TAG,"Data 2: "+cursor.getString(1));
                Log.d(TAG,"Data 3: "+cursor.getString(2));
                Log.d(TAG,"Data 4: "+cursor.getString(3));
                Log.d(TAG,"Data 5: "+cursor.getString(4));
                Log.d(TAG,"Data 6: "+cursor.getString(5));
            }


            cursor.close();
            return true;

        }

    }
    //end for the firstime login, need to be online. User record will be insert to local device
}

package jasonpaulraj.myseaco;


public class Config {

    //Address of our scripts was here
    public static final String URL_MAIN 	  					= "https://seacogis000.appspot.com/"; //will be replace into octopus.seaco.asia. if your are using LAN connection, please put your IP Address here
    public static final String URL_ADD_USER		  	            = URL_MAIN +"db_checkInsertUser.php";
    public static final String URL_CHECK_USER 					= URL_MAIN +"db_checkUser.php";
    public static final String URL_ADD_UPDATE_LOCATION 		  	= URL_MAIN +"db_insertUpdateLocation.php";
    public static final String URL_ALL_LOCATION		  		    = URL_MAIN +"db_getAllLocation.php";
    public static final String URL_CHECK_LOCATION		  		= URL_MAIN +"db_checkLocation.php";
    public static final String URL_PERMARKER_LOCATION		  	= URL_MAIN +"db_getLocationPerMarker.php";
    public static final String URL_ADD_LOCATION		  	        = URL_MAIN +"db_insertLocation.php";
    public static final String URL_ALL_LOCATION_PERUSER	  	    = URL_MAIN +"db_getAllLocationPerUser.php";
    public static final String URL_ALL_LOCATION_PERUSER_PAGE	= URL_MAIN +"db_getAllLocationPerUserCount.php";
    public static final String URL_DELETE_LOCATION	            = URL_MAIN +"db_deleteLocation.php";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_FULLNAME = "userFullName";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PASSWORD = "userPassword";
    public static final String KEY_USER_Regtype = "userRegtype";
    public static final String KEY_USER_REGDATE = "regDate";

    public static final String KEY_SEARCH_SUB = "subValues";
    public static final String KEY_SEARCH_CURRPAGE = "currentPage";

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
    public static final String KEY_ISEDIT = "isEdit";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_BARCODE = "seaco_barcode";
    public static final String TAG_ID = "id";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_ADDRESS1 = "address1";
    public static final String TAG_ADDRESS2 = "address2";
    public static final String TAG_ADDRESS2_NO = "address2_no";
    public static final String TAG_ADDRESS2_STREETTYPE = "address2_streetType";
    public static final String TAG_ADDRESS2_STREETNAME = "address2_streetName";
    public static final String TAG_ADDRESS2_AREATYPE = "address2_areaType";
    public static final String TAG_ADDRESS2_AREANAME = "address2_areaName";
    public static final String TAG_ADDRESS2_BATU = "address2_batu";
    public static final String TAG_ADDRESS2_MUKIM = "address2_mukim";
    public static final String TAG_ADDRESS2_CREATEDBY = "Createdby";
    public static final String TAG_ADDRESS2_CREATEDDATE = "createdDate";
    public static final String TAG_ADDRESS2_MODIFIEDBY = "Modifiedby";
    public static final String TAG_ADDRESS2_MODIFIEDDATE = "modifiedDate";
    public static final String TAG_CHECK_LOCATION = "checkLatLonIsExist";
    public static final String TAG_CHECKDUP_LOCATION = "checkLatLonNotDup";

    public static final String TAG_PAGE_COUNT = "pageCount";

}
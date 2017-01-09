package jasonpaulraj.myseaco;

import android.util.Log;
import android.widget.RadioButton;

/**
 * Created by GPH on 11/3/2016.
 */

public class CommonRadioCheck {

    public final String TAG = "CommonRadioCheck";

    public void getStreetTypeCheck(String streetType, RadioButton radBtnHouseStreetType_Jln, RadioButton radBtnHouseStreetType_Lrg, RadioButton radBtnHouseStreetType_NA){

        if(streetType.equals("1")) {
            radBtnHouseStreetType_Jln.setChecked(true);
        }
        else if(streetType.equals("2")) {
            radBtnHouseStreetType_Lrg.setChecked(true);
        }
        else if(streetType.equals("3")) {
            radBtnHouseStreetType_Lrg.setChecked(true);
        }
        else {
            //radBtnHouseStreetType_NA.setChecked(true);
        }

    }

    public void getAreaTypeCheck(String areaType, RadioButton radBtnHouseAreaType_Tmn, RadioButton radBtnHouseAreaType_Kg, RadioButton radBtnHouseAreaType_Felda, RadioButton radBtnHouseAreaType_NA){

        if(areaType.equals("1")) {
            radBtnHouseAreaType_Tmn.setChecked(true);
        }
        else if(areaType.equals("2")) {
            radBtnHouseAreaType_Kg.setChecked(true);
        }
        else if(areaType.equals("3")) {
            radBtnHouseAreaType_Felda.setChecked(true);
        }
        else if(areaType.equals("4")) {
            radBtnHouseAreaType_NA.setChecked(true);
        }
        else{

        }

    }

    public void getMukimCheck(String address2_mukim, RadioButton radBtnHouseMukim_Bekok, RadioButton radBtnHouseMukim_Chaah, RadioButton radBtnHouseMukim_Gemereh, RadioButton radBtnHouseMukim_Segamat, RadioButton radBtnHouseMukim_Jabi){

        if(address2_mukim.equals("1")) {
            radBtnHouseMukim_Bekok.setChecked(true);
        }
        else if(address2_mukim.equals("2")) {
            radBtnHouseMukim_Chaah.setChecked(true);
        }
        else if(address2_mukim.equals("3")) {
            radBtnHouseMukim_Gemereh.setChecked(true);
        }
        else if(address2_mukim.equals("4")) {
            radBtnHouseMukim_Segamat.setChecked(true);
        }
        else if(address2_mukim.equals("5")) {
            radBtnHouseMukim_Jabi.setChecked(true);
        }
        else{

        }

    }

}


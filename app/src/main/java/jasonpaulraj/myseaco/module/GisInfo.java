package jasonpaulraj.myseaco.module;

/**
 * Created by GPH on 10/13/2016.
 */

public class GisInfo {

    private int id;
    private String seaco_barcode;
    private String latitude;
    private String longitude;
    private String address1;
    private String address2;
    private String address2_no;
    private String address2_streetType;
    private String address2_streetName;
    private String address2_areaType;
    private String address2_areaName;
    private String address2_batu;
    private String address2_mukim;
    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private String modifiedDate;
    private String flagUpload;

    public GisInfo(){}
    public GisInfo(String seaco_barcode, String latitude, String longitude, String address1, String address2, String address2_no, String address2_streetType,
                   String address2_streetName, String address2_areaType, String address2_areaName, String address2_batu, String address2_mukim, String createdBy, String createdDate,
                   String modifiedBy, String modifiedDate, String flagUpload){

        super();
        this.seaco_barcode = seaco_barcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address1 = address1;
        this.address2 = address2;
        this.address2_no = address2_no;
        this.address2_streetType = address2_streetType;
        this.address2_streetName = address2_streetName;
        this.address2_areaType = address2_areaType;
        this.address2_areaName = address2_areaName;
        this.address2_batu = address2_batu;
        this.address2_mukim = address2_mukim;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.flagUpload = flagUpload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeaco_barcode() {
        return seaco_barcode;
    }

    public void setSeaco_barcode(String seaco_barcode) {
        this.seaco_barcode = seaco_barcode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress2_no() {
        return address2_no;
    }

    public void setAddress2_no(String address2_no) {
        this.address2_no = address2_no;
    }

    public String getAddress2_streetType() {
        return address2_streetType;
    }

    public void setAddress2_streetType(String address2_streetType) {
        this.address2_streetType = address2_streetType;
    }

    public String getAddress2_streetName() {
        return address2_streetName;
    }

    public void setAddress2_streetName(String address2_streetName) {
        this.address2_streetName = address2_streetName;
    }

    public String getAddress2_areaType() {
        return address2_areaType;
    }

    public void setAddress2_areaType(String address2_areaType) {
        this.address2_areaType = address2_areaType;
    }

    public String getAddress2_areaName() {
        return address2_areaName;
    }

    public void setAddress2_areaName(String address2_areaName) {
        this.address2_areaName = address2_areaName;
    }

    public String getAddress2_batu() {
        return address2_batu;
    }

    public void setAddress2_batu(String address2_batu) {
        this.address2_batu = address2_batu;
    }

    public String getAddress2_mukim() {
        return address2_mukim;
    }

    public void setAddress2_mukim(String address2_mukim) {
        this.address2_mukim = address2_mukim;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getFlagUpload() {
        return flagUpload;
    }

    public void setFlagUpload(String flagUpload) {
        this.flagUpload = flagUpload;
    }
}


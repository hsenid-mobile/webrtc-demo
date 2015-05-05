/*
*  (C) Copyright 2010-2015 hSenid Mobile Solutions (Pvt) Limited.
*  All Rights Reserved.
*
*  These materials are unpublished, proprietary, confidential source code of
*  hSenid Mobile Solutions (Pvt) Limited and constitute a TRADE SECRET
*  of hSenid Mobile Solutions (Pvt) Limited.
*
*  hSenid Mobile Solutions (Pvt) Limited retains all title to and intellectual
*  property rights in these materials.
*/


package hms.webrtc.demo.controller.bean;

public class AdItemForm {

    private String adTopic;
    private String adDescription;
    private String itemPrice;
    private AdvertisementType advertisementType;
    private String mobileNumber;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAdTopic() {
        return adTopic;
    }

    public void setAdTopic(String adTopic) {
        this.adTopic = adTopic;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public AdvertisementType getAdvertisementType() {
        return advertisementType;
    }

    public void setAdvertisementType(AdvertisementType advertisementType) {
        this.advertisementType = advertisementType;
    }
}

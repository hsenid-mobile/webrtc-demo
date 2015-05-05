/*
 * (C) Copyright 2010-2014 hSenid Mobile Solutions (Pvt) Limited.
 * All Rights Reserved.
 *
 * These materials are unpublished, proprietary, confidential source code of
 * hSenid Mobile Solutions (Pvt) Limited and constitute a TRADE SECRET
 *  of hSenid Mobile Solutions (Pvt) Limited.
 *
 * hSenid Mobile Solutions (Pvt) Limited retains all title to and intellectual
 * property rights in these materials.
 */

package hms.webrtc.demo.service;


import hms.webrtc.demo.controller.bean.AdItemForm;
import hms.webrtc.demo.controller.bean.AdvertisementType;
import hms.webrtc.demo.dao.AdItemDao;
import hms.webrtc.demo.domain.AdItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdItemService {

    @Autowired
    private AdItemDao adItemDao;

    private static final Logger logger = LoggerFactory.getLogger(AdItemService.class);


    public boolean saveAdItem(AdItemForm adItemForm, String adId, String requestedScript, String uploadPosterURL, String mobileNumber) {
        try {
            AdItem adItem = createAdItem(adItemForm, uploadPosterURL, adId, requestedScript, mobileNumber);
            adItemDao.save(adItem);
            logger.debug("Ad item Created Successfully.");
            return true;
        } catch (Exception ex) {
            logger.error("Error Occurred While creating Ad Item.", ex);
        }
        return false;
    }

    private AdItem createAdItem(AdItemForm adItemForm, String uploadPosterURL, String adId,
                                String requestedScript, String mobileNumber) {
        AdItem adItem = new AdItem();
        adItem.setAdTopic(adItemForm.getAdTopic());
        adItem.setAdCategory(adItemForm.getAdvertisementType().name());
        adItem.setAdDescription(adItemForm.getAdDescription());
        adItem.setItemPrice(adItemForm.getItemPrice());
        adItem.setMobileNumber(mobileNumber);
        adItem.setPosterUrl(uploadPosterURL);
        adItem.setDate(new Date());
        adItem.setAdId(adId);
        adItem.setScript(requestedScript);
        return adItem;
    }


    public List<AdItem> getAllAdItems() {
        return adItemDao.getAllAdItems();
    }

    public AdItem getAdUnitById(String adItemId) {
        return adItemDao.getAdUnitById(adItemId);
    }

}

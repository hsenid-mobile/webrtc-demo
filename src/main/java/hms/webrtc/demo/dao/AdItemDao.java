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

package hms.webrtc.demo.dao;


import hms.webrtc.demo.domain.AdItem;

import java.util.List;

public interface AdItemDao extends BaseDAO {

    public List<AdItem> getAllAdItems();

    public AdItem getAdUnitById(String adItemId);

}

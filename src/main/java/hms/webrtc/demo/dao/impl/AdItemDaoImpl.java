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

package hms.webrtc.demo.dao.impl;


import hms.webrtc.demo.dao.AdItemDao;
import hms.webrtc.demo.domain.AdItem;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class AdItemDaoImpl extends AbstractDAO implements AdItemDao {

    @Override
    public List<AdItem> getAllAdItems() {
        Session session = getHibernateUtil().getSession();
        getHibernateUtil().beginTransction();
        Criteria selectOffersQuery = session.createCriteria(AdItem.class);
        List<AdItem> adItemList = selectOffersQuery.list();
        getHibernateUtil().commitTransaction();
        return adItemList;
    }

    @Override
    public AdItem getAdUnitById(String adItemId) {
        Session session = getHibernateUtil().getSession();
        getHibernateUtil().beginTransction();
        Criteria selectOne = session.createCriteria(AdItem.class);
        selectOne.add(Restrictions.eq("adId", adItemId));
        selectOne.setMaxResults(1);
        AdItem adItem = (AdItem) selectOne.list().get(0);
        getHibernateUtil().commitTransaction();
        return adItem;
    }

}

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


import hms.webrtc.demo.domain.IPersistable;
import hms.webrtc.demo.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/**
 * Abstract Data Access Object class for all the other Data Access Object Instances
 */
public abstract class AbstractDAO {

    private HibernateUtil hibernateUtil;
    private int batchSize = 50;

    public void save(IPersistable persistable) {
        Session session = hibernateUtil.getSession();
        getHibernateUtil().beginTransction();
        session.save(persistable);
        getHibernateUtil().commitTransaction();
    }

    public void update(IPersistable persistable) {
        Session session = hibernateUtil.getSession();
        session.update(persistable);
    }

    public void delete(IPersistable persistable) {
        Session session = hibernateUtil.getSession();
        session.delete(persistable);
    }

    public void batchInsert(List<?> T) {
        Session session = hibernateUtil.getSession();
        for (Object persistable : T) {
            int i = 0;
            session.save(persistable);
            if (++i % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    public void batchRemove(List<?> T) {
        Session session = hibernateUtil.getSession();
        for (Object persistable : T) {
            int i = 0;
            session.delete(persistable);
            if (++i % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    public HibernateUtil getHibernateUtil() {
        return hibernateUtil;
    }

    public void setHibernateUtil(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}

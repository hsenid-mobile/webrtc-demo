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



import hms.webrtc.demo.domain.IPersistable;
import hms.webrtc.demo.util.HibernateUtil;

import java.util.List;

/**
 * Super interface for all the Data Access Objects
 */
public interface BaseDAO {

    void save(IPersistable persistable);

    void update(IPersistable persistable);

    void delete(IPersistable persistable);

    void batchInsert(List<?> T);

    void batchRemove(List<?> T);

    HibernateUtil getHibernateUtil();

}

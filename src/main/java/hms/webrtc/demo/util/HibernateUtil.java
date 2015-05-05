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

package hms.webrtc.demo.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {

    private SessionFactory sessionFactory;
    private Session session;

    public void initSession() {

        if(sessionFactory.getCurrentSession() == null) {
            session = sessionFactory.openSession();
        } else {
            session = sessionFactory.getCurrentSession();
        }
    }

    public Session getSession(){

        if(sessionFactory.getCurrentSession() == null) {
            session = sessionFactory.openSession();
        } else {
            session = sessionFactory.getCurrentSession();
        }
        return session;
    }

    public void beginTransction() {
        session.beginTransaction();
    }

    public void commitTransaction(){

        if(session != null) {
            session.getTransaction().commit();
        }
    }

    public void closeSession() {
        if(session != null && session.isOpen()){
            session.close();
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}

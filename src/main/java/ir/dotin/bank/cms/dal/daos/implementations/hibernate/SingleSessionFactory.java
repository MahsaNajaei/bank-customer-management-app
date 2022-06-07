package ir.dotin.bank.cms.dal.daos.implementations.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SingleSessionFactory {

    private static SessionFactory singleSessionFactory;

    public static synchronized SessionFactory getInstance() {
        if (singleSessionFactory == null) {
            singleSessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return singleSessionFactory;
    }
}

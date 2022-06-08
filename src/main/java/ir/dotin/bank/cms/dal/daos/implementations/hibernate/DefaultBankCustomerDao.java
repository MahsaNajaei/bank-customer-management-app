package ir.dotin.bank.cms.dal.daos.implementations.hibernate;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.exceptions.NoResultFoundException;
import ir.dotin.bank.cms.dal.daos.interfaces.BankCustomerDao;
import ir.dotin.bank.cms.dal.exceptions.CustomerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class DefaultBankCustomerDao implements BankCustomerDao {
    private static final Logger logger = LogManager.getLogger(DefaultBankCustomerDao.class);

    @Override
    public void addCustomer(BankCustomerEntity customer) {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        session.save(customer);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void updateCustomer(BankCustomerEntity customer) {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        session.update(customer);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void deleteCustomer(String customerId) {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        Object persistentInstance = session.load(BankCustomerEntity.class, customerId);
        if (persistentInstance != null) {
            session.delete(persistentInstance);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public BankCustomerEntity retrieveCustomerById(String customerId) throws CustomerNotFoundException {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        BankCustomerEntity bankCustomerEntity = session.get(BankCustomerEntity.class, customerId);
        session.close();
        if (bankCustomerEntity == null)
            throw new CustomerNotFoundException();
        return bankCustomerEntity;
    }

    @Override
    public List<BankCustomerEntity> searchDBFor(String columnName, String searchValue) {
        Session session = SingleSessionFactory.getInstance().openSession();
        Query query = session.createQuery("select p from BankCustomerEntity p where " + columnName + " like :value");
        query.setParameter("value", searchValue + "%");
        List<BankCustomerEntity> customers = query.getResultList();
        session.close();
        return customers;
    }

    @Override
    public boolean exclusiveIdExists(String exclusiveId) {
        Session session = SingleSessionFactory.getInstance().openSession();
        Query query = session.createQuery("select p from BankCustomerEntity p where p.exclusiveId = :value");
        query.setParameter("value", exclusiveId);
        boolean exists = !query.getResultList().isEmpty();
        session.close();
        return exists;
    }

    public BankCustomerEntity retrieveCustomerByExclusiveId(String exclusiveId) throws CustomerNotFoundException {
        try {
            Session session = SingleSessionFactory.getInstance().openSession();
            Query query = session.createQuery("select p from BankCustomerEntity p where p.exclusiveId = :exclusiveId");
            query.setParameter("exclusiveId", exclusiveId);
            BankCustomerEntity bankCustomerEntity = (BankCustomerEntity) query.getSingleResult();
            session.close();
            return bankCustomerEntity;
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void updateCustomerLoans(String customerId, LoanTypeEntity loanType) throws NoResultFoundException {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from BankCustomerEntity customer where customer.customerId = :customerId and :loan not member of customer.receivedLoans");
        query.setParameter("customerId", customerId);
        query.setParameter("loan", loanType);
        try {
            BankCustomerEntity bankCustomerEntity = (BankCustomerEntity) query.getSingleResult();
            bankCustomerEntity.getReceivedLoans().add(loanType);
            session.getTransaction().commit();
            session.close();
        } catch (NoResultException e) {
            logger.error("No result found for customer with id[" + customerId + "] that has not registered loanType[" + loanType.getName() + "] yet! \n" + e);
            throw new NoResultFoundException();
        }
    }

    @Override
    public List<BankCustomerEntity> searchDBFor(Map<String, String> searchKeyValues) {
        Session session = SingleSessionFactory.getInstance().openSession();
        String queryString = "FROM BankCustomerEntity ";
        if (searchKeyValues.size() != 0) {
            String conditions = buildQuerySelectConditions(searchKeyValues);
            queryString += "where " + conditions;
        }
        List<BankCustomerEntity> customers = session.createQuery(queryString).list();
        session.close();
        return customers;
    }

    private String buildQuerySelectConditions(Map<String, String> searchKeyValues) {
        String conditions = "";
        String operator = "";
        for (String key : searchKeyValues.keySet()) {
            String value = searchKeyValues.get(key);
            if (key.equalsIgnoreCase("DATE"))
                value = "%" + value;
            conditions += operator + key + " LIKE '" + value + "%'";
            operator = " AND ";
        }
        return conditions;
    }

    public List<BankCustomerEntity> searchUsingQueryByExample(BankCustomerEntity exampleSearchObject) {
        Session session = SingleSessionFactory.getInstance().openSession();
        Example example = Example.create(exampleSearchObject).ignoreCase().enableLike(MatchMode.START).excludeZeroes();
        List<BankCustomerEntity> bankCustomerEntities = session.createCriteria(BankCustomerEntity.class).add(example).list();
        session.close();
        return bankCustomerEntities;
    }

}

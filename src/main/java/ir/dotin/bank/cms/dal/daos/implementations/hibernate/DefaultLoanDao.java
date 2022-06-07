package ir.dotin.bank.cms.dal.daos.implementations.hibernate;

import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.dal.daos.interfaces.LoanDao;
import ir.dotin.bank.cms.dal.exceptions.NoLoanTypeFoundException;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

public class DefaultLoanDao implements LoanDao {
    @Override
    public boolean add(LoanTypeEntity loanType) {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        session.save(loanType);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public List<LoanTypeEntity> retrieveLoanNamesAndIds() throws NoLoanTypeFoundException {
        Session session = SingleSessionFactory.getInstance().openSession();
        List<LoanTypeEntity> loanTypeEntities = session.createQuery("select new LoanTypeEntity(loanType.id, loanType.name) from LoanTypeEntity loanType").getResultList();
        if (loanTypeEntities.size() == 0)
            throw new NoLoanTypeFoundException();
        return loanTypeEntities;
    }

    public LoanTypeEntity retrieveLoanTypeById(int loanId) throws NoLoanTypeFoundException {
        Session session = SingleSessionFactory.getInstance().openSession();
        session.beginTransaction();
        LoanTypeEntity loanType = session.get(LoanTypeEntity.class, loanId);
        session.close();
        if (loanType == null)
            throw new NoLoanTypeFoundException();
        return loanType;
    }

    public LoanTypeEntity retrieveLoanTypeIfContractConditionIsAcceptable(int loanId, BigDecimal contractAmount, int contractDuration) throws NoLoanTypeFoundException {
        try {
            Session session = SingleSessionFactory.getInstance().openSession();
            Query query = session.createQuery("from LoanTypeEntity loanType where loanType.loanId =:id " +
                    "and exists (from GrantConditionEntity grantCondition where loanType.loanId = grantCondition.loanType.loanId " +
                    "and grantCondition.maxContractAmount >= :contractAmount and grantCondition.minContractAmount <= :contractAmount " +
                    "and grantCondition.maxContractDuration >= :contractDuration and grantCondition.minContractDuration <= :contractDuration)");
            query.setParameter("id", loanId);
            query.setParameter("contractAmount", contractAmount);
            query.setParameter("contractDuration", contractDuration);
            LoanTypeEntity loanType = (LoanTypeEntity) query.getSingleResult();
            session.close();
            return loanType;
        }catch (NoResultException e){
            throw new NoLoanTypeFoundException();
        }
    }

}

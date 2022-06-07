package ir.dotin.bank.cms.dal.daos.interfaces;

import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.dal.exceptions.NoLoanTypeFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface LoanDao {
    boolean add(LoanTypeEntity loanType);

    List<LoanTypeEntity> retrieveLoanNamesAndIds() throws NoLoanTypeFoundException;

    LoanTypeEntity retrieveLoanTypeById(int loanTypeId) throws NoLoanTypeFoundException;

    LoanTypeEntity retrieveLoanTypeIfContractConditionIsAcceptable(int loanId, BigDecimal contractAmount, int contractDuration) throws NoLoanTypeFoundException;
}

package ir.dotin.bank.cms.business.dataobjects.entities;

import java.util.List;

public class LoanTypeEntity {
    private int loanId;
    private String name;
    private double interestRate;
    List<BankCustomerEntity> loanApplicants;
    List<GrantConditionEntity> grantConditions;

    public LoanTypeEntity(){}
    public LoanTypeEntity(int loanId, String name){
        this.loanId = loanId;
        this.name = name;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public List<BankCustomerEntity> getLoanApplicants() {
        return loanApplicants;
    }

    public void setLoanApplicants(List<BankCustomerEntity> loanApplicants) {
        this.loanApplicants = loanApplicants;
    }

    public List<GrantConditionEntity> getGrantConditions() {
        return grantConditions;
    }

    public void setGrantConditions(List<GrantConditionEntity> grantConditions) {
        this.grantConditions = grantConditions;
    }
}

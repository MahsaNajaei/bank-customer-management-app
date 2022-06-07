package ir.dotin.bank.cms.business.dataobjects.entities;

import java.math.BigDecimal;

public class GrantConditionEntity {
    private int grantId;
    private String name;
    private int maxContractDuration;
    private int minContractDuration;
    private BigDecimal maxContractAmount;
    private BigDecimal minContractAmount;
    private LoanTypeEntity loanType;

    public int getGrantId() {
        return grantId;
    }

    public void setGrantId(int grantId) {
        this.grantId = grantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxContractDuration() {
        return maxContractDuration;
    }

    public void setMaxContractDuration(int maxContractDuration) {
        this.maxContractDuration = maxContractDuration;
    }

    public int getMinContractDuration() {
        return minContractDuration;
    }

    public void setMinContractDuration(int minContractDuration) {
        this.minContractDuration = minContractDuration;
    }

    public BigDecimal getMaxContractAmount() {
        return maxContractAmount;
    }

    public void setMaxContractAmount(BigDecimal maxContractAmount) {
        this.maxContractAmount = maxContractAmount;
    }

    public BigDecimal getMinContractAmount() {
        return minContractAmount;
    }

    public void setMinContractAmount(BigDecimal minContractAmount) {
        this.minContractAmount = minContractAmount;
    }

    public LoanTypeEntity getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanTypeEntity loanType) {
        this.loanType = loanType;
    }
}


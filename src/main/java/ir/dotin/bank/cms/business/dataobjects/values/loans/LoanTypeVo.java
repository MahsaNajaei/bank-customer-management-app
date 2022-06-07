package ir.dotin.bank.cms.business.dataobjects.values.loans;

import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;

import java.util.List;

public class LoanTypeVo {
    private String name;
    private String interestRate;
    private List<GrantConditionVo> grantConditions;
    private List<BankCustomerVo> loanApplicants;

    private LoanTypeVo(Builder loanTypeBuilder) {
        this.name = loanTypeBuilder.name;
        this.interestRate = loanTypeBuilder.interestRate;
        this.grantConditions = loanTypeBuilder.grantConditions;
        this.loanApplicants = loanTypeBuilder.loanApplicants;
    }

    public String getName() {
        return name;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public List<GrantConditionVo> getGrantConditions() {
        return grantConditions;
    }

    public List<BankCustomerVo> getLoanApplicants() {
        return loanApplicants;
    }

    public static class Builder {
        private String name;
        private String interestRate;
        private List<GrantConditionVo> grantConditions;
        private List<BankCustomerVo> loanApplicants;

        public Builder() {
        }

        public Builder(String name, String interestRate, List<GrantConditionVo> grantConditions) {
            this.name = name;
            this.interestRate = interestRate;
            this.grantConditions = grantConditions;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setInterestRate(String interestRate){
            this.interestRate = interestRate;
            return this;
        }

        public Builder setGrantConditions(List<GrantConditionVo> grantConditions) {
            this.grantConditions = grantConditions;
            return this;
        }

        public Builder setLoanApplicants(List<BankCustomerVo> loanApplicants) {
            this.loanApplicants = loanApplicants;
            return this;
        }

        public LoanTypeVo build() {
            return new LoanTypeVo(this);
        }

    }

}

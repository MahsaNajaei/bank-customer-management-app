package ir.dotin.bank.cms.business.dataobjects.values.loans;

public class GrantConditionVo {
    private String name;
    private String maxContractDuration;
    private String minContractDuration;
    private String maxContractAmount;
    private String minContractAmount;
    private LoanTypeVo loanType;

    public GrantConditionVo(Builder grantConditionBuilder) {
        this.name = grantConditionBuilder.name;
        this.maxContractDuration = grantConditionBuilder.maxContractDuration;
        this.minContractDuration = grantConditionBuilder.minContractDuration;
        this.maxContractAmount = grantConditionBuilder.maxContractAmount;
        this.minContractAmount = grantConditionBuilder.minContractAmount;
        this.loanType = grantConditionBuilder.loanType;
    }

    public String getName() {
        return name;
    }

    public String getMaxContractDuration() {
        return maxContractDuration;
    }

    public String getMinContractDuration() {
        return minContractDuration;
    }

    public String
    getMaxContractAmount() {
        return maxContractAmount;
    }

    public String
    getMinContractAmount() {
        return minContractAmount;
    }

    public LoanTypeVo getLoanType() {
        return loanType;
    }

    public static class Builder {
        private String name;
        private String maxContractDuration;
        private String minContractDuration;
        private String
                maxContractAmount;
        private String
                minContractAmount;
        private LoanTypeVo loanType;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setMaxContractDuration(String maxContractDuration) {
            this.maxContractDuration = maxContractDuration;
            return this;
        }

        public Builder setMinContractDuration(String minContractDuration) {
            this.minContractDuration = minContractDuration;
            return this;
        }

        public Builder setMaxContractAmount(String
                                                    maxContractAmount) {
            this.maxContractAmount = maxContractAmount;
            return this;
        }

        public Builder setMinContractAmount(String
                                                    minContractAmount) {
            this.minContractAmount = minContractAmount;
            return this;
        }

        public Builder setLoanType(LoanTypeVo loanType) {
            this.loanType = loanType;
            return this;
        }

        public GrantConditionVo build() {
            return new GrantConditionVo(this);
        }
    }
}

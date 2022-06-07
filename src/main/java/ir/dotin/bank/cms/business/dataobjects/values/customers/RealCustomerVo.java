package ir.dotin.bank.cms.business.dataobjects.values.customers;

import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;

import java.sql.Date;
import java.util.List;

public class RealCustomerVo extends BankCustomerVo {
    private String name;
    private String surname;
    private String fathersName;
    private Date birthDate;
    private String nationalCode;
    private List<LoanTypeVo> receivedLoanTypeVos;

    private RealCustomerVo(long customerId, CustomerType customerType, Builder builder) {
        super(customerId, customerType);
        this.name = builder.name;
        this.surname = builder.surname;
        this.fathersName = builder.fathersName;
        this.birthDate = builder.birthDate;
        this.nationalCode = builder.nationalCode;
        this.receivedLoanTypeVos = builder.receivedLoanTypeVos;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFathersName() {
        return fathersName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public List<LoanTypeVo> getReceivedLoanTypeVos() {
        return receivedLoanTypeVos;
    }

    public static class Builder {
        private String name;
        private String surname;
        private String fathersName;
        private Date birthDate;
        private String nationalCode;
        private List<LoanTypeVo> receivedLoanTypeVos;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder fathersName(String fathersName) {
            this.fathersName = fathersName;
            return this;
        }

        public Builder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder nationalCode(String nationalCode) {
            this.nationalCode = nationalCode;
            return this;
        }

        public Builder receivedLoans(List<LoanTypeVo> loanTypeVos) {
            this.receivedLoanTypeVos = loanTypeVos;
            return this;
        }

        public RealCustomerVo build(long customerId, CustomerType customerType) {
            return new RealCustomerVo(customerId, customerType, this);
        }
    }

}

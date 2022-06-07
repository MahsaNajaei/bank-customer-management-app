package ir.dotin.bank.cms.business.dataobjects.values.customers;

import java.sql.Date;

public class LegalCustomerVo extends BankCustomerVo {
    private Date registrationDate;
    private String companyName;
    private String economicId;

    private LegalCustomerVo(long customerId, CustomerType customerType, Builder builder) {
        super(customerId, customerType);
        registrationDate = builder.registrationDate;
        companyName = builder.companyName;
        economicId = builder.economicId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEconomicId() {
        return economicId;
    }

    public static class Builder {
        private Date registrationDate;
        private String companyName;
        private String economicId;

        public Builder registrationDate(Date registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder economicId(String economicId) {
            this.economicId = economicId;
            return this;
        }

        public LegalCustomerVo build(long customerId, CustomerType customerType) {
            return new LegalCustomerVo(customerId, customerType, this);
        }

    }

}

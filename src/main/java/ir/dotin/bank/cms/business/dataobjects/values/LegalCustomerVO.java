package ir.dotin.bank.cms.business.dataobjects.values;

import java.sql.Date;

public class LegalCustomerVO extends BankCustomerVO {
    private Date registrationDate;
    private String companyName;
    private String economicId;

    private LegalCustomerVO(long customerId, CustomerType customerType, Builder builder) {
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

        public LegalCustomerVO build(long customerId, CustomerType customerType) {
            return new LegalCustomerVO(customerId, customerType, this);
        }

    }

}

package ir.dotin.bank.cms.business.objects.values;

import java.sql.Date;

public class LegalCustomer extends BankCustomer {
    private Date registrationDate;
    private String companyName;
    private String economicId;

    private LegalCustomer(long customerId, CustomerType customerType, Builder builder) {
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

        public LegalCustomer build(long customerId, CustomerType customerType) {
            return new LegalCustomer(customerId, customerType, this);
        }

    }

}

package ir.dotin.bank.cms.business.dto;

import java.sql.Date;

public class LegalCustomer extends BankCustomer {

    private Date registrationDate;
    private String companyName;
    private String economicId;

    public LegalCustomer(long customerId) {
        super(customerId);
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEconomicId() {
        return economicId;
    }

    public void setEconomicId(String economicId) {
        this.economicId = economicId;
    }

    @Override
    public String toString() {
        return super.toString() + ",registrationDate=" + registrationDate + ",companyName=" + companyName
                + ",economicId=" + economicId;
    }
}

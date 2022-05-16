package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.dto.CustomerType;

public class BankCustomerDAOFactory {
    public BankCustomerDao getBankCustomerDAO(CustomerType customerType) {
        if (customerType == CustomerType.LEGAL)
            return new LegalCustomerDAO();
        if (customerType == CustomerType.REAL)
            return new RealCustomerDAO();
        return null;
    }
}

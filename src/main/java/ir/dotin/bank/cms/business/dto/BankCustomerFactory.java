package ir.dotin.bank.cms.business.dto;

public class BankCustomerFactory {
    public BankCustomer getBankCustomer(CustomerType customerType, long customerId){
        if (customerType == CustomerType.LEGAL)
            return new LegalCustomer(customerId);
        if (customerType == CustomerType.NATURAL)
            return new NaturalCustomer(customerId);
        return null;
    }
}

package ir.dotin.bank.cms.business.dataobjects.values;

public class BankCustomerVO {

    private final long customerId;
    private CustomerType customerType;

    public BankCustomerVO(long customerId, CustomerType customerType) {
        this.customerId = customerId;
        this.customerType = customerType;
    }

    public long getCustomerId() {
        return customerId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

}

package ir.dotin.bank.cms.business.dataobjects.values.customers;

public class BankCustomerVo {

    private final long customerId;
    private CustomerType customerType;

    public BankCustomerVo(long customerId, CustomerType customerType) {
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

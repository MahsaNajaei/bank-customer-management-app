package ir.dotin.bank.cms.business.objects.values;

public class BankCustomer {

    private final long customerId;
    private CustomerType customerType;

    public BankCustomer(long customerId, CustomerType customerType) {
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

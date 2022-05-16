package ir.dotin.bank.cms.business.dto;

public class BankCustomer {

    private final long customerId;
    private CustomerType customerType;

    public BankCustomer(long customerId) {
        this.customerId = customerId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    @Override
    public String toString() {
        return  "customerId=" + customerId + ",customerType=" + customerType;
    }
}

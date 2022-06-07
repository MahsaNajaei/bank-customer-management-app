package ir.dotin.bank.cms.dal.exceptions;

public class CustomerNotFoundException extends Throwable {
    public CustomerNotFoundException() {
        super("Such a customer does not exists!");
    }
}

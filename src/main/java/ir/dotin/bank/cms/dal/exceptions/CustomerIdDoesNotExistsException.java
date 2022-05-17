package ir.dotin.bank.cms.dal.exceptions;

public class CustomerIdDoesNotExistsException extends Throwable {
    public CustomerIdDoesNotExistsException() {
        super("The customer Id does not exists!");
    }
}

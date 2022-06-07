package ir.dotin.bank.cms.business.exceptions;

public class GrantConditionNotExistsException extends Exception {
    public GrantConditionNotExistsException(){
        super("No grant condition found! Entering At least one condition is mandatory!");
    }
}

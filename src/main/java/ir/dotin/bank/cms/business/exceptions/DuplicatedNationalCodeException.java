package ir.dotin.bank.cms.business.exceptions;

public class DuplicatedNationalCodeException extends Exception {
    public DuplicatedNationalCodeException() {
        super("National Code is Duplicated!");
    }
}

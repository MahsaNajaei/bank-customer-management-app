package ir.dotin.bank.cms.business.exceptions;

public class DuplicatedNationalCodeException extends Throwable {
    public DuplicatedNationalCodeException() {
        super("National Code is Duplicated!");
    }
}

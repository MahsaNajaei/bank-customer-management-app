package ir.dotin.bank.cms.business.exceptions;

public class DuplicatedNationalCode extends Throwable {
    public DuplicatedNationalCode() {
        super("National Code is Duplicated!");
    }
}

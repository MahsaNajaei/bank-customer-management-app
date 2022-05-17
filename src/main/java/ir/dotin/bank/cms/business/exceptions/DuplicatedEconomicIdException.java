package ir.dotin.bank.cms.business.exceptions;

public class DuplicatedEconomicIdException extends Throwable {
    public DuplicatedEconomicIdException() {
        super("Economic ID is duplicated!");
    }
}

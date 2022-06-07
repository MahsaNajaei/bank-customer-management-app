package ir.dotin.bank.cms.business.exceptions;

public class DuplicatedEconomicIdException extends Exception {
    public DuplicatedEconomicIdException() {
        super("Economic ID is duplicated!");
    }
}

package ir.dotin.bank.cms.business.exceptions;

public class InvalidEconomicIdException extends Exception {
    public InvalidEconomicIdException() {
        super("please enter economicId correctly!");
    }
}

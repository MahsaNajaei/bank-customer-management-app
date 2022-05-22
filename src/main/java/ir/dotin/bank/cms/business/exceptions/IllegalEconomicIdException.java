package ir.dotin.bank.cms.business.exceptions;

public class IllegalEconomicIdException extends Throwable {
    public IllegalEconomicIdException() {
        super("please enter economicId correctly!");
    }
}

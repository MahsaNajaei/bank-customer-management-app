package ir.dotin.bank.cms.business.exceptions;

public class DuplicatedEconomicId extends Throwable {
    public DuplicatedEconomicId() {
        super("Economic ID is duplicated!");
    }
}

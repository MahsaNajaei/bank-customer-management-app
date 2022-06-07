package ir.dotin.bank.cms.business.exceptions;

public class NullLoanTypeException extends Throwable {
    public NullLoanTypeException(){
        super("LoanType is null!");
    }
}

package ir.dotin.bank.cms.business.exceptions;

public class IllegalValueTypeException extends Throwable{
    public IllegalValueTypeException(){
        super("Illegal value type has been entered!");
    }
}

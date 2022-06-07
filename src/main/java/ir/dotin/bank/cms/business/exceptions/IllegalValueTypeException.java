package ir.dotin.bank.cms.business.exceptions;

public class IllegalValueTypeException extends Exception{
    public IllegalValueTypeException(){
        super("Illegal value type has been entered!");
    }
}

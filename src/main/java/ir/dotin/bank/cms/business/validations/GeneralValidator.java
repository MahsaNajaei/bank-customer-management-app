package ir.dotin.bank.cms.business.validations;

import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import org.apache.commons.lang3.StringUtils;

public class GeneralValidator {

    public static boolean isNumeric(String numberString) throws IllegalValueTypeException {
        if (!StringUtils.isNumeric(numberString))
            throw new IllegalValueTypeException();
        return true;
    }
}

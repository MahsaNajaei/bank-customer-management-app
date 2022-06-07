package ir.dotin.bank.cms.business.validatiors;

import ir.dotin.bank.cms.business.exceptions.IllegalNumberOrderException;
import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class GeneralValidator {
    public static void checkNumericValueIntegrity(String numberString) throws IllegalValueTypeException {
        if (!StringUtils.isNumeric(numberString))
            throw new IllegalValueTypeException();
    }

    public void checkMaxMinValidation(String minValue, String maxValue) throws IllegalNumberOrderException {
        BigDecimal min = new BigDecimal(minValue);
        BigDecimal max = new BigDecimal(maxValue);
        if (max.compareTo(min) < 0)
            throw new IllegalNumberOrderException();
    }
}

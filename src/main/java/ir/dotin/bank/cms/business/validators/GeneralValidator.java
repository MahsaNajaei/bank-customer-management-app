package ir.dotin.bank.cms.business.validators;

import ir.dotin.bank.cms.business.exceptions.IllegalNumberOrderException;
import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import ir.dotin.bank.cms.business.exceptions.NullValueException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class GeneralValidator {
    private static final Logger logger = LogManager.getLogger(GeneralValidator.class);

    public static void checkNumericValueIntegrity(String numberString) throws IllegalValueTypeException, NullValueException {
        if (numberString == null) {
            logger.error("Null Value has been entered for numeric field!");
            throw new NullValueException();
        }
        if (!StringUtils.isNumeric(numberString)) {
            logger.error("Illegal Value has been entered for numeric field! [value: " + numberString + "]");
            throw new IllegalValueTypeException();
        }
    }

    public void checkMaxMinValidation(String minValue, String maxValue) throws IllegalNumberOrderException {
        BigDecimal min = new BigDecimal(minValue);
        BigDecimal max = new BigDecimal(maxValue);
        if (max.compareTo(min) < 0) {
            logger.error("maxValue['" + max + "'] is less than minValue['" + min + "']");
            throw new IllegalNumberOrderException();
        }
    }
}

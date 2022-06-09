package ir.dotin.bank.cms.business.validators;

import ir.dotin.bank.cms.business.dataobjects.values.loans.GrantConditionVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;
import ir.dotin.bank.cms.business.exceptions.*;

public class LoanValidator extends GeneralValidator {

    public void validateLoanType(LoanTypeVo loanTypeVo) throws GrantConditionNotExistsException, NullLoanTypeException, IllegalValueTypeException, IllegalNumberOrderException, NullValueException {
        if (loanTypeVo == null)
            throw new NullLoanTypeException();
        if (loanTypeVo.getGrantConditions() == null || loanTypeVo.getGrantConditions().size() == 0)
            throw new GrantConditionNotExistsException();
        checkNumericValueIntegrity(loanTypeVo.getInterestRate());
        for (GrantConditionVo grantConditionVo : loanTypeVo.getGrantConditions()) {
            String min = grantConditionVo.getMinContractDuration();
            String max = grantConditionVo.getMaxContractDuration();
            checkNumericValueIntegrity(min);
            checkNumericValueIntegrity(min);
            checkMaxMinValidation(min, max);
            min = grantConditionVo.getMinContractAmount();
            max = grantConditionVo.getMaxContractAmount();
            checkNumericValueIntegrity(min);
            checkNumericValueIntegrity(min);
            checkMaxMinValidation(min, max);
        }
    }

    public void validateLoanRequestProfileInfo(String customerId, String loanTypeId, String contractAmount, String contractDuration) throws IllegalValueTypeException, NullValueException {
        GeneralValidator.checkNumericValueIntegrity(loanTypeId);
        GeneralValidator.checkNumericValueIntegrity(contractDuration);
        GeneralValidator.checkNumericValueIntegrity(contractAmount);
        GeneralValidator.checkNumericValueIntegrity(customerId);
    }
}

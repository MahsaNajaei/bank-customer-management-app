package ir.dotin.bank.cms.business.validatiors;

import ir.dotin.bank.cms.business.dataobjects.values.loans.GrantConditionVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;
import ir.dotin.bank.cms.business.exceptions.GrantConditionNotExistsException;
import ir.dotin.bank.cms.business.exceptions.IllegalNumberOrderException;
import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import ir.dotin.bank.cms.business.exceptions.NullLoanTypeException;

public class LoanValidator extends GeneralValidator {
    public void validateLoanType(LoanTypeVo loanTypeVo) throws GrantConditionNotExistsException, NullLoanTypeException, IllegalValueTypeException, IllegalNumberOrderException {
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
}

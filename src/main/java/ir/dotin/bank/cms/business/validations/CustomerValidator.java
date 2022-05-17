package ir.dotin.bank.cms.business.validations;

import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCodeException;
import ir.dotin.bank.cms.business.objects.values.BankCustomer;
import ir.dotin.bank.cms.business.objects.values.LegalCustomer;
import ir.dotin.bank.cms.business.objects.values.RealCustomer;
import ir.dotin.bank.cms.dal.BankCustomerDao;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;

import java.sql.SQLException;

public class CustomerValidator {

    private int dbConnectionTrialCounter;

    public CustomerValidator() {
        dbConnectionTrialCounter = 0;
    }

    public void validateCustomer(BankCustomer bankCustomer) throws DuplicatedEconomicIdException, DuplicatedNationalCodeException, SQLException {
        if (bankCustomer instanceof LegalCustomer) {
            validateLegalCustomer((LegalCustomer) bankCustomer);
        } else if (bankCustomer instanceof RealCustomer) {
            validateRealCustomer((RealCustomer) bankCustomer);
        }
    }

    private void validateRealCustomer(RealCustomer realCustomer) throws DuplicatedNationalCodeException, SQLException {
        boolean isUnique = checkExclusiveIdUniqueness(realCustomer.getNationalCode());
        if (!isUnique) {
            throw new DuplicatedNationalCodeException();
        }
    }

    private void validateLegalCustomer(LegalCustomer legalCustomer) throws DuplicatedEconomicIdException, SQLException {
        boolean isUnique = checkExclusiveIdUniqueness(legalCustomer.getEconomicId());
        if (!isUnique)
            throw new DuplicatedEconomicIdException();
    }

    public boolean checkExclusiveIdUniqueness(String exclusiveId) throws SQLException {
        BankCustomerDao bankCustomerDao = new DefaultBankCustomerDAO();
        return bankCustomerDao.exclusiveIdExists(exclusiveId);
    }
}

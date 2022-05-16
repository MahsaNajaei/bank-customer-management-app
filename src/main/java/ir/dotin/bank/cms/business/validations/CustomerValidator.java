package ir.dotin.bank.cms.business.validations;

import ir.dotin.bank.cms.business.dto.BankCustomer;
import ir.dotin.bank.cms.business.dto.CustomerType;
import ir.dotin.bank.cms.business.dto.LegalCustomer;
import ir.dotin.bank.cms.business.dto.RealCustomer;
import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicId;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCode;
import ir.dotin.bank.cms.dal.LegalCustomerDAO;
import ir.dotin.bank.cms.dal.RealCustomerDAO;

import java.sql.SQLException;

public class CustomerValidator {
    private RealCustomerDAO realCustomerDAO;
    private LegalCustomerDAO legalCustomerDAO;

    public CustomerValidator() {
        legalCustomerDAO = new LegalCustomerDAO();
        realCustomerDAO = new RealCustomerDAO();
    }

    public void validateCustomer(BankCustomer bankCustomer) throws DuplicatedEconomicId, SQLException, DuplicatedNationalCode {
        if (CustomerType.LEGAL == bankCustomer.getCustomerType())
            checkEconomicIdUniqueness(((LegalCustomer)bankCustomer).getEconomicId());
        else if (CustomerType.REAL == bankCustomer.getCustomerType())
            checkNationalCodeUniqueness(((RealCustomer)bankCustomer).getNationalCode());
    }

    public void checkEconomicIdUniqueness(String economicId) throws DuplicatedEconomicId, SQLException {
        if (legalCustomerDAO.economicIdExists(economicId))
            throw new DuplicatedEconomicId();
    }

    public void checkNationalCodeUniqueness(String nationalCode) throws DuplicatedNationalCode, SQLException {
        if (realCustomerDAO.nationalCodeExists(nationalCode))
            throw new DuplicatedNationalCode();
    }
}

package ir.dotin.bank.cms.business.validatiors;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo;
import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCodeException;
import ir.dotin.bank.cms.business.exceptions.InvalidEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.InvalidNationalCodeException;
import ir.dotin.bank.cms.dal.daos.interfaces.BankCustomerDao;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.List;

public class CustomerValidator extends GeneralValidator {

    private BankCustomerDao bankCustomerDao;

    public CustomerValidator() {
        bankCustomerDao = new DefaultBankCustomerDao();
    }

    public void checkValidationToRegister(BankCustomerVo bankCustomerVO) throws DuplicatedEconomicIdException, DuplicatedNationalCodeException, InvalidNationalCodeException, InvalidEconomicIdException {
        if (bankCustomerVO instanceof LegalCustomerVo) {
            LegalCustomerVo legalCustomerVO = (LegalCustomerVo) bankCustomerVO;
            validateLegalCustomer(legalCustomerVO);
            if (!checkExclusiveIdUniqueness(legalCustomerVO.getEconomicId()))
                throw new DuplicatedEconomicIdException();
        } else if (bankCustomerVO instanceof RealCustomerVo) {
            RealCustomerVo realCustomerVO = (RealCustomerVo) bankCustomerVO;
            validateRealCustomer(realCustomerVO);
            if (!checkExclusiveIdUniqueness(realCustomerVO.getNationalCode()))
                throw new DuplicatedNationalCodeException();
        }
    }

    public void validateLegalCustomerForUpdate(LegalCustomerVo legalCustomerVo) throws DuplicatedEconomicIdException, InvalidEconomicIdException {
        List<BankCustomerEntity> bankCustomerEntities = bankCustomerDao.searchDBFor("EXCLUSIVE_ID", legalCustomerVo.getEconomicId());
        if (bankCustomerEntities.size() != 0) {
            BankCustomerEntity bankCustomerEntity = bankCustomerEntities.get(0);
            if (!bankCustomerEntity.getCustomerId().equalsIgnoreCase(String.valueOf(legalCustomerVo.getCustomerId())))
                throw new DuplicatedEconomicIdException();
        }
        validateLegalCustomer(legalCustomerVo);
    }

    public void validateRealCustomerForUpdate(RealCustomerVo realCustomerVo) throws DuplicatedNationalCodeException, InvalidNationalCodeException {
        List<BankCustomerEntity> bankCustomerEntities = bankCustomerDao.searchDBFor("EXCLUSIVE_ID", realCustomerVo.getNationalCode());
        if (bankCustomerEntities.size() != 0) {
            BankCustomerEntity bankCustomerEntity = bankCustomerEntities.get(0);
            if (!bankCustomerEntity.getCustomerId().equalsIgnoreCase(String.valueOf(realCustomerVo.getCustomerId())))
                throw new DuplicatedNationalCodeException();
        }
        validateRealCustomer(realCustomerVo);
    }

    private void validateRealCustomer(RealCustomerVo realCustomer) throws InvalidNationalCodeException {
        String nationalCode = realCustomer.getNationalCode();
        if (nationalCode == null || nationalCode.length() != 10 || !StringUtils.isNumeric(nationalCode))
            throw new InvalidNationalCodeException();
    }

    private void validateLegalCustomer(LegalCustomerVo legalCustomer) throws InvalidEconomicIdException {
        String economicId = legalCustomer.getEconomicId();
        if (economicId == null || economicId.length() != 12 || !StringUtils.isNumeric(economicId))
            throw new InvalidEconomicIdException();
        validateDate(legalCustomer.getRegistrationDate());
    }

    private void validateDate(Date date) {
        //Todo
    }

    private boolean checkExclusiveIdUniqueness(String exclusiveId) {
        return !bankCustomerDao.exclusiveIdExists(exclusiveId);
    }

}

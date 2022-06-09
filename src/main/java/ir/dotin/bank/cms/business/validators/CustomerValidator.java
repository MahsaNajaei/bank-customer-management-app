package ir.dotin.bank.cms.business.validators;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo;
import ir.dotin.bank.cms.business.exceptions.*;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import ir.dotin.bank.cms.dal.daos.interfaces.BankCustomerDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.List;

public class CustomerValidator extends GeneralValidator {
    private static final Logger logger = LogManager.getLogger(CustomerValidator.class);
    private BankCustomerDao bankCustomerDao;

    public CustomerValidator() {
        bankCustomerDao = new DefaultBankCustomerDao();
    }

    public void checkValidationToRegister(BankCustomerVo bankCustomerVO) throws DuplicatedEconomicIdException, DuplicatedNationalCodeException, InvalidNationalCodeException, InvalidEconomicIdException {
        if (bankCustomerVO instanceof LegalCustomerVo) {
            LegalCustomerVo legalCustomerVO = (LegalCustomerVo) bankCustomerVO;
            validateLegalCustomer(legalCustomerVO);
            if (!checkExclusiveIdUniqueness(legalCustomerVO.getEconomicId())) {
                logger.error("EconomicId['" + legalCustomerVO.getEconomicId() + "'] is duplicated!");
                throw new DuplicatedEconomicIdException();
            }
        } else if (bankCustomerVO instanceof RealCustomerVo) {
            RealCustomerVo realCustomerVO = (RealCustomerVo) bankCustomerVO;
            validateRealCustomer(realCustomerVO);
            if (!checkExclusiveIdUniqueness(realCustomerVO.getNationalCode())) {
                logger.error("NationalCode['" + realCustomerVO.getNationalCode() + "'] is duplicated!");
                throw new DuplicatedNationalCodeException();
            }
        }
    }

    public void validateLegalCustomerForUpdate(LegalCustomerVo legalCustomerVo) throws DuplicatedEconomicIdException, InvalidEconomicIdException {
        List<BankCustomerEntity> bankCustomerEntities = bankCustomerDao.searchDBFor("EXCLUSIVE_ID", legalCustomerVo.getEconomicId());
        if (bankCustomerEntities.size() != 0) {
            BankCustomerEntity bankCustomerEntity = bankCustomerEntities.get(0);
            if (!bankCustomerEntity.getCustomerId().equalsIgnoreCase(String.valueOf(legalCustomerVo.getCustomerId()))) {
                logger.error("EconomicId['" + legalCustomerVo.getEconomicId() + "'] is duplicated!");
                throw new DuplicatedEconomicIdException();
            }
        }
        validateLegalCustomer(legalCustomerVo);
    }

    public void validateRealCustomerForUpdate(RealCustomerVo realCustomerVo) throws DuplicatedNationalCodeException, InvalidNationalCodeException {
        List<BankCustomerEntity> bankCustomerEntities = bankCustomerDao.searchDBFor("EXCLUSIVE_ID", realCustomerVo.getNationalCode());
        if (bankCustomerEntities.size() != 0) {
            BankCustomerEntity bankCustomerEntity = bankCustomerEntities.get(0);
            if (!bankCustomerEntity.getCustomerId().equalsIgnoreCase(String.valueOf(realCustomerVo.getCustomerId()))) {
                logger.error("NationalCode['" + realCustomerVo.getNationalCode() + "'] is duplicated!");
                throw new DuplicatedNationalCodeException();
            }
        }
        validateRealCustomer(realCustomerVo);
    }

    private void validateRealCustomer(RealCustomerVo realCustomer) throws InvalidNationalCodeException {
        String nationalCode = realCustomer.getNationalCode();
        if (nationalCode == null || nationalCode.length() != 10 || !StringUtils.isNumeric(nationalCode)) {
            logger.error("NationalCode['" + nationalCode + "'] is invalid!");
            throw new InvalidNationalCodeException();
        }
    }

    private void validateLegalCustomer(LegalCustomerVo legalCustomer) throws InvalidEconomicIdException {
        String economicId = legalCustomer.getEconomicId();
        if (economicId == null || economicId.length() != 12 || !StringUtils.isNumeric(economicId)) {
            logger.error("EconomicId['" + economicId + "'] is invalid!");
            throw new InvalidEconomicIdException();
        }
        validateDate(legalCustomer.getRegistrationDate());
    }

    private void validateDate(Date date) {
        //Todo
    }

    private boolean checkExclusiveIdUniqueness(String exclusiveId) {
        return !bankCustomerDao.exclusiveIdExists(exclusiveId);
    }

    public void validateNationalCode(String nationalCode) throws InvalidNationalCodeException, NullValueException {
        try {
            checkNumericValueIntegrity(nationalCode);
        } catch (IllegalValueTypeException e) {
            logger.error("National code [" + nationalCode + "] is not numeric!");
            throw new InvalidNationalCodeException();
        }

        if (nationalCode.length() != 10)
            throw new InvalidNationalCodeException();

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int singleDigit = Integer.parseInt(nationalCode.charAt(i) + "");
            sum += singleDigit * (10 - i);
        }
        int remainder = sum % 11;
        int rightMostDigit = Integer.parseInt(nationalCode.charAt(9) + "");
        if (remainder != rightMostDigit && remainder != 11 - rightMostDigit) {
            logger.error("National code [" + nationalCode + "] is invalid!");
            throw new InvalidNationalCodeException();
        }
    }

}

package ir.dotin.bank.cms.business.validations;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.BankCustomerVO;
import ir.dotin.bank.cms.business.dataobjects.values.LegalCustomerVO;
import ir.dotin.bank.cms.business.dataobjects.values.RealCustomerVO;
import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCodeException;
import ir.dotin.bank.cms.business.exceptions.IllegalEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.IllegalNationalCodeException;
import ir.dotin.bank.cms.dal.BankCustomerDao;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

public class CustomerValidator {

    private BankCustomerDao bankCustomerDao;

    public CustomerValidator() {
        bankCustomerDao = new DefaultBankCustomerDAO();
    }

    public void checkValidationToRegister(BankCustomerVO bankCustomerVO) throws SQLException, DuplicatedEconomicIdException, DuplicatedNationalCodeException, IllegalNationalCodeException, IllegalEconomicIdException {
        if (bankCustomerVO instanceof LegalCustomerVO) {
            LegalCustomerVO legalCustomerVO = (LegalCustomerVO) bankCustomerVO;
            validateLegalCustomer(legalCustomerVO);
            if (!checkExclusiveIdUniqueness(legalCustomerVO.getEconomicId()))
                throw new DuplicatedEconomicIdException();
        } else if (bankCustomerVO instanceof RealCustomerVO) {
            RealCustomerVO realCustomerVO = (RealCustomerVO) bankCustomerVO;
            validateRealCustomer(realCustomerVO);
            if (!checkExclusiveIdUniqueness(realCustomerVO.getNationalCode()))
                throw new DuplicatedNationalCodeException();
        }
    }

    public void validateLegalCustomerForUpdate(LegalCustomerVO legalCustomerVo) throws SQLException, DuplicatedEconomicIdException, IllegalEconomicIdException {
        List<BankCustomerEntity> bankCustomerEntities = bankCustomerDao.searchDBFor("exclusiveId", legalCustomerVo.getEconomicId());
        if (bankCustomerEntities.size() != 0) {
            BankCustomerEntity bankCustomerEntity = bankCustomerEntities.get(0);
            if (!bankCustomerEntity.getCustomerId().equalsIgnoreCase(String.valueOf(legalCustomerVo.getCustomerId())))
                throw new DuplicatedEconomicIdException();
        }
        validateLegalCustomer(legalCustomerVo);
    }

    public void validateRealCustomerForUpdate(RealCustomerVO realCustomerVo) throws SQLException, DuplicatedNationalCodeException, IllegalNationalCodeException {
        List<BankCustomerEntity> bankCustomerEntities = bankCustomerDao.searchDBFor("exclusiveId", realCustomerVo.getNationalCode());
        if (bankCustomerEntities.size() != 0) {
            BankCustomerEntity bankCustomerEntity = bankCustomerEntities.get(0);
            if (!bankCustomerEntity.getCustomerId().equalsIgnoreCase(String.valueOf(realCustomerVo.getCustomerId())))
                throw new DuplicatedNationalCodeException();
        }
        validateRealCustomer(realCustomerVo);
    }

    private void validateRealCustomer(RealCustomerVO realCustomer) throws IllegalNationalCodeException {
        String nationalCode = realCustomer.getNationalCode();
        if (nationalCode == null || nationalCode.length() != 10 || StringUtils.isNumeric(nationalCode))
            throw new IllegalNationalCodeException();
    }

    private void validateLegalCustomer(LegalCustomerVO legalCustomer) throws IllegalEconomicIdException {
        String economicId = legalCustomer.getEconomicId();
        if (economicId == null || economicId.length() != 12 || StringUtils.isNumeric(economicId))
            throw new IllegalEconomicIdException();
    }

    private boolean checkExclusiveIdUniqueness(String exclusiveId) throws SQLException {
        return !bankCustomerDao.exclusiveIdExists(exclusiveId);
    }

}

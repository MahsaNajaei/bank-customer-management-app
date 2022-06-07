package ir.dotin.bank.cms.business.tools;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.CustomerType;
import ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDataMapper {

    public BankCustomerEntity mapBankCustomerVOToEntity(BankCustomerVo bankCustomerVO) {
        BankCustomerEntity bankCustomerEntity = new BankCustomerEntity();

        String name = null;
        Date date = null;
        String exclusiveId = null;

        if (bankCustomerVO.getCustomerType().equals(CustomerType.REAL)) {
            RealCustomerVo realCustomer = (RealCustomerVo) bankCustomerVO;
            name = realCustomer.getName();
            date = realCustomer.getBirthDate();
            exclusiveId = realCustomer.getNationalCode();
            bankCustomerEntity.setParentName(realCustomer.getFathersName());
            bankCustomerEntity.setSurname(realCustomer.getSurname());

            if (realCustomer.getReceivedLoanTypeVos() != null && realCustomer.getReceivedLoanTypeVos().size() != 0) {
                List<LoanTypeEntity> loanTypeEntities = new ArrayList<>();
                LoanDataMapper loanDataMapper = new LoanDataMapper();
                for (LoanTypeVo loanTypeVo : realCustomer.getReceivedLoanTypeVos()) {
                    LoanTypeEntity loanTypeEntity = loanDataMapper.mapLoanTypeVoToLoanTypeEntity(loanTypeVo);
                    loanTypeEntities.add(loanTypeEntity);
                }
                bankCustomerEntity.setReceivedLoans(loanTypeEntities);
            }

        } else if (bankCustomerVO.getCustomerType().equals(CustomerType.LEGAL)) {

            LegalCustomerVo legalCustomer = (LegalCustomerVo) bankCustomerVO;
            name = legalCustomer.getCompanyName();
            date = legalCustomer.getRegistrationDate();
            exclusiveId = legalCustomer.getEconomicId();

        }

        bankCustomerEntity.setCustomerId(String.valueOf(bankCustomerVO.getCustomerId()));
        bankCustomerEntity.setCustomerType(bankCustomerVO.getCustomerType().name());
        bankCustomerEntity.setName(name);
        bankCustomerEntity.setDate(date);
        bankCustomerEntity.setExclusiveId(exclusiveId);

        return bankCustomerEntity;
    }

    public Map<CustomerType, List<BankCustomerVo>> convertCustomerEntitiesToCustomerTypeBankCustomerVoMap(List<BankCustomerEntity> customerEntities) {
        Map<CustomerType, List<BankCustomerVo>> customerTypeToBankCustomersMap = new HashMap<>();

        List<BankCustomerVo> legalCustomerVOS = new ArrayList<>();
        List<BankCustomerVo> realCustomerVOS = new ArrayList<>();
        for (BankCustomerEntity bankCustomerEntity : customerEntities) {
            BankCustomerVo bankCustomerVO = mapCustomerEntityToBankCustomerVO(bankCustomerEntity);
            if (bankCustomerVO.getCustomerType().equals(CustomerType.REAL))
                realCustomerVOS.add(bankCustomerVO);
            else if (bankCustomerVO.getCustomerType().equals(CustomerType.LEGAL)) {
                legalCustomerVOS.add(bankCustomerVO);
            }
        }
        if (legalCustomerVOS.size() != 0)
            customerTypeToBankCustomersMap.put(CustomerType.LEGAL, legalCustomerVOS);
        if (realCustomerVOS.size() != 0)
            customerTypeToBankCustomersMap.put(CustomerType.REAL, realCustomerVOS);

        return customerTypeToBankCustomersMap;

    }

    public BankCustomerVo mapCustomerEntityToBankCustomerVO(BankCustomerEntity bankCustomerEntity) {
        String customerType = bankCustomerEntity.getCustomerType();
        long customerId = Long.parseLong(bankCustomerEntity.getCustomerId());

        BankCustomerVo bankCustomerVO = null;
        if (customerType.equalsIgnoreCase("legal")) {
            bankCustomerVO = new LegalCustomerVo.Builder()
                    .companyName(bankCustomerEntity.getName())
                    .economicId(bankCustomerEntity.getExclusiveId())
                    .registrationDate(bankCustomerEntity.getDate())
                    .build(customerId, CustomerType.LEGAL);

        } else if (customerType.equalsIgnoreCase("real")) {
            bankCustomerVO = new RealCustomerVo.Builder()
                    .name(bankCustomerEntity.getName())
                    .surname(bankCustomerEntity.getSurname())
                    .fathersName(bankCustomerEntity.getParentName())
                    .birthDate(bankCustomerEntity.getDate())
                    .nationalCode(bankCustomerEntity.getExclusiveId())
                    .build(customerId, CustomerType.REAL);
        }

        return bankCustomerVO;
    }

}


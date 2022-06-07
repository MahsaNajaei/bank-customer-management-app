package ir.dotin.bank.cms.business.tools;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.entities.GrantConditionEntity;
import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.GrantConditionVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;

import java.math.BigDecimal;
import java.util.ArrayList;

public class LoanDataMapper {

    public LoanTypeEntity mapLoanTypeVoToLoanTypeEntity(LoanTypeVo loanTypeVo) {
        LoanTypeEntity loanTypeEntity = new LoanTypeEntity();
        loanTypeEntity.setName(loanTypeVo.getName());
        loanTypeEntity.setInterestRate(Double.parseDouble(loanTypeVo.getInterestRate().split("%")[0]));

        for (GrantConditionVo grantConditionVo : loanTypeVo.getGrantConditions()) {
            if (loanTypeEntity.getGrantConditions() == null)
                loanTypeEntity.setGrantConditions(new ArrayList<>());
            GrantConditionEntity grantConditionEntity = mapGrantConditionVoToGrantConditionEntity(grantConditionVo);
            loanTypeEntity.getGrantConditions().add(grantConditionEntity);
        }

        if (loanTypeVo.getLoanApplicants() != null) {
            CustomerDataMapper customerDataMapper = new CustomerDataMapper();
            for (BankCustomerVo bankCustomerVo : loanTypeVo.getLoanApplicants()) {
                if (loanTypeEntity.getLoanApplicants() == null)
                    loanTypeEntity.setLoanApplicants(new ArrayList<>());
                BankCustomerEntity bankCustomerEntity = customerDataMapper.mapBankCustomerVOToEntity(bankCustomerVo);
                loanTypeEntity.getLoanApplicants().add(bankCustomerEntity);
            }
        }
        return loanTypeEntity;
    }

    public GrantConditionEntity mapGrantConditionVoToGrantConditionEntity(GrantConditionVo grantConditionVo) {
        GrantConditionEntity grantConditionEntity = new GrantConditionEntity();
        grantConditionEntity.setName(grantConditionVo.getName());
        grantConditionEntity.setMinContractDuration(Integer.parseInt(grantConditionVo.getMinContractDuration()));
        grantConditionEntity.setMaxContractDuration(Integer.parseInt(grantConditionVo.getMaxContractDuration()));
        grantConditionEntity.setMinContractAmount(new BigDecimal(grantConditionVo.getMinContractAmount()));
        grantConditionEntity.setMaxContractAmount(new BigDecimal(grantConditionVo.getMaxContractAmount()));
        return grantConditionEntity;
    }
}

package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCodeException;
import ir.dotin.bank.cms.business.exceptions.InvalidEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.InvalidNationalCodeException;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.tools.CustomerDataMapper;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.business.tools.UniqueIdGenerator;
import ir.dotin.bank.cms.business.validatiors.CustomerValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "registrationServlet")
public class RegistrationServlet extends HttpServlet {

    private UniqueIdGenerator uniqueIdGenerator;
    private CustomerValidator customerValidator;

    @Override
    public void init() throws ServletException {
        super.init();
        uniqueIdGenerator = new UniqueIdGenerator(10);
        customerValidator = new CustomerValidator();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long customerId = uniqueIdGenerator.generateNumberId();
            DataExtractor dataExtractor = new DataExtractor();
            BankCustomerVo bankCustomerVO = null;
            String servletPath = request.getServletPath();

            if (servletPath.contains("legal"))
                bankCustomerVO = dataExtractor.extractLegalCustomerVOFromRequestParams(request, customerId);
            else if (servletPath.contains("real"))
                bankCustomerVO = dataExtractor.extractRealCustomerVoFromRequestParams(request, customerId);

            customerValidator.checkValidationToRegister(bankCustomerVO);
            BankCustomerEntity bankCustomerEntity = new CustomerDataMapper().mapBankCustomerVOToEntity(bankCustomerVO);
            new DefaultBankCustomerDao().addCustomer(bankCustomerEntity);
            response.getWriter().println(customerId);

        } catch (DuplicatedEconomicIdException e) {
            response.setStatus(CustomHttpStatusCode.DUPLICATED_ECONOMIC_ID);
            response.getWriter().println(e.getMessage());
        } catch (DuplicatedNationalCodeException e) {
            response.setStatus(CustomHttpStatusCode.DUPLICATED_NATIONAL_CODE);
            response.getWriter().println(e.getMessage());
        } catch (InvalidEconomicIdException e) {
            response.setStatus(CustomHttpStatusCode.INVALID_ECONOMIC_ID);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (InvalidNationalCodeException e) {
            response.setStatus(CustomHttpStatusCode.INVALID_NATIONAL_CODE);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(CustomHttpStatusCode.INTERNAL_SERVER_ERROR);
            response.getWriter().println("Sorry, a problem has occurred in server! Please try later!");
            e.printStackTrace();
        }
    }
}

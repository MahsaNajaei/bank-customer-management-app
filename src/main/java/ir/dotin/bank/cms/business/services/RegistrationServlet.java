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
import ir.dotin.bank.cms.business.validators.CustomerValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "registrationServlet")
public class RegistrationServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RegistrationServlet.class);
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
            logger.info("Customer with id['" + customerId + "']" + " has been registered successfully!");
        } catch (DuplicatedEconomicIdException e) {
            response.setStatus(CustomHttpStatusCode.DUPLICATED_ECONOMIC_ID);
            response.getWriter().println(e.getMessage());
            logger.error("Duplicated economic id!");
        } catch (DuplicatedNationalCodeException e) {
            logger.error("Duplicated national code!");
            response.setStatus(CustomHttpStatusCode.DUPLICATED_NATIONAL_CODE);
            response.getWriter().println(e.getMessage());
        } catch (InvalidEconomicIdException e) {
            logger.error("Invalid economic id has been entered!");
            response.setStatus(CustomHttpStatusCode.INVALID_ECONOMIC_ID);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (InvalidNationalCodeException e) {
            logger.error("Invalid national code has been entered!");
            response.setStatus(CustomHttpStatusCode.INVALID_NATIONAL_CODE);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        }
    }
}

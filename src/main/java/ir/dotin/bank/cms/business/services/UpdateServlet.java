package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.CustomerType;
import ir.dotin.bank.cms.business.dataobjects.values.customers.LegalCustomerVo;
import ir.dotin.bank.cms.business.dataobjects.values.customers.RealCustomerVo;
import ir.dotin.bank.cms.business.exceptions.*;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.tools.CustomerDataMapper;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.business.validators.CustomerValidator;
import ir.dotin.bank.cms.business.validators.GeneralValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import ir.dotin.bank.cms.dal.exceptions.CustomerNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "updateServlet")
public class UpdateServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(UpdateServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String customerId = request.getParameter("customer-id");
            GeneralValidator.checkNumericValueIntegrity(customerId);
            BankCustomerEntity bankCustomerEntity = new DefaultBankCustomerDao().retrieveCustomerById(customerId);
            BankCustomerVo bankCustomerVO = new CustomerDataMapper().mapCustomerEntityToBankCustomerVO(bankCustomerEntity);
            request.setAttribute("customer", bankCustomerVO);
            if (bankCustomerVO.getCustomerType().equals(CustomerType.REAL))
                request.getRequestDispatcher("/presentation/realEditPage.jsp").include(request, response);
            else if (bankCustomerVO.getCustomerType().equals(CustomerType.LEGAL))
                request.getRequestDispatcher("/presentation/legalEditPage.jsp").include(request, response);
            logger.info("The information of requested customer has been sent successfully to the client! [customerId: " + customerId + "]");
        } catch (IllegalValueTypeException e) {
            logger.error("Customer id is not numeric!");
            response.getWriter().println(e.getMessage() + "Customer id is not numeric!");
            e.printStackTrace();
        } catch (CustomerNotFoundException e) {
            logger.error("Customer with [Id: " + request.getParameter("customer-id") + "] is not found!");
            response.setStatus(CustomHttpStatusCode.CUSTOMER_NOT_FOUND);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (NullValueException e) {
            logger.error("Customer id is null!");
            response.setStatus(CustomHttpStatusCode.NULL_VALUE);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long customerId = Long.parseLong(request.getParameter("customer-id"));
        CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());
        DataExtractor dataExtractor = new DataExtractor();
        CustomerValidator customerValidator = new CustomerValidator();

        try {
            BankCustomerVo updatedCustomerVo = null;
            if (CustomerType.LEGAL == customerType) {
                updatedCustomerVo = dataExtractor.extractLegalCustomerVOFromRequestParams(request, customerId);
                customerValidator.validateLegalCustomerForUpdate((LegalCustomerVo) updatedCustomerVo);

            } else if (CustomerType.REAL == customerType) {
                updatedCustomerVo = dataExtractor.extractRealCustomerVoFromRequestParams(request, customerId);
                customerValidator.validateRealCustomerForUpdate((RealCustomerVo) updatedCustomerVo);
            }
            BankCustomerEntity updatedBankCustomerEntity = new CustomerDataMapper().mapBankCustomerVOToEntity(updatedCustomerVo);
            new DefaultBankCustomerDao().updateCustomer(updatedBankCustomerEntity);
            response.getWriter().println("ویرایش با موفقیت انجام شد!");
            logger.info("Update successfully for customer info with id['" + customerId + "']");
        } catch (DuplicatedEconomicIdException e) {
            logger.warn("Duplicated economic id has been entered!");
            response.setStatus(CustomHttpStatusCode.DUPLICATED_ECONOMIC_ID);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (DuplicatedNationalCodeException e) {
            logger.warn("Duplicated national code has been entered!");
            response.setStatus(CustomHttpStatusCode.DUPLICATED_NATIONAL_CODE);
            e.printStackTrace();
            response.getWriter().println(e.getMessage());
        } catch (InvalidEconomicIdException e) {
            logger.warn("Economic Id is invalid!");
            response.setStatus(CustomHttpStatusCode.INVALID_ECONOMIC_ID);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (InvalidNationalCodeException e) {
            logger.warn("National code is invalid!");
            response.setStatus(CustomHttpStatusCode.INVALID_NATIONAL_CODE);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        }

    }
}

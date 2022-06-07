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
import ir.dotin.bank.cms.business.validatiors.CustomerValidator;
import ir.dotin.bank.cms.business.validatiors.GeneralValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import ir.dotin.bank.cms.dal.exceptions.CustomerNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.crypto.Data;
import java.io.IOException;

@WebServlet(name = "updateServlet")
public class UpdateServlet extends HttpServlet {

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

        } catch (IllegalValueTypeException e) {
            response.getWriter().println(e.getMessage() + "Customer id is not numeric!");
            e.printStackTrace();
        } catch (CustomerNotFoundException e) {
            response.setStatus(CustomHttpStatusCode.CUSTOMER_NOT_FOUND);
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(CustomHttpStatusCode.INTERNAL_SERVER_ERROR);
            response.getWriter().println("Sorry, a problem has occurred in server! Please try later!");
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
            response.getWriter().println("update succeeded!");

        } catch (DuplicatedEconomicIdException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (DuplicatedNationalCodeException e) {
            e.printStackTrace();
            response.getWriter().println(e.getMessage());
        } catch (InvalidEconomicIdException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (InvalidNationalCodeException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Sorry a problem has occurred in server. Please try again!");
        }

    }
}

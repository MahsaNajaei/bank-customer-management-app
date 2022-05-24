package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.BankCustomerVO;
import ir.dotin.bank.cms.business.dataobjects.values.CustomerType;
import ir.dotin.bank.cms.business.dataobjects.values.LegalCustomerVO;
import ir.dotin.bank.cms.business.dataobjects.values.RealCustomerVO;
import ir.dotin.bank.cms.business.exceptions.*;
import ir.dotin.bank.cms.business.tools.DataMapper;
import ir.dotin.bank.cms.business.validations.CustomerValidator;
import ir.dotin.bank.cms.business.validations.GeneralValidator;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
import ir.dotin.bank.cms.dal.exceptions.CustomerIdDoesNotExistsException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "updateServlet")
public class UpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String customerId = request.getParameter("customer-id");
            GeneralValidator.isNumeric(customerId);
            BankCustomerEntity bankCustomerEntity = new DefaultBankCustomerDAO().retrieveCustomerById(customerId);
            BankCustomerVO bankCustomerVO = new DataMapper().convertCustomerEntityToBankCustomerVO(bankCustomerEntity);
            request.setAttribute("customer", bankCustomerVO);
            if (bankCustomerVO.getCustomerType().equals(CustomerType.REAL))
                request.getRequestDispatcher("/presentation/realEditPage.jsp").include(request, response);
            else if (bankCustomerVO.getCustomerType().equals(CustomerType.LEGAL))
                request.getRequestDispatcher("/presentation/legalEditPage.jsp").include(request, response);

        } catch (IllegalValueTypeException e) {
            response.getWriter().println(e.getMessage() + "Customer id is not numeric!");
            e.printStackTrace();
        } catch (CustomerIdDoesNotExistsException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            response.getWriter().println("Sorry, a problem has occurred in server! Please try later!");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long customerId = Long.parseLong(request.getParameter("customer-id"));
        CustomerType customerType = CustomerType.valueOf(request.getParameter("customer-type").toUpperCase());
        DataMapper dataMapper = new DataMapper();
        CustomerValidator customerValidator = new CustomerValidator();

        try {
            BankCustomerVO updatedCustomerVo = null;
            if (CustomerType.LEGAL == customerType) {
                updatedCustomerVo = dataMapper.mapRequestParamsToLegalCustomerVO(request, customerId);
                customerValidator.validateLegalCustomerForUpdate((LegalCustomerVO) updatedCustomerVo);

            } else if (CustomerType.REAL == customerType) {
                updatedCustomerVo = dataMapper.mapRequestParamsToRealCustomerVO(request, customerId);
                customerValidator.validateRealCustomerForUpdate((RealCustomerVO) updatedCustomerVo);
            }
            BankCustomerEntity updatedBankCustomerEntity = dataMapper.convertBankCustomerVOToEntity(updatedCustomerVo);
            new DefaultBankCustomerDAO().updateCustomer(updatedBankCustomerEntity);
            response.getWriter().println("update succeeded!");

        } catch (DuplicatedEconomicIdException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (DuplicatedNationalCodeException e) {
            e.printStackTrace();
            response.getWriter().println(e.getMessage());
        } catch (IllegalEconomicIdException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (IllegalNationalCodeException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Sorry a problem has occurred in server. Please try again!");
        }

    }
}

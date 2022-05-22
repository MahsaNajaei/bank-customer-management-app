package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.values.BankCustomerVO;
import ir.dotin.bank.cms.business.exceptions.DuplicatedEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.DuplicatedNationalCodeException;
import ir.dotin.bank.cms.business.exceptions.IllegalEconomicIdException;
import ir.dotin.bank.cms.business.exceptions.IllegalNationalCodeException;
import ir.dotin.bank.cms.business.tools.DataMapper;
import ir.dotin.bank.cms.business.tools.UniqueIdGenerator;
import ir.dotin.bank.cms.business.validations.CustomerValidator;
import ir.dotin.bank.cms.dal.DefaultBankCustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

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
            DataMapper dataMapper = new DataMapper();
            BankCustomerVO bankCustomerVO = null;
            String servletPath = request.getServletPath();

            if (servletPath.contains("legal"))
                bankCustomerVO = dataMapper.mapRequestParamsToLegalCustomerVO(request, customerId);
            else if (servletPath.contains("real"))
                bankCustomerVO = dataMapper.mapRequestParamsToRealCustomerVO(request, customerId);

            customerValidator.checkValidationToRegister(bankCustomerVO);
            BankCustomerEntity bankCustomerEntity = dataMapper.convertBankCustomerVOToEntity(bankCustomerVO);
            new DefaultBankCustomerDAO().addCustomer(bankCustomerEntity);
            response.getWriter().println(customerId);

        } catch (DuplicatedEconomicIdException e) {
            response.getWriter().println(e.getMessage());
        } catch (DuplicatedNationalCodeException e) {
            response.getWriter().println(e.getMessage());
        } catch (SQLException e) {
            response.getWriter().println("Sorry a problem has occurred in server. Please try again!");
            e.printStackTrace();
        } catch (IllegalEconomicIdException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        } catch (IllegalNationalCodeException e) {
            response.getWriter().println(e.getMessage());
            e.printStackTrace();
        }
    }
}

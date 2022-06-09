package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.dataobjects.values.customers.BankCustomerVo;
import ir.dotin.bank.cms.business.exceptions.IllegalValueTypeException;
import ir.dotin.bank.cms.business.exceptions.InvalidNationalCodeException;
import ir.dotin.bank.cms.dal.exceptions.NoResultFoundException;
import ir.dotin.bank.cms.business.exceptions.NullValueException;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.tools.CustomerDataMapper;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.business.validators.CustomerValidator;
import ir.dotin.bank.cms.business.validators.LoanValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultBankCustomerDao;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultLoanDao;
import ir.dotin.bank.cms.dal.exceptions.CustomerNotFoundException;
import ir.dotin.bank.cms.dal.exceptions.NoLoanTypeFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LoanProfileRegistrarServlet")
public class LoanProfileRegistrarServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LoanProfileRegistrarServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<LoanTypeEntity> loanTypeEntities = new DefaultLoanDao().retrieveLoanNamesAndIds();
            Map<Integer, String> loanIdToNameMap = new DataExtractor().extractLoanIdToNameMapFromLoanTypeEntities(loanTypeEntities);
            request.setAttribute("loan-idName-map", loanIdToNameMap);
            logger.info("loanIdToNameMap with size['" + loanIdToNameMap.size() + "'] has been set as request attribute in order to be sent to loanRequest.jsp page!");
        } catch (NoLoanTypeFoundException e) {
            logger.warn("No LoanType has been defined yet!");
            response.setStatus(CustomHttpStatusCode.NULL_LOAN_TYPE);
            response.getWriter().println("در حال حاضر هیچ نوع تسهیلاتی برای ارایه تعریف نشده است!");
            e.printStackTrace();
        }

        String nationalCode = request.getParameter("identity-number");
        if (nationalCode != null) {
            try {
                new CustomerValidator().validateNationalCode(nationalCode);
                BankCustomerEntity bankCustomerEntity = new DefaultBankCustomerDao().retrieveCustomerByExclusiveId(nationalCode);
                BankCustomerVo bankCustomerVo = new CustomerDataMapper().mapCustomerEntityToBankCustomerVO(bankCustomerEntity);
                request.setAttribute("bank-customer", bankCustomerVo);
                logger.info("bankCustomer has been set as request attribute in order to be sent to loanRequest.jsp page!");
            } catch (CustomerNotFoundException e) {
                response.setStatus(CustomHttpStatusCode.CUSTOMER_NOT_FOUND);
                request.setAttribute("server-message", "هیچ مشتری ای با کد ملی وارد شده یافت نشد!");
                logger.warn("National Code Not Found! [nationalCode: " + nationalCode + "]");
                e.printStackTrace();
            } catch (InvalidNationalCodeException e) {
                logger.warn("Invalid national code!");
                request.setAttribute("server-message", "خطا! \n کد ملی وارد شده صحیح نمی باشد!");
                response.setStatus(CustomHttpStatusCode.INVALID_NATIONAL_CODE);
                e.printStackTrace();
            } catch (NullValueException e) {
                logger.error("national code is null!");
                request.setAttribute("server-message", "خطا! \n لطفا کد ملی مشتری را وارد کنید!");
                response.setStatus(CustomHttpStatusCode.NULL_VALUE);
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("presentation/loanRequest.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loanTypeId = request.getParameter("loan-id");
        String contractDuration = request.getParameter("contract-duration");
        String contractAmount = request.getParameter("contract-amount");
        String customerId = request.getParameter("customer-id");

        try {
            new LoanValidator().validateLoanRequestProfileInfo(customerId, loanTypeId, contractAmount, contractDuration);
            LoanTypeEntity loanTypeEntity = new DefaultLoanDao().retrieveLoanTypeIfContractConditionIsAcceptable(Integer.parseInt(loanTypeId), new BigDecimal(contractAmount), Integer.parseInt(contractDuration));
            new DefaultBankCustomerDao().updateCustomerLoans(customerId, loanTypeEntity);
            response.getWriter().println("درخواست تسهیلات با موفقیت ثبت شد!");
            logger.info("loan Request has been registered successfully! [customerId: " + customerId + ", LoanName: " + loanTypeEntity.getName() + "]");
        } catch (NoLoanTypeFoundException e) {
            logger.warn("Requested contract does not match with any of loan conditions! [contractDuration: " + contractDuration + ", contractAmount: " + contractAmount + ", loanId: " + loanTypeId + "]");
            response.setStatus(CustomHttpStatusCode.NULL_LOAN_TYPE);
            response.getWriter().println("خطا! \n شرایط قرارداد مورد نظر مشتری با هیچ یک از شروط اعطای تسهیلات انتخابی مطابقت ندارد!");
            e.printStackTrace();
        } catch (IllegalValueTypeException e) {
            logger.warn("Illegal input for numerical fields has been entered!");
            response.setStatus(CustomHttpStatusCode.ILLEGAL_VALUE_TYPE);
            response.getWriter().println("خطا! \n مقادیر عددی وارد شده صحیح نیستند!");
            e.printStackTrace();
        } catch (NullValueException e) {
            logger.error("Client has sent null value!");
            response.getWriter().println("خطا! \n پر کردن فیلدهای کد ملی، مبلغ قرارداد و مدت قرار داد اجباری است!");
            response.setStatus(CustomHttpStatusCode.NULL_VALUE);
            e.printStackTrace();
        } catch (NoResultFoundException e) {
            response.getWriter().println("در حال حاضر مشتری انتخابی در حال بهرمندی از این نوع تسهیلات است و امکان درخواست دوباره آن وجود ندارد!");
            response.setStatus(CustomHttpStatusCode.DUPLICATED_LOAN_REQUEST);
            logger.error("Client has already received the loan!");
            e.printStackTrace();
        }
    }
}

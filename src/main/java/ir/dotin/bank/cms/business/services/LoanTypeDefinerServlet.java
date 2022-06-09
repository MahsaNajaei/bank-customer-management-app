package ir.dotin.bank.cms.business.services;

import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.business.dataobjects.values.loans.GrantConditionVo;
import ir.dotin.bank.cms.business.dataobjects.values.loans.LoanTypeVo;
import ir.dotin.bank.cms.business.exceptions.*;
import ir.dotin.bank.cms.business.tools.CustomHttpStatusCode;
import ir.dotin.bank.cms.business.tools.DataExtractor;
import ir.dotin.bank.cms.business.tools.LoanDataMapper;
import ir.dotin.bank.cms.business.validators.LoanValidator;
import ir.dotin.bank.cms.dal.daos.implementations.hibernate.DefaultLoanDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "loanTypeDefinerServlet")
public class LoanTypeDefinerServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LoanTypeDefinerServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DataExtractor dataExtractor = new DataExtractor();
        String redirectAddress = "presentation/loanConditionsDefinition.jsp";
        if (request.getParameter("loan-name") != null) {
            LoanTypeVo loanType = dataExtractor.extractLoanTypeVoFromRequestParams(request);
            request.getSession().setAttribute("loan-type", loanType);
            logger.info("client has defined new loan type with name['" + loanType.getName() + "']!");
        } else if (request.getParameter("condition-name") != null) {
            GrantConditionVo grantCondition = dataExtractor.extractGrantConditionVoFromRequestParams(request);
            LoanTypeVo loanTypeVo = (LoanTypeVo) request.getSession().getAttribute("loan-type");
            loanTypeVo.getGrantConditions().add(grantCondition);
            redirectAddress += "#" + request.getParameter("display-Section-id");
            logger.info("client is defining new loan conditions! conditionName['" + loanTypeVo.getName() + "']");
        }
        response.sendRedirect(redirectAddress);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LoanTypeVo loanTypeVo = (LoanTypeVo) request.getSession().getAttribute("loan-type");
        try {
            new LoanValidator().validateLoanType(loanTypeVo);
            LoanTypeEntity loanTypeEntity = new LoanDataMapper().mapLoanTypeVoToLoanTypeEntity(loanTypeVo);
            new DefaultLoanDao().add(loanTypeEntity);
            request.getSession().removeAttribute("loan-type");
            response.getWriter().println("نوع تسهیلات جدید با موفقیت ثبت شد!");
            logger.info("LoanType " + loanTypeEntity.getName() + "is registered successfully!");
        } catch (NullLoanTypeException e) {
            logger.error("Null LoanType in requestParams!");
            response.setStatus(CustomHttpStatusCode.NULL_LOAN_TYPE);
            response.getWriter().println("خطا! \n نوع تسهیلات تعریف نشده است!");
            e.printStackTrace();
        } catch (GrantConditionNotExistsException e) {
            response.setStatus(CustomHttpStatusCode.GRANT_CONDITION_NOT_EXISTS);
            response.getWriter().println("خطا! \n برای تعریف تسهیلات جدید باید حداقل یک شرط اعطاء تعیین کنید!");
            logger.error("No grant condition is defined for loan type!");
            e.printStackTrace();
        } catch (IllegalValueTypeException e) {
            response.setStatus(CustomHttpStatusCode.ILLEGAL_VALUE_TYPE);
            logger.error("Illegal value is found for loan conditions numeric fields!");
            response.getWriter().println("خطا! \n مقدار وارد شده برای فیلدهای عددی باید یک عدد غیر منفی باشد!");
            e.printStackTrace();
        } catch (IllegalNumberOrderException e) {
            logger.warn("min entered value is greater than max value!");
            response.setStatus(CustomHttpStatusCode.ILLEGAL_NUMBER_ORDER);
            response.getWriter().println("خطا! \n حداقل مقدار وارد شده نباید از حداکثر مقدار وارد شده بیشتر باشد!");
            e.printStackTrace();
        } catch (NullValueException e) {
            logger.error("loanType contains null values!");
            response.setStatus(CustomHttpStatusCode.NULL_VALUE);
            response.getWriter().println("وارد کردن تمامی فیلدهای مربوط به شرط اعطا ضروری است!");
            e.printStackTrace();
        }
    }
}

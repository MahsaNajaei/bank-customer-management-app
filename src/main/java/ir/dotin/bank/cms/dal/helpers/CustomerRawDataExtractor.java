package ir.dotin.bank.cms.dal.helpers;

import ir.dotin.bank.cms.business.dto.BankCustomer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomerRawDataExtractor {

    public synchronized static String extractAssignmentList(BankCustomer bankCustomer) {
        String assignmentList = "";
        String separator = "";
        for (Method method : bankCustomer.getClass().getMethods()) {
            try {
                if (method.getName().contains("get") && !method.getName().equalsIgnoreCase("getClass")) {
                    Object methodResult = method.invoke(bankCustomer);
                    if (methodResult != null) {
                        assignmentList += (separator + method.getName().substring(3) + " = '" + methodResult + "'");
                        separator = ", ";
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return assignmentList;
    }

}

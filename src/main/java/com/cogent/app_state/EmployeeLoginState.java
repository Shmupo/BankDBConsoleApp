package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_employee.EmployeeAccountDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import jakarta.persistence.NoResultException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Transaction;

import java.util.ArrayList;

public class EmployeeLoginState implements State{
    @Override
    public void execute() {
        EmployeeAccountDaoImp account = null;
        while (true) {
            try {
                Bank.session = Bank.sessionFactory.openSession();
                System.out.println("LOGGING INTO EMPLOYEE");
                System.out.println("ENTER 'EXIT' TO RETURN TO MAIN MENU");
                System.out.print("Enter username: ");
                String username = Bank.scanner.next();
                if (username.equals("EXIT")) {
                    System.out.println("Returning to MAIN MENU.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.changeState(new MainMenuState());
                    return;
                }
                System.out.print("Enter password: ");
                String password = Bank.scanner.next();
                if (password.equals("EXIT")) {
                    System.out.println("Returning to MAIN MENU.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.changeState(new MainMenuState());
                    return;
                }

                String sql = "FROM EmployeeAccountDaoImp C WHERE C.user = :username AND C.pass = :password";
                account = (EmployeeAccountDaoImp) Bank.session.createQuery(sql).setParameter("username", username).setParameter("password", password).uniqueResult();

                if (account == null) {
                    System.out.println("INVALID ACCOUNT CREDENTIALS TRY AGAIN.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                } else {
                    System.out.println("LOGIN AUTHORIZED");
                    System.out.println("WELCOME EMPLOYEE " + username + ".");
                    EmployeeLoggedInState employeeLoggedInState = new EmployeeLoggedInState(account);
                    Bank.changeState(employeeLoggedInState); // this should change the state and leave this for garbage collection
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    return;
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                Bank.session.close();
            }
        }
    }
}

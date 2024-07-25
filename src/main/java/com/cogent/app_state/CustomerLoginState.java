package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import org.hibernate.Transaction;

public class CustomerLoginState implements State{
    @Override
    public void execute() {
        CustomerAccountDaoImp account = null;
        while (true) {
            try {
                Bank.session = Bank.sessionFactory.openSession();
                Transaction transaction = Bank.session.beginTransaction(); // Begin transaction

                System.out.println("LOGGING INTO CUSTOMER");
                System.out.println("ENTER 'EXIT' TO RETURN TO MAIN MENU");
                System.out.print("Enter username: ");
                String username = Bank.scanner.next();
                if (username.equals("EXIT")) {
                    System.out.println("Returning to MAIN MENU.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.session.close(); // Close session before changing state
                    Bank.changeState(new MainMenuState());
                    return;
                }

                System.out.print("Enter password: ");
                String password = Bank.scanner.next();
                if (password.equals("EXIT")) {
                    System.out.println("Returning to MAIN MENU.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    Bank.session.close(); // Close session before changing state
                    Bank.changeState(new MainMenuState());
                    return;
                }

                String sql = "FROM CustomerAccountDaoImp C WHERE C.user = :username AND C.pass = :password";
                account = (CustomerAccountDaoImp) Bank.session.createQuery(sql)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .uniqueResult();

                if (account == null) {
                    System.out.println("INVALID ACCOUNT CREDENTIALS TRY AGAIN.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                } else {
                    System.out.println("LOGIN AUTHORIZED");
                    System.out.println("WELCOME " + username + ".");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    CustomerLoggedInState customerLoggedInState = new CustomerLoggedInState(account);
                    Bank.session.close();
                    Bank.changeState(customerLoggedInState);
                    return;
                }

                transaction.commit();

            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                if (Bank.session != null && Bank.session.isOpen()) {
                    Bank.session.close();
                }
            }
        }
    }
}

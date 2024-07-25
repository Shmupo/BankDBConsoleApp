package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.id.uuid.StandardRandomStrategy;

public class CustomerAccountCreationState implements State {
    @Override
    public void execute() {
        while (true) {
            System.out.println("ENTER 'EXIT' ANYTIME TO CANCEL ACCOUNT CREATION");
            System.out.print("ENTER USERNAME FOR NEW ACCOUNT: ");
            String user = Bank.scanner.next();

            if (user.equals("EXIT")) {
                Bank.changeState(new MainMenuState());
                System.out.println("RETURNING TO MAIN MENU");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                return;
            }

            System.out.print("ENTER PASSWORD FOR NEW ACCOUNT: ");
            String pass = Bank.scanner.next();

            if (pass.equals("EXIT")) {
                Bank.changeState(new MainMenuState());
                System.out.println("RETURNING TO MAIN MENU");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                return;
            }

            CustomerAccountDaoImp account = new CustomerAccountDaoImp();
            account.setUser(user);
            account.setPass(pass);

            Transaction transaction = null;

            try {
                Bank.session = Bank.sessionFactory.openSession();
                transaction = Bank.session.beginTransaction();

                Bank.session.save(account);

                transaction.commit();
                Bank.session.close();

                System.out.println("ACCOUNT SUCCESSFULLY CREATED");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                Bank.changeState(new MainMenuState());
                return;
            } catch (ConstraintViolationException e) {
                System.out.println("INVALID USERNAME AND PASSWORD, TRY AGAIN");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                System.out.println(e);
                if (transaction != null) transaction.rollback();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                System.out.println(e);
            } finally {
                if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
            }

        }
    }
}

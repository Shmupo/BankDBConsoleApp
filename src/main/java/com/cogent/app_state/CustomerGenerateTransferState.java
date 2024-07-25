package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_transfer.BalanceTransferDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import org.hibernate.Transaction;

public class CustomerGenerateTransferState implements State {
    CustomerAccountDaoImp account;

    public CustomerGenerateTransferState(CustomerAccountDaoImp account) {
        this.account = account;
    }

    @Override
    public void execute() {
        System.out.println("GENERATE ACCOUNT BALANCE TRANSFER REQUEST");
        while (true) {
            System.out.print("ENTER CHECKING ACCOUNT NUMBER ID TO TRANSFER TO: ");
            int accountNumber = Bank.scanner.nextInt();

            if (validAccountNumber(accountNumber)) {
                System.out.println("ENTER A BALANCE TO SEND TO AN ACCOUNT.");
                System.out.println("BALANCE TRANSFERS ARE LIMITED TO $ 50,000");
                System.out.print("ENTER BALANCE TO TRANSFER: ");
                int balance = Bank.scanner.nextInt();

                if (balance <= 50000 && balance > 0) {
                    generateTransfer(accountNumber, balance);
                    Bank.changeState(new CustomerLoggedInState(account));
                    System.out.println("RETURNING TO CUSTOMER SELECTION");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    return;
                } else {
                    System.out.println("INVALID BALANCE. TRY AGAIN.");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                }
            } else {
                System.out.println("INVALID OPERATION, TRY AGAIN.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
            }
        }
    }

    private boolean validAccountNumber(int accountNumber) {
        Transaction transaction = null;
        boolean isValid = true;

        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            CheckingAccountDaoImp toCheckingAccount = (CheckingAccountDaoImp) Bank.session.get(CheckingAccountDaoImp.class, accountNumber);

            if (toCheckingAccount == null) {
                System.out.println("ERROR: ACCOUNT WITH GIVEN ID DOES NOT EXIST.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                isValid = false;
            } else if (toCheckingAccount.getCustomerId() == account.getId()) {
                System.out.println("ERROR: CANNOT TRANSFER TO THE SAME CUSTOMER ACCOUNT.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                isValid = false;
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e);
        } finally {
            if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
        }
        return isValid;
    }


    private void generateTransfer(int toId, int balance) {
        Transaction transaction = null;
        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            BalanceTransferDaoImp newTransfer = new BalanceTransferDaoImp();
            newTransfer.setFromAccountNumber(account.getId());
            newTransfer.setToAccountNumber(toId);
            newTransfer.setBalance(balance);

            Bank.session.save(newTransfer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e);
        } finally {
            if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
        }
    }

}

package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_transfer.BalanceTransferDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;

public class CustomerSelectedCheckingAccountState implements State{
    static CustomerAccountDaoImp customerAccount;
    static CheckingAccountDaoImp checkingAccount;

    public CustomerSelectedCheckingAccountState(CustomerAccountDaoImp customerAccount, CheckingAccountDaoImp checkingAccount) {
        this.customerAccount = customerAccount;
        this.checkingAccount = checkingAccount;
    }

    @Override
    public void execute() {
        while (true) {
            int selection = optionSelection();

            switch (selection) {
                case 1: // view balance
                    printBalance();
                    break;
                case 2: // deposit
                    promptDeposit();
                    break;
                case 3: // withdraw
                    promptWithdraw();
                    break;
                case 4:
                    promptTransfer();
                    break;
                case -1:
                    Bank.changeState(new CustomerViewCheckingAccountState(customerAccount));
                    System.out.println("RETURNING TO SELECTION");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    return;
                default:
                    System.out.println("INVALID SELECTION, TRY AGAIN");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
                    break;
            }
        }
    }

    private static int optionSelection() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+    HI " + customerAccount.getUser());
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                  SELECT A CHECKING ACCOUNT BELOW TO BEGIN                     +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    PRESS 1: VIEW BALANCE, INFO                                                +");
        System.out.println("+    PRESS 2: DEPOSIT                                                           +");
        System.out.println("+    PRESS 3: WITHDRAW                                                          +");
        System.out.println("+    PRESS 4: REQUEST TRANSFER                                                  +");
        System.out.println("+    ENTER -1 : BACK                                                            +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection = Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private static void printBalance() {
        System.out.println("ACCOUNT BALANCE: ");
        System.out.println(checkingAccount);
    }

    private static void promptDeposit() {
        System.out.print("Enter the amount to deposit: ");
        int depositAmt = Bank.scanner.nextInt();

        if (!Bank.depositIntoAccount(checkingAccount, depositAmt)) {
            System.out.println("DEPOSIT FAILED.");
            System.out.println("Try again.");
        }
    }

    private static void promptWithdraw() {
        System.out.print("Enter the amount to withdraw: ");
        int withdrawAmt = Bank.scanner.nextInt();

        if (!Bank.withdrawFromAccount(checkingAccount, withdrawAmt)) {
            System.out.println("WITHDRAWAL FAILED.");
            System.out.println("Try again.");
        }
    }

    private static void promptTransfer() {
        System.out.println("GENERATE ACCOUNT BALANCE TRANSFER REQUEST");
        System.out.print("Enter the checking account number to transfer to: ");
        int accountNumber = Bank.scanner.nextInt();

        if (validAccountNumber(accountNumber)) {
            System.out.println("ENTER A NEGATIVE BALANCE TO REQUEST FUNDS TO YOUR ACCOUNT, ENTER POSITIVE BALANCE TO REQUEST TO SEND FUNDS TO THE DESIGNATED ACCOUNT.");
            System.out.println("BALANCE TRANSFERS ARE LIMITED TO $50,000");
            System.out.print("Enter balance to transfer: ");
            int balance = Bank.scanner.nextInt();

            if (balance <= 50000 && balance >= -50000) {
                generateTransfer(accountNumber, balance);
                System.out.println("Transfer request generated.");
            } else {
                System.out.println("INVALID BALANCE. TRY AGAIN.");
            }
        }
    }

    // verify checking account number
    private static boolean validAccountNumber(int accountNumber) {
        Transaction transaction = null;
        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            CheckingAccountDaoImp toCheckingAccount = Bank.session.get(CheckingAccountDaoImp.class, accountNumber);

            if (toCheckingAccount == null) {
                System.out.println("ERROR: ACCOUNT WITH GIVEN ID DOES NOT EXIST.");
                return false;
            } else if (accountNumber == checkingAccount.getAccountNumber()) { // same account number
                System.out.println("ERROR: CANNOT TRANSFER TO THE SAME CUSTOMER ACCOUNT.");
                return false;
            }

            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e);
            return false;
        } finally {
            transaction.commit();
            if (Bank.session != null && Bank.session.isOpen()) Bank.session.close();
        }
    }

    private static void generateTransfer(int toId, int balance) {
        Transaction transaction = null;

        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            BalanceTransferDaoImp newTransfer = new BalanceTransferDaoImp();
            newTransfer.setFromAccountNumber(checkingAccount.getAccountNumber());
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

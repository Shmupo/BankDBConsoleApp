package com.cogent;

import com.cogent.app_state.State;
import com.cogent.app_state.StateMachine;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_transfer.BalanceTransferDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Bank {
    private static Configuration configuration;
    public static SessionFactory sessionFactory;
    public static Session session;
    public static Scanner scanner;
    private static StateMachine consoleStateMachine = null;
    private static boolean running = false;

    static final Level BANKEVENT = Level.forName("BANKEVENT", 50);
    static final Level TRANSACTIONEVENT = Level.forName("TRANSACTION", 1);
    public static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        setup();

        logEvent("starting application");
        logTransaction("starting application");

        while (running) {
            //System.out.println("DEBUG ONLY - CURRENT STATE : " + consoleStateMachine.currentState);
            System.out.println();
            System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
            System.out.println();
            consoleStateMachine.update();
        }

        session.close(); // just in case
        System.out.println("CLOSING CONSOLE APPLICATION...");
    }

    public static void setup() {
        configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        scanner = new Scanner(System.in);
        running = true;
        consoleStateMachine = new StateMachine();
    }

    public static void stop() {
        running = false;
        logEvent("user close application");
        logTransaction("user close application");
    }

    public static void changeState(State newState) {
        consoleStateMachine.setState(newState);
    }

    // log custom events to events.log
    public static void logEvent(String message) {
        logger.log(BANKEVENT, message);
    }

    // log custom events to events.log
    public static void logTransaction(String message) {
        logger.log(TRANSACTIONEVENT, message);
    }

    public static boolean withdrawFromAccount(CheckingAccountDaoImp account, int amount) {
        Transaction transaction = null;
        Bank.logTransaction("Request withdrawal of " + amount + " from checking account " + account);
        if (amount > account.getBalance()) {
            Bank.logTransaction("Error during deposit: INSUFFICIENT FUNDS");
            System.out.println("ERROR: INSUFFICIENT FUNDS");
            return false;
        } else if (amount <= 0) {
            Bank.logTransaction("Error during withdrawal: NEGATIVE AMOUNT OR AMOUNT IS 0");
            System.out.println("ERROR: AMOUNT MUST BE NON-NEGATIVE OR NOT 0");
            return false;
        }
        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = session.beginTransaction();
            account.setBalance(account.getBalance() - amount);
            session.update(account);
            transaction.commit();
            Bank.logTransaction("Withdraw successful: Checking Account Number " + account.getAccountNumber() + " withdrew " + account + " dollars.");
            Bank.logTransaction("Checking account " + account.getAccountNumber() + " has a balance of  " + account.getBalance() + " dollars.");
            System.out.println("UPDATED ACCOUNT : " + account);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            Bank.logTransaction("Error during withdrawal: " + e.getMessage());
            Bank.logTransaction("Withdrawal cancelled");
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static boolean depositIntoAccount(CheckingAccountDaoImp account, int amount) {
        Transaction transaction = null;
        Bank.logTransaction("Request deposit of " + amount + " from checking account " + account);
        if (amount <= 0) {
            Bank.logTransaction("Error during withdrawal: NEGATIVE AMOUNT OR AMOUNT IS 0");
            System.out.println("ERROR: AMOUNT MUST BE NON-NEGATIVE OR NOT 0");
            return false;
        }
        try {
            Bank.session = Bank.sessionFactory.openSession();
            transaction = session.beginTransaction();
            account.setBalance(account.getBalance() + amount);
            session.update(account);
            transaction.commit();
            Bank.logTransaction("Deposit successful: Checking Account Number " + account.getAccountNumber() + " received " + account + " dollars.");
            Bank.logTransaction("Checking account " + account.getAccountNumber() + " has a balance of  " + account.getBalance() + " dollars.");
            System.out.println("UPDATED ACCOUNT : " + account);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            Bank.logTransaction("Error during deposit: " + e.getMessage());
            Bank.logTransaction("Deposit cancelled");
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static boolean performBalanceTransfer(CustomerAccountDaoImp account, BalanceTransferDaoImp balanceTransfer) {
        Bank.logTransaction("Customer " + account + " ACCEPTED balance transfer " + balanceTransfer);
        logTransaction("Beginning balance transaction for " + balanceTransfer);
        Transaction transaction = null;
        try {
            session = Bank.sessionFactory.openSession();
            transaction = Bank.session.beginTransaction();

            if (balanceTransfer.getBalance() <= 0) { // balance is negative or 0
                System.out.println("ERROR: cannot transfer balance less than or equal to 0.");
                logTransaction("ERROR: cannot transfer balance less than or equal to 0.");
                logTransaction("Setting balance transfer automatically to REJECTED");

                session = Bank.sessionFactory.openSession();

                balanceTransfer.setStatus("REJECTED"); // set invalid balances to rejected, although the user should not be able to do this

                session.update(balanceTransfer);
                transaction.commit();
                session.close();
                return false;
            }

            if (balanceTransfer.getStatus().equals("REJECTED") || balanceTransfer.getStatus().equals("ACCEPTED")) {
                System.out.println("ERROR: cannot accept a balance transfer that is not PENDING.");
                logTransaction("ERROR: cannot accept a balance transfer that is not PENDING.");
                transaction.rollback();
                session.close();
                return false;
            }

            int toAccountId = balanceTransfer.getToAccountNumber();
            int frAccountId = balanceTransfer.getFromAccountNumber();
            int balance = balanceTransfer.getBalance();

            CheckingAccountDaoImp toAccount = Bank.session.get(CheckingAccountDaoImp.class, toAccountId);
            CheckingAccountDaoImp frAccount = Bank.session.get(CheckingAccountDaoImp.class, frAccountId);

            if (frAccount.getBalance() < balance) { // make sure withdrawal is valid
                transaction.rollback();
                System.out.println("ERROR: from account has insufficient funds.");
                System.out.println("Transfer will remain PENDING until account has sufficient funds.");
                return false;
            }

            frAccount.setBalance(frAccount.getBalance() - balance); // withdraw
            session.update(frAccount);
            toAccount.setBalance(toAccount.getBalance() + balance); // deposit
            session.update(toAccount);

            balanceTransfer.setStatus("ACCEPTED");
            Bank.session.update(balanceTransfer);
            transaction.commit();
            System.out.println("TRANSFER ACCEPTED");

            Bank.logTransaction("Withdraw successful: Checking Account Number " + frAccount.getAccountNumber() + " withdrew " + frAccount + " dollars.");
            Bank.logTransaction("Checking account " + frAccount.getAccountNumber() + " has a balance of  " + frAccount.getBalance() + " dollars.");
            Bank.logTransaction("Deposit successful: Checking Account Number " + toAccount.getAccountNumber() + " received " + toAccount + " dollars.");
            Bank.logTransaction("Checking account " + toAccount.getAccountNumber() + " has a balance of  " + toAccount.getBalance() + " dollars.");

            logTransaction("Transaction complete for " + balanceTransfer);
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("ERROR: Transaction failed. Rolled back changes.");
            return false;
        } finally {
            if (Bank.session != null) {
                Bank.session.close();
            }
        }
    }
}

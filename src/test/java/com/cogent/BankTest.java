package com.cogent;

import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_transfer.BalanceTransferDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

class BankTest {
    private static Bank bank;
    private static Configuration configuration;
    public static SessionFactory sessionFactory;
    public static Session session;
    private static CheckingAccountDaoImp account;
    private static CheckingAccountDaoImp account2;

    @BeforeAll
    static void beforeAll() {
        configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        bank = new Bank();
        Bank.setup();
        account = new CheckingAccountDaoImp();
        account2 = new CheckingAccountDaoImp();
        session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(account);
        session.save(account2);
        transaction.commit();
        session.close();
    }

    @BeforeEach
    void beforeEach() {
        session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        account.setBalance(500000);
        account2.setBalance(0);
        session.update(account2);
        session.update(account);
        transaction.commit();
        session.close();
    }

    @AfterAll
    static void afterAll() {
        session = sessionFactory.openSession();
        session.delete(account);
        session.delete(account2);
        if (session.isOpen()) session.close();
    }

    @Test
    @DisplayName("Withdrawal")
    void withdrawFromAccount() {
        Bank.withdrawFromAccount(account, 20000); // positive balance
        assert account.getBalance() == 480000;

        Bank.withdrawFromAccount(account, 0); // 0 balance
        assert account.getBalance() == 480000;

        Bank.withdrawFromAccount(account, -100); // negative balance
        assert account.getBalance() == 480000;
    }

    @Test
    @DisplayName("Deposit")
    void depositIntoAccount() {
        Bank.depositIntoAccount(account, 10000); // positive balance
        assert account.getBalance() == 510000;

        Bank.depositIntoAccount(account, 0); // 0 balance
        assert account.getBalance() == 510000;

        Bank.depositIntoAccount(account, -1); // negative balance
        assert account.getBalance() == 510000;
    }

    @Test
    @DisplayName("BalanceTransfer")
    void performBalanceTransfer() {
        // dummy objects
        CustomerAccountDaoImp dummy = new CustomerAccountDaoImp();
        BalanceTransferDaoImp balanceTransfer = new BalanceTransferDaoImp();

        // CASE : NORMAL TRANSACTION
        System.out.println("TESTING VALID FUNDS TRANSFER");
        session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        balanceTransfer.setFromAccountNumber(account.getAccountNumber());
        balanceTransfer.setToAccountNumber(account2.getAccountNumber());
        balanceTransfer.setStatus("PENDING");
        account.setBalance(1000);
        account2.setBalance(0);
        balanceTransfer.setBalance(1000);
        session.update(account);
        session.update(account2);
        session.save(balanceTransfer);
        transaction.commit();
        session.close();

        // Verify initial balances
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 1000;
        assert account2.getBalance() == 0;

        // Perform balance transfer
        Bank.performBalanceTransfer(dummy, balanceTransfer);

        // Verify balances after transfer
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 0;
        assert account2.getBalance() == 1000;
        assert balanceTransfer.getStatus().equals("ACCEPTED");

        // DELETE DUMMY OBJECTS
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.delete(balanceTransfer);
        session.delete(dummy);
        transaction.commit();
        session.close();
        System.out.println("PASS VALID FUNDS TRANSFER");


        // CASE : INSUFFICIENT FUNDS
        System.out.println("TESTING INSUFFICIENT FUNDS TRANSFER");
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        balanceTransfer = new BalanceTransferDaoImp();
        balanceTransfer.setFromAccountNumber(account.getAccountNumber());
        balanceTransfer.setToAccountNumber(account2.getAccountNumber());
        account.setBalance(0);
        account2.setBalance(1000);
        balanceTransfer.setBalance(1000);
        session.update(account);
        session.update(account2);
        session.save(balanceTransfer);
        transaction.commit();
        session.close();

        // Verify initial balances
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 0;
        assert account2.getBalance() == 1000;

        // Perform balance transfer
        Bank.performBalanceTransfer(dummy, balanceTransfer);

        // Verify balances after transfer
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 0;
        assert account2.getBalance() == 1000;
        assert balanceTransfer.getStatus().equals("PENDING");

        // DELETE DUMMY OBJECTS
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.delete(balanceTransfer);
        session.delete(dummy);
        transaction.commit();
        session.close();
        System.out.println("PASS INSUFFICIENT FUNDS TRANSFER");


        // CASE : 0 BALANCE TRANSFER
        System.out.println("TESTING 0 BALANCE TRANSFER");
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        balanceTransfer.setFromAccountNumber(account.getAccountNumber());
        balanceTransfer.setToAccountNumber(account2.getAccountNumber());
        balanceTransfer = new BalanceTransferDaoImp();
        account.setBalance(0);
        account2.setBalance(1000);
        balanceTransfer.setBalance(0);
        session.update(account);
        session.update(account2);
        session.save(balanceTransfer);
        transaction.commit();
        session.close();

        // Verify initial balances
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 0;
        assert account2.getBalance() == 1000;

        // Perform balance transfer
        Bank.performBalanceTransfer(dummy, balanceTransfer);

        // Verify balances after transfer
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 0;
        assert account2.getBalance() == 1000;
        assert balanceTransfer.getStatus().equals("REJECTED");

        // DELETE DUMMY OBJECTS
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.delete(balanceTransfer);
        session.delete(dummy);
        transaction.commit();
        session.close();
        System.out.println("PASS 0 BALANCE TRANSFER");



        // CASE : NEGATIVE TRANSFER
        System.out.println("TESTING NEGATIVE BALANCE TRANSFER");
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        balanceTransfer.setFromAccountNumber(account.getAccountNumber());
        balanceTransfer.setToAccountNumber(account2.getAccountNumber());
        balanceTransfer = new BalanceTransferDaoImp();
        account.setBalance(0);
        account2.setBalance(1000);
        balanceTransfer.setBalance(-1000);
        session.update(account);
        session.update(account2);
        session.save(balanceTransfer);
        transaction.commit();
        session.close();

        // Verify initial balances
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 0;
        assert account2.getBalance() == 1000;

        // Perform balance transfer
        Bank.performBalanceTransfer(dummy, balanceTransfer);

        // Verify balances after transfer
        session = sessionFactory.openSession();
        account = session.get(CheckingAccountDaoImp.class, account.getAccountNumber());
        account2 = session.get(CheckingAccountDaoImp.class, account2.getAccountNumber());
        session.close();

        assert account.getBalance() == 0;
        assert account2.getBalance() == 1000;
        assert balanceTransfer.getStatus().equals("REJECTED");

        // DELETE DUMMY OBJECTS
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.delete(balanceTransfer);
        session.delete(dummy);
        transaction.commit();
        session.close();
        System.out.println("PASS NEGATIVE BALANCE TRANSFER");
    }
}
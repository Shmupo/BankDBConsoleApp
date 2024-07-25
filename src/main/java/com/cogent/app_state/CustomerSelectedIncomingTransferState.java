package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_customer.CustomerAccountDaoImp;
import com.cogent.bank_transfer.BalanceTransferDaoImp;
import com.cogent.checking_account.CheckingAccountDaoImp;
import org.hibernate.Transaction;

public class CustomerSelectedIncomingTransferState implements State {
    static CustomerAccountDaoImp account;
    static BalanceTransferDaoImp balanceTransfer;

    public CustomerSelectedIncomingTransferState(CustomerAccountDaoImp account, BalanceTransferDaoImp balanceTransfer) {
        CustomerSelectedIncomingTransferState.account = account;
        CustomerSelectedIncomingTransferState.balanceTransfer = balanceTransfer;
    }

    @Override
    public void execute() {
        while (true) {
            int selection = optionsPrompt();

            switch (selection) {
                case 1: // accept
                    acceptTransfer();
                    break;
                case 2: // reject
                    rejectTransfer();
                    break;
                case -1: // back
                    Bank.changeState(new CustomerViewTransfersState(account));
                    System.out.println("Returning to selection.");
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

    private static int optionsPrompt() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                       - SELECTED INCOMING TRANSFER -                          +");
        System.out.println(balanceTransfer);
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                       Select from the options below                           +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+    PRESS 1 : ACCEPT TRANSFER                                                  +");
        System.out.println("+    PRESS 2 : REJECT TRANSFER                                                  +");
        System.out.println("+    ENTER -1 : BACK                                                            +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        int selection = Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }

    private static void acceptTransfer() {
        if (balanceTransfer.getStatus().equals("REJECTED") || balanceTransfer.getStatus().equals("ACCEPTED")) {
            System.out.println("ERROR: cannot accept a non-pending transaction.");
            System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
            System.out.println();
        }

        if (Bank.performBalanceTransfer(account, balanceTransfer)) {
            System.out.println("Transaction successfully performed.");
            System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
            System.out.println();
            Bank.logTransaction("Customer " + account + " ACCEPTED balance transfer " + balanceTransfer);
        }
    }

    private static void rejectTransfer() {
        try {
            if (balanceTransfer.getStatus().equals("REJECTED") || balanceTransfer.getStatus().equals("ACCEPTED")) {
                System.out.println("ERROR: cannot reject a non-pending transaction.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                return;
            }

            Bank.session = Bank.sessionFactory.openSession();
            Transaction transaction = Bank.session.beginTransaction();

            balanceTransfer.setStatus("REJECTED");

            Bank.session.update(balanceTransfer);

            transaction.commit();
            System.out.println("TRANSFER SUCCESSFULLY REJECTED");
            System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
            System.out.println();
            Bank.logTransaction("Customer " + account + " REJECTED balance transfer " + balanceTransfer);
        } finally {
            Bank.session.close();
        }
    }
}

package com.cogent.app_state;

import com.cogent.Bank;
import com.cogent.bank_employee.EmployeeAccountDaoImp;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EmployeeSelectedTransactionLog implements State {
    static EmployeeAccountDaoImp account;
    static String transactionLogFilePath;

    public EmployeeSelectedTransactionLog(EmployeeAccountDaoImp account, String transactionLogFilePath) {
        EmployeeSelectedTransactionLog.account = account;
        EmployeeSelectedTransactionLog.transactionLogFilePath = transactionLogFilePath;
    }

    @Override
    public void execute() {
        optionSelection();
        while (true) {
            System.out.print("Select option: ");
            int selection = Bank.scanner.nextInt();
            System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
            System.out.println();

            if (selection == -1) {
                Bank.changeState(new EmployeeViewTransactionLogs(account));
                System.out.println("Returning to selection.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
                return;
            } else {
                System.out.println("INVALID SELECTION, TRY AGAIN.");
                System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                System.out.println();
            }
        }
    }

    private void optionSelection() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                              DISPLAYING FILE:                                 +");
        System.out.println("+    " + transactionLogFilePath);
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("_________________________________________________________________________________");
        displayFile();
        System.out.println("__________________________________END OF FILE____________________________________");
        System.out.println("+    ENTER -1 : RETURN                                                          +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    private static void displayFile() {
        File logFile = new File(transactionLogFilePath);

        try (Scanner fileReader = new Scanner(logFile)) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("ERROR: could not read file " + transactionLogFilePath);
            System.out.println(e);
        }
    }
}

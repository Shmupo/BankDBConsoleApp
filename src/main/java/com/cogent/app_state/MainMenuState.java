package com.cogent.app_state;

import com.cogent.Bank;

import java.util.Scanner;

public class MainMenuState implements State{
    @Override
    public void execute() {
        while(true) {
            int selection = mainMenuPrompt();

            switch (selection) {
                case 1: // customer login
                    Bank.changeState(new CustomerLoginState());
                    System.out.println("INITIATING CUSTOMER LOGIN");
                    return;
                case 2: // customer account creation
                    Bank.changeState(new CustomerAccountCreationState());
                    System.out.println("INITIATING ACCOUNT CREATION");
                    return;
                case 3: // employee login
                    Bank.changeState(new EmployeeLoginState());
                    return;
                case -1: // close application
                    Bank.stop();
                    return;
                default:
                    System.out.println("INVALID SELECTION, TRY AGAIN");
                    System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
                    System.out.println();
            }
        }
    }

    private static int mainMenuPrompt() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+                   WELCOME TO THE BANK TERMINAL APPLICATION                    +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+                                                                               +");
        System.out.println("+   Select from the options below:                                              +");
        System.out.println("+                                                                               +");
        System.out.println("+   PRESS 1 : CUSTOMER LOGIN                                                    +");
        System.out.println("+   PRESS 2 : CREATE NEW CUSTOMER ACCOUNT                                       +");
        System.out.println("+   PRESS 3 : EMPLOYEE LOGIN                                                    +");
        System.out.println("+   ENTER -1 : CLOSE APPLICATION                                                +");
        System.out.println("+                                                                               +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.print("Select option: ");
        Scanner scanner = new Scanner(System.in);
        int selection =  Bank.scanner.nextInt();
        System.out.println("+   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +   +");
        System.out.println();
        return selection;
    }
}

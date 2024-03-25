package org.example;

import database.DataBaseActions;
import database.DataBaseActionsImplementation;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DataBaseActions dataBaseActions = new DataBaseActionsImplementation();
        dataBaseActions.printActions();
        dataBaseActions.action();

    }
}
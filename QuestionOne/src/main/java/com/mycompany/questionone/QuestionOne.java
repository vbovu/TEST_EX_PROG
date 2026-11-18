package com.mycompany.questionone;

import java.util.Scanner;


/**
 * Console entry point for Question 1. Captures three months of mattress sales
 * across the three top-performing products into a 2D array, pairs them with the
 * 1D product-name array, and prints the summary report via PrintProduct.
 *
 * References:
 * [1] Oracle, "Arrays," The Java Tutorials. Accessed: Nov. 18, 2026. [Online].
 *     Available: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
 * [2] Oracle, "Scanner (Java Platform SE 8)," Java Platform SE 8 API Specification.
 *     Accessed: Nov. 18, 2026. [Online].
 *     Available: https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
 * [3] Oracle, "The try-with-resources Statement," The Java Tutorials. Accessed: Nov. 18, 2026.
 *     [Online]. Available: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
 */
public class QuestionOne {

    public static void main(String[] args) {
        // API/documentation reference [2]: Scanner (Java Platform SE 8): https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
        // API/documentation reference [3]: The try-with-resources Statement:  https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        // Here I will use one shared Scanner on System.in owned by main and closed only when the application exits.
        try (Scanner scanner = new Scanner(System.in)) {
            runApplication(scanner);
        }
    }

    //Start of Application Flow section
    public static void runApplication(Scanner scanner) {
        //1D array of the company's top three performing mattress products.
        String[] productNames = Product.DEFAULT_PRODUCT_NAMES;

        //2D array where rows represent products (0..2) and columns represent months (0..2).
        int[][] productSales = captureSales(scanner, productNames, Product.DEFAULT_MONTH_COUNT);

        Product product = new Product(productNames, productSales);
        PrintProduct reportPrinter = new PrintProduct(product);
        reportPrinter.printReport();
    }
    //End of Application Flow section

    //Start of Console Input Helpers section
    // API/documentation reference [1]: Arrays: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
    private static int[][] captureSales(Scanner scanner, String[] productNames, int monthCount) {
        int[][] sales = new int[productNames.length][monthCount];

        for (int row = 0; row < productNames.length; row++) {
            for (int month = 0; month < monthCount; month++) {
                String prompt = "Enter number of sales for month " + (month + 1)
                        + " for " + productNames[row] + " : ";
                sales[row][month] = readNonNegativeInt(scanner, prompt);
            }

            //Print a blank line between products to match the sample console layout.
            if (row < productNames.length - 1) {
                System.out.println();
            }
        }

        return sales;
    }

    //This is so that for the purpose of Read a full line and parse it with Integer.parseInt so invalid text or leftover
    //newlines from nextInt() can never corrupt subsequent prompts as noted from my research. 
    private static int readNonNegativeInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(line);

                if (value >= 0) {
                    return value;
                }

                System.out.println("Sales cannot be negative. Please enter 0 or more.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number '" + line + "'. Please enter a whole number.");
            }
        }
    }
    //End of Console Input Helpers section
}

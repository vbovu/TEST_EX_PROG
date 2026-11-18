package com.mycompany.questionone;

/**
 * Responsible for printing the formatted product sales report required by Question 1.3.
 *
 * Extends Product so subclasses/callers can pass the 1D and 2D arrays directly to
 * super(...), while also supporting a constructor that accepts an existing IProduct
 * reference. Keeping console output in PrintProduct leaves Product free of I/O side
 * effects for unit testing.
 *
 * References:
 * [1] Oracle, "Overriding and Hiding Methods," The Java Tutorials. Accessed: Nov. 18, 2026.
 *     [Online]. Available: https://docs.oracle.com/javase/tutorial/java/IandI/override.html
 */
public class PrintProduct extends Product {

    public static final String REPORT_TITLE = "PRODUCT SALES REPORT - 2026";
    public static final int DIVIDER_LENGTH = 45;

    //Exactly 45 asterisks as required by Question 1.1.
    public static final String REPORT_DIVIDER =
            "*********************************************"; //I counted the literal * on the question paper

    private final IProduct targetProduct;

    //Start of Constructor section
    // API/documentation reference [1]: Overriding and Hiding Methods: https://docs.oracle.com/javase/tutorial/java/IandI/override.html
    public PrintProduct(String[] productNames, int[][] productSales) {
        //Delegate validation and array copying to Product via super(...).
        super(productNames, productSales);
        this.targetProduct = this;
    }

    public PrintProduct(int[][] productSales) {
        super(productSales);
        this.targetProduct = this;
    }

    public PrintProduct(IProduct product) {
        super(DEFAULT_PRODUCT_NAMES, new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        if (product == null) {
            throw new IllegalArgumentException("A Product instance is required to print the report.");
        }
        this.targetProduct = product;
    }
    //End of Constructor section

    //Start of Report Printing section
    //Distinct methods required by Question 1.3 to print total, average, and most popular sales.
    public void printTotalProductSales() {
        System.out.println("TOTAL PRODUCT SALES: " + targetProduct.GetTotalProductsSold());
    }

    public void printAverageProductSales() {
        //Round to the nearest whole number for display so 17.0 prints as 17, matching the sample report.
        long roundedAverage = Math.round(targetProduct.GetAverageProductSold());
        System.out.println("AVERAGE PRODUCT SALES: " + roundedAverage);
    }

    public void printMostPopularProduct() {
        System.out.println("MOST POPULAR PRODUCT: " + targetProduct.GetMostPopularProduct());
    }

    public void printReport() {
        System.out.println();
        System.out.println(REPORT_TITLE);
        System.out.println();
        System.out.println(REPORT_DIVIDER);
        System.out.println();
        printTotalProductSales();
        printAverageProductSales();
        printMostPopularProduct();
        System.out.println(REPORT_DIVIDER);
    }
    //End of Report Printing section

    //Start of Report Formatting Helper section
    //Builds the exact report text as a String so tests can verify formatting without capturing System.out.
    public String buildReport() {
        long roundedAverage = Math.round(targetProduct.GetAverageProductSold());
        StringBuilder report = new StringBuilder();

        report.append(REPORT_TITLE).append("\n\n");
        report.append(REPORT_DIVIDER).append("\n\n");
        report.append("TOTAL PRODUCT SALES: ").append(targetProduct.GetTotalProductsSold()).append("\n");
        report.append("AVERAGE PRODUCT SALES: ").append(roundedAverage).append("\n");
        report.append("MOST POPULAR PRODUCT: ").append(targetProduct.GetMostPopularProduct()).append("\n");
        report.append(REPORT_DIVIDER);

        return report.toString();
    }
    //End of Report Formatting Helper section
}

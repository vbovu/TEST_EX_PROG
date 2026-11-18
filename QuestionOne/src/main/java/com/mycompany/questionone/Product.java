package com.mycompany.questionone;

import java.util.Arrays;

/**
 * Stores the 1D product-name array, the 2D monthly sales array, and the derived
 * 1D per-product totals array for the three top-performing mattresses.
 *
 * Precomputing the 1D productTotals array from the 2D productSales grid during
 * construction avoids repeating the nested row-sum loop whenever total or
 * most-popular queries run, and guarantees that the most popular product is
 * chosen by its 3-month row sum (e.g. 25 + 18 + 20 = 63) rather than a single
 * monthly cell (25).
 *
 * References:
 * [1] Oracle, "Arrays," The Java Tutorials. Accessed: Nov. 18, 2026. [Online].
 *     Available: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
 * [2] Oracle, "Arrays (Java Platform SE 8)," Java Platform SE 8 API Specification.
 *     Accessed: Nov. 18, 2026. [Online].
 *     Available: https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html
 * [3] Oracle, "Overriding and Hiding Methods," The Java Tutorials. Accessed: Nov. 18, 2026.
 *     [Online]. Available: https://docs.oracle.com/javase/tutorial/java/IandI/override.html
 */
public class Product implements IProduct {

    public static final int DEFAULT_MONTH_COUNT = 3;

    //1D array holding the three mattress names in row order (0, 1, 2).
    public static final String[] DEFAULT_PRODUCT_NAMES = {
        "Cozy Nights Monarch mattress",
        "Restonic Barbados mattress",
        "Sealy Phuket mattress"
    };

    private final String[] productNames;
    private final int[][] productSales;
    private final int[] productTotals;
    private final int totalEntries;

    //Start of Constructor section
    //Convenience constructor for unit tests or callers that supply only the 2D sales grid.
    public Product(int[][] productSales) {
        this(DEFAULT_PRODUCT_NAMES, productSales);
    }

    public Product(String[] productNames, int[][] productSales) {
        //Validate both the 1D names array and 2D sales grid before storing any state,
        //so a partially constructed Product can never exist.
        String[] preparedNames = validateAndCopyNames(productNames);
        int[][] preparedSales = validateAndCopySales(productSales, preparedNames.length);

        this.productNames = preparedNames;
        this.productSales = preparedSales;

        //Build the 1D per-product totals array (e.g. [63, 37, 53]) once from the 2D grid.
        this.productTotals = computeProductTotals(preparedSales);
        this.totalEntries = countSalesEntries(preparedSales);
    }
    //End of Constructor section

    //Start of IProduct Implementation section
    // API/documentation reference [3]: Overriding and Hiding Methods — https://docs.oracle.com/javase/tutorial/java/IandI/override.html
    // The assessment requires these exact PascalCase method names from IProduct.
    @Override
    public int GetTotalProductsSold() {
        int grandTotal = 0;

        //Sum the precomputed 1D productTotals array ([63, 37, 53] -> 153) in O(P) time
        //instead of re-scanning every cell of the 2D array on each call.
        for (int productTotal : productTotals) {
            grandTotal += productTotal;
        }

        return grandTotal;
    }

    @Override
    public double GetAverageProductSold() {
        if (totalEntries == 0) {
            return 0.0;
        }

        //Cast to double before division so integer division does not truncate the fractional part.
        //153 total units across 9 monthly entries (3 products x 3 months) = 17.0.
        return (double) GetTotalProductsSold() / totalEntries;
    }

    @Override
    public String GetMostPopularProduct() {
        int bestIndex = 0;
        int highestTotal = productTotals[0];

        //Compare each product's 3-month total in the 1D productTotals array ([63, 37, 53]).
        //Using the row total (63) rather than an individual month cell (25) ensures the
        //winner is the product with the highest cumulative sales across all three months.
        //Strict '>' keeps the earliest product on a tie so the result is deterministic.
        for (int row = 1; row < productTotals.length; row++) {
            if (productTotals[row] > highestTotal) {
                highestTotal = productTotals[row];
                bestIndex = row;
            }
        }

        return productNames[bestIndex] + " (" + highestTotal + ")";
    }
    //End of IProduct Implementation section

    //Start of Array Calculation and Snapshot Helpers section
    // API/documentation reference [1]: Arrays — https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
    private static int[] computeProductTotals(int[][] salesGrid) {
        int[] rowTotals = new int[salesGrid.length];

        //Outer loop walks each product row; inner loop sums that product's monthly figures.
        for (int row = 0; row < salesGrid.length; row++) {
            int rowSum = 0;

            for (int month = 0; month < salesGrid[row].length; month++) {
                rowSum += salesGrid[row][month];
            }

            rowTotals[row] = rowSum;
        }

        return rowTotals;
    }

    private static int countSalesEntries(int[][] salesGrid) {
        int count = 0;

        for (int row = 0; row < salesGrid.length; row++) {
            count += salesGrid[row].length;
        }

        return count;
    }

    public int getProductTotal(int productIndex) {
        if (productIndex < 0 || productIndex >= productTotals.length) {
            throw new IllegalArgumentException("Product index out of range: " + productIndex);
        }

        return productTotals[productIndex];
    }

    //Returns the highest 3-month total (63 for the sample data) alongside the winning product name.
    public int getHighestProductTotal() {
        int highestTotal = productTotals[0];

        for (int row = 1; row < productTotals.length; row++) {
            if (productTotals[row] > highestTotal) {
                highestTotal = productTotals[row];
            }
        }

        return highestTotal;
    }

    //Provided in case a question variant defines average per product line (153 / 3 = 51.0).
    public double getAveragePerProductLine() {
        if (productNames.length == 0) {
            return 0.0;
        }

        return (double) GetTotalProductsSold() / productNames.length;
    }

    // API/documentation reference [2]: Arrays (Java Platform SE 8) — https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html
    // Return defensive copies so external code cannot mutate internal array state.
    public String[] getProductNames() {
        return Arrays.copyOf(productNames, productNames.length);
    }

    public int[] getProductTotals() {
        return Arrays.copyOf(productTotals, productTotals.length);
    }

    public int[][] getProductSalesSnapshot() {
        int[][] copy = new int[productSales.length][];

        for (int row = 0; row < productSales.length; row++) {
            copy[row] = Arrays.copyOf(productSales[row], productSales[row].length);
        }

        return copy;
    }
    //End of Array Calculation and Snapshot Helpers section

    //Start of Validation Helpers section
    private static String[] validateAndCopyNames(String[] names) {
        if (names == null || names.length == 0) {
            throw new IllegalArgumentException("At least one product name is required.");
        }

        String[] copy = new String[names.length];

        for (int i = 0; i < names.length; i++) {
            if (names[i] == null || names[i].trim().isEmpty()) {
                throw new IllegalArgumentException("Product name at index " + i + " cannot be blank.");
            }
            copy[i] = names[i].trim();
        }

        return copy;
    }

    private static int[][] validateAndCopySales(int[][] sales, int expectedRows) {
        if (sales == null || sales.length != expectedRows) {
            throw new IllegalArgumentException(
                    "Sales array must contain exactly " + expectedRows + " product row(s).");
        }

        int[][] copy = new int[sales.length][];

        for (int row = 0; row < sales.length; row++) {
            if (sales[row] == null || sales[row].length == 0) {
                throw new IllegalArgumentException("Monthly sales row " + row + " cannot be empty.");
            }

            copy[row] = new int[sales[row].length];

            for (int month = 0; month < sales[row].length; month++) {
                if (sales[row][month] < 0) {
                    throw new IllegalArgumentException(
                            "Sales cannot be negative (row " + row + ", month " + (month + 1) + ").");
                }
                copy[row][month] = sales[row][month];
            }
        }

        return copy;
    }
    //End of Validation Helpers section
}

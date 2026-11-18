package com.mycompany.questionone;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Question 1.4 verifying total sales calculation, average sales
 * calculation, most popular product selection (by 3-month row total rather than
 * single-month cell), and input validation.
 *
 * References:
 * [1] Oracle, "Arrays," The Java Tutorials. Accessed: Nov. 18, 2026. [Online].
 *     Available: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
 */
public class ProductTest {

    private static final double DELTA = 0.001;

    //Sample sales from Question 1.1:
    //Rough math:  
    //so if 
    //Row 0 - Cozy Nights Monarch mattress: 25 + 18 + 20 = 63 (highest 3-month total)??
    //Row 1 - Restonic Barbados mattress:   15 + 10 + 12 = 37
    //Row 2 - Sealy Phuket mattress:        18 + 15 + 20 = 53
    private static final int[][] VALID_SALES_DATA = {
        {25, 18, 20},
        {15, 10, 12},
        {18, 15, 20}
    };

    //Start of Required Question 1.4 Unit Tests section
    @Test
    public void GetTotalProductsSales_WithValidData_ReturnsCorrectValue() {
        Product product = new Product(Product.DEFAULT_PRODUCT_NAMES, VALID_SALES_DATA);

        int actualTotal = product.GetTotalProductsSold();

        //63 + 37 + 53 = 153 total mattresses sold over the three months.
        assertEquals(153, actualTotal);
    }

    @Test
    public void GetAverageSales_WithValidData_ReturnsCorrectValue() {
        Product product = new Product(Product.DEFAULT_PRODUCT_NAMES, VALID_SALES_DATA);

        double actualAverage = product.GetAverageProductSold();

        //153 total sales divided by 9 monthly entries (3 products x 3 months) = 17.0.
        assertEquals(17.0, actualAverage, DELTA);
    }
    //End of Required Question 1.4 Unit Tests section

    //Start of Most Popular and Array Verification Tests section
    @Test
    public void GetMostPopularProduct_WithValidData_UsesRowTotal63NotSingleMonth25() {
        // API/documentation reference [1]: Arrays — https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
        Product product = new Product(Product.DEFAULT_PRODUCT_NAMES, VALID_SALES_DATA);

        //Verify the 1D productTotals array stores the 3-month sums [63, 37, 53], not single cells.
        assertArrayEquals(new int[]{63, 37, 53}, product.getProductTotals());
        assertEquals(63, product.getHighestProductTotal());
        assertEquals("Cozy Nights Monarch mattress (63)", product.GetMostPopularProduct());
    }

    @Test
    public void GetMostPopularProduct_WhenSingleCellDiffersFromRowSum_SelectsHighestRowSum() {
        //Product 0 has the single largest month (30), but Product 2 has the highest 3-month total (66).
        int[][] salesWhereRowSumWins = {
            {30, 5, 5},   //Total = 40 (highest single cell = 30)
            {15, 15, 15}, //Total = 45
            {22, 22, 22}  //Total = 66 (highest total sales)
        };

        Product product = new Product(Product.DEFAULT_PRODUCT_NAMES, salesWhereRowSumWins);

        assertEquals(66, product.getHighestProductTotal());
        assertEquals("Sealy Phuket mattress (66)", product.GetMostPopularProduct());
    }

    @Test
    public void PrintProduct_WithValidData_FormatsReportWith45Asterisks() {
        PrintProduct printer = new PrintProduct(Product.DEFAULT_PRODUCT_NAMES, VALID_SALES_DATA);

        String report = printer.buildReport();

        assertEquals(45, PrintProduct.REPORT_DIVIDER.length());
        assertTrue(report.contains("TOTAL PRODUCT SALES: 153"));
        assertTrue(report.contains("AVERAGE PRODUCT SALES: 17"));
        assertTrue(report.contains("MOST POPULAR PRODUCT: Cozy Nights Monarch mattress (63)"));
    }

    @Test
    public void Constructor_WithNegativeSales_ThrowsIllegalArgumentException() {
        int[][] invalidSales = {
            {25, -1, 20},
            {15, 10, 12},
            {18, 15, 20}
        };

        assertThrows(IllegalArgumentException.class,
                () -> new Product(Product.DEFAULT_PRODUCT_NAMES, invalidSales));
    }
    //End of Most Popular and Array Verification Tests section
}

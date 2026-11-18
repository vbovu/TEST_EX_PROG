package com.mycompany.questionone;

/**
 * Contract for calculating summary statistics across the recorded product sales.
 *
 * The method names keep the exact PascalCase identifiers mandated by Question 1.2.
 *
 * References:
 * [1] Oracle, "Interfaces," The Java Tutorials. Accessed: Nov. 18, 2026. [Online].
 *     Available: https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html
 */
public interface IProduct {

    // API/documentation reference [1]: Interfaces — https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html
    // The assessment requires these exact PascalCase method signatures in IProduct.
    int GetTotalProductsSold();

    double GetAverageProductSold();

    String GetMostPopularProduct();
}

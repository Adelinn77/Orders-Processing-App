package BusinessLogic;

import DataAccess.ProductDAO;
import Model.Product;

import javax.swing.*;
import java.util.ArrayList;

public class ProductBLL {
    private static ProductDAO productDAO = new ProductDAO();

    public static JTable getProductsTable() {
        ArrayList<Product> products;
        products = productDAO.findAll();
        return ProductDAO.buildTableFromList(products);
    }

    public static void addProduct(Product product) {
        productDAO.insert(product);
    }

    public static ArrayList<Product> findAllProducts() {
        return productDAO.findAll();
    }

    public static void updateProduct(Product product) {
        productDAO.update(product);
    }

    public static void deleteProduct(Product product) {
        productDAO.delete(product);
    }

    public static Product findProduct(int productId) {
        return productDAO.findById(productId);
    }
}

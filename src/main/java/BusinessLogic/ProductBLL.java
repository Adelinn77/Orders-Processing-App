package BusinessLogic;

import DataAccess.ClientDAO;
import DataAccess.ProductDAO;
import Model.Client;
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
}

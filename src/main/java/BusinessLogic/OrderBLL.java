package BusinessLogic;

import DataAccess.OrderDAO;
import Model.Orderr;
import Model.Product;

import javax.swing.*;
import java.util.ArrayList;

public class OrderBLL {
    private static OrderDAO orderDAO = new OrderDAO();

    public static JTable getOrdersTable() {
        ArrayList<Orderr> orders;
        orders = orderDAO.findAll();
        return OrderDAO.buildTableFromList(orders);
    }

    public static void addOrder(Orderr order) {
        orderDAO.insert(order);
    }
}

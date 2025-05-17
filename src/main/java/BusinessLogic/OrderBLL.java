package BusinessLogic;

import DataAccess.OrderDAO;
import Model.Orderr;

import javax.swing.*;
import java.util.ArrayList;

public class OrderBLL {
    private static OrderDAO orderDAO = new OrderDAO();

    public static JTable getOrdersTable() {
        ArrayList<Orderr> orders;
        orders = orderDAO.findAll();
        return OrderDAO.buildTableFromList(orders);
    }
}

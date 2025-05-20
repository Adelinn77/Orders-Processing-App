package BusinessLogic;

import DataAccess.BillDao;
import DataAccess.ProductDAO;
import Model.Bill;
import Model.Product;

import javax.swing.*;
import java.util.ArrayList;

public class BillBLL {
    private static BillDao billDAO = new BillDao();

    public static void addBill(Bill bill) {
        billDAO.insert(bill);
    }

    public static ArrayList<Bill> findAllBills() {
        return billDAO.findAll();
    }

    public static JTable getBillsTable() {
        ArrayList<Bill> bills;
        bills = billDAO.findAll();
        return BillDao.buildTableFromList(bills);
    }
}

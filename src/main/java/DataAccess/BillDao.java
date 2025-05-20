package DataAccess;

import Model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BillDao extends AbstractDAO<Bill>{
    public BillDao() {}
    @Override
    public ArrayList<Bill> findAll() {
        ArrayList<Bill> list = new ArrayList<>();
        String query = "SELECT * FROM bill";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bill b = new Bill(
                        rs.getString("clientName"),
                        rs.getString("productName"),
                        rs.getInt("quantity"),
                        rs.getFloat("totalPrice"),
                        rs.getDate("date")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}

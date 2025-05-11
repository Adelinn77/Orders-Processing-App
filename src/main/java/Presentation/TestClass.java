package Presentation;

import DataAccess.ConnectionFactory;
import Model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestClass {
    private final static String findStatementString  = "SELECT * FROM Client where id  = ?";

    public static Client findById(int id) throws SQLException {
        Client client = null;

        Connection dbConncection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;

        try{
            findStatement = dbConncection.prepareStatement(findStatementString);
            findStatement.setInt(1, id);
            rs = findStatement.executeQuery();

            if(rs.next()){
                client = new Client(rs.getString("name"), rs.getInt("age"), rs.getString("email"), rs.getString("adress"), rs.getString("phoneNumber"));
            }
        } catch (SQLException e) {
            client = new Client();
            System.out.println(e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConncection);
        }
        return client;
    }
    public static void main(String[] args) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        if (conn != null) {
            System.out.println("✅ Conexiunea cu baza de date funcționează!");
            ConnectionFactory.close(conn);
        } else {
            System.out.println("❌ Conexiunea a eșuat.");
        }


        if(conn != null) {
            Client client = TestClass.findById(1);
            System.out.println(client);
        }
    }
}

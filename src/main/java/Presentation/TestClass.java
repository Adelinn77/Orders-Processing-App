package Presentation;

import DataAccess.ConnectionFactory;
import Model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestClass {

    public static void main(String[] args) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        if (conn != null) {
            System.out.println("✅ Conexiunea cu baza de date funcționează!");
            ConnectionFactory.close(conn);
        } else {
            System.out.println("❌ Conexiunea a eșuat.");
        }

        View view = new View("Orders Management");

    }
}

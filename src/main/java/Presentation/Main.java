package Presentation;

import DataAccess.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

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

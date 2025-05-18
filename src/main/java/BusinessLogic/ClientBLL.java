package BusinessLogic;

import DataAccess.AbstractDAO;
import DataAccess.ClientDAO;
import Model.Client;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ClientBLL {
    private static ClientDAO clientDAO = new ClientDAO();

    public static JTable getClientsTable() {
        ArrayList<Client> clients;
        clients = clientDAO.findAll();
        return ClientDAO.buildTableFromList(clients);
    }

    public static void addClient(Client client) {
        clientDAO.insert(client);
    }
}

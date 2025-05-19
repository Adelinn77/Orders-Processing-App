package BusinessLogic;

import DataAccess.AbstractDAO;
import DataAccess.ClientDAO;
import Model.Client;
import Model.Product;

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

    public static ArrayList<Client> findAllClients() {
        return clientDAO.findAll();
    }

    public static void updateClient(Client client) {
        clientDAO.update(client);
    }

    public static void deleteClient(Client client) {
        clientDAO.delete(client);
    }

    public static Client findClient(int clientID) {
        return clientDAO.findById(clientID);
    }
}

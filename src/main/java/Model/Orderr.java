package Model;

import java.time.LocalDate;
import java.util.Date;

/**
 * Represents an order placed by a client for a specific product.
 * <p>
 * Each order includes the ID of the client and product, the quantity ordered,
 * the date when the order was placed, and an auto-generated order ID.
 * </p>
 */

public class Orderr {
    private int id;
    private Date date;
    private int quantity;
    private int productID;
    private int clientID;


    public Orderr() {}
    public Orderr(int clientID, int productID, int quantity) {
        this.clientID = clientID;
        this.productID = productID;
        this.quantity = quantity;
        this.date = new Date();
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

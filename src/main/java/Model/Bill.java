package Model;

import java.util.Date;

/**
 * Represents a bill/invoice for a placed order.
 * <p>
 * This record is immutable and corresponds to entries in the database table {@code bill}.
 * Each Bill contains client information, product details, the quantity ordered, the total price,
 * and the date the order was placed.
 * </p>
 *
 * @param clientName   the name of the client who placed the order
 * @param productName  the name of the product ordered
 * @param quantity     the quantity of the product ordered
 * @param totalPrice   the total price for the quantity ordered
 * @param date         the date when the bill was created
 */

public record Bill(String clientName, String productName, int quantity, float totalPrice, Date date) {}
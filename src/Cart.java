import java.util.ArrayList;
import java.util.List;

class ShippingService {
    private static final double SHIPPING_RATE_PER_KG = 27.27;
    
    public static void processShipment(List<ShippableItem> items) {
        if (items.isEmpty()) {
            return;
        }
        
        System.out.println("** Shipment notice **");
        double totalWeight = 0.0;
        
        for (ShippableItem item : items) {
            double itemWeight = item.getWeight() * item.getQuantity();
            totalWeight += itemWeight;
            System.out.printf("%dx %-12s %dg%n", 
                item.getQuantity(), 
                item.getName(), 
                (int)(itemWeight * 1000));
        }
        
        System.out.printf("Total package weight %.1fkg%n", totalWeight);
    }
    
    public static double calculateShippingCost(List<ShippableItem> items) {
        double totalWeight = 0.0;
        for (ShippableItem item : items) {
            totalWeight += item.getWeight() * item.getQuantity();
        }
        return totalWeight * SHIPPING_RATE_PER_KG;
    }
}

class ShippableItem implements Shippable {
    private String name;
    private double weight;
    private int quantity;
    
    public ShippableItem(String name, double weight, int quantity) {
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public double getWeight() {
        return weight;
    }
    
    public int getQuantity() {
        return quantity;
    }
}

public class Cart {
    private List<CartItem> items;
    
    public Cart() {
        this.items = new ArrayList<>();
    }
    
    public boolean add(Product product, int quantity) {
        if (product == null) {
            System.out.println("Error: Product cannot be null");
            return false;
        }
        
        if (quantity <= 0) {
            System.out.println("Error: Quantity must be greater than 0");
            return false;
        }
        
        if (!product.isAvailable(quantity)) {
            System.out.println("Error: Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + quantity);
            return false;
        }
        
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                int newQuantity = item.getQuantity() + quantity;
                if (!product.isAvailable(newQuantity)) {
                    System.out.println("Error: Adding " + quantity + " more would exceed available stock");
                    return false;
                }
                item.setQuantity(newQuantity);
                System.out.println("Updated quantity for " + product.getName() + " to " + newQuantity);
                return true;
            }
        }
        
        items.add(new CartItem(product, quantity));
        System.out.println("Added " + quantity + " " + product.getName() + "(s) to cart");
        return true;
    }
    
    public boolean addProduct(Product product, int quantity) {
        return add(product, quantity);
    }
    
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : items) {
            subtotal += item.getProduct().getPrice() * item.getQuantity();
        }
        return subtotal;
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }
    
    private List<ShippableItem> getShippableItems() {
        List<ShippableItem> shippableItems = new ArrayList<>();
        for (CartItem item : items) {
            if (item.getProduct().isShippable()) {
                shippableItems.add(new ShippableItem(
                    item.getProduct().getName(),
                    item.getProduct().getWeight(),
                    item.getQuantity()
                ));
            }
        }
        return shippableItems;
    }
    
    public boolean checkout(Customer customer) {
        if (isEmpty()) {
            System.out.println("Error: Cart is empty");
            return false;
        }
        
        for (CartItem item : items) {
            if (item.getProduct().isExpired()) {
                System.out.println("Error: Product " + item.getProduct().getName() + " is expired");
                return false;
            }
        }
        
        for (CartItem item : items) {
            if (!item.getProduct().isAvailable(item.getQuantity())) {
                System.out.println("Error: Product " + item.getProduct().getName() + " is out of stock");
                return false;
            }
        }
        
        double subtotal = calculateSubtotal();
        List<ShippableItem> shippableItems = getShippableItems();
        double shippingCost = ShippingService.calculateShippingCost(shippableItems);
        double totalAmount = subtotal + shippingCost;
        
        if (customer.getBalance() < totalAmount) {
            System.out.println("Error: Customer's balance is insufficient");
            return false;
        }
        
        ShippingService.processShipment(shippableItems);
        
        customer.deductBalance(totalAmount);
        
        for (CartItem item : items) {
            item.getProduct().reduceQuantity(item.getQuantity());
        }
        
        System.out.println("** Checkout receipt **");
        for (CartItem item : items) {
            System.out.printf("%dx %-12s %d%n", 
                item.getQuantity(), 
                item.getProduct().getName(), 
                (int)(item.getProduct().getPrice() * item.getQuantity()));
        }
        System.out.println("----------------------");
        System.out.printf("Subtotal         %d%n", (int)subtotal);
        System.out.printf("Shipping         %d%n", (int)shippingCost);
        System.out.printf("Amount           %d%n", (int)totalAmount);
        System.out.println("END.");
        
        items.clear();
        return true;
    }
    
    // Clear cart
    public void clear() {
        items.clear();
    }
}

// Helper class to represent items in the cart
class CartItem {
    private Product product;
    private int quantity;
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

// Customer class to manage balance
class Customer {
    private String name;
    private double balance;
    
    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
    
    public String getName() {
        return name;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void addBalance(double amount) {
        this.balance += amount;
    }
    
    public boolean deductBalance(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Balance: $%.2f)", name, balance);
    }
}

class CheckoutService {
    public static void checkout(Customer customer, Cart cart) {
        cart.checkout(customer);
    }
} 
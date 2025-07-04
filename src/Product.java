import java.time.LocalDate;

interface Shippable {
    String getName();
    double getWeight();
}

public abstract class Product {
    protected String name;
    protected double price;
    protected int quantity;
    
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public boolean isAvailable(int requestedQuantity) {
        return this.quantity >= requestedQuantity;
    }
    
    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }
    
    public abstract boolean isExpirable();
    public abstract boolean isShippable();
    public abstract double getWeight();
    public abstract boolean isExpired();
    
    @Override
    public String toString() {
        return String.format("%s - $%.2f (Qty: %d)", name, price, quantity);
    }
}

class SimpleProduct extends Product {
    public SimpleProduct(String name, double price, int quantity) {
        super(name, price, quantity);
    }
    
    @Override
    public boolean isExpirable() {
        return false;
    }
    
    @Override
    public boolean isShippable() {
        return false;
    }
    
    @Override
    public double getWeight() {
        return 0.0;
    }
    
    @Override
    public boolean isExpired() {
        return false;
    }
}

class ExpirableProduct extends Product {
    private LocalDate expiryDate;
    
    public ExpirableProduct(String name, double price, int quantity, LocalDate expiryDate) {
        super(name, price, quantity);
        this.expiryDate = expiryDate;
    }
    
    @Override
    public boolean isExpirable() {
        return true;
    }
    
    @Override
    public boolean isShippable() {
        return false;
    }
    
    @Override
    public double getWeight() {
        return 0.0;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
}

class ShippableProduct extends Product implements Shippable {
    private double weight;
    
    public ShippableProduct(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }
    
    @Override
    public boolean isExpirable() {
        return false;
    }
    
    @Override
    public boolean isShippable() {
        return true;
    }
    
    @Override
    public double getWeight() {
        return weight;
    }
    
    @Override
    public boolean isExpired() {
        return false;
    }
}

class ExpirableShippableProduct extends Product implements Shippable {
    private LocalDate expiryDate;
    private double weight;
    
    public ExpirableShippableProduct(String name, double price, int quantity, LocalDate expiryDate, double weight) {
        super(name, price, quantity);
        this.expiryDate = expiryDate;
        this.weight = weight;
    }
    
    @Override
    public boolean isExpirable() {
        return true;
    }
    
    @Override
    public boolean isShippable() {
        return true;
    }
    
    @Override
    public double getWeight() {
        return weight;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
} 
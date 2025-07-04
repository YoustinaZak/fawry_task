import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program Started");
        
        Product cheese = new ExpirableShippableProduct("Cheese", 100.0, 10, 
                                                       LocalDate.now().plusDays(7), 0.2);
        Product tv = new ShippableProduct("TV", 500.0, 5, 0.7);
        Product scratchCard = new SimpleProduct("Mobile scratch card", 25.0, 50);
        Product biscuits = new ExpirableProduct("Biscuits", 75.0, 20, 
                                               LocalDate.now().plusDays(30));
        
        Customer customer = new Customer("John Doe", 1000.0);
        System.out.println("Customer: " + customer + "\n");
        
        Cart cart = new Cart();
        
        System.out.println("Testing");
        cart.add(cheese, 2);
        cart.add(tv, 1);
        cart.add(scratchCard, 1);
        
        CheckoutService.checkout(customer, cart);
        
        System.out.println("\n1. Empty Cart Test:");
        Cart emptyCart = new Cart();
        CheckoutService.checkout(customer, emptyCart);
        
        System.out.println("\n2. Insufficient Balance Test:");
        Customer poorCustomer = new Customer("Poor Customer", 50.0);
        Cart expensiveCart = new Cart();
        expensiveCart.add(tv, 2);
        CheckoutService.checkout(poorCustomer, expensiveCart);
        
        System.out.println("\n3. Expired Product Test:");
        Product expiredCheese = new ExpirableShippableProduct("Expired Cheese", 100.0, 10, 
                                                             LocalDate.now().minusDays(1), 0.2);
        Cart expiredCart = new Cart();
        expiredCart.add(expiredCheese, 1);
        CheckoutService.checkout(customer, expiredCart);
        
        System.out.println("\n4. Out of Stock Test:");
        Product limitedTV = new ShippableProduct("Limited TV", 500.0, 2, 0.7);
        Cart stockCart = new Cart();
        stockCart.add(limitedTV, 5);
        
        System.out.println("\n5. Mixed Cart Test:");
        Cart mixedCart = new Cart();
        mixedCart.add(cheese, 1);
        mixedCart.add(biscuits, 2);
        mixedCart.add(scratchCard, 1);
        CheckoutService.checkout(customer, mixedCart);

    }
} 
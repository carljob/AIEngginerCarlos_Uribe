package eci.edu.byteProgramming.ejercicio.paper.util;

public class CreditCardFactory extends PaymentMethod implements PaymentFactory{
    private String number;
    private String name;
    private String expirationDate;
    private String cvv;
    private String cardType;
    private String address;
    
    public CreditCardFactory(double amount, String customerID, String description,
                     String number, String name, String expirationDate, String cvv, String address) {
        super(amount, customerID, description);
        this.number = number;
        this.name = name;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.cardType = determineCardType(number);
    }

    @Override
    public boolean validatePaymentMethod() {
        return validateCardNumber() && validateCVV() && validateExpirationDate();
    }

    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        setAmount(amount);
        setCustomerId(customerId);
        setDescription(description);
        this.transactionID = generateTransactionIdWithPrefix(getPaymentMethod());
        this.status = PaymentStatus.PENDING;
        this.timestamp = new java.util.Date();
        return this;
    }
    
    private boolean validateCardNumber() {
        return number != null && number.length() >= 13 && number.length() <= 19;
    }
    
    private boolean validateCVV() {
        return cvv != null && cvv.length() >= 3 && cvv.length() <= 4;
    }
    
    private boolean validateExpirationDate() {
        // Formato MM/YY
        return expirationDate != null && expirationDate.matches("\\d{2}/\\d{2}");
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing Credit Card payment...");
        
        if (!validatePaymentMethod()) {
            System.out.println("Credit Card validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        // Simulación del procesamiento
        try {
            Thread.sleep(2000);
            System.out.println("Contacting bank for card: " + maskCardNumber());
            System.out.println("Payment authorized by bank");
            
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "CREDIT_CARD";
    }
    
    private String determineCardType(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return "UNKNOWN";
        }
        if (cardNumber.startsWith("4")) return "VISA";
        if (cardNumber.startsWith("5")) return "MASTERCARD";
        if (cardNumber.startsWith("3")) return "AMEX";
        return "UNKNOWN";
    }
    
    public String maskCardNumber() {
        if (number == null || number.length() < 4) {
            return "****";
        }
        return "**** **** **** " + number.substring(number.length() - 4);
    }
    
    public String getCardHolderName() { return name; }
    public String getCardType() { return cardType; }
}

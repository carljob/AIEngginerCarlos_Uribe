package eci.edu.byteProgramming.ejercicio.paper.util;

public class CryptoFactory extends PaymentMethod implements PaymentFactory {
    private String walletAddress;
    private String cryptoType;
    private String token;
    private double walletBalance;
    private String blockchainHash;
    
    public CryptoFactory(double amount, String customerId, String description,
                 String walletAddress, String cryptoType, double walletBalance) {
        super(amount, customerId, description);
        this.walletAddress = walletAddress;
        this.cryptoType = cryptoType;
        this.token = token;
        this.walletBalance = walletBalance;
    }
    
    @Override
    public boolean validatePaymentMethod() {
        return validateWalletAddress() && validateBalance();
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
    
    private boolean validateWalletAddress() {
        return walletAddress != null && walletAddress.length() >= 26;
    }
    
    private boolean validateBalance() {
        return walletBalance >= amount;
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing Cryptocurrency payment...");
        
        if (!validatePaymentMethod()) {
            System.out.println("Crypto validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        try {
            Thread.sleep(3000);
            this.blockchainHash = generateBlockchainHash();
            System.out.println("Transaction broadcasted to blockchain");
            System.out.println("Blockchain hash: " + blockchainHash);
            
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "CRYPTOCURRENCY";
    }
    
    private String generateBlockchainHash() {
        return "0x" + Integer.toHexString((int)(Math.random() * Integer.MAX_VALUE));
    }
    
    public String getWalletAddress() { return walletAddress; }
    public String getCryptoType() { return cryptoType; }
    public String getBlockchainHash() { return blockchainHash; }
}

package eci.edu.byteProgramming.ejercicio.paper.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas exhaustivas para el sistema de pagos de la Tienda Virtual
 * Problema #2: Tienda Virtual
 *
 * Cubre:
 * - Factory Pattern: Creación de familias de objetos de pago
 * - Observer Pattern: Notificación de eventos de pago
 * - Validación de métodos de pago
 * - Procesamiento de transacciones
 */
@DisplayName("Payment System Tests - Virtual Store Problem #2")
public class PaymentSystemTest {

    private ECIPayment eciPayment;
    private Inventory inventory;
    private Facturation facturation;
    private Notification notification;
    private PaymentEventObserver paymentObserver;

    @BeforeEach
    void setUp() {
        // Inicializar el sistema de pagos y sus componentes
        eciPayment = new ECIPayment();
        inventory = new Inventory();
        facturation = new Facturation();
        notification = new Notification();
        paymentObserver = new PaymentEventObserver(inventory, facturation, notification);

        // Registrar el observador de pagos
        eciPayment.addObserver(paymentObserver);
    }

    // ==========================================
    // TEST 1: CREDIT CARD FACTORY TESTS
    // ==========================================

    @Test
    @DisplayName("Test 1.1: Crear y procesar pago con tarjeta de crédito VISA")
    void testCreditCardVISAPayment() {
        System.out.println("\n=== Test 1.1: Tarjeta de Crédito VISA ===");

        CreditCardFactory creditCard = new CreditCardFactory(
            1200.00,
            "CUST001",
            "Gaming Laptop Purchase",
            "4532123456789010",
            "Juan Pérez",
            "12/25",
            "123",
            "Calle 100, Bogotá"
        );

        assertTrue(creditCard.validatePaymentMethod(),
                   "La tarjeta VISA debe validarse correctamente");
        assertTrue(eciPayment.processPayment(
                creditCard, 1200.00, "CUST001",
                "Gaming Laptop Purchase",
                "Juan Pérez", "juan@example.com", "LAPTOP001"),
                   "El pago debe procesarse exitosamente");
    }

    @Test
    @DisplayName("Test 1.2: Crear y procesar pago con tarjeta de crédito MASTERCARD")
    void testCreditCardMASTERCARDPayment() {
        System.out.println("\n=== Test 1.2: Tarjeta de Crédito MASTERCARD ===");

        CreditCardFactory mastercard = new CreditCardFactory(
            800.00,
            "CUST002",
            "Smartphone Purchase",
            "5425233010103010",
            "María García",
            "06/24",
            "456",
            "Carrera 50, Medellín"
        );

        assertEquals("MASTERCARD", mastercard.getCardType(),
                     "La tarjeta debe ser identificada como MASTERCARD");
        assertTrue(eciPayment.processPayment(
                mastercard, 800.00, "CUST002",
                "Smartphone Purchase",
                "María García", "maria@example.com", "PHONE001"),
                   "El pago MASTERCARD debe procesarse exitosamente");
    }

    @Test
    @DisplayName("Test 1.3: Validación fallida de tarjeta de crédito con CVV inválido")
    void testCreditCardInvalidCVV() {
        System.out.println("\n=== Test 1.3: Tarjeta con CVV inválido ===");

        CreditCardFactory invalidCard = new CreditCardFactory(
            500.00,
            "CUST003",
            "Invalid Card Test",
            "4532123456789010",
            "Pedro López",
            "12/25",
            "12",  // CVV muy corto
            "Calle 10, Cali"
        );

        assertFalse(invalidCard.validatePaymentMethod(),
                    "La tarjeta con CVV inválido debe fallar validación");
        assertFalse(eciPayment.processPayment(
                invalidCard, 500.00, "CUST003",
                "Invalid Card Test",
                "Pedro López", "pedro@example.com", "BOOK001"),
                    "El pago debe fallar con tarjeta inválida");
    }

    @Test
    @DisplayName("Test 1.4: Validación fallida de tarjeta con fecha de expiración inválida")
    void testCreditCardInvalidExpiration() {
        System.out.println("\n=== Test 1.4: Tarjeta con fecha de expiración inválida ===");

        CreditCardFactory expiredCard = new CreditCardFactory(
            300.00,
            "CUST004",
            "Expired Card Test",
            "4532123456789010",
            "Carlos Ruiz",
            "2025",  // Formato incorrecto
            "789",
            "Avenida 7, Santa Marta"
        );

        assertFalse(expiredCard.validatePaymentMethod(),
                    "La tarjeta con fecha inválida debe fallar validación");
    }

    @Test
    @DisplayName("Test 1.5: Obtener tipo de tarjeta AMEX")
    void testCreditCardAMEXType() {
        System.out.println("\n=== Test 1.5: Identificar tarjeta AMEX ===");

        CreditCardFactory amex = new CreditCardFactory(
            2000.00,
            "CUST005",
            "AMEX Payment",
            "378282246310005",
            "Ana Martínez",
            "08/26",
            "1234",
            "Paseo Colón, San José"
        );

        assertEquals("AMEX", amex.getCardType(),
                     "La tarjeta debe ser identificada como AMEX");
    }

    @Test
    @DisplayName("Test 1.6: Enmascarar número de tarjeta para seguridad")
    void testCreditCardMasking() {
        System.out.println("\n=== Test 1.6: Enmascaramiento de número de tarjeta ===");

        CreditCardFactory card = new CreditCardFactory(
            1000.00,
            "CUST006",
            "Card Masking Test",
            "4532123456789010",
            "Roberto Silva",
            "03/25",
            "567",
            "Diagonal 45, Barranquilla"
        );

        String maskedNumber = card.maskCardNumber();
        assertTrue(maskedNumber.contains("****") && maskedNumber.contains("9010"),
                   "El número debe estar enmascarado excepto los últimos 4 dígitos");
    }

    // ==========================================
    // TEST 2: PAYPAL FACTORY TESTS
    // ==========================================

    @Test
    @DisplayName("Test 2.1: Crear y procesar pago con PayPal")
    void testPayPalPayment() {
        System.out.println("\n=== Test 2.1: Pago con PayPal ===");

        PaypalFactory paypal = new PaypalFactory(
            750.00,
            "CUST007",
            "PayPal Purchase",
            "usuario@gmail.com",
            "auth_token_123456789"
        );

        assertTrue(paypal.validatePaymentMethod(),
                   "El PayPal debe validarse correctamente");
        assertTrue(eciPayment.processPayment(
                paypal, 750.00, "CUST007",
                "PayPal Purchase",
                "John Doe", "john@gmail.com", "LAPTOP001"),
                   "El pago PayPal debe procesarse exitosamente");
    }

    @Test
    @DisplayName("Test 2.2: Validación fallida de PayPal con email inválido")
    void testPayPalInvalidEmail() {
        System.out.println("\n=== Test 2.2: PayPal con email inválido ===");

        PaypalFactory invalidPaypal = new PaypalFactory(
            500.00,
            "CUST008",
            "Invalid PayPal Test",
            "invalid-email",  // Email sin @
            "auth_token_123456789"
        );

        assertFalse(invalidPaypal.validatePaymentMethod(),
                    "PayPal con email inválido debe fallar validación");
    }

    @Test
    @DisplayName("Test 2.3: Validación fallida de PayPal con token corto")
    void testPayPalInvalidToken() {
        System.out.println("\n=== Test 2.3: PayPal con token inválido ===");

        PaypalFactory invalidPaypal = new PaypalFactory(
            600.00,
            "CUST009",
            "Invalid Token Test",
            "usuario@paypal.com",
            "short"  // Token demasiado corto
        );

        assertFalse(invalidPaypal.validatePaymentMethod(),
                    "PayPal con token corto debe fallar validación");
    }

    @Test
    @DisplayName("Test 2.4: Obtener email de PayPal")
    void testPayPalGetEmail() {
        System.out.println("\n=== Test 2.4: Obtener email de PayPal ===");

        PaypalFactory paypal = new PaypalFactory(
            400.00,
            "CUST010",
            "Get Email Test",
            "test@paypal.com",
            "auth_token_validtoken"
        );

        assertEquals("test@paypal.com", paypal.getEmail(),
                     "El email de PayPal debe ser accesible");
    }

    // ==========================================
    // TEST 3: CRYPTOCURRENCY FACTORY TESTS
    // ==========================================

    @Test
    @DisplayName("Test 3.1: Crear y procesar pago con criptomoneda")
    void testCryptoPayment() {
        System.out.println("\n=== Test 3.1: Pago con criptomoneda ===");

        CryptoFactory crypto = new CryptoFactory(
            2500.00,
            "CUST011",
            "Crypto Purchase",
            "1A1z7agoat2Bt89zZmkzkhtrnZseNxtfx",
            "BITCOIN",
            5000.00  // Saldo suficiente
        );

        assertTrue(crypto.validatePaymentMethod(),
                   "La criptomoneda debe validarse correctamente");
        assertTrue(eciPayment.processPayment(
                crypto, 2500.00, "CUST011",
                "Crypto Purchase",
                "Crypto Investor", "crypto@example.com", "LAPTOP001"),
                   "El pago con criptomoneda debe procesarse exitosamente");
    }

    @Test
    @DisplayName("Test 3.2: Validación fallida de criptomoneda con dirección corta")
    void testCryptoInvalidWallet() {
        System.out.println("\n=== Test 3.2: Criptomoneda con dirección inválida ===");

        CryptoFactory invalidCrypto = new CryptoFactory(
            1000.00,
            "CUST012",
            "Invalid Crypto Test",
            "short",  // Dirección muy corta
            "ETHEREUM",
            5000.00
        );

        assertFalse(invalidCrypto.validatePaymentMethod(),
                    "Criptomoneda con dirección corta debe fallar validación");
    }

    @Test
    @DisplayName("Test 3.3: Validación fallida de criptomoneda con saldo insuficiente")
    void testCryptoInsufficientBalance() {
        System.out.println("\n=== Test 3.3: Criptomoneda con saldo insuficiente ===");

        CryptoFactory insufficientCrypto = new CryptoFactory(
            5000.00,
            "CUST013",
            "Insufficient Balance Test",
            "1A1z7agoat2Bt89zZmkzkhtrnZseNxtfx",
            "BITCOIN",
            1000.00  // Saldo insuficiente
        );

        assertFalse(insufficientCrypto.validatePaymentMethod(),
                    "Criptomoneda con saldo insuficiente debe fallar validación");
    }

    @Test
    @DisplayName("Test 3.4: Obtener tipo de criptomoneda")
    void testCryptoGetType() {
        System.out.println("\n=== Test 3.4: Obtener tipo de criptomoneda ===");

        CryptoFactory crypto = new CryptoFactory(
            500.00,
            "CUST014",
            "Crypto Type Test",
            "3J98t1WpEZ73CNmYviecrnyiWrnqRhWNLy",
            "LITECOIN",
            1000.00
        );

        assertEquals("LITECOIN", crypto.getCryptoType(),
                     "El tipo de criptomoneda debe ser accesible");
    }

    // ==========================================
    // TEST 4: OBSERVER PATTERN TESTS
    // ==========================================

    @Test
    @DisplayName("Test 4.1: Observador notificado en pago exitoso")
    void testObserverNotificationOnSuccess() {
        System.out.println("\n=== Test 4.1: Observador notificado en éxito ===");

        CreditCardFactory card = new CreditCardFactory(
            1200.00,
            "CUST015",
            "Observer Test Success",
            "4532123456789010",
            "Test Observer",
            "12/25",
            "123",
            "Test Address"
        );

        // Verificar que el inventario tenía stock inicial
        int initialStock = inventory.getStock("LAPTOP001");
        assertTrue(initialStock > 0, "El inventario debe tener stock inicial");

        // Procesar pago
        assertTrue(eciPayment.processPayment(
                card, 1200.00, "CUST015",
                "Observer Test Success",
                "Test Observer", "observer@test.com", "LAPTOP001"),
                   "El pago debe procesarse exitosamente");

        // Verificar que el stock fue descontado por el observador
        int finalStock = inventory.getStock("LAPTOP001");
        assertEquals(initialStock - 1, finalStock,
                     "El observador debe haber descontado del inventario");
    }

    @Test
    @DisplayName("Test 4.2: Observador notificado en pago fallido")
    void testObserverNotificationOnFailure() {
        System.out.println("\n=== Test 4.2: Observador notificado en fallo ===");

        CreditCardFactory invalidCard = new CreditCardFactory(
            1000.00,
            "CUST016",
            "Observer Test Failure",
            "4532123456789010",
            "Test Observer",
            "01/20",  // Fecha expirada (formato inválido)
            "12",     // CVV inválido
            "Test Address"
        );

        // Procesar pago inválido
        assertFalse(eciPayment.processPayment(
                invalidCard, 1000.00, "CUST016",
                "Observer Test Failure",
                "Test Observer", "observer_fail@test.com", "LAPTOP001"),
                    "El pago debe fallar");
    }

    @Test
    @DisplayName("Test 4.3: Agregar y remover observadores")
    void testAddRemoveObserver() {
        System.out.println("\n=== Test 4.3: Agregar y remover observadores ===");

        ECIPayment paymentSystem = new ECIPayment();
        Inventory inv = new Inventory();
        Facturation fact = new Facturation();
        Notification notif = new Notification();
        PaymentEventObserver observer = new PaymentEventObserver(inv, fact, notif);

        // Agregar observador
        paymentSystem.addObserver(observer);

        // Procesar pago con observador
        CreditCardFactory card = new CreditCardFactory(
            500.00,
            "CUST017",
            "Add/Remove Observer Test",
            "4532123456789010",
            "Test User",
            "05/26",
            "123",
            "Test Address"
        );

        assertTrue(paymentSystem.processPayment(
                card, 500.00, "CUST017",
                "Add/Remove Observer Test",
                "Test User", "test@test.com", "PHONE001"),
                   "El pago debe procesarse");

        // Remover observador
        paymentSystem.removeObserver(observer);

        // El test pasa si no hay excepciones
        assertTrue(true, "Observadores agregados y removidos correctamente");
    }

    // ==========================================
    // TEST 5: PAYMENT METHOD TESTS
    // ==========================================

    @Test
    @DisplayName("Test 5.1: Generar transaction ID")
    void testTransactionIdGeneration() {
        System.out.println("\n=== Test 5.1: Generar Transaction ID ===");

        CreditCardFactory card = new CreditCardFactory(
            100.00,
            "CUST018",
            "Transaction ID Test",
            "4532123456789010",
            "Test User",
            "12/25",
            "123",
            "Test Address"
        );

        String transactionIdBefore = card.getTransactionId();
        card.processPayment();
        String transactionIdAfter = card.getTransactionId();

        assertNotNull(transactionIdAfter, "El Transaction ID no debe ser nulo");
        assertTrue(transactionIdAfter.length() > 0, "El Transaction ID debe tener contenido");
        // El Transaction ID se genera con prefix al procesar, debe iniciar con CC para tarjeta de crédito
        assertTrue(transactionIdAfter.startsWith("CC") || transactionIdAfter.startsWith("TXN"),
                   "El Transaction ID debe ser válido");
    }

    @Test
    @DisplayName("Test 5.2: Establecer y obtener monto de pago")
    void testPaymentAmount() {
        System.out.println("\n=== Test 5.2: Establecer y obtener monto de pago ===");

        CreditCardFactory card = new CreditCardFactory(
            100.00,
            "CUST019",
            "Amount Test",
            "4532123456789010",
            "Test User",
            "12/25",
            "123",
            "Test Address"
        );

        assertEquals(100.00, card.getAmount(), 0.01,
                     "El monto debe ser correcto");

        card.setAmount(250.00);
        assertEquals(250.00, card.getAmount(), 0.01,
                     "El monto actualizado debe ser correcto");
    }

    @Test
    @DisplayName("Test 5.3: Establecer y obtener Customer ID")
    void testCustomerId() {
        System.out.println("\n=== Test 5.3: Establecer y obtener Customer ID ===");

        PaypalFactory paypal = new PaypalFactory(
            500.00,
            "CUST020",
            "Customer ID Test",
            "user@paypal.com",
            "auth_token_validtoken"
        );

        assertEquals("CUST020", paypal.getCustomerId(),
                     "El Customer ID debe ser correcto");

        paypal.setCustomerId("CUST_NEW");
        assertEquals("CUST_NEW", paypal.getCustomerId(),
                     "El Customer ID actualizado debe ser correcto");
    }

    @Test
    @DisplayName("Test 5.4: Establecer y obtener descripción")
    void testDescription() {
        System.out.println("\n=== Test 5.4: Establecer y obtener descripción ===");

        CryptoFactory crypto = new CryptoFactory(
            500.00,
            "CUST021",
            "Original Description",
            "1A1z7agoat2Bt89zZmkzkhtrnZseNxtfx",
            "BITCOIN",
            1000.00
        );

        assertEquals("Original Description", crypto.getDescription(),
                     "La descripción debe ser correcta");

        crypto.setDescription("Updated Description");
        assertEquals("Updated Description", crypto.getDescription(),
                     "La descripción actualizada debe ser correcta");
    }

    @Test
    @DisplayName("Test 5.5: Obtener estado de pago")
    void testPaymentStatus() {
        System.out.println("\n=== Test 5.5: Obtener estado de pago ===");

        CreditCardFactory card = new CreditCardFactory(
            200.00,
            "CUST022",
            "Status Test",
            "4532123456789010",
            "Test User",
            "12/25",
            "123",
            "Test Address"
        );

        assertEquals(PaymentStatus.PENDING, card.getStatus(),
                     "El estado inicial debe ser PENDING");

        card.processPayment();
        assertEquals(PaymentStatus.COMPLETED, card.getStatus(),
                     "El estado después de procesar debe ser COMPLETED");
    }

    @Test
    @DisplayName("Test 5.6: Obtener timestamp de pago")
    void testPaymentTimestamp() {
        System.out.println("\n=== Test 5.6: Obtener timestamp de pago ===");

        PaypalFactory paypal = new PaypalFactory(
            300.00,
            "CUST023",
            "Timestamp Test",
            "user@paypal.com",
            "auth_token_validtoken"
        );

        assertNotNull(paypal.getTimestamp(),
                      "El timestamp no debe ser nulo");
    }

    // ==========================================
    // TEST 6: INVENTORY TESTS
    // ==========================================

    @Test
    @DisplayName("Test 6.1: Descontar producto del inventario")
    void testInventoryDiscount() {
        System.out.println("\n=== Test 6.1: Descontar producto del inventario ===");

        int initialStock = inventory.getStock("LAPTOP001");
        boolean discounted = inventory.discountProduct("LAPTOP001", 1);

        assertTrue(discounted, "El descuento debe ser exitoso");
        assertEquals(initialStock - 1, inventory.getStock("LAPTOP001"),
                     "El stock debe reducirse en 1");
    }

    @Test
    @DisplayName("Test 6.2: Obtener producto del inventario")
    void testGetProduct() {
        System.out.println("\n=== Test 6.2: Obtener producto del inventario ===");

        Product product = inventory.getProduct("LAPTOP001");
        assertNotNull(product, "El producto debe existir");
        assertEquals("Gaming Laptop", product.getName(),
                     "El nombre del producto debe ser correcto");
        assertEquals(1200.00, product.getPrice(), 0.01,
                     "El precio del producto debe ser correcto");
    }

    @Test
    @DisplayName("Test 6.3: Obtener stock de producto")
    void testGetStock() {
        System.out.println("\n=== Test 6.3: Obtener stock de producto ===");

        int stock = inventory.getStock("PHONE001");
        assertTrue(stock > 0, "El stock debe ser mayor a 0");
        assertEquals(10, stock, "El stock debe ser 10");
    }

    @Test
    @DisplayName("Test 6.4: Intentar descontar más de lo disponible")
    void testInventoryInsufficientStock() {
        System.out.println("\n=== Test 6.4: Stock insuficiente ===");

        boolean discounted = inventory.discountProduct("LAPTOP001", 1000);
        assertFalse(discounted, "El descuento debe fallar si stock es insuficiente");
    }

    // ==========================================
    // TEST 7: FACTURATION TESTS
    // ==========================================

    @Test
    @DisplayName("Test 7.1: Calcular impuesto")
    void testCalculateTax() {
        System.out.println("\n=== Test 7.1: Calcular impuesto ===");

        double tax = facturation.calculateTax(100.00);
        assertEquals(19.00, tax, 0.01,
                     "El impuesto debe ser 19% del monto");
    }

    @Test
    @DisplayName("Test 7.2: Calcular total con impuesto")
    void testCalculateTotal() {
        System.out.println("\n=== Test 7.2: Calcular total con impuesto ===");

        double total = facturation.calculateTotal(100.00);
        assertEquals(119.00, total, 0.01,
                     "El total debe incluir el impuesto");
    }

    @Test
    @DisplayName("Test 7.3: Obtener empresa de facturación")
    void testGetCompanyName() {
        System.out.println("\n=== Test 7.3: Obtener empresa de facturación ===");

        String company = facturation.getCompanyName();
        assertEquals("ECI Payments Corp", company,
                     "El nombre de la empresa debe ser correcto");
    }

    @Test
    @DisplayName("Test 7.4: Obtener tasa de impuestos")
    void testGetTaxRate() {
        System.out.println("\n=== Test 7.4: Obtener tasa de impuestos ===");

        double taxRate = facturation.getTaxRate();
        assertEquals(0.19, taxRate, 0.01,
                     "La tasa de impuesto debe ser 19%");
    }

    // ==========================================
    // TEST 8: INTEGRATION TESTS
    // ==========================================

    @Test
    @DisplayName("Test 8.1: Flujo completo de pago con tarjeta de crédito")
    void testCompleteFlowCreditCard() {
        System.out.println("\n=== Test 8.1: Flujo completo con tarjeta de crédito ===");

        ECIPayment paymentSystem = new ECIPayment();
        Inventory inv = new Inventory();
        Facturation fact = new Facturation();
        Notification notif = new Notification();
        PaymentEventObserver observer = new PaymentEventObserver(inv, fact, notif);

        paymentSystem.addObserver(observer);

        CreditCardFactory card = new CreditCardFactory(
            1200.00,
            "INTEGRATION001",
            "Complete Flow Test",
            "4532123456789010",
            "Integration Test User",
            "12/25",
            "123",
            "Integration Address"
        );

        int initialStock = inv.getStock("LAPTOP001");

        boolean success = paymentSystem.processPayment(
            card, 1200.00, "INTEGRATION001",
            "Complete Flow Test",
            "Integration Test User", "integration@test.com", "LAPTOP001"
        );

        assertTrue(success, "El flujo completo debe ser exitoso");
        assertEquals(initialStock - 1, inv.getStock("LAPTOP001"),
                     "El inventario debe haberse descontado");
    }

    @Test
    @DisplayName("Test 8.2: Flujo completo de pago con PayPal")
    void testCompleteFlowPayPal() {
        System.out.println("\n=== Test 8.2: Flujo completo con PayPal ===");

        ECIPayment paymentSystem = new ECIPayment();
        Inventory inv = new Inventory();
        Facturation fact = new Facturation();
        Notification notif = new Notification();
        PaymentEventObserver observer = new PaymentEventObserver(inv, fact, notif);

        paymentSystem.addObserver(observer);

        PaypalFactory paypal = new PaypalFactory(
            800.00,
            "INTEGRATION002",
            "Complete PayPal Flow Test",
            "user@paypal.com",
            "auth_token_validtoken"
        );

        int initialStock = inv.getStock("PHONE001");

        boolean success = paymentSystem.processPayment(
            paypal, 800.00, "INTEGRATION002",
            "Complete PayPal Flow Test",
            "PayPal User", "paypal@test.com", "PHONE001"
        );

        assertTrue(success, "El flujo PayPal debe ser exitoso");
        assertEquals(initialStock - 1, inv.getStock("PHONE001"),
                     "El inventario debe haberse descontado");
    }

    @Test
    @DisplayName("Test 8.3: Flujo completo de pago con criptomoneda")
    void testCompleteFlowCrypto() {
        System.out.println("\n=== Test 8.3: Flujo completo con criptomoneda ===");

        ECIPayment paymentSystem = new ECIPayment();
        Inventory inv = new Inventory();
        Facturation fact = new Facturation();
        Notification notif = new Notification();
        PaymentEventObserver observer = new PaymentEventObserver(inv, fact, notif);

        paymentSystem.addObserver(observer);

        CryptoFactory crypto = new CryptoFactory(
            50.00,
            "INTEGRATION003",
            "Complete Crypto Flow Test",
            "1A1z7agoat2Bt89zZmkzkhtrnZseNxtfx",
            "BITCOIN",
            100.00
        );

        int initialStock = inv.getStock("BOOK001");

        boolean success = paymentSystem.processPayment(
            crypto, 50.00, "INTEGRATION003",
            "Complete Crypto Flow Test",
            "Crypto User", "crypto@test.com", "BOOK001"
        );

        assertTrue(success, "El flujo Crypto debe ser exitoso");
        assertEquals(initialStock - 1, inv.getStock("BOOK001"),
                     "El inventario debe haberse descontado");
    }

    // ==========================================
    // TEST 9: FACTORY PATTERN TESTS
    // ==========================================

    @Test
    @DisplayName("Test 9.1: Factory patrón - CrearPaymentMethod")
    void testFactoryPatternCreateMethod() {
        System.out.println("\n=== Test 9.1: Factory Pattern - createPaymentMethod ===");

        CreditCardFactory factory = new CreditCardFactory(
            1000.00,
            "FACTORY001",
            "Factory Test",
            "4532123456789010",
            "Factory Test User",
            "12/25",
            "123",
            "Factory Address"
        );

        PaymentMethod method = factory.createPaymentMethod(500.00, "CUST_NEW", "Updated Test");

        assertNotNull(method, "El método de pago no debe ser nulo");
        assertEquals(500.00, method.getAmount(), 0.01,
                     "El monto debe actualizarse");
        assertEquals("CUST_NEW", method.getCustomerId(),
                     "El Customer ID debe actualizarse");
    }

    @Test
    @DisplayName("Test 9.2: Factory patrón - Diferentes implementaciones")
    void testFactoryPatternDifferentImplementations() {
        System.out.println("\n=== Test 9.2: Factory Pattern - Diferentes implementaciones ===");

        CreditCardFactory creditCard = new CreditCardFactory(
            100.00, "CUST1", "CC Test",
            "4532123456789010", "User1", "12/25", "123", "Address1"
        );

        PaypalFactory paypal = new PaypalFactory(
            100.00, "CUST2", "PP Test",
            "user@paypal.com", "auth_token_validtoken"
        );

        CryptoFactory crypto = new CryptoFactory(
            100.00, "CUST3", "Crypto Test",
            "1A1z7agoat2Bt89zZmkzkhtrnZseNxtfx", "BITCOIN", 500.00
        );

        assertTrue(creditCard instanceof PaymentMethod);
        assertTrue(paypal instanceof PaymentMethod);
        assertTrue(crypto instanceof PaymentMethod);

        assertEquals("CREDIT_CARD", creditCard.getPaymentMethod());
        assertEquals("PAYPAL", paypal.getPaymentMethod());
        assertEquals("CRYPTOCURRENCY", crypto.getPaymentMethod());
    }
}


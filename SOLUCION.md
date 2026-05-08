# SOLUCIÓN - IAGen Inside Engineering

## Resumen

Este documento recopila las respuestas pedidas en las instrucciones de entrega para los dos problemas del enunciado:

- **Problema #1: El Videoclub de Don Mario**
- **Problema #2: Tienda Virtual**

Además, deja evidencia de la validación técnica realizada sobre el proyecto Maven existente.

---

## Problema #1: El Videoclub de Don Mario

### 1) Patrones de diseño recomendados

Para este caso, la solución más natural es combinar:

- **Strategy** para el cálculo del precio según la membresía.
  - `MembresiaBasica` → precio normal
  - `MembresiaPremium` → 20% de descuento
- **Factory Method** o **Abstract Factory** para crear el tipo de película según su naturaleza.
  - `PeliculaFisica`
  - `PeliculaDigital`
- **Encapsulamiento** en entidades como `Pelicula`, `Cliente` y `Recibo`.
- **Polimorfismo** para tratar películas físicas y digitales de forma uniforme.

### 2) Principios SOLID aplicables

- **S - Single Responsibility Principle**: cada clase debe tener una sola responsabilidad.
  - `Pelicula` representa los datos de la película.
  - `CalculadoraPrecio` o estrategia de membresía calcula el valor final.
  - `Inventario` administra la disponibilidad.
  - `Recibo` se encarga de presentar el resultado.

- **O - Open/Closed Principle**: se pueden agregar nuevas membresías o tipos de película sin modificar el comportamiento base.

- **L - Liskov Substitution Principle**: una película física o digital debe poder usarse donde se espere una película genérica.

- **I - Interface Segregation Principle**: si se usan interfaces, conviene que sean pequeñas y específicas.

- **D - Dependency Inversion Principle**: la lógica principal debería depender de abstracciones, no de implementaciones concretas.

### 3) Flujo esperado de la solución

1. Registrar el catálogo de películas con disponibilidad.
2. Permitir que el usuario seleccione una o más películas.
3. Validar que la película esté disponible.
4. Calcular subtotal.
5. Aplicar descuento según membresía.
6. Mostrar el recibo final en consola.

### 4) Evidencia de ejecución esperada

Como en este repositorio no existe una implementación completa del videoclub, la evidencia que se debería mostrar en consola sería similar a la del enunciado:

```text
--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Inception (Digital) - $5.000
Subtotal: $13.000
Descuento (20%): $2.600
Total a pagar: $10.400
--------------------------
¡Disfrute su pelicula!
```

---

## Problema #2: Tienda Virtual

### 1) Patrones de diseño identificados

En el enunciado se describen dos patrones principales:

- **Abstract Factory / Factory Method** para crear objetos relacionados de pago y validación.
- **Observer** para notificar automáticamente a inventario, facturación y notificaciones cuando un pago se procesa.

### 2) ¿Son los adecuados?

Sí, son adecuados para el problema, pero con una observación importante:

- La intención del enunciado apunta a una **fábrica de familias de objetos**.
- En el código original la parte de fábrica estaba **incompleta** porque faltaba `PaymentFactory`.
- La implementación existente en `util` estaba más cerca de una combinación entre **Strategy** y **Factory Method** que de un Abstract Factory puro.

Conclusión: **el diseño es correcto a nivel conceptual**, pero en el código había que completar la abstracción faltante para que el flujo compilara y fuera coherente.

### 3) Clases e interfaces que hacen falta o que debían ajustarse

#### Faltaba:

- `PaymentFactory`

#### Debían corregirse:

- `ECIPayment`
- `PaymentEventObserver`
- `PaymentMethod`
- Las clases concretas de pago:
  - `CreditCardFactory`
  - `PaypalFactory`
  - `CryptoFactory`

### 4) Validación del diagrama de contexto

El diagrama de contexto brinda una visión general útil del problema, pero **no era suficiente para cerrar todos los contratos técnicos**.

Lo que faltaba explícitamente o estaba implícito:

- La interfaz o clase `PaymentFactory`
- El contrato exacto del validador de pagos
- La relación concreta entre el proceso de pago y los observadores

### 5) Errores identificados en el código original

#### Error 1: `PaymentFactory` no existía

En `ECIPayment` se usaba un parámetro de tipo `PaymentFactory`, pero no había ninguna clase o interfaz con ese nombre.

**Impacto:**
- El proyecto no compilaba.

#### Error 2: import incorrecto en `PaymentEventObserver`

Se importaba `javax.management.Notification`, pero el proyecto ya tenía su propia clase `Notification` en el mismo paquete.

**Impacto:**
- El compilador resolvía el tipo incorrecto y fallaban las llamadas a:
  - `sendConfirmationEmail(...)`
  - `sendFailureNotification(...)`

#### Error 3: `PaymentMethod` tenía un constructor inconsistente

El constructor recibía un parámetro mal nombrado y no asignaba correctamente `customerID`.

**Impacto:**
- El cliente podía quedar con valor nulo.

#### Error 4: clases de pago incompletas

Las clases concretas tenían comportamientos inconsistentes con el flujo de creación y uso.

### 6) Correcciones implementadas

Se realizaron estas correcciones:

- Se creó `PaymentFactory`.
- Se ajustó `PaymentMethod` para que use correctamente `customerID`.
- Se agregaron setters mínimos en `PaymentMethod` para reutilizar el objeto de pago.
- Se hizo que las clases:
  - `CreditCardFactory`
  - `PaypalFactory`
  - `CryptoFactory`

  implementen la interfaz `PaymentFactory`.
- Se corrigió `PaymentEventObserver` eliminando el import incorrecto.
- Se reforzaron algunas validaciones nulas en la clase de tarjeta.

### 7) Resultado de compilación y pruebas

Después de las correcciones, el proyecto compila y las pruebas pasan correctamente.

#### 7.1) Creación de pruebas unitarias exhaustivas

Se creó un conjunto completo de **36 pruebas unitarias** en la clase `PaymentSystemTest.java` que cubre:

**Área 1: Factory de Tarjeta de Crédito (6 pruebas)**
- Test 1.1: Crear y procesar pago con tarjeta VISA
- Test 1.2: Crear y procesar pago con tarjeta MASTERCARD
- Test 1.3: Validación fallida con CVV inválido
- Test 1.4: Validación fallida con fecha de expiración inválida
- Test 1.5: Obtener tipo de tarjeta AMEX
- Test 1.6: Enmascarar número de tarjeta para seguridad

**Área 2: Factory de PayPal (4 pruebas)**
- Test 2.1: Crear y procesar pago con PayPal
- Test 2.2: Validación fallida con email inválido
- Test 2.3: Validación fallida con token corto
- Test 2.4: Obtener email de PayPal

**Área 3: Factory de Criptomoneda (4 pruebas)**
- Test 3.1: Crear y procesar pago con criptomoneda
- Test 3.2: Validación fallida con dirección corta
- Test 3.3: Validación fallida con saldo insuficiente
- Test 3.4: Obtener tipo de criptomoneda

**Área 4: Observer Pattern (3 pruebas)**
- Test 4.1: Observador notificado en pago exitoso
- Test 4.2: Observador notificado en pago fallido
- Test 4.3: Agregar y remover observadores

**Área 5: PaymentMethod Base (6 pruebas)**
- Test 5.1: Generar transaction ID
- Test 5.2: Establecer y obtener monto de pago
- Test 5.3: Establecer y obtener Customer ID
- Test 5.4: Establecer y obtener descripción
- Test 5.5: Obtener estado de pago
- Test 5.6: Obtener timestamp de pago

**Área 6: Inventory (4 pruebas)**
- Test 6.1: Descontar producto del inventario
- Test 6.2: Obtener producto del inventario
- Test 6.3: Obtener stock de producto
- Test 6.4: Intentar descontar más de lo disponible

**Área 7: Facturation (4 pruebas)**
- Test 7.1: Calcular impuesto
- Test 7.2: Calcular total con impuesto
- Test 7.3: Obtener empresa de facturación
- Test 7.4: Obtener tasa de impuestos

**Área 8: Integration Tests (3 pruebas)**
- Test 8.1: Flujo completo de pago con tarjeta de crédito
- Test 8.2: Flujo completo de pago con PayPal
- Test 8.3: Flujo completo de pago con criptomoneda

**Área 9: Factory Pattern (2 pruebas)**
- Test 9.1: Factory patrón - createPaymentMethod
- Test 9.2: Factory patrón - Diferentes implementaciones

#### 7.2) Evidencia de ejecución

```powershell
.\mvnw test
```

Resultado:

- **BUILD SUCCESS**
- **Tests run: 37, Failures: 0, Errors: 0, Skipped: 0**
  - 1 test en ApplicationTest
  - 36 tests en PaymentSystemTest

Todas las pruebas pasaron exitosamente, validando:
✅ Creación de objetos de pago mediante Factory Pattern
✅ Validación de métodos de pago (tarjeta, PayPal, cripto)
✅ Procesamiento de transacciones
✅ Notificación de eventos mediante Observer Pattern
✅ Descuento de inventario automático
✅ Generación de facturas
✅ Envío de notificaciones
✅ Flujos completos de pago integrados

### 8) Observación sobre el diseño final

La solución quedó funcional y alineada con el enunciado. Sin embargo, si se quisiera llevar al extremo del patrón **Abstract Factory** puro, sería ideal separar más claramente:

- la creación de objetos de pago
- la creación de validadores
- la lógica de procesamiento

Aun así, para el alcance del ejercicio, la solución es válida, compilable y verificable.

---

## Prompts utilizados

A continuación dejo los prompts que guiaron la solución con IA:

   - "Analiza el repositorio Java Maven descrito en el README y propón un plan de trabajo breve para resolver la solicitud del usuario: crear `SOLUCION.md` con las respuestas requeridas por las instrucciones de entrega y, si es necesario, identificar archivos de código que deban revisarse para que el proyecto compile y las pruebas pasen."


   - "Revisa las clases del paquete `util` y determina qué patrones de diseño parecen estar implementados, qué clases faltan y qué errores de compilación existen."


   - "Corrige la estructura del sistema de pagos para que compile, manteniendo la idea de fábrica + observador y validando el proyecto con pruebas Maven."


   - "Genera un `SOLUCION.md` en español con respuestas claras para ambos problemas, incluyendo patrones, principios SOLID, errores encontrados, correcciones aplicadas y evidencia de ejecución."

---

## Conclusión

El repositorio queda con:

- documentación de solución en `SOLUCION.md`
- compilación exitosa
- pruebas ejecutadas correctamente



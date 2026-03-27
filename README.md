
# 🚀 Spring Boot Payment Command Pattern

This project demonstrates a robust implementation of the **Command Design Pattern** using Spring Boot to handle complex, multi-step payment workflows in a scalable and maintainable way.

Perfect for systems like:
- Payment Gateway
- Checkout Processing
- Order Workflow Orchestration

## 🧠 Core Concepts
This project combines several powerful design approaches:

### Command Pattern

Each action is encapsulated into its own class:
- GopayCommand
- CreditCardCommand
- InventoryCommand

👉 Benefits:
- Decoupled business logic
- Easy to extend new payment methods
- Clean and maintainable code

### Dynamic Validation (JSR-303 Groups)

Validation is applied dynamically based on the selected payment method.

Example:
- Credit Card → requires `cardNumber`, `cvv`
- Gopay → requires `phoneNumber`

```

validator.validate(request, bindingResult, payment.getValidationGroup());

```
👉 Benefits:
- No messy if-else validation
- Clean separation of concerns

### Macro Command (Execution Orchestration)
All commands are executed sequentially using:
```

PaymentMacroExecutor

```
Example flow:
```

Payment → Inventory

```

### Automatic Rollback (Manual Compensation)

If any step fails:

👉 All previously executed steps are automatically rolled back
```

private void rollback() {
    while (!history.isEmpty()) {
        history.pop().refund(request);
    }
}

```

👉 Similar to:
- Saga Pattern
- Compensation Transactions

## ⚙️ Architecture Overview

```

Controller
   ↓
CheckoutService
   ↓
Command Layer (Strategy + Command Pattern)
   ↓
MacroExecutor (Orchestration + Rollback)

```

##  📦 Project Structure

```

command/
 ├── PaymentCommand.java
 ├── GopayCommand.java
 ├── CreditCardCommand.java
 └── InventoryCommand.java

service/
 ├── CheckoutService.java
 └── CheckoutServiceImpl.java

executor/
 └── PaymentMacroExecutor.java

dto/
 └── PaymentRequest.java

config/
 └── ValidationGroups.java

exception/
 ├── CustomValidationException.java
 └── GlobalExceptionHandler.java

```

## 🔥 API Example

### POST /checkout

```

{
  "method": "GOPAY",
  "amount": 50000,
  "phoneNumber": "08123456789"
}

```

### Request (CREDIT_CARD)

```

{
  "method": "CREDIT_CARD",
  "amount": 100000,
  "cardNumber": "4111111111111111",
  "cvv": "123"
}

```



### ✅ Success Response

> "Transaction Successful!"



### ❌ Validation Error Response

```

{
  "status": "VALIDATION_FAILED",
  "errors": {
    "cardNumber": "Card number is required",
    "cvv": "CVV is required"
  }
}

```


### 💥 Rollback Scenario Example

Flow:

```

1. Gopay succeeds 
2. Inventory fails (out of stock)

```
👉 Result:
> Gopay refund is automatically executed


### 🧪 Unit Test (Rollback Verification)

This test proves that:

-   First command succeeds
-   Second command fails
-   First command is rolled back

```

verify(gopay, times(1)).refund(request);
verify(inventory, times(0)).refund(request);

```

👉 Key insight:

> Only successfully executed commands are rolled back


## 🧩 Why This Design is Powerful

  

Without this pattern, you might end up with:

```

if (method.equals("GOPAY")) { ... }
else if (method.equals("CC")) { ... }

```

❌ Problems:

-   Hard to scale
-   Messy logic
-   Difficult to maintain



With this approach:

- Open/Closed Principle  
- Easily extend new payment methods  
- Clean orchestration  
- Built-in rollback mechanism



## 🚀 How to Extend
### Add New Payment Method (Example: QRIS)

1. Create new class:

```

@Component("QRIS")
public class QrisCommand implements PaymentCommand {
    ...
}

```

2.  Add validation group
3.  Done — automatically registered in `commandMap`

**🔥 No need to modify existing service!**


## 🧠 Senior-Level Insight

This design is actually a combination of:

-   Command Pattern
-   Strategy Pattern
-   Chain of Responsibility (implicit flow)
-   Saga Pattern (manual compensation)

👉 Suitable for:

-   Lightweight orchestration
-   Payment systems
-   Order processing workflows

## ⚠️ Limitations

-   No distributed transaction (not a full Saga across services)
-   Rollback is synchronous
-   No idempotency handling yet

## 🧑‍💻 Author

Built with a focus on:
-   Scalability
-   Clean Architecture
-   Real-world backend challenges


## ⭐ Support
If this project helps you:
-   ⭐ Star this repository
-   Share it with your team
-   Fork it and experiment

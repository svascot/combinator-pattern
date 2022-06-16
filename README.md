# combinator-pattern
Combinator pattern to implement a validator that allows to create custom validations e.g. Given the object User with some
fields. We can implement a full object validation or implement partial validations.
```java

// Business Object to be validated.
class Customer {
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final LocalDate dob;
}    

// Service class.
class Service {

    // Full object validation.
    EnumSet<CustomerValidationResult> result = EnumSetApproachValidator.isEmailValid()
            .and(EnumSetApproachValidator.isPhoneNumberValid())
            .and(EnumSetApproachValidator.isAdult())
            .apply(badCustomer);

    // Partial validation. email and age.
    EnumSet<CustomerValidationResult> result = EnumSetApproachValidator.isEmailValid()
            .and(EnumSetApproachValidator.isAdult())
            .apply(badCustomer);

    // Partial validation. phone and email.
    EnumSet<CustomerValidationResult> result = EnumSetApproachValidator.isEmailValid()
            .and(EnumSetApproachValidator.isPhoneNumberValid())
            .apply(badCustomer);
}
```
The idea is to create an interface that extends Function<BusinessObject, ValidationResult>, Implement an EnumSet with the
states e.g. `SUCCESS, FIELD_ONE_NOT_VALID, FIELD_TWO_NOT_VALID` implement the business validation in Functions that takes
this interface as argument and return the EnumSet state. Also implement a default method `and` with this interface argument
to perform the "next" validation.

```java
public interface EnumSetApproachValidator extends Function<Customer, EnumSet<CustomerValidationResult>> {

    EnumSet<CustomerValidationResult> SUCCESS_ONLY = EnumSet.of(SUCCESS);

    static EnumSetApproachValidator isEmailValid() {
        return customer -> customer.getEmail().contains("@") ?
                SUCCESS_ONLY : EnumSet.of(EMAIL_NOT_VALID);
    }

    static EnumSetApproachValidator isPhoneNumberValid() {
        return customer -> customer.getPhoneNumber().startsWith("+1") ?
                SUCCESS_ONLY : EnumSet.of(PHONE_NUMBER_NOT_VALID);
    }

    default EnumSetApproachValidator and(EnumSetApproachValidator other){
        return customer -> {
            EnumSet<CustomerValidationResult> thisResult = this.apply(customer);
            EnumSet<CustomerValidationResult> otherResult = other.apply(customer);

            if(thisResult.equals(SUCCESS_ONLY)) return otherResult;
            if(otherResult.equals(SUCCESS_ONLY)) return thisResult;

            EnumSet<CustomerValidationResult> combinedResult = EnumSet.copyOf(thisResult);
            combinedResult.addAll(otherResult);
            return combinedResult;
        };
    }

    enum CustomerValidationResult{
        SUCCESS,
        PHONE_NUMBER_NOT_VALID,
        EMAIL_NOT_VALID,
        IS_NOT_AN_ADULT
    }
}
```

The way to use this validation will be:
```java
EnumSet<CustomerValidationResult> resultBadCostumer = EnumSetApproachValidator.isEmailValid()
                .and(EnumSetApproachValidator.isPhoneNumberValid())
                .and(EnumSetApproachValidator.isAdult())
                .apply(badCustomer);
```

To execute this project go to Main class and run the main method.
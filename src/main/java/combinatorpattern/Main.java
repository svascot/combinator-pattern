package combinatorpattern;

import java.time.LocalDate;

import static combinatorpattern.CustomerRegistrationValidator.*;

public class Main {
    public static void main(String[] args) {

        Customer customer = new Customer(
                "Santiago",
                "svascot@gmail.com",
                "+12014336900",
                LocalDate.of(1992, 8, 14)
        );

        Customer badCustomer = new Customer(
                "Santiago",
                "svascotgmail.com",
                "+02014336900",
                LocalDate.of(1992, 8, 14)
        );

        // e.g. if valid we cna store de Custmer in the DB
        System.out.println(new CustomerValidatorService().isValid(customer));

        // Using combinator pattern
        ValidationResult result = isEmailValid()
                .and(isPhoneNumberValid())
                .and(isAdult())
                .apply(badCustomer);

        System.out.println(result);

        if(result != ValidationResult.SUCCESS){
            throw new IllegalStateException(result.name());
        }
    }
}

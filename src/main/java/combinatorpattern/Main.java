package combinatorpattern;

import java.time.LocalDate;
import java.util.EnumSet;

import static combinatorpattern.CustomerRegistrationValidator.*;
import static combinatorpattern.EnumSetApproachValidator.*;

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
        //System.out.println(new CustomerValidatorService().isValid(customer));

        // Using combinator pattern
        /*ValidationResult result = isEmailValid()
                .and(isPhoneNumberValid())
                .and(isAdult())
                .apply(badCustomer);

        System.out.println(result);

        if(result != ValidationResult.SUCCESS){
            throw new IllegalStateException(result.name());
        }*/

        // Using combined result approach
        EnumSet<CustomerValidationResult> result = EnumSetApproachValidator.isEmailValid()
                .and(EnumSetApproachValidator.isAdult())
                .apply(customer);

        EnumSet<CustomerValidationResult> resultBadCostumer = EnumSetApproachValidator.isEmailValid()
                .and(EnumSetApproachValidator.isPhoneNumberValid())
                .and(EnumSetApproachValidator.isAdult())
                .apply(badCustomer);

        System.out.println(result);
        System.out.println(resultBadCostumer);

        if(!result.equals(SUCCESS_ONLY)) throw new IllegalStateException(result.toString());
        if(!resultBadCostumer.equals(SUCCESS_ONLY)) throw new IllegalStateException(resultBadCostumer.toString());
    }
}

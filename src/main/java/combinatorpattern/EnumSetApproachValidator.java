package combinatorpattern;

import java.time.LocalDate;
import java.time.Period;
import java.util.EnumSet;
import java.util.function.Function;

import static combinatorpattern.EnumSetApproachValidator.*;
import static combinatorpattern.EnumSetApproachValidator.CustomerValidationResult.*;

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

    static EnumSetApproachValidator isAdult() {
        return customer -> Period.between(customer.getDob(), LocalDate.now()).getYears() > 16 ?
                SUCCESS_ONLY : EnumSet.of(IS_NOT_AN_ADULT);
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

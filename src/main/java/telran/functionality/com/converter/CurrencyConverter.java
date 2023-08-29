package telran.functionality.com.converter;

import org.springframework.stereotype.Component;
import telran.functionality.com.enums.ConvertingValue;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.exceptions.UnsupportedConvertingTypesOfCurrencyException;

import java.util.Arrays;

@Component
public class CurrencyConverter {

    public double convertCurrency(double amount, Currency fromCurrency, Currency toCurrency) {
        String toConvert = fromCurrency.name() + "TO" + toCurrency.name();
        if (!Arrays.stream(ConvertingValue.values()).map(Enum::name).toList().contains(toConvert)) {
            throw new UnsupportedConvertingTypesOfCurrencyException(
                    String.format("Unable to convert money from %s to %s, because this type of converting is not supported in the Bank.",
                            fromCurrency.name(), toCurrency.name()));
        }
        double currentCoefficient = ConvertingValue.valueOf(toConvert).getCoefficient();
        return amount * currentCoefficient;
    }

}

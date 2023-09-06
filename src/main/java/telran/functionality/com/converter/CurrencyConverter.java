package telran.functionality.com.converter;

/**
 * Class - Converter for the Currency
 *
 * @author Olena Averchenko
 * */

import telran.functionality.com.enums.ConvertingValue;
import telran.functionality.com.enums.Currency;
import telran.functionality.com.exceptions.UnsupportedConvertingTypesOfCurrencyException;

import java.util.Arrays;

public class CurrencyConverter {

    /**
     * Method for currency conversion from one type to another according to the current exchange rate
     * @param amount sum to convert
     * @param fromCurrency initial currency
     * @param fromCurrency currency to convert
     * @return double amount - converted value
     * */

    public static double convertCurrency(double amount, Currency fromCurrency, Currency toCurrency) {
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
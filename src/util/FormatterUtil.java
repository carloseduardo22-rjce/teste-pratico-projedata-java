package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatterUtil {

	private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final NumberFormat CURRENCY_FORMATTER = 
        NumberFormat.getCurrencyInstance(LOCALE_BR);
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatCurrency(BigDecimal value) {
        return CURRENCY_FORMATTER.format(value);
    }

    public static String formatDate(LocalDate date) {
        return DATE_FORMATTER.format(date);
    }
	
}

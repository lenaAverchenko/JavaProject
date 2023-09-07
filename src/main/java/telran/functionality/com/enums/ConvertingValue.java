package telran.functionality.com.enums;
/**
 * Class - enum, for storing coefficients to convert currencies
 *
 * @author Olena Averchenko
 */
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConvertingValue {
    UAHTOUSD(0.027),
    UAHTOEUR(0.025),
    UAHTOPLN(0.11),
    PLNTOUSD(0.24),
    PLNTOEUR(0.22),
    PLNTOUAH(8.92),
    USDTOPLN(4.14),
    USDTOEUR(0.93),
    USDTOUAH(36.92),
    EURTOPLN(4.47),
    EURTOUSD(1.08),
    EURTOUAH(39.90);


    private double coefficient;
}

package be.sixefyle.transdimquarry.utils;

import java.text.DecimalFormat;

public class NumberUtil {

    public static String formatToEnergy(double number) {
        if (number < 1000) {
            return String.valueOf(number);
        }

        String[] suffixes = {" FE", " kFE", " MFE", " GFE", " TFE"};
        int suffixIndex = 0;
        double formatedNumber = number;

        while (formatedNumber >= 1000) {
            formatedNumber /= 1000;
            suffixIndex++;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(formatedNumber) + suffixes[suffixIndex];
    }
}

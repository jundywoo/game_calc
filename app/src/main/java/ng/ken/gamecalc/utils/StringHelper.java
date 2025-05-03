package ng.ken.gamecalc.utils;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringHelper {

    public static final DecimalFormat DECIMAL_FORMAT_MAX_2_DIGITS = new DecimalFormat("#.##");

    public static String toHumanDigits(double avg) {
        return DECIMAL_FORMAT_MAX_2_DIGITS.format(avg);
    }

    public static String toHumanAvgAndsum(double avg, int sum) {
        return String.format("%s / %d", DECIMAL_FORMAT_MAX_2_DIGITS.format(avg - sum), sum);
    }

    public static String getLevelUpTitle(int levelUp) {
        return " 升" + (levelUp == 1 ? "" : levelUp) + "級?";
    }

}

package ng.ken.gamecalc.utils;

import static ng.ken.gamecalc.utils.Constants.BIG2_SCORE_SUMMARY;

import java.text.DecimalFormat;
import java.util.Locale;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringHelper {

    public static final DecimalFormat DECIMAL_FORMAT_MAX_2_DIGITS = new DecimalFormat("#.##");

    public static String toHumanDigits(double avg) {
        return DECIMAL_FORMAT_MAX_2_DIGITS.format(avg);
    }

    public static String getBig2Title(double avg) {
        return String.format(Locale.getDefault(), "%s (平均: %s)", BIG2_SCORE_SUMMARY, DECIMAL_FORMAT_MAX_2_DIGITS.format(avg));
    }

    public static String toHumanAvgAndSum(double avg, int sum) {
        return String.format(Locale.getDefault(), "%s - %d", DECIMAL_FORMAT_MAX_2_DIGITS.format(avg - sum), sum);
    }

    public static String getLevelUpTitle(int levelUp) {
        return " 升" + (levelUp == 1 ? "" : levelUp) + "級?";
    }

}

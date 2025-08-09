package ng.ken.gamecalc.points24;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

public class Points24Calculator {

    public static final List<String> CARDS = List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");
    private final List<Integer> cards;
    @Getter
    private final Set<String> solutions = new HashSet<>();
    @Getter
    private final boolean solved;

    public Points24Calculator(String a, String b, String c, String d) {
        this(
                CARDS.indexOf(a) + 1,
                CARDS.indexOf(b) + 1,
                CARDS.indexOf(c) + 1,
                CARDS.indexOf(d) + 1
        );
    }

    public Points24Calculator(int a, int b, int c, int d) {
        cards = List.of(a, b, c, d);

        solve(cards, new ArrayList<>());
        solved = !solutions.isEmpty();
    }

    // 将牌转换为字符串表示
    public String cardsToString() {
        StringBuilder sb = new StringBuilder();
        for (int card : cards) {
            sb.append(CARDS.get(card - 1)).append(" ");
        }
        return sb.toString().trim();
    }

    // 递归求解24点
    private void solve(List<Integer> nums, List<String> expressions) {
        if (nums.size() == 1) {
            if (Math.abs(nums.get(0) - 24) < 1e-6) {
                String exp = expressions.get(0).replaceAll("\\.0", "");
                solutions.add(exp.substring(1, exp.length() - 1));
                return;
            }
            return;
        }
        for (int i = 0; i < nums.size(); i++) {
            for (int j = 0; j < nums.size(); j++) {
                if (i != j) {
                    List<Integer> nextNums = new ArrayList<>();
                    List<String> nextExpressions = new ArrayList<>();
                    for (int k = 0; k < nums.size(); k++) {
                        if (k != i && k != j) {
                            nextNums.add(nums.get(k));
                            nextExpressions.add(expressions.isEmpty() ? String.valueOf(nums.get(k)) : expressions.get(k));
                        }
                    }
                    int a = nums.get(i), b = nums.get(j);
                    String expA = expressions.isEmpty() ? String.valueOf(a) : expressions.get(i);
                    String expB = expressions.isEmpty() ? String.valueOf(b) : expressions.get(j);

                    // 尝试所有运算
                    if (a > b) {
                        addNext(nextNums, nextExpressions, a + b, "(" + expA + " + " + expB + ")");
                        addNext(nextNums, nextExpressions, a - b, "(" + expA + " - " + expB + ")");
                        addNext(nextNums, nextExpressions, a * b, "(" + expA + " * " + expB + ")");
                    }
                    if (b > a)
                        addNext(nextNums, nextExpressions, b - a, "(" + expB + " - " + expA + ")");
                    if (b != 0 && a % b == 0)
                        addNext(nextNums, nextExpressions, a / b, "(" + expA + " / " + expB + ")");
                    if (a != 0 && b % a == 0)
                        addNext(nextNums, nextExpressions, b / a, "(" + expB + " / " + expA + ")");
                }
            }
        }
    }

    // 添加下一步运算
    private void addNext(List<Integer> nums, List<String> expressions, int result, String expression) {
        nums.add(result);
        expressions.add(expression);
        solve(nums, expressions);
        nums.remove(nums.size() - 1);
        expressions.remove(expressions.size() - 1);
    }

}

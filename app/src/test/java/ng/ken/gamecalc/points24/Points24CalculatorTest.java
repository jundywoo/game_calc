package ng.ken.gamecalc.points24;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Points24CalculatorTest extends TestCase {
    /**
     * 测试能否通过四则运算得到24点
     */
    public void testCan_run_24_point_compute() {
        List<Integer> cards = drawCards();
        Points24Calculator calc = new Points24Calculator(cards.get(0), cards.get(1), cards.get(2), cards.get(3));
        System.out.println("抽取的牌: " + calc.cardsToString());
        if (calc.isSolved()) {
            System.out.println("可以通过四则运算得到24点！");
            System.out.println("算式如下：");
            calc.getSolutions().forEach(System.out::println);
        } else {
            System.out.println("无法通过四则运算得到24点！");
        }
    }

    // 随机抽取4张牌
    private List<Integer> drawCards() {
        Random random = new Random();
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cards.add(random.nextInt(13) + 1); // 1-13对应A-K
        }
        return cards;
    }
}
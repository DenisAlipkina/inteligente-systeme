package AuffindenVonStrukturen;

import java.util.List;

public class MainZumTestendesKriteriums {
    public static void main(String[] args) {
        EvalZusammenhang eval = new EvalZusammenhang("src/main/java/AuffindenVonStrukturen/Daten/data0.csv", "src/main/java/AuffindenVonStrukturen/Daten/label0.csv");
        List<Integer> list_diff_sum = eval.diff_der_summe_umkreis();
        System.out.println(list_diff_sum.stream().mapToInt(Integer::intValue).min());
        System.out.println(list_diff_sum.stream().mapToInt(Integer::intValue).average());
        System.out.println(list_diff_sum.stream().mapToInt(Integer::intValue).max());
    }
}

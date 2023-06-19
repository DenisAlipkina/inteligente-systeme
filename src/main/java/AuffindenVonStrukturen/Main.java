package AuffindenVonStrukturen;

import ReisezeitOptimierung.Tuple;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        //Datenleser datenleser = new Datenleser();
        //datenleser.read2DCSV("src/main/java/AuffindenVonStrukturen/Daten/data0.csv");
        //Zum Testen
        Labelleser labelleser = new Labelleser();
        labelleser.readCSVLabel("src/main/java/AuffindenVonStrukturen/Daten/label0.csv");
        Evaluation evaluation = new Evaluation();
        List<Tuple<Integer, Integer>> label = labelleser.getCsvLabel();
        System.out.println(label.size());
        System.out.println(evaluation.recall(label, label.subList(0, 222)));
        System.out.println(evaluation.precision(label, label));
        System.out.println(evaluation.precision(label, label.subList(0, 221)));
        double recall = evaluation.recall(label, label.subList(0, 222));
        double precision = evaluation.precision(label, label);
        System.out.println(evaluation.f_score(recall, precision));

//        List<Tuple<Integer, Integer>> tupleList = new LinkedList<>();
//        List<List<Integer>> a = datenleser.getCsvDaten();
//        System.out.println(a.get(83).get(2554));
//        System.out.println(a.get(1).get(1));
//        System.out.println(a.get(0).get(0));
//        System.out.println(a);
    }
}

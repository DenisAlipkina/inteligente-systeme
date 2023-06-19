package AuffindenVonStrukturen;

import ReisezeitOptimierung.Tuple;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Eval2 {
    private final Landschaft landschaft;
    private final List<Tuple<Integer, Integer>> labels; // tupel.fst() = y, tuple.snd() = x

    public Eval2(Landschaft l, String pathlabels) {
        Labelleser labelleser = new Labelleser();
        try {
            labelleser.readCSVLabel(pathlabels);
        } catch (Exception ignored) {
        }
        this.landschaft = l;
        this.labels = labelleser.getCsvLabel();
    }

    public void run() {
        int true_counter = 0;
        int false_counter = 0;
        landschaft.runTest();
        //labels.forEach(t -> landschaft.getPotenzielle_Labels().forEach(ls -> ls.stream().anyMatch(s -> s.isEqualTo(Stelle.stelle(t.snd(), t.fst(), 0)))));
        boolean found = false;
        List<Stelle> nicht_gefundene_labels = new LinkedList<>();
        for(Tuple<Integer, Integer> t : labels) {
            for(List<Stelle> ls : landschaft.getPotenzielle_Labels()) {
                if(ls.stream().anyMatch(s -> s.isEqualTo(Stelle.stelle(t.snd(), t.fst(), 0)))) {
                    found = true;
                }
            }
            if(found) true_counter++;
            else {
                false_counter++;
                nicht_gefundene_labels.add(landschaft.getStelle(t.snd(), t.fst()));
            }
            found = false;

        }
        System.out.println("Anzahl potenzielle Labels: " + landschaft.getPotenzielle_Labels().size());
        System.out.println("Potenzielle Labels: " + landschaft.getPotenzielle_Labels());
        System.out.println("Nicht gefundene Labels: " + nicht_gefundene_labels);
        List<Stelle> tmp = new LinkedList<>();
        for(List<Stelle> ls : landschaft.getPotenzielle_Labels()) {
            for(Stelle s : ls) {
                tmp.add(s);
            }
        }
        System.out.println("Egal: " + tmp.size());
        System.out.println("Egal: " + tmp.stream().distinct().collect(Collectors.toList()).size());



        System.out.println("Es wurden " + true_counter + " richtige und " + false_counter + " falsche gefunden");
    }

    public List<List<Stelle>> getGefundeneMaximas() {
        return landschaft.getPotenzielle_Labels();
    }
}

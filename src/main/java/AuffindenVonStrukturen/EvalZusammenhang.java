package AuffindenVonStrukturen;

import ReisezeitOptimierung.Tuple;

import java.util.LinkedList;
import java.util.List;

import static AuffindenVonStrukturen.Landschaft.landschaft;

public class EvalZusammenhang {
    private final Landschaft landschaft;
    private final List<Tuple<Integer, Integer>> labels; // tupel.fst() = y, tuple.snd() = x

    public EvalZusammenhang(String pathlandschaft, String pathlabels) {
        Labelleser labelleser = new Labelleser();
        try {
            labelleser.readCSVLabel(pathlabels);
        } catch (Exception ignored) {

        }
        this.landschaft = landschaft(pathlandschaft);
        this.labels = labelleser.getCsvLabel();
    }

    public List<Integer> diff_der_summe_umkreis() {
        List<Integer> result = new LinkedList<>();
        for(Tuple<Integer, Integer> t: labels) {
            result.add(landschaft.getStelle(t.snd(), t.fst()).getWert() - landschaft.getStelle(t.snd(), t.fst()).getUmkreisWert(1));
            if(landschaft.getStelle(t.snd(), t.fst()).getWert() - landschaft.getStelle(t.snd(), t.fst()).getUmkreisWert(1) == 9) {
                System.out.println("9: " + t);
            }
        }
        return result;
    }
}

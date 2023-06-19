package AuffindenVonStrukturen;

import ReisezeitOptimierung.Tuple;

import java.util.List;

public class Evaluation {

    /**
     * Berechnet den Recall Wert
     * Berechnung erfolgt anhand Allen Korrekten Labeln und den Gefunden Korrekten Labeln
     * Formel: R = |Gefundene Korrekte Label| / |Alle Korrekten Label|
     *
     * @param alleKorrektenLabel
     * @param gefundeneKorreteLabel
     * @return Recall Wert
     */
    public double recall(List<Tuple<Integer,Integer>> alleKorrektenLabel, List<Tuple<Integer, Integer>> gefundeneKorreteLabel) {
        return ((double) gefundeneKorreteLabel.size() / alleKorrektenLabel.size());
    }

    /**
     * Berechnet den Precision Wert
     * Berechnung erfolgt anhand Allen Gefunden Label und den Gefundenen Korrekten Labeln
     * Formel: P = |Gefundene Korrekte Label| / |Alle Gefundenen Label|
     *
     * @param alleGefundenLabel
     * @param gefundeneKorreteLabel
     * @return Precision Wert
     */
    public double precision(List<Tuple<Integer,Integer>> alleGefundenLabel, List<Tuple<Integer, Integer>> gefundeneKorreteLabel) {
        return ((double) gefundeneKorreteLabel.size() / alleGefundenLabel.size());
    }

    /**
     * Berechnet den F-Score
     * Berechnung erfolgt anhand des Recall und dem Precision Wert
     * Formel: F = (2 * P * R) / (P + R)
     *
     * @param recall
     * @param precision
     * @return F-Score
     */
    public double f_score(double recall, double precision) {
        return ((2 * precision * recall) / (precision + recall));
    }
}

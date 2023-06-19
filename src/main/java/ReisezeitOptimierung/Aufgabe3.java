package ReisezeitOptimierung;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import static ReisezeitOptimierung.DateiSpeicherung.*;
import static ReisezeitOptimierung.Hauptbahnhof.hauptbahnhofPercentileFürSchlangenLänge;
import static ReisezeitOptimierung.Strassenbahn.*;
import static ReisezeitOptimierung.Taxi.taxi;

public class Aufgabe3 {

    // jede Schlangenlänge betrachten und dann den Schwellwert anschauen
    // Percienttiel bestimmt als Schwellwert


    public static void main(String[] args) {
        // Anlegen zweier Listen für das Speichern der Reisezeiten
        List<Tuple<Integer, Double>> reisezeitenStrassenbahn = new LinkedList<>();
        List<Triple<Integer, Integer, Double>> reisezeitenTaxi = new LinkedList<>();
        List<Tuple<String, Double>> reisezeiten = new LinkedList<>();
        // Anlegen von Strassenbahn und Taxi mit entsprechenden Werten
        Strassenbahn str = strassenbahn(0, 0, 300);
        Taxi taxi = taxi(0, 10, 1);
        // Bahnhof zur Berechnung der Reisezeiten und Wartezeiten anlegen
        Hauptbahnhof hauptbahnhof = hauptbahnhofPercentileFürSchlangenLänge(str, taxi);

        int anzahlTaxi = 0;
        int anzahlStrassenbahn = 0;
            for (int i = 0; i < 50000; i++) {
                hauptbahnhof.setStrassenbahn(strassenbahn(0, 0, 300));
                hauptbahnhof.setTaxi(taxi(0, 10, 1));
                if (hauptbahnhof.wartenAufBeidesMitSchlangenLänge() == 1) {
                    anzahlTaxi++;
                    reisezeitenTaxi.add(Triple.triple(hauptbahnhof.getTaxi().getSchlange().getInitial_Länge(), hauptbahnhof.getTaxi().getSchlange().getEntscheidungs_Länge(), hauptbahnhof.getTaxi().getTotalZeit()));
                    reisezeiten.add(Tuple.tuple("Taxi", hauptbahnhof.getTaxi().getTotalZeit()));
                } else {
                    anzahlStrassenbahn++;
                    reisezeitenStrassenbahn.add(Tuple.tuple(hauptbahnhof.getStrassenbahn().getSchlange().getInitial_Länge(), hauptbahnhof.getStrassenbahn().getTotalZeit()));
                    reisezeiten.add(Tuple.tuple("Strassenbahn", hauptbahnhof.getStrassenbahn().getTotalZeit()));
                }
            }
        // nur zum Speichern der CSV
        saveChoicesTuple(reisezeiten, "Aufgabe3");
        saveChoicesQueueTriple(reisezeitenTaxi, "Aufgabe3_Taxi");
        saveChoicesQueueTuple(reisezeitenStrassenbahn, "Aufgabe3_Strassenbahn");
        // nur für die Ausgabe
        System.out.println("Taxi: Maximale Reistezeit " + reisezeitenTaxi.stream().flatMapToDouble(tuple -> DoubleStream.of(tuple.trd)).max().getAsDouble());
        System.out.println("Strassenbahn: Maximale Reisezeit: " + reisezeitenStrassenbahn.stream().flatMapToDouble(tuple -> DoubleStream.of(tuple.snd)).max().getAsDouble());
        System.out.println("Anzahl Taxis: " + anzahlTaxi + "\nAnzahl Straßenbahn: " + anzahlStrassenbahn);
    }
}

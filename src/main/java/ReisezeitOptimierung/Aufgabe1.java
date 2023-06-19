package ReisezeitOptimierung;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import static ReisezeitOptimierung.Hauptbahnhof.hauptbahnhof;
import static ReisezeitOptimierung.Strassenbahn.strassenbahn;
import static ReisezeitOptimierung.Taxi.taxi;

public class Aufgabe1 {

    public static void main(String[] args) {
        // Anlegen zweier Listen für das Speichern der Reisezeiten
        List<Double> reisezeitenStrassenbahn = new LinkedList<>();
        List<Double> reisezeitenTaxi = new LinkedList<>();
        // Anlegen von Strassenbahn und Taxi mit entsprechenden Werten
        Strassenbahn str = strassenbahn(0, 0, 300);
        Taxi taxi = taxi(0, 10, 1);
        // Bahnhof zur Berechnung der Reisezeiten und Wartezeiten anlegen
        Hauptbahnhof hauptbahnhof = hauptbahnhof(str, taxi);

        for (int i = 0; i < 100000; i++) {
            hauptbahnhof.setStrassenbahn(strassenbahn(0,0, 300));
            //hauptbahnhof.setStrassenbahn(strassenbahn(0, 0, 300));
            hauptbahnhof.wartenAufStrassenbahn();
            reisezeitenStrassenbahn.add(hauptbahnhof.getStrassenbahn().getFahrzeit());

        }
        // Speichern der Reisezeiten
        DateiSpeicherung.save(reisezeitenStrassenbahn, "Strassenbahn_1_Glocke");
        System.out.println("Nur mit der Straßenbahn reisen im Durchschnitt: " + reisezeitenStrassenbahn.stream().flatMapToDouble(a -> DoubleStream.of(a)).average().getAsDouble() + "\n");

        for (int i = 0; i < 100000; i++) {
            hauptbahnhof.setTaxi(taxi(0,10, 1));
            //hauptbahnhof.setTaxi(taxi(0, 10, 1));
            hauptbahnhof.wartenAufTaxi();
            reisezeitenTaxi.add(hauptbahnhof.getTaxi().getFahrzeit());

        }
        // nur zum Speichern der CSV
        DateiSpeicherung.save(reisezeitenTaxi, "Taxi_1_Glocke");
        // nur für die Ausgabe
        System.out.println("Nur mit dem Taxi reisen im Durchschnitt: " + reisezeitenTaxi.stream().flatMapToDouble(a -> DoubleStream.of(a)).average().getAsDouble() + "\n");

    }
}

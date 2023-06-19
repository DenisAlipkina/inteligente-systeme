package ReisezeitOptimierung;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import static ReisezeitOptimierung.DateiSpeicherung.saveChoicesTriple;
import static ReisezeitOptimierung.Hauptbahnhof.hauptbahnhofMitMittlererWartezeitFürtaxi;
import static ReisezeitOptimierung.Strassenbahn.strassenbahn;
import static ReisezeitOptimierung.Taxi.taxi;

public class Aufgabe2 {

    public static void main(String[] args) {
        // Anlegen von Strassenbahn und Taxi mit entsprechenden Werten
        Strassenbahn str = strassenbahn(0, 0, 300);
        Taxi taxi = taxi(0, 10, 1);
        // Bahnhof zur Berechnung der Reisezeiten und Wartezeiten anlegen
        Hauptbahnhof hauptbahnhof = hauptbahnhofMitMittlererWartezeitFürtaxi(str, taxi);


        int anzahlTaxi = 0;
        int anzahlStrassenbahn = 0;
        List<Triple<String, Integer, Double>> reisezeiten = new LinkedList<>();

        for (int i = 0; i < 10000; i++) {
            hauptbahnhof.setStrassenbahn(strassenbahn(0, 0, 300));
            hauptbahnhof.setTaxi(taxi(0, 10, 1));
            if (hauptbahnhof.wartenAufBeides() == 1) {
                anzahlTaxi++;
                reisezeiten.add(Triple.triple("Taxi", hauptbahnhof.getTaxi().getSchlange().getEntscheidungs_Länge(), hauptbahnhof.getTaxi().getTotalZeit()));
            } else {
                anzahlStrassenbahn++;
                reisezeiten.add(Triple.triple("Strassenbahn", hauptbahnhof.getStrassenbahn().getSchlange().getInitial_Länge(), hauptbahnhof.getStrassenbahn().getTotalZeit()));
            }
        }
        // nur zum Speichern der CSV
        saveChoicesTriple(reisezeiten, "Aufgabe2");
        // nur für die Ausgabe
        System.out.println("Durchschnittlich Reisezeit: "
                + reisezeiten.stream().flatMapToDouble(tuple -> DoubleStream.of(tuple.trd)).average().getAsDouble());
        System.out.println("Anzahl Taxis: " + anzahlTaxi + "\nAnzahl Straßenbahn: " + anzahlStrassenbahn);
    }
}

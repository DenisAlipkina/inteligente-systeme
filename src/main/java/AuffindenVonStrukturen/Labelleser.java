package AuffindenVonStrukturen;

import ReisezeitOptimierung.Tuple;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Labelleser {

    private List<Tuple<Integer, Integer>> csvLabel;

    /**
     * Liest eine Label CSV Datei ein und packt die Werte in eine Liste von Tupeln
     * Die Tupel enhalten hier Koordinaten X,Y von Werten aus der CSV Datei mit den Daten
     *
     * @param dateiPfad
     * @throws IOException
     */
    public void readCSVLabel(String dateiPfad) throws IOException {
        String eingabeZeile;
        final FileInputStream fis = new FileInputStream(dateiPfad);
        final DataInputStream inputStream = new DataInputStream(fis);
        csvLabel = new LinkedList<>();
        while ((eingabeZeile = inputStream.readLine()) != null) { // Zeile einlesen
            String[] split = eingabeZeile.split(","); // Zeile bei dem Komma splitten
            csvLabel.add(Tuple.tuple(Integer.parseInt(split[0]), Integer.parseInt(split[1]))); // Zeile als Tupel in die Liste fügen
        }
    }

    public List<Tuple<Integer, Integer>> getCsvLabel() {
        return this.csvLabel;
    }
}

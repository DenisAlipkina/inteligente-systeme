package AuffindenVonStrukturen;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Datenleser {

    private List<List<Integer>> csvDaten;

    /**
     * Liest die Daten aus einer CSV Datei ein
     * Die Daten werden in eine 2D Liste gepackt (2D Matritz)
     *
     * @param dateiPfad
     * @throws IOException
     */
    public void read2DCSV(String dateiPfad) throws IOException {
            String eingabeZeile;
            final FileInputStream fis = new FileInputStream(dateiPfad);
            final DataInputStream inputStream = new DataInputStream(fis);
            csvDaten = new LinkedList<>();
            while ((eingabeZeile = inputStream.readLine()) != null) { // Zeile einlesen
                csvDaten.add(Arrays.stream(eingabeZeile.split(","))
                        .flatMapToInt(x -> IntStream.of(Integer.parseInt(x))).boxed().collect(Collectors.toList())); // Zeile in die Liste fügen
            }
    }

    public List<List<Integer>> getCsvDaten() {
        return this.csvDaten;
    }
}

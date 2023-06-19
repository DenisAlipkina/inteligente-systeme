package ReisezeitOptimierung;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class DateiSpeicherung {

    /**
     * Methode zum speichern eines Wertes aus einer Liste
     * @param zeiten
     * @param dateiName
     */
    public static void save(List<Double> zeiten, String dateiName) {
        // Angeben des Dateipfades zum speichern der Datei -> Dateien werden in Berechnungen gespeichert
        File csvFileOutput = new File("src/main/java/ReisezeitOptimierung/Berechnungen/" + dateiName + ".csv");
        try {
            PrintWriter pw = new PrintWriter(csvFileOutput);
            pw.println("Reisezeit");
            zeiten.forEach(pw::println);
            pw.flush();
            pw.close();

            System.out.println("Berechnungen wurden gespeichert");
        } catch (Exception e) {
            System.out.println("Berechnungen wurden nicht gespeichert");
        }
    }

    /**
     * Zum Speichern einer Liste mit einem Triple (jeder Eintrag der Liste hat 3 Werte)
     * 3 Werte -> Transportmittel, Schlange, Reisezeit
     * @param zeiten
     * @param dateiName
     */
    public static void saveChoicesTriple(List<Triple<String, Integer, Double>> zeiten, String dateiName) {
        // Angeben des Dateipfades zum speichern der Datei -> Dateien werden in Berechnungen gespeichert
        File csvFileOutput = new File("src/main/java/ReisezeitOptimierung/Berechnungen/" + dateiName + ".csv");
        try {
            PrintWriter pw = new PrintWriter(csvFileOutput);
            pw.println("Transportmittel,Schlange,Reisezeit");
            zeiten.forEach(tuple -> pw.println(String.format("%s,%d,%s", tuple.fst,tuple.snd, tuple.trd.toString())));
            pw.flush();
            pw.close();

            System.out.println("Berechnungen wurden gespeichert");
        } catch (Exception e) {
            System.out.println("Berechnungen wurden nicht gespeichert");
        }
    }

    /**
     * Zum Speichern einer Liste mit einem Tuple (jeder Eintrag in der Liste hat 2 Werte)
     * 2 Werte -> Transportmittel, Reisezeit
     * @param zeiten
     * @param dateiName
     */
    public static void saveChoicesTuple(List<Tuple<String, Double>> zeiten, String dateiName) {
        // Angeben des Dateipfades zum speichern der Datei -> Dateien werden in Berechnungen gespeichert
        File csvFileOutput = new File("src/main/java/ReisezeitOptimierung/Berechnungen/" + dateiName + ".csv");
        try {
            PrintWriter pw = new PrintWriter(csvFileOutput);
            pw.println("Transportmittel,Reisezeit");
            zeiten.forEach(tuple -> pw.println(String.format("%s,%s", tuple.fst,tuple.snd.toString())));
            pw.flush();
            pw.close();

            System.out.println("Berechnungen wurden gespeichert");
        } catch (Exception e) {
            System.out.println("Berechnungen wurden nicht gespeichert");
        }
    }

    /**
     * Zum Speichern einer Liste mit einem Tuple (jeder Eintrag in der Liste hat 2 Werte)
     * 2 Werte -> Schlange, Reisezeit
     * @param zeiten
     * @param dateiName
     */
    public static void saveChoicesQueueTuple(List<Tuple<Integer, Double>> zeiten, String dateiName) {
        // Angeben des Dateipfades zum speichern der Datei -> Dateien werden in Berechnungen gespeichert
        File csvFileOutput = new File("src/main/java/ReisezeitOptimierung/Berechnungen/" + dateiName + ".csv");
        try {
            PrintWriter pw = new PrintWriter(csvFileOutput);
            pw.println("Schlange,Reisezeit");
            zeiten.forEach(tuple -> pw.println(String.format("%s,%s", tuple.fst, tuple.snd.toString())));
            pw.flush();
            pw.close();

            System.out.println("Berechnungen wurden gespeichert");
        } catch (Exception e) {
            System.out.println("Berechnungen wurden nicht gespeichert");
        }
    }

    /**
     * Zum Speichern einer Liste mit einem Triple (jeder Eintrag der Liste hat 3 Werte)
     * 3 Werte -> Schlange Anfangs Länge, Schlange Entscheidungs Länge, Reisezeit
     * @param zeiten
     * @param dateiName
     */
    public static void saveChoicesQueueTriple(List<Triple<Integer, Integer, Double>> zeiten, String dateiName) {
        // Angeben des Dateipfades zum speichern der Datei -> Dateien werden in Berechnungen gespeichert
        File csvFileOutput = new File("src/main/java/ReisezeitOptimierung/Berechnungen/" + dateiName + ".csv");
        try {
            PrintWriter pw = new PrintWriter(csvFileOutput);
            pw.println("Schlange Anfangs Länge,Schlange Entscheidungs Länge,Reisezeit");
            zeiten.forEach(tuple -> pw.println(String.format("%s,%s,%s", tuple.fst, tuple.snd, tuple.trd.toString())));
            pw.flush();
            pw.close();

            System.out.println("Berechnungen wurden gespeichert");
        } catch (Exception e) {
            System.out.println("Berechnungen wurden nicht gespeichert");
        }
    }
}

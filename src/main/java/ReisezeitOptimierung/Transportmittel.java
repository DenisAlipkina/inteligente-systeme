package ReisezeitOptimierung;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Transportmittel {

    private final double initial_fahrzeit;
    private final double fahrzeit;
    private final int eintreffWahrscheinlichkeit;
    private int wartezeit;
    private double totalWartezeit;
    private double totalZeit;
    private boolean verfuegbar = false;
    private Schlange schlange;

    /**
     * Konstruktor zum erstellen eines Transportmittels
     * @param fahrzeit
     * @param standardabweichung
     * @param mittelwert
     * @param schlange
     * @param wartezeit
     * @param eintreffWahrscheinlichkeit
     */
    protected Transportmittel(int fahrzeit, int standardabweichung, int mittelwert, Schlange schlange, int wartezeit,
                              int eintreffWahrscheinlichkeit) {
        this.initial_fahrzeit = fahrzeit;
        double fahrt_abweichung = new Random().nextGaussian() * standardabweichung + mittelwert; // Berechnung der Fahrtzeitabweichung positiv sowie negativ
        this.fahrzeit = fahrzeit + fahrt_abweichung;
        this.totalWartezeit = wartezeit;
        this.totalZeit = 0;
        this.schlange = schlange;
        this.wartezeit = wartezeit;
        this.eintreffWahrscheinlichkeit = eintreffWahrscheinlichkeit;
    }

    /**
     * Methode zum fahren bis zum Bahnhof
     * Berechnet die Zeit bis zum Eintreffen in dem Bahnhof
     * Wenn die schlange länder ist wird die Wartezeit auf die Totale Wartezeit angerechnet
     */
    public void faehrtZumHBF() {
        if (this.getWartezeit() > 0) { // Läuft so lange bis man im Bahnhof angekommen ist
            this.setWartezeit(this.getWartezeit() - 1);
            this.setTotalZeit(this.getTotalZeit() + 1);
            // Wenn die Schlange leer ist dann sind wir an der Reihe
        } else if (this.getSchlange().anDerReihe()) {
            this.setVerfuegbar(true);
            // Wenn die Schlange nicht leer ist, dann verlässt eine Person die Schlange
        } else {
            this.getSchlange().verlassen();
            this.setWartezeit(wartezeit(eintreffWahrscheinlichkeit)); // Neue Wartezeit berechnen
            this.setTotalWartezeit(this.wartezeit); // Neue Wartezeit auf die alte dazu rechnen
        }
    }

    /**
     * Methode zum Berechnen der Wartezeit bis das Transportmittel im Bahnhof ankommt
     * @param eintreffWahrscheinlichkeit
     * @return
     */
    protected static int wartezeit(int eintreffWahrscheinlichkeit) {
        int zeit = 0;
        List<Integer> list = IntStream.range(1, eintreffWahrscheinlichkeit + 1).boxed().collect(Collectors.toList());
        while (!list.contains(new Random().nextInt(100))) zeit++;
        return zeit;
    }


    // --------------- Getter und Setter der Klasse ---------------

    /**
     * Liefert die Wahrscheinlichkeit wann ein Transportmittel eintrifft am HBF
     * @return
     */
    protected int getEintreffWahrscheinlichkeit() {
        return this.eintreffWahrscheinlichkeit;
    }

    /**
     * Liefert die inititale Fahrzeit des Transportmittels ohne Gaußglocke
     * @return
     */
    protected double getInitial_Fahrzeit() {
        return this.initial_fahrzeit;
    }

    /**
     * Liefert und die Fahrzeit mit der Standardabweichung
     * @return
     */
    protected double getFahrzeit() {
        return this.fahrzeit;
    }

    /**
     * Liefert die zufällige Wartezeit bis ein Transportmittel eintrifft
     * @return
     */
    protected int getWartezeit() {
        return this.wartezeit;
    }

    /**
     * Setzt die Wartezeit auf den Wert @wartezeit
     * @param wartezeit
     */
    protected void setWartezeit(int wartezeit) {
        this.wartezeit = wartezeit;
    }

    /**
     * Setzt die Totale Wartezeit für das Transportmittel zu dem aktuellen Zeitpunkt
     * @param wartezeitHinzufügen
     */
    protected void setTotalWartezeit(int wartezeitHinzufügen) {
        this.totalWartezeit += wartezeitHinzufügen;
    }

    /**
     * Liefert die Totale Wartezeit für das Transportmittel bis zu dem gewarteten Zeitpunkt
     * @return
     */
    protected double getTotalWartezeit() {
        return this.totalWartezeit;
    }

    /**
     * Liefert die totale Reisezeit des Transportmittels
     * @return
     */
    protected double getTotalZeit() {
        return this.totalZeit;
    }

    /**
     * Setzt die totale Reisezeit des Transportmittels
     * @param totalZeit
     */
    protected void setTotalZeit(double totalZeit) {
        this.totalZeit = totalZeit;
    }

    /**
     * Liefert die Schlange des Transportmittels
     * @return
     */
    protected Schlange getSchlange() {
        return this.schlange;
    }

    /**
     * Setzt die Schlange des Transportmittels
     * @param schlange
     */
    protected void setSchlange(Schlange schlange) {
        this.schlange = schlange;
    }

    /**
     * Prüft ob das Transportmittel im HBF zur Verfügung steht
     * @return
     */
    protected boolean isVerfuegbar() {
        return this.verfuegbar;
    }

    /**
     * Setzt ob das Transportmittel verfügbar ist oder nicht
     * @param verfuegbar
     */
    protected void setVerfuegbar(boolean verfuegbar) {
        this.verfuegbar = verfuegbar;
    }
}

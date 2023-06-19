package ReisezeitOptimierung;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import static ReisezeitOptimierung.Strassenbahn.strassenbahn;
import static ReisezeitOptimierung.Strassenbahn.strassenbahnBekannteWartezeit;
import static ReisezeitOptimierung.Taxi.taxi;

public class Hauptbahnhof {

    private Strassenbahn strassenbahn;
    private Taxi taxi;
    private double mittlereWartezeitTaxi;
    private Map<Integer, DescriptiveStatistics> percentileTaxi;
    private DescriptiveStatistics percentileTaxiStrassenbahn;

    /**
     * Konstruktor zum erstllen des Bahnhofes mit Strassenbahn und Taxi
     *
     * @param strassenbahn
     * @param taxi
     */
    private Hauptbahnhof(Strassenbahn strassenbahn, Taxi taxi) {
        this.strassenbahn = strassenbahn;
        this.taxi = taxi;
        this.percentileTaxi = new LinkedHashMap<>();
        this.percentileTaxiStrassenbahn = new DescriptiveStatistics();
    }

    /**
     * Konstruktor zum erstellen des Bahnhofes mit nur der Strassenbahn
     *
     * @param strassenbahn
     */
    private Hauptbahnhof(Strassenbahn strassenbahn) {
        this.strassenbahn = strassenbahn;
        this.taxi = null;
        this.percentileTaxi = new LinkedHashMap<>();
        this.percentileTaxiStrassenbahn = new DescriptiveStatistics();
    }

    /**
     * Konstruktor zum erstellen des Bahnhofes mit nur dem Taxi
     *
     * @param taxi
     */
    private Hauptbahnhof(Taxi taxi) {
        this.strassenbahn = null;
        this.taxi = taxi;
        this.percentileTaxi = new LinkedHashMap<>();
        this.percentileTaxiStrassenbahn = new DescriptiveStatistics();
    }

    /**
     * Statische Methode zum erstellen des Bahnhofes mit Strassenbahn und Taxi
     *
     * @param strassenbahn
     * @param taxi
     * @return
     */
    public static Hauptbahnhof hauptbahnhof(Strassenbahn strassenbahn, Taxi taxi) {
        return new Hauptbahnhof(strassenbahn, taxi);
    }

    /**
     * Statische Methode zum erstellen des Bahnhofes mit Strassenbahn und Taxi.
     * Mit berechneter mittlerer Wartezeit für das Taxi
     *
     * @param strassenbahn
     * @param taxi
     * @return
     */
    public static Hauptbahnhof hauptbahnhofMitMittlererWartezeitFürtaxi(Strassenbahn strassenbahn, Taxi taxi) {
        Hauptbahnhof hbf = new Hauptbahnhof(strassenbahn, taxi);
        hbf.setztErrechneMittlereWartezeitFürTaxi();
        return hbf;
    }

    /**
     * Statische Methode zum erstellen des Bahnhofes mit Strassenbahn und Taxi
     * Mit berechneten Percentilen für Strassenbahn und Taxi
     *
     * @param strassenbahn
     * @param taxi
     * @return
     */
    public static Hauptbahnhof hauptbahnhofPercentileFürSchlangenLänge(Strassenbahn strassenbahn, Taxi taxi) {
        Hauptbahnhof hbf = new Hauptbahnhof(strassenbahn, taxi);
        hbf.errechnePercentileFürDieSchlangenLängen();
        return hbf;
    }

    /**
     * Statische Methode zum erstellen des Bahnhofes mit nur der Strassenbahn
     *
     * @param strassenbahn
     * @return
     */
    public static Hauptbahnhof hauptbahnhofKeinTaxi(Strassenbahn strassenbahn) {
        return new Hauptbahnhof(strassenbahn);
    }

    /**
     * Statische Methode zum erstellen des Bahnhofes mit nur dem Taxi
     *
     * @param taxi
     * @return
     */
    public static Hauptbahnhof hauptbahnhofKeineStrassenbahn(Taxi taxi) {
        return new Hauptbahnhof(taxi);
    }


    /**
     * Methode zum warten auf die Strassenbahn
     */
    public void wartenAufStrassenbahn() {
        if (this.strassenbahn != null) {
            while (!this.strassenbahn.isVerfuegbar()) {
                this.strassenbahn.faehrtZumHBF();
            }
            this.strassenbahn.setTotalZeit(this.strassenbahn.getTotalWartezeit() + this.strassenbahn.getFahrzeit());
        }
    }

    /**
     * Methode zum warten auf das Taxi
     */
    public void wartenAufTaxi() {
        if (this.taxi != null) {
            while (!this.taxi.isVerfuegbar()) {
                this.taxi.faehrtZumHBF();
            }
            this.taxi.setTotalZeit(this.taxi.getTotalWartezeit() + this.taxi.getFahrzeit());
        }
    }

    /**
     * Methode zum errechnen der mittleren Wartezeit eines Taxi, dabei betrachten wir nur die Schlange von 0
     * Also nur bis ein Taxi eintrifft
     *
     * @return setzt die mittlere Wartezeit auf das errechnete
     */
    public void setztErrechneMittlereWartezeitFürTaxi() {
        List<Double> wartezeiten = new LinkedList<>();
        Taxi taxi = taxi(0, 0, 1);
        Hauptbahnhof hauptbahnhof = hauptbahnhofKeineStrassenbahn(taxi);

        for (int i = 0; i < 10000; i++) {
            hauptbahnhof.setTaxi(taxi(0, 0, 1));
            hauptbahnhof.wartenAufTaxi();
            wartezeiten.add(hauptbahnhof.getTaxi().getTotalWartezeit());
        }
        this.mittlereWartezeitTaxi = wartezeiten.stream().flatMapToDouble(DoubleStream::of).average().getAsDouble();
    }

    /**
     * Methoden zum Warten auf Taxi und Strassenbahn
     * Wir stellen uns bei dem Taxi an und beobachten zudem auch noch die Strassenbahn
     * Sollte die Strassenbahn eintreffen entscheiden wir uns anhand der restlichen Länge der Schlange
     *
     * @return 1 für Taxi und 0 für Strassenbahn. 3 wenn beides nicht zutrifft (Fehler)
     */
    public int wartenAufBeides() {
        if (this.taxi != null && this.strassenbahn != null) {
            // Wir stellen uns beim Taxi an und schauen gleichzeitig noch auf die Strassenbahn
            // Und warten bis eins der beiden verfügbar ist
            while (!this.taxi.isVerfuegbar() && !this.strassenbahn.isVerfuegbar()) {
                this.taxi.faehrtZumHBF();
                this.strassenbahn.faehrtZumHBF();
            }
            // Wir prüfen ob das Taxi verfügbar ist
            if (this.taxi.isVerfuegbar()) {
                this.taxi.setTotalZeit(this.taxi.getTotalWartezeit() + this.taxi.getFahrzeit());
                return 1;
                // Wir gucken ob die mittlere Fahrzeit der Strassenbahn schneller ist als wie lange das Taxi noch ungefair brauchen könnte
            } else if (this.strassenbahn.getInitial_Fahrzeit() < (this.taxi.getSchlange().getLaenge() + 1)
                    * this.mittlereWartezeitTaxi + this.taxi.getInitial_Fahrzeit()) {
                this.strassenbahn.setTotalZeit(this.strassenbahn.getTotalWartezeit() + this.strassenbahn.getFahrzeit());
                return 0;
                // sollten wir das Taxi schneller einschätzen kommt in 5 min eine neue Strassenbahn
                // und wir warten weiterhin in der Schlange des Taxis
            } else {
                this.taxi.getSchlange().setLaengeFürEntscheidung(this.taxi.getSchlange().getLaenge());
                this.setStrassenbahn(strassenbahnBekannteWartezeit(0, 0, 300));
                return wartenAufBeides();
            }
        }
        return 3;
    }


    /**
     * Methode die alle Daten in ein DescriptiveStrucutre fügt um ein percentileTaxi zu erstellen
     */
    public void errechnePercentileFürDieSchlangenLängen() {
        DescriptiveStatistics reisezeitenTaxi = new DescriptiveStatistics();
        Hauptbahnhof hauptbahnhof = hauptbahnhof(
                strassenbahn(0, 0, 300),
                taxi(0, 10, 1));
        for (int s = 0; s < 12; s++) {
            reisezeitenTaxi = new DescriptiveStatistics();
            for (int i = 0; i < 10000; i++) {
                hauptbahnhof.setTaxi(taxi(s, s, 1));
                hauptbahnhof.wartenAufTaxi();
                reisezeitenTaxi.addValue(hauptbahnhof.getTaxi().getTotalZeit());
            }
            percentileTaxi.put(s, reisezeitenTaxi);
        }
        for (int i = 0; i < 10000; i++) {
            hauptbahnhof.setStrassenbahn(strassenbahn(0, 0, 300));
            hauptbahnhof.wartenAufStrassenbahn();
            percentileTaxiStrassenbahn.addValue(hauptbahnhof.getStrassenbahn().getFahrzeit());
        }
    }

    /**
     * Methoden zum Warten auf Taxi und Strassenbahn
     * Wir überprüfen hier ob es sich überhaupt lohnt das Taxi zu nehmen,
     * wenn wir die Maximale Reisezeit so gering wie möglich halten wollen.
     *
     * @return 1 für Taxi und 0 für Strassenbahn. 3 wenn beides nicht zutrifft (Fehler)
     */
    public int wartenAufBeidesMitSchlangenLänge() {
        if (this.taxi != null && this.strassenbahn != null) {
            // Wir prüfen ob es sich überhaupt lohnt das Taxi in betracht zu ziehen
            while (!this.taxi.isVerfuegbar() && !this.strassenbahn.isVerfuegbar()) {
                this.taxi.faehrtZumHBF();
                this.strassenbahn.faehrtZumHBF();
            }
            // Wir prüfen ob das Taxi verfügbar ist
            if (this.taxi.isVerfuegbar()) {
                this.taxi.setTotalZeit(this.taxi.getTotalWartezeit() + this.taxi.getFahrzeit());
                return 1;
                // Wir gucken ob die Percentile Fahrzeit der Strassenbahn schneller ist als wie das Percentile des Taxi
            } else if (percentileTaxiStrassenbahn.getPercentile(99)
                    < percentileTaxi.get(this.taxi.getSchlange().getLaenge() + 1).getPercentile(99)) {
                this.strassenbahn.setTotalZeit(this.strassenbahn.getTotalWartezeit() + this.strassenbahn.getFahrzeit());
                return 0;
                // sollten das Taxi schneller sein kommt in 5 min eine neue Strassenbahn
                // und wir warten weiterhin in der Schlange des Taxis
            } else {
                this.taxi.getSchlange().setLaengeFürEntscheidung(this.taxi.getSchlange().getLaenge());
                this.setStrassenbahn(strassenbahnBekannteWartezeit(0, 0, 300));
                return wartenAufBeidesMitSchlangenLänge();
            }
        }
        return 3;
    }

    // --------------- Getter und Setter der Klasse ---------------

    /**
     * Liefert die Strassenbahn des HBF
     *
     * @return
     */
    public Strassenbahn getStrassenbahn() {
        return this.strassenbahn;
    }

    /**
     * Setzt eine neue Strassenbahn für den HBF
     *
     * @param strassenbahn
     */
    public void setStrassenbahn(Strassenbahn strassenbahn) {
        this.strassenbahn = strassenbahn;
    }

    /**
     * Liefert das Taxi des HBF
     *
     * @return
     */
    public Taxi getTaxi() {
        return this.taxi;
    }

    /**
     * Setzt ein neues Taxi für den HBF
     *
     * @param taxi
     */
    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }
}

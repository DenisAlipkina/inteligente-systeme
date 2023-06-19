package ReisezeitOptimierung;

import java.util.Random;

public class Schlange {

    private int laenge;
    private int initial_Länge;
    private int entscheidungs_Länge;

    /**
     * Konstruktor initialisiert uns eine Schlange mit einer Länge zwischen den beiden Werten
     *
     * @param min Minimale Größe
     * @param max Maximale Größe
     */
    private Schlange(int min, int max) {
        this.laenge = new Random().nextInt((max - min) + 1) + min;
        this.initial_Länge = this.laenge;
    }

    /**
     * Statische Methode zum initialiseren einer Schlange
     * @param min
     * @param max
     * @return
     */
    public static Schlange schlange(int min, int max) {
        return new Schlange(min, max);
    }

    /**
     * Void Methode damit eine Platz in der Schlange wieder frei wird
     */
    public void verlassen() {
        if (this.laenge > 0) {
            this.laenge -= 1;
        }
    }

    // --------------- Getter und Setter der Klasse ---------------

    /**
     * liefert uns die Länge der Schlange
     * @return Länge der gesamten Schlange OHNE UNS
     */
    public int getLaenge() {
        return this.laenge;
    }

    /**
     * Liefert uns die inititiale Länge der Schlange
     * @return Länge der gesamten Schlange OHNE UNS
     */
    public int getInitial_Länge() {
        return this.initial_Länge;
    }

    /**
     * Setzt uns die Länge bei welcher Länge wir uns entschieden haben zu warten
     * @param laenge
     */
    public void setLaengeFürEntscheidung(int laenge) {
        this.entscheidungs_Länge = laenge;
    }

    /**
     * Liefert uns die Länge der Schlange wenn wir uns entscheiden weiter zu warten
     * @return die Länge der Schlange als wir uns entschieden haben
     */
    public int getEntscheidungs_Länge() {
        return this.entscheidungs_Länge;
    }

    /**
     * prüft ob WIR an der Reihe sind
     * @return ja oder nein
     */
    public boolean anDerReihe() {
        return this.laenge == 0;
    }
}

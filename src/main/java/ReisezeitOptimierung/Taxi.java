package ReisezeitOptimierung;

import static ReisezeitOptimierung.Schlange.schlange;

public class Taxi extends Transportmittel {

    /**
     * Konstruktor zum erstellen von einem Taxi
     * @param min
     * @param max
     * @param wartezeit
     * @param eintreffWahrscheinlichkeit
     */
    private Taxi(int min, int max, int wartezeit, int eintreffWahrscheinlichkeit) {
        super(500, 50, 0, schlange(min, max), wartezeit, eintreffWahrscheinlichkeit);
    }

    /**
     * Statische Methode zum erstellen eines Taxi
     * @param minQueue
     * @param maxQueue
     * @param eintreffWahrscheinlichkeit
     * @return
     */
    public static Taxi taxi(int minQueue, int maxQueue, int eintreffWahrscheinlichkeit) {
        return new Taxi(minQueue, maxQueue, wartezeit(eintreffWahrscheinlichkeit), eintreffWahrscheinlichkeit);
    }
}

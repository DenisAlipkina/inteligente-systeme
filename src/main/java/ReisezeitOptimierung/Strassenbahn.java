package ReisezeitOptimierung;

import java.util.Random;

import static ReisezeitOptimierung.Schlange.schlange;

public class Strassenbahn extends Transportmittel {

    /**
     * Konstruktor zum erstellen von einer Strassenbahn
     * @param min
     * @param max
     * @param wartezeit
     * @param eintreffWahrscheinlichkeit
     */
    private Strassenbahn(int min, int max, int wartezeit, int eintreffWahrscheinlichkeit) {
        super(1000, 10, 0, schlange(min, max), wartezeit, eintreffWahrscheinlichkeit);
    }

    /**
     * Statische Methode zum erstellen einer Strassenbahn mit unbekannter Wartezeit (nur Eingrenzung)
     * @param minQueue
     * @param maxQueue
     * @param maxWartezeit
     * @return
     */
    public static Strassenbahn strassenbahn(int minQueue, int maxQueue, int maxWartezeit) {
        return new Strassenbahn(minQueue, maxQueue, new Random().nextInt(maxWartezeit + 1), 100);
    }

    /**
     * Statische Methode zum erstellen einer Strassenbahn mit bekannter Wartezeit
     * @param minQueue
     * @param maxQueue
     * @param Wartezeit
     * @return
     */
    public static Strassenbahn strassenbahnBekannteWartezeit(int minQueue, int maxQueue, int Wartezeit) {
        return new Strassenbahn(minQueue, maxQueue, Wartezeit, 100);
    }
}

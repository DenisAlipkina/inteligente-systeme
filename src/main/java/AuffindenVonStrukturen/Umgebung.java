package AuffindenVonStrukturen;

import ReisezeitOptimierung.Triple;
import ReisezeitOptimierung.Tuple;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static ReisezeitOptimierung.Tuple.tuple;

public class Umgebung {
    private List<List<Triple<Integer, Integer, Integer>>> csvDatei = new LinkedList<>();
    private List<Agent> agenten = new LinkedList<>();
    private Random r = new Random();
    private Integer x_laenge;
    private Integer y_laenge;

    public Umgebung(String path) {
        Datenleser2 datenleser = new Datenleser2();
        try {
            datenleser.read2DCSV(path);
            //this.csvDatei = datenleser.getCsvDatei();
            this.x_laenge = datenleser.getX_laenge();
            this.y_laenge = datenleser.getY_laenge();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void moveAgenten() {
        /*try {
            Triple<Integer, Integer, Integer> oben;
            Tuple<Integer, Integer> mitte;
            Triple<Integer, Integer, Integer> unten;
            for (Agent a : this.agenten) {
                if (!a.isAngekommen()) {
                    if (a.getX() > 0 || a.getX() < csvDatei.size() - 1) {
                        mitte = tuple(csvDatei.get(a.getX() - 1).snd(), csvDatei.get(a.getX() + 1).snd());
                        a.move(mitte, "kein Rand");
                    } else if (a.getX() == 0) {
                        mitte = tuple(csvDatei.get(a.getX()).snd(), csvDatei.get(a.getX() + 1).snd());
                        a.move(mitte, "linker Rand");
                    } else if (a.getX() == csvDatei.size() - 1) {
                        mitte = tuple(csvDatei.get(a.getX() - 1).snd(), csvDatei.get(a.getX()).snd());
                        a.move(mitte, "rechter Rand");
                    }


                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }*/
    }
    public boolean checkAgenten() {
        return agenten.stream().allMatch(Agent::isAngekommen);
    }
    public void fillAgenten(int anzahl) {
        List<Tuple<Integer, Integer>> spawnKoordinaten = spawnRaster_bestimmen(anzahl);
        int random_index;
        for(int i = 0; i < anzahl; i++) {
            random_index = r.nextInt(spawnKoordinaten.size() + 1);
            agenten.add(new Agent(spawnKoordinaten.get(random_index).fst(), spawnKoordinaten.get(random_index).snd(),
                    csvDatei.get(spawnKoordinaten.get(random_index).snd()).get(spawnKoordinaten.get(random_index).fst()).trd(), r));
        }
    }
    public List<Tuple<Integer, Integer>> spawnRaster_bestimmen(int anzahl){
        Tuple<Integer, Integer> tuple;
        int start = (int) Math.round(Math.pow(anzahl, 1.0 / 2.0));
        if(anzahl <= Math.pow(start, 2)) {
            tuple = tuple(start, start);
        } else {
            if(this.r.nextBoolean()) {
                tuple = tuple(start + 1, start);
            } else {
                tuple = tuple(start, start + 1);
            }
        }
        int x_schritt = x_laenge / (tuple.fst());
        int y_schritt = y_laenge / (tuple.snd());
        List<Tuple<Integer, Integer>> spawnKoordinaten = new LinkedList<>();
        if((tuple.fst() * tuple.snd()) % 2 == 0) {

            for(int i = 1; i <= tuple.snd(); i++) {
                for(int j = 1; j <= tuple.fst(); j++) {
                    spawnKoordinaten.add(tuple(j * x_schritt - x_schritt / 2, i * y_schritt - y_schritt / 2));
                }
            }
        } else {
            for(int i = 1; i <= tuple.snd(); i++) {
                for(int j = 1; j <= tuple.fst(); j++) {
                    spawnKoordinaten.add(tuple(j * x_schritt - x_schritt / 2 - 1, i * y_schritt - y_schritt / 2 - 1));
                }
            }
        }
        System.out.println(spawnKoordinaten);
        return spawnKoordinaten;
    }


    public void check(int x, int y) {

    }
}

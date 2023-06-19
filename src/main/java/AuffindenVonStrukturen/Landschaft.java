package AuffindenVonStrukturen;

import java.util.LinkedList;
import java.util.List;

public class Landschaft {
    private int x_laenge;
    private int y_laenge;
    private List<List<Stelle>> landschaft;
    private final int anzahlAgenten = 4;
    private List<Agent2> agenten;
    private boolean test_fertig;
    private List<List<Stelle>> potenzielle_Labels;

    private Landschaft(String path) {
        try {
            Datenleser2 leser = new Datenleser2();
            leser.read2DCSV(path);
            this.landschaft = leser.getCsvDatei();
            this.y_laenge = landschaft.size();
            this.x_laenge = landschaft.get(0).size();
            this.agenten = spawnAgenten();
            this.test_fertig = false;
            this.potenzielle_Labels = new LinkedList<>();
        } catch (Exception e) {
            System.err.println(e);
        }

    }
    public static Landschaft landschaft(String path) {
        Landschaft l = new Landschaft(path);
        stelle_kennt_landschaft(l);
        return l;
    }
    private static void stelle_kennt_landschaft(Landschaft l) {
        for(List<Stelle> list : l.getLandschaft()) {
            for(Stelle s : list) {
                s.setL(l);
            }
        }
    }
    public Stelle getStelle(int x, int y) {
        return this.landschaft.get(y).get(x);
    }

    public List<Agent2> spawnAgenten() {
        List<Agent2> agenten = new LinkedList<>();
        int spawn_schritt = (int) Math.ceil((double)y_laenge/(double)anzahlAgenten);
        for(int i = 0; i < anzahlAgenten; i++) {
            agenten.add(Agent2.agent2(0, i * spawn_schritt, this));
        }

        return agenten;
    }
    public void moveAgenten() {
        for (Agent2 a : agenten) {
            if(!a.isAngekommen())
                a.move();
        }
        if(agenten.stream().allMatch(a -> a.isAngekommen())) test_fertig = true;
    }
    public void runTest() {
        while (!test_fertig) {
            moveAgenten();
        }
    }

    public void addLokalMax(List<Stelle> plataeu) {
        this.potenzielle_Labels.add(plataeu);
    }

    public void check(int x, int y) {
        String output = "";
        for(int i = -6; i <= 6; i++) {
            for(int j = -6; j <= 6; j++) {
                try {
                    if(i == 0 && j == 0) {
                        output += landschaft.get(y + i).get(x + j).getWert() + "; ";
                    } else {
                        output += landschaft.get(y + i).get(x + j).getWert() + ", ";
                    }
                } catch (Exception ignored) {

                }
            }
            output += "\n";
        }
        System.out.println(output);
    }

    public int getX_laenge() {
        return x_laenge;
    }

    public void setX_laenge(int x_laenge) {
        this.x_laenge = x_laenge;
    }

    public int getY_laenge() {
        return y_laenge;
    }

    public void setY_laenge(int y_laenge) {
        this.y_laenge = y_laenge;
    }

    public List<List<Stelle>> getLandschaft() {
        return landschaft;
    }

    public void setLandschaft(List<List<Stelle>> landschaft) {
        this.landschaft = landschaft;
    }

    public int getAnzahlAgenten() {
        return anzahlAgenten;
    }

    public List<Agent2> getAgenten() {
        return agenten;
    }

    public void setAgenten(List<Agent2> agenten) {
        this.agenten = agenten;
    }

    public boolean isTest_fertig() {
        return test_fertig;
    }

    public void setTest_fertig(boolean test_fertig) {
        this.test_fertig = test_fertig;
    }

    public List<List<Stelle>> getPotenzielle_Labels() {
        return potenzielle_Labels;
    }

    public void setPotenzielle_Labels(List<List<Stelle>> potenzielle_Labels) {
        this.potenzielle_Labels = potenzielle_Labels;
    }
}

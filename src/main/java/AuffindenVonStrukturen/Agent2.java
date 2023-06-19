package AuffindenVonStrukturen;

import java.util.LinkedList;
import java.util.List;

public class Agent2 {

    private int x;
    private int initialized_y;
    private int y;
    private Stelle label;
    private List<Stelle> plateau;
    public Landschaft l;
    private boolean angekommen;

    private Agent2(int x, int y, Landschaft l) {
        this.x = x;
        this.y = y;
        this.initialized_y = y;
        this.label = null;
        this.plateau = new LinkedList<>();
        this.l = l;
        this.angekommen = false;
    }

    public static Agent2 agent2(int x, int y, Landschaft l) {
        return new Agent2(x, y, l);
    }

    public void findLabel() {
        int maxWert = 0;
        int i = 1;
        Stelle st;
        while (plateau.size() > 1) {
            int finalI = i;
            maxWert = plateau.stream().mapToInt(s -> s.getUmkreisWert(finalI)).max().getAsInt();
            for (int j = 0; j < plateau.size(); j++) {
                st = plateau.get(j);
                if (st.getUmkreisWert(i) < maxWert) {
                    plateau.remove(st);
                    j--;
                }
            }
            i++;
        }
    }

    public void move() {
        int max_y = (int) Math.ceil((double)l.getY_laenge()/(double)l.getAnzahlAgenten());
        if(x == l.getX_laenge() - 1 && y == initialized_y + max_y - 1) {
            angekommen = true;
        } else {
            if (x < l.getX_laenge() - 1) {
                x++;
            } else {
                x = 0;
                y++;
            }
            if(y < l.getY_laenge() && x < l.getX_laenge())
                checkStelle();
        }

    }

    public void checkStelle() {
        Stelle s = l.getLandschaft().get(y).get(x);
        if(!s.isBesucht()) {
            s.setBesucht(true);
            s.setPlataeuBesucht();
            if(s.isPlataeuMax()) {
                l.addLokalMax(s.getPlataeu());
            }
        }

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Stelle getLabel() {
        return label;
    }

    public void setLabel(Stelle label) {
        this.label = label;
    }

    public List<Stelle> getPlateau() {
        return plateau;
    }

    public void setPlateau(List<Stelle> plateau) {
        this.plateau = plateau;
    }

    public Landschaft getL() {
        return l;
    }

    public void setL(Landschaft l) {
        this.l = l;
    }

    public boolean isAngekommen() {
        return angekommen;
    }

    public void setAngekommen(boolean angekommen) {
        this.angekommen = angekommen;
    }
}

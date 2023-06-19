package AuffindenVonStrukturen;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stelle {
    private final int x;
    private final int y;
    private final int wert;
    private boolean besucht;
    private List<Stelle> plataeu;
    private boolean lok_max;
    private Landschaft l;

    /**
     *
     * @param x
     * @param y
     * @param wert
     */

    private Stelle(int x, int y, int wert) {
         this.x = x;
         this.y = y;
         this.wert = wert;
         this.besucht = false;
         this.plataeu = new LinkedList<>();
         this.lok_max = false;
    }

    public static Stelle stelle(int x, int y, int wert) {
        return new Stelle(x, y, wert);
    }

    /**
     * @param radius Gibt den zu betrachteten Umkreis an. z.B. 1 -> 3 x 3, 2 -> 5 x 5
     * @return Errechnete Steilheit/Flachheit. Je höher Wert, desto flacher
     */
    public Integer getUmkreisWert(int radius) {
        int result = 0;
        int count = 0;
        for(int y = -radius; y <= radius; y++) {
            for(int x = -radius; x <= radius; x++) {
                try {
                    result += l.getLandschaft().get(this.y + y).get(this.x + x).getWert();
                    count++;
                } catch (Exception ignored) {

                }
            }
        }
        return Math.toIntExact(Math.round((double) result / (double) count));
    }
    public boolean isUmkreisKleiner() {
        for(int y = -1; y <= 1; y++) {
            for(int x = -1; x <= 1; x++) {
                try {
                    if(l.getLandschaft().get(this.y + y).get(this.x + x).getWert() > getWert())
                        return false;
                } catch (Exception ignored) {

                }
            }
        }
        return true;
    }

    /**
     *
     * @return Liste mit Stellen auf gleicher Höhe
     */
    public List<Stelle> getUmkreisPlataeu() {
        List<Stelle> list = new LinkedList<>();
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                try {
                    if(l.getLandschaft().get(this.y + i).get(this.x + j).getWert() == getWert()) {
                        list.add(l.getLandschaft().get(this.y + i).get(this.x + j));
                    }
                } catch (Exception ignored) {

                }
            }
        }
        return list;
    }


    public List<Stelle> berechnePlataeu() {
        List<Stelle> plat = Collections.singletonList(this);
        int alte_listsize = plat.size();
        int neue_listsize = 0;
        while(alte_listsize != neue_listsize) {
            alte_listsize = plat.size();
            for (int i = 0; i < alte_listsize; i++) {
                plat = Stream.concat(plat.stream(), plat.get(i).getUmkreisPlataeu().stream()).collect(Collectors.toList());
            }
            plat = new LinkedList<>(new HashSet<>(plat));
            neue_listsize = plat.size();
        }
        this.plataeu = plat;
        return this.plataeu;
    }

    public boolean isPlataeuMax() {
        if(this.plataeu.isEmpty() || this.plataeu == null) berechnePlataeu();
        return this.plataeu.stream().allMatch(s -> s.isUmkreisKleiner());
    }

    public void setPlataeuBesucht() {
        if(this.plataeu.isEmpty() || this.plataeu == null) berechnePlataeu();
        for(Stelle s : this.plataeu) {
            s.setBesucht(true);
        }
    }

    public Stelle findLabelinPlateau() {
        int maxWert = 0;
        int i = 1;
        Stelle st;
        List<Stelle> plateau_tmp = this.plataeu;
        while(plateau_tmp.size() > 1) {
            int finalI = i;
            maxWert = plateau_tmp.stream().mapToInt(s -> s.getUmkreisWert(finalI)).max().getAsInt();
            for(int j = 0; j < plateau_tmp.size(); j++) {
                st = plateau_tmp.get(j);
                if(st.getUmkreisWert(i) < maxWert) {
                    plateau_tmp.remove(st);
                    j--;
                }
            }
            i++;
        }
        //System.out.println(plateau_tmp.get(0));
        return plateau_tmp.get(0);
    }

    public boolean isEqualTo(Stelle s) {
        return this.x == s.getX() && this.y == s.getY();
    }






    /**
    *Getter- und Settermethoden
     */
    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int getWert() {
        return wert;
    }

    public Landschaft getL() {
        return l;
    }

    public void setL(Landschaft l) {
        this.l = l;
    }

    public boolean isBesucht() {
        return besucht;
    }

    public void setBesucht(boolean besucht) {
        this.besucht = besucht;
    }

    public List<Stelle> getPlataeu() {
        return plataeu;
    }

    @Override
    public String toString() {
        return "Stelle{" +
                "x=" + x +
                ", y=" + y +
                ", wert=" + wert +
                '}';
    }
}

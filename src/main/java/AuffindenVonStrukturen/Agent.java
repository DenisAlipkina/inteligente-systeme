package AuffindenVonStrukturen;

import ReisezeitOptimierung.Tuple;

import java.util.Random;

public class Agent {
    private Integer x;
    private Integer y;
    private Integer wert;
    private boolean angekommen;
    private String richtung;
    private Random r;

    public Agent(int x, int y, int wert, Random r) {
        this.x = x;
        this.y = y;
        this.wert = wert;
        this.angekommen = false;
        this.richtung = null;
        this.r = r;
    }

    public void move(Tuple<Integer, Integer> t, String rand) throws Exception {
        if(rand.equals("kein Rand")) {
            if(t.fst() <= this.wert && t.snd() <= wert) {
                setAngekommen(true);
            } else if(t.fst() > this.wert && t.snd() <= wert) {
                move_links(t.fst());
            } else if(t.fst() <= this.wert && t.snd() > wert) {
                move_rechts(t.snd());
            } else if (t.fst() >= this.wert && t.snd() >= wert) {
                if(this.richtung == null) {
                    if(r.nextBoolean()) {
                        setRichtung("links");
                    } else {
                        setRichtung("rechts");
                    }
                }
                if(getRichtung().equals("links")) {
                    move_links(t.fst());
                } else if(getRichtung().equals("rechts")) {
                    move_rechts(t.snd());
                } else {
                    throw new Exception("Agent Richtung Variable ist falsch");
                }
            }
        } else if(rand.equals("linker Rand")) {
            if(getRichtung().equals("links")) {
                setAngekommen(true);
            } else {
                move_rechts(t.snd());
            }
        } else if(rand.equals("rechter Rand")) {
            if(getRichtung().equals("rechts")) {
                setAngekommen(true);
            } else {
                move_links(t.fst());
            }
        } else {
            throw new Exception("Agent muss am linken, rechten Rand der csvListe sein oder drinne. Hier nicht erfüllt. String ist falsch");
        }
    }

    public void move_rechts(Integer wert) {
        setWert(wert);
        setX(getX() + 1);
        setRichtung("rechts");
    }
    public void move_links(Integer wert) {
        setWert(wert);
        setX(getX() - 1);
        setRichtung("links");
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWert() {
        return wert;
    }

    public void setWert(Integer wert) {
        this.wert = wert;
    }

    public boolean isAngekommen() {
        return angekommen;
    }

    public void setAngekommen(boolean angekommen) {
        this.angekommen = angekommen;
    }

    public String getRichtung() {
        return richtung;
    }

    public void setRichtung(String richtung) {
        this.richtung = richtung;
    }
}

package AuffindenVonStrukturen;

import java.io.IOException;

import static AuffindenVonStrukturen.Landschaft.landschaft;

public class Main2 {

    public static void main(String[] args) throws IOException {
        //Umgebung u = new Umgebung("src/main/java/AuffindenVonStrukturen/Daten/data0.csv");
        Landschaft l = landschaft("src/main/java/AuffindenVonStrukturen/Daten/data0.csv");


        Eval2 eval1 = new Eval2(l, "src/main/java/AuffindenVonStrukturen/Daten/label0.csv");
        eval1.run(); //Algo1 (weniger stellen)


        //l.check(3083, 461);
        //l.check(3239, 699);


        /*l.check(0, 113); //Maxima aber nicht label
        l.check(617, 267);
        l.check(2550, 350);
        l.check(307, 137); //1440 - 1457*/

        /*Stelle s = l.getStelle(2725, 532);
        Stelle s1;
        List<Stelle> ls =  s.getPlataeu();
        for(Stelle st : ls) {
            //System.out.println(st);
        }
        //System.out.println(s.findLabelinPlateau());
        */
        /*
        Stelle s, s1;
        List<Stelle> ls = new LinkedList<>();
        Datenleser2 dl = new Datenleser2();
        Landschaft ll = landschaft("src/main/java/AuffindenVonStrukturen/Daten/label0.csv");
        int counter = 0;
        for(List<Stelle> lst : ll.getLandschaft()) {
            s = l.getStelle(lst.get(1).getWert(), lst.get(0).getWert());
            ls =  s.getPlataeu();
            s1 = s.findLabelinPlateau();
            if(s1.getY() == lst.get(0).getWert() && s1.getX() == lst.get(1).getWert()) {
                counter++;
            } else {
                System.out.println(s1);
            }
        }
        System.out.println(counter);*/


        //Findet er
        /*
        Stelle{x=2725, y=532, wert=4899}
        Stelle{x=694, y=184, wert=5182}
        Stelle{x=714, y=571, wert=5233}*/
        //Findet er nicht
        /*
        Stelle{x=2554, y=82, wert=5199}
        Stelle{x=2266, y=619, wert=6089}
        Stelle{x=2503, y=111, wert=5236}*/













        //System.out.println(s.getX() + " " + s.getY());
        //System.out.println(s.getUmkreisWert(1));
        //Agent2 a = Agent2.agent2(Stelle.stelle(1,1,1), l);
        //a.findLabel();




        //u.spawnRaster_bestimmen(9);

        /*u.fillAgenten(10);
        while(!u.checkAgenten()) {
            u.moveAgenten();
        }
        u.check(1);
        System.out.println("\n");
        u.check(4);
        System.out.println("\n");
        u.check(5);
        System.out.println("\n");
        u.check(9);
        System.out.println("\n");*/
        //System.out.println(u.agenten.get(0).getWert());


    }
}

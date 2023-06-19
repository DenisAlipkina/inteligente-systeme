package AuffindenVonStrukturen;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Datenleser2 {

    private List<List<Stelle>> csvDatei = new LinkedList<>();
    private int x_laenge;
    private int y_laenge;

    public void read2DCSV(String dateiPfad) throws IOException {
        String eingabeZeile;
        final FileInputStream fis = new FileInputStream(dateiPfad);
        final DataInputStream inputStream = new DataInputStream(fis);
        int index_x = 0;
        int index_y = 0;
        List<Stelle> list;
        while ((eingabeZeile = inputStream.readLine()) != null) {
            index_x = 0;
            list = new LinkedList<>();
            for(String s : Arrays.stream(eingabeZeile.split(",")).collect(Collectors.toList())) {
                list.add(Stelle.stelle(index_x, index_y, Integer.parseInt(s)));
                index_x++;
            }
            csvDatei.add(list);
            index_y++;
        }
        x_laenge = index_x;
        y_laenge = index_y;
    }

    public List<List<Stelle>> getCsvDatei() {
        return this.csvDatei;
    }

    public int getX_laenge() {
        return x_laenge;
    }

    public int getY_laenge() {
        return y_laenge;
    }
}

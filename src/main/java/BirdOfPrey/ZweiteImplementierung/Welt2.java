package BirdOfPrey.ZweiteImplementierung;

import BirdOfPrey.Agent;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import thl.isys.State;
import thl.isys.World;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class Welt2 {

    private Agent2 agent;
    private World world;
    private State state;
    private int worldNummer;
    File[] listOfFiles;
    private DescriptiveStatistics descriptiveStatisticsPoints;
    private DescriptiveStatistics descriptiveStatisticsTime;

    /**
     * Erstellt eine Instanz der Welt mit Hilfe der {@link Welt2#welt()} Methode
     * Initialisiert {@link Welt2#worldNummer}, {@link Welt2#listOfFiles}, {@link Welt2#agent}, {@link Welt2#state}, {@link Welt2#world}
     */
    private Welt2() {
        worldNummer = 0;
        listOfFiles = new File("src/main/java/BirdOfPrey/Daten").listFiles();
        agent = new Agent2();
        state = new State(0, true, 0, false, false, new LinkedList<>());
        descriptiveStatisticsPoints = new DescriptiveStatistics();
        descriptiveStatisticsTime = new DescriptiveStatistics();
        try {
            world = new World(Objects.requireNonNull(listOfFiles)[worldNummer].getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt eine Welt mit Hilfe des Konstruktors {@link Welt2#Welt2()}
     *
     * @return {@link Welt2#Welt2()}
     */
    public static Welt2 welt() {
        return new Welt2();
    }

    /**
     * Startet die Beobachtung, die Beobachtung läuft so lange {@link State#isObserving()} für den {@link Welt2#state} wahr ist
     */
    public void run() {
        //merkt sich die states
        while (state.isObserving()) {
            agent.saveCurrentState(state);
            state = this.world.nextState(-1);
        }
        //analysiert das gelernte
        agent.analyseMemory();


        while (state.isAlive() && !state.hasSurvived()) {
            //sucht sich einen passenden prey aus
            int energyBefore = state.energy();
            state = this.world.nextState(agent.analyzeCurrentState(state));
            int energyAfter = state.energy();
            if (energyAfter > energyBefore) agent.addPoints();
        }
        descriptiveStatisticsPoints.addValue(agent.getPoints());
        descriptiveStatisticsTime.addValue(state.time());
        String tab = "";
        if(agent.getEatID() == -1) tab += "\t";
        System.out.println("WorldNr: " + worldNummer + "\t Energy: " + state.energy() + "\t Time: " + state.time() + "\t ID_TO_EAT:" + agent.getEatID() + tab + "\t Punkte: " + agent.getPoints());
        createNewWorld();
    }

    /**
     * Erstellt eine neue {@link Welt2#world} mit neuem {@link Agent#agent()} und erhöht die {@link Welt2#worldNummer}
     */
    public void createNewWorld() {
        if (worldNummer < 99) {
            worldNummer++;
            try {
                this.world = new World(Objects.requireNonNull(listOfFiles)[worldNummer].getPath());
                agent = new Agent2();
                state = new State(0, true, 0, false, false, new LinkedList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.run();
        } else {
            System.out.println("Points: ");
            System.out.println(descriptiveStatisticsPoints);
            System.out.println("Time: ");
            System.out.println(descriptiveStatisticsTime);
        }
    }

    /**
     * Liefert einen String mit der {@link Welt2#worldNummer}
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("Welt Nummer: %d\n", worldNummer);
    }

    public static void main(String[] args) {
        Welt2 welt = welt();
        welt.run();
        System.out.println(welt);
    }
}

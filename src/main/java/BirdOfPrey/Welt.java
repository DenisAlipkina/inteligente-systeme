package BirdOfPrey;

import thl.isys.State;
import thl.isys.World;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static BirdOfPrey.Agent.agent;

public class Welt {

    private List<Integer> collectedPoints;
    private Agent agent;
    private World world;
    private State state;
    private int worldNummer;
    File[] listOfFiles;

    /**
     * Erstellt eine Instanz der Welt mit Hilfe der {@link Welt#welt()} Methode
     * Initialisiert {@link Welt#worldNummer}, {@link Welt#listOfFiles}, {@link Welt#agent}, {@link Welt#state}, {@link Welt#world}
     */
    private Welt() {
        worldNummer = 0;
        listOfFiles = new File("src/main/java/BirdOfPrey/Daten").listFiles();
        agent = agent();
        state = new State(0, true, 0, false, false, new LinkedList<>());
        collectedPoints = new LinkedList<>();
        try {
            world = new World(Objects.requireNonNull(listOfFiles)[worldNummer].getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt eine Welt mit Hilfe des Konstruktors {@link Welt#Welt()}
     *
     * @return {@link Welt#Welt()}
     */
    public static Welt welt() {
        return new Welt();
    }

    /**
     * Startet die Beobachtung, die Beobachtung läuft so lange {@link State#isObserving()} für den {@link Welt#state} wahr ist
     */
    public void run() {
        while (state.isObserving()) {
            agent.saveObservation(state.observations());
            state = this.world.nextState(-1);
        }
        agent.analyseAverageLifetime();
        agent.analyseHeading();
        agent.analyseMoveLength();

        while (state.isAlive() && !state.hasSurvived()) {
            agent.saveGameObservation(state.observations());
            agent.classification();
            int energyBefore = state.energy();
            state = this.world.nextState(agent.eatID);
            int energyAfter = state.energy();
            if (energyAfter > energyBefore) agent.addPoints();
        }
        collectedPoints.add(agent.getPoints());
        System.out.format("Welt: %d\tEnergie: %d\tZeit: %d\tPunkte: %d\n", worldNummer + 1, state.energy(), state.time(), agent.getPoints());
//        System.out.format("%d,%d,%d,%d,%d\n", worldNummer + 1, state.energy(), state.time(), agent.getPoints(), collectedPoints.stream().mapToInt(i -> i).sum() / (worldNummer + 1));
        createNewWorld();
    }

    /**
     * Erstellt eine neue {@link Welt#world} mit neuem {@link Agent#agent()} und erhöht die {@link Welt#worldNummer}
     */
    public void createNewWorld() {
        if (worldNummer < getListOfFilesLength() - 1) {
            worldNummer++;
            try {
                this.world = new World(Objects.requireNonNull(listOfFiles)[worldNummer].getPath());
                this.state = new State(0, true, 0, false, false, new LinkedList<>());
                agent = agent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            run();
        } else {
            System.out.println(collectedPoints.stream().mapToInt(i -> i).sum() / 100);
        }
    }

    /**
     * Liefert einen String mit der {@link Welt#worldNummer}
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("Welt Nummer: %d\n", worldNummer);
    }

    /**
     * Liefert die {@link Welt#worldNummer}
     *
     * @return
     */
    public int getWorldNummer() {
        return this.worldNummer;
    }

    /**
     * Liefert die Länge der {@link Welt#listOfFiles}
     *
     * @return
     */
    public int getListOfFilesLength() {
        return this.listOfFiles.length;
    }
}

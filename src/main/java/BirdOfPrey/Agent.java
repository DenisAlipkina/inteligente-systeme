package BirdOfPrey;

import ReisezeitOptimierung.Triple;
import ReisezeitOptimierung.Tuple;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import thl.isys.Observation;
import thl.isys.PreyType;

import java.util.*;

import static BirdOfPrey.Direction.*;
import static thl.isys.PreyType.GOOD;

public class Agent {
    private int points;
    private Map<Integer, List<Observation>> memory;
    private Map<Integer, Double> observation_Length_Move_Good;
    private Map<Integer, Double> observation_Length_Move_Poison;
    private Map<Integer, Double> observation_Length_Move_Neutral;
    private Map<Direction, Double> observation_Heading_Good;
    private Map<Direction, Double> observation_Heading_Poison;
    private Map<Direction, Double> observation_Heading_Neutral;
    private int total_Moves_Good;
    private int total_Moves_Poison;
    private int total_Moves_Neutral;
    private int total_Heading_Good;
    private int total_Heading_Poison;
    private int total_Heading_Neutral;
    public int eatID;
    private int MIN_OBSERVARION_SIZE = 0;
    private int DIFFERENZ = 10;


    /**
     * Erstellt einen Agenten
     * Für die Erstellung wird die Fabrikmethode {@link Agent#agent()} benutzt
     * Initialisiert die Datenfeldert: {@link Agent#memory}, {@link Agent#observation_Length_Move_Good}, {@link Agent#observation_Length_Move_Poison},
     * {@link Agent#observation_Length_Move_Neutral}, {@link Agent#observation_Heading_Good}, {@link Agent#observation_Heading_Poison}, {@link Agent#observation_Heading_Neutral},
     * {@link Agent#total_Moves_Good}, {@link Agent#total_Moves_Poison}, {@link Agent#total_Moves_Neutral}, {@link Agent#total_Heading_Good}, {@link Agent#total_Heading_Poison}, {@link Agent#total_Heading_Neutral}
     */
    private Agent() {
        memory = new HashMap<>();
        observation_Length_Move_Good = new HashMap<>();
        observation_Length_Move_Poison = new HashMap<>();
        observation_Length_Move_Neutral = new HashMap<>();
        observation_Heading_Good = new HashMap<>();
        observation_Heading_Poison = new HashMap<>();
        observation_Heading_Neutral = new HashMap<>();
        total_Moves_Good = 0;
        total_Moves_Poison = 0;
        total_Moves_Neutral = 0;
        total_Heading_Good = 0;
        total_Heading_Poison = 0;
        total_Heading_Neutral = 0;
    }

    /**
     * Erstellt einen Agenten mit Hilfe des Konstruktors {@link Agent#Agent()}
     *
     * @return {@link Agent#Agent()}
     */
    public static Agent agent() {
        return new Agent();
    }

    /**
     * Speichert die Beobachtungen in den ersten 300 Zeitschritten
     *
     * @param observations
     */
    public void saveObservation(List<Observation> observations) {
        observations.forEach(obs -> {
            List<Observation> tmp = this.memory.getOrDefault(obs.id, new LinkedList<>());
            tmp.add(obs);
            memory.put(obs.id, tmp);
        });
    }

    /**
     * Speicher die Beobachtung aus dem Spiel wo keine Informationen über den {@link Observation#type} vorliegen.
     * Zusätzlich werden Irrelevante Observationen gelöscht die nicht mehr zur Vefügung stehen.
     *
     * @param observations
     */
    public void saveGameObservation(List<Observation> observations) {
        memory.keySet().removeIf(key -> observations.stream().noneMatch(obs -> key == obs.id));
        saveObservation(observations);
    }


    /**
     * Berechnet die einzelnen Wahrscheinlichekiten für ein Gutes, Schlechtes oder Neutrales Beutetier
     */
    public void classification() {
        Map<Integer, Triple<Double, Double, Double>> probabilities = new HashMap<>();
        double probabilityGood;
        double probabilityPoison;
        double probabilityNeutral;
        int step = 0;
        Direction direction = KEINE;
        Observation lastObservation;
        for (int key : memory.keySet()) {
            probabilityGood = 0;
            probabilityPoison = 0;
            probabilityNeutral = 0;
            lastObservation = null;
            for (Observation observation : memory.get(key)) {
                if (lastObservation != null) {
                    step = caclulateStepLength(lastObservation, observation);
                    direction = calculateHeading(lastObservation.heading, observation.heading);
                    probabilityGood += observation_Length_Move_Good.get(step) + observation_Heading_Good.get(direction);
                    probabilityPoison += observation_Length_Move_Poison.get(step) + observation_Heading_Poison.get(direction);
                    probabilityNeutral += observation_Length_Move_Neutral.get(step) + observation_Heading_Neutral.get(direction);
                }
                lastObservation = observation;
            }
            probabilities.put(key, Triple.triple(probabilityGood, probabilityPoison, probabilityNeutral));
        }
        eatID = argmax(probabilities);
    }

    /**
     * Prüft welches Beutetier gegessen werden soll
     *
     * @param probabilities
     * @return
     */
    private int argmax(Map<Integer, Triple<Double, Double, Double>> probabilities) {
        Tuple<Integer, Triple<Double, Double, Double>> bestPrey = null;
        for (int key : probabilities.keySet()) {
            if (memory.get(key).size() > agent().MIN_OBSERVARION_SIZE) {
                if (bestPrey != null && probabilities.get(key).fst > bestPrey.fst && probabilities.get(key).fst > probabilities.get(key).trd && (probabilities.get(key).fst - DIFFERENZ) > probabilities.get(key).snd) {
                    bestPrey = Tuple.tuple(key, probabilities.get(key));
                } else if (probabilities.get(key).fst > probabilities.get(key).trd && (probabilities.get(key).fst - DIFFERENZ) > probabilities.get(key).snd) {
                    bestPrey = Tuple.tuple(key, probabilities.get(key));
                }
            }
        }
        return bestPrey != null ? bestPrey.fst : -1;
    }

    /**
     * Analysiert das Laufverhalten der Beutetiere. Berechnet die mittlere Schritt Anzahl bevor ein Richtungswechsel statt findet
     * Für die Berechnung wird ein {@link DescriptiveStatistics} verwendet.
     */
    public void analyseMoveLength() {
        for (Integer id : memory.keySet()) {
            Observation lastObservation = null;
            for (Observation observation : memory.get(id)) {

                if (lastObservation != null) {
                    addLengthMoves(caclulateStepLength(lastObservation, observation), observation);
                }
                lastObservation = observation;
            }
        }
//        System.out.println(observation_Length_Move_Good);
//        System.out.println(observation_Length_Move_Poison);
//        System.out.println(observation_Length_Move_Neutral);
        addPercentageMoves();
//        System.out.println(observation_Length_Move_Good);
//        System.out.println(observation_Length_Move_Poison);
//        System.out.println(observation_Length_Move_Neutral);
    }

    /**
     * Berechnet die durchschnittliche
     */
    public void analyseAverageLifetime() {
        int totalLifeTime = 0;
        for (int key : memory.keySet()) {
            totalLifeTime += memory.get(key).size();
        }
        MIN_OBSERVARION_SIZE = (totalLifeTime / memory.keySet().size());
    }

    /**
     * Berechnet die Anzahl der einzelnen Richtungen in die die Beutetiere laufen
     */
    public void analyseHeading() {
        Direction direction = KEINE;
        Observation lastObservation = null;
        for (Integer id : memory.keySet()) {
            lastObservation = null;
            for (Observation observation : memory.get(id)) {
                if (lastObservation != null) {
                    direction = calculateHeading(lastObservation.heading, observation.heading);
                    if (observation.type.equals(GOOD)) {
                        observation_Heading_Good.put(direction, observation_Heading_Good.getOrDefault(direction, 0.0) + 1);
                        total_Heading_Good++;
                    } else if (observation.type.equals(PreyType.POISONOUS)) {
                        observation_Heading_Poison.put(direction, observation_Heading_Poison.getOrDefault(direction, 0.0) + 1);
                        total_Heading_Poison++;
                    } else {
                        observation_Heading_Neutral.put(direction, observation_Heading_Neutral.getOrDefault(direction, 0.0) + 1);
                        total_Heading_Neutral++;
                    }
                }
                lastObservation = observation;
            }
        }
//        System.out.println(observation_Heading_Good);
//        System.out.println(observation_Heading_Poison);
//        System.out.println(observation_Heading_Neutral);
        addPercentageHeading();
//        System.out.println(observation_Heading_Good);
//        System.out.println(observation_Heading_Poison);
//        System.out.println(observation_Heading_Neutral);
    }

    /**
     * Gibt an in Welche Richtung das Beutetier läuft
     *
     * @param lastHeading
     * @param currentHeading
     * @return VORNE || RECHTS || UNTEN || LINKS || KEINE RICHTUNG
     */
    private Direction calculateHeading(int lastHeading, int currentHeading) {
        int heading = (lastHeading - currentHeading) / 90;
        if (heading == 0) {
            return VOR;
        } else if (heading == -1 || heading == 3) {
            return RECHTS;
        } else if (heading == -2 || heading == 2) {
            return RUNTER;
        } else if (heading == -3 || heading == 1) {
            return LINKS;
        } else return KEINE;
    }

    /**
     * Berechnet die Länge eines Schrittes zwischen zwei {@link Observation}
     *
     * @param lasObservation
     * @param newObservation
     * @return Schrittlänge
     */
    private int caclulateStepLength(Observation lasObservation, Observation newObservation) {
        int stepX = Math.abs(lasObservation.x - newObservation.x);
        if (lasObservation.y == newObservation.y) {
            return stepX;
        } else {
            return (int) Math.hypot(stepX, Math.abs(lasObservation.y - newObservation.y));
        }
    }

    /**
     * Berechnet die Häufigkeit der einzelnen Schrittlängen.
     * Die Werte werden in {@link Agent#observation_Length_Move_Good}, {@link Agent#observation_Length_Move_Poison} oder {@link Agent#observation_Length_Move_Neutral}
     *
     * @param tmp
     * @param observation
     */
    private void addLengthMoves(int tmp, Observation observation) {
        if (observation.type == GOOD) {
            observation_Length_Move_Good.put(tmp, observation_Length_Move_Good.getOrDefault(tmp, 0.0) + 1);
            total_Moves_Good++;
        } else if (observation.type == PreyType.POISONOUS) {
            observation_Length_Move_Poison.put(tmp, observation_Length_Move_Poison.getOrDefault(tmp, 0.0) + 1);
            total_Moves_Poison++;
        } else {
            observation_Length_Move_Neutral.put(tmp, observation_Length_Move_Neutral.getOrDefault(tmp, 0.0) + 1);
            total_Moves_Neutral++;
        }
    }

    /**
     * Berechnet den prozentualen Anteil der Headings und zieht den Logarithmus.
     * Die Werte werden in {@link Agent#observation_Heading_Good}, {@link Agent#observation_Heading_Poison} und {@link Agent#observation_Heading_Neutral} gespeichert
     */
    private void addPercentageHeading() {
        observation_Heading_Good.forEach((key, value) -> {
            observation_Heading_Good.replace(key, Math.log((value) / total_Heading_Good));
        });
        observation_Heading_Poison.forEach((key, value) -> {
            observation_Heading_Poison.replace(key, Math.log((value) / total_Heading_Poison));
        });
        observation_Heading_Neutral.forEach((key, value) -> {
            observation_Heading_Neutral.replace(key, Math.log((value) / total_Heading_Neutral));
        });
    }

    /**
     * Berechnet den prozentualen Anteil der Schritte und zieht den Logarithmus.
     * Die Werte werden in {@link Agent#observation_Length_Move_Good}, {@link Agent#observation_Length_Move_Poison} und {@link Agent#observation_Length_Move_Neutral} gespeichert
     */
    private void addPercentageMoves() {
        observation_Length_Move_Good.forEach((key, value) -> {
            observation_Length_Move_Good.replace(key, Math.log((value) / total_Moves_Good));
        });
        observation_Length_Move_Poison.forEach((key, value) -> {
            observation_Length_Move_Poison.replace(key, Math.log((value) / total_Moves_Poison));
        });
        observation_Length_Move_Neutral.forEach((key, value) -> {
            observation_Length_Move_Neutral.replace(key, Math.log((value) / total_Moves_Neutral));
        });
    }

    /**
     * Liefert die {@link Agent#eatID}
     *
     * @return
     */
    public int getEatID() {
        return eatID;
    }

    /**
     * Fügt den {@link Agent#points} 5 Punkte hinzu
     */
    public void addPoints() {
        points += 5;
    }

    /**
     * Liefert die gesammelten {@link Agent#points} des {@link Agent}
     *
     * @return
     */
    public int getPoints() {
        return points;
    }
}

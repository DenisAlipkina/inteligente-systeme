package BirdOfPrey.ZweiteImplementierung;

import BirdOfPrey.Direction;
import ReisezeitOptimierung.Triple;
import thl.isys.Observation;
import thl.isys.PreyType;
import thl.isys.State;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ReisezeitOptimierung.Triple.triple;

public class Agent2 {
    private State currentState;
    private Map<Integer, List<Observation>> workingMemory;
    private Map<PreyType, Map<Integer, Double>> stepFrequency;
    private Map<PreyType, Map<Direction, Double>> directionFrequency;
    private int eatID;
    private int minObservationSize;
    private int points;
    public double averagePreyLifetime;
    /**
     * Gibt an, ab welcher Abweichung zur 1 (siehe calculatePrecision()), die Precision 0% sein soll.
     */
    private final double SCHWELLWERT_0_PROZENT = 3.6;

    private Map<Integer, Double> lengthMoveGood;
    private Map<Integer, Double> lengthMovePoison;
    private Map<Integer, Double> lengthMoveNeutral;
    private int totalMovesGood;
    private int totalMovesPoison;
    private int totalMovesNeutral;

    private Map<Direction, Double> directionGood;
    private Map<Direction, Double> directionPoison;
    private Map<Direction, Double> directionNeutral;
    private int totalDirectionGood;
    private int totalDirectionPoison;
    private int totalDirectionNeutral;


    public Agent2() {
        workingMemory = new HashMap<>();
        stepFrequency = new HashMap<>();
        directionFrequency = new HashMap<>();
        points = 0;
        averagePreyLifetime = 0;

        lengthMoveGood = new HashMap<>();
        lengthMovePoison = new HashMap<>();
        lengthMoveNeutral = new HashMap<>();
        totalMovesGood = 0;
        totalMovesPoison = 0;
        totalMovesNeutral = 0;

        directionGood = new HashMap<>();
        directionPoison = new HashMap<>();
        directionNeutral = new HashMap<>();
        totalDirectionGood = 0;
        totalDirectionPoison = 0;
        totalDirectionNeutral = 0;
    }


    /**
     * Merkt sich die States
     *
     * @param state
     */
    public void saveCurrentState(State state) {
        fillMemory(state);
    }

    /**
     * Verarbeitet einmalig die gemerkten Daten
     */
    public void analyseMemory() {
        List<Integer> lifeTime = new LinkedList<>();
        for (Integer id : workingMemory.keySet()) {
            Observation lastObservation = null;
            lifeTime.add(workingMemory.get(id).size());
            for (Observation observation : workingMemory.get(id)) {
                if (lastObservation != null) {
                    addLengthMoves(caclulateStepLength(lastObservation, observation), observation);
                    addDirectionMoves(calculateDirection(lastObservation, observation), observation);
                }
                lastObservation = observation;
            }
        }
        averagePreyLifetime = lifeTime.stream().mapToInt(Integer::intValue).average().getAsDouble();
        minObservationSize = (int) averagePreyLifetime;
        addPercentage();
        addDirection();
    }

    /**
     * Berechnet die prozentuale Verteilung der Bewegungen in eine bestimmte Richtung
     */
    private void addDirection() {
        Map<Direction, Double> tmp = new HashMap<>();
        for (Direction direction : directionGood.keySet()) {
            tmp.put(direction, directionGood.getOrDefault(direction, 0.0) / totalDirectionGood);
            directionFrequency.put(PreyType.GOOD, tmp);
        }
        tmp = new HashMap<>();
        for (Direction direction : directionPoison.keySet()) {
            tmp.put(direction, directionPoison.getOrDefault(direction, 0.0) / totalDirectionPoison);
            directionFrequency.put(PreyType.POISONOUS, tmp);
        }
        tmp = new HashMap<>();
        for (Direction direction : directionNeutral.keySet()) {
            tmp.put(direction, directionNeutral.getOrDefault(direction, 0.0) / totalDirectionNeutral);
            directionFrequency.put(PreyType.NEUTRAL, tmp);
        }
    }

    /**
     * Zählt die Anzahl der Bewegungen in bestimmte Richtungen
     *
     * @param direction
     * @param observation
     */
    private void addDirectionMoves(Direction direction, Observation observation) {
        if (observation.type == PreyType.GOOD) {
            directionGood.put(direction, directionGood.getOrDefault(direction, 0.0) + 1);
            totalDirectionGood++;
        } else if (observation.type == PreyType.POISONOUS) {
            directionPoison.put(direction, directionPoison.getOrDefault(direction, 0.0) + 1);
            totalDirectionPoison++;
        } else {
            directionNeutral.put(direction, directionNeutral.getOrDefault(direction, 0.0) + 1);
            totalDirectionNeutral++;
        }
    }

    /**
     * Berechnet anhand der aktuellen und vorherigen Beobachtung, in welche Richtung der Prey sich bewegt
     *
     * @param lastObservation
     * @param observation
     * @return
     */
    private Direction calculateDirection(Observation lastObservation, Observation observation) {
        if (observation.heading == lastObservation.heading) return Direction.VOR;
        if (Math.abs(observation.heading - lastObservation.heading) == 180) return Direction.RUNTER;
        if (lastObservation.heading - observation.heading == 90 || lastObservation.heading - observation.heading == -270)
            return Direction.LINKS;
        return Direction.RECHTS;
    }


    /**
     * Untersucht die aktuelle State und versucht ein Pray zu finden den man essen kann.
     *
     * @param state
     * @return
     */
    public int analyzeCurrentState(State state) {
        fillMemory(state);
        deleteIrrelevants();

        Map<Integer, Triple<Double, Double, Double>> probabilitiesSL = new HashMap<>();
        Map<Integer, Triple<Double, Double, Double>> probabilitiesD = new HashMap<>();

        for (int id : workingMemory.keySet()) {
            if (workingMemory.get(id).get(0).type == PreyType.GOOD) {
                eatID = id;
                return id;
            }
            if (workingMemory.get(id).size() >= minObservationSize) {
                Observation lastobservation = null;
                double probabilityGoodSL = 0;
                double probabilityPoisonSL = 0;
                double probabilityNeutralSL = 0;
                double probabilityGoodD = 0;
                double probabilityPoisonD = 0;
                double probabilityNeutralD = 0;
                int totalCount = 0;
                for (Observation observation : workingMemory.get(id)) {
                    if (lastobservation != null) {
                        int step = caclulateStepLength(lastobservation, observation);
                        Direction direction = calculateDirection(lastobservation, observation);
                        probabilityGoodSL += Math.log(stepFrequency.get(PreyType.GOOD).get(step));
                        probabilityPoisonSL += Math.log(stepFrequency.get(PreyType.POISONOUS).get(step));
                        probabilityNeutralSL += Math.log(stepFrequency.get(PreyType.NEUTRAL).get(step));
                        probabilityGoodD += Math.log(directionFrequency.get(PreyType.GOOD).get(direction));
                        probabilityPoisonD += Math.log(directionFrequency.get(PreyType.POISONOUS).get(direction));
                        probabilityNeutralD += Math.log(directionFrequency.get(PreyType.NEUTRAL).get(direction));

                        totalCount++;
                    }
                    lastobservation = observation;
                }
                probabilitiesSL.put(id, calculatePrecisioninMap(probabilityGoodSL, probabilityPoisonSL, probabilityNeutralSL, totalCount, stepFrequency));
                probabilitiesD.put(id, calculatePrecisioninMap(probabilityGoodD, probabilityPoisonD, probabilityNeutralD, totalCount, directionFrequency));
            }
        }

        eatID = argmax(probabilitiesSL, probabilitiesD);
        return eatID;
    }

    /**
     * Gibt einen Triple zurück, welcher angibt wie gut sich der (Wahrscheinlichkeitspfad oder Markov Kette) errechnete Wert an
     * das erlernte Model passt. Je näher die Zahl an der 1 ist, desto mehr zusammenhänge zwischen Pfad und erlerntem Model gibt es.
     * Mögliche Ausgabe Triple(0.86, 0.21, 0.1) : Wahrscheinlichkeit dass es ein Guter ist wäre 86 %. Ein schlechter hätte 21 %.
     *
     * @param probabilityGood
     * @param probabilityPoison
     * @param probabilityNeutral
     * @param totalCount
     * @return
     */
    private <A> Triple<Double, Double, Double> calculatePrecisioninMap(double probabilityGood, double probabilityPoison, double probabilityNeutral, int totalCount, Map<PreyType, Map<A, Double>> map) {

        double precisionGood = 0, precisionPoison = 0, precisionNeutral = 0;
        double dividerGood = 0, dividerPoison = 0, dividerNeutral = 0;
        for(PreyType type : map.keySet()) {
            for(Double value : map.get(type).values()) {
                if(type == PreyType.GOOD) dividerGood += Math.log(value) * value;
                if(type == PreyType.POISONOUS) dividerPoison += Math.log(value) * value;
                if(type == PreyType.NEUTRAL) dividerNeutral += Math.log(value) * value;
            }
            if(type == PreyType.GOOD) {
                precisionGood = (probabilityGood / (totalCount)) / (dividerGood / map.get(PreyType.GOOD).values().size());
                precisionGood = calculatePrecision(precisionGood);
            }
            if(type == PreyType.POISONOUS) {
                    precisionPoison = (probabilityPoison / (totalCount)) / (dividerPoison / map.get(PreyType.POISONOUS).values().size());
                    precisionPoison = calculatePrecision(precisionPoison);
            }
            if(type == PreyType.NEUTRAL) {
                precisionNeutral = (probabilityNeutral / (totalCount)) / (dividerNeutral / map.get(PreyType.NEUTRAL).values().size());
                precisionNeutral = calculatePrecision(precisionNeutral);
            }
        }
        return triple(precisionGood, precisionPoison, precisionNeutral);
    }

    /**
     * Bekommt einen Wert. Wenn dieser Wert 1 ist, dann gibt es eine 100% übereinstimmung mit dem erlernten und die Methode liefert 1 zurück.
     * Je weiter weg der Eingangswert von 1 ist, desto stärker nähert sich der Rückgabewert der 0 (0% Übereinstimmung)
     *
     * @param value
     * @return
     */
    private double calculatePrecision(double value) {
        double distance = 0;
        if (value < 0) distance = -value + 1;
        else distance = value - 1;

        double min = SCHWELLWERT_0_PROZENT;
        double max = 0;
        double y_achsenabschnitt = ((1 * min) - (0 * max)) / (-0 + SCHWELLWERT_0_PROZENT);
        double steigung = (0 - y_achsenabschnitt) / min;
        double sicherheit = steigung * distance + y_achsenabschnitt;
        return sicherheit <= 0 ? 0.0 : sicherheit >= 1 ? 1 : sicherheit;

    }

    /**
     * setzt die {@link Agent2#currentState} und befüllt die {@link Agent2#workingMemory}
     *
     * @param state
     */
    private void fillMemory(State state) {
        currentState = state;
        List<Observation> tmp;
        for (Observation observation : state.observations()) {
            tmp = workingMemory.getOrDefault(observation.id, new LinkedList<>());
            tmp.add(observation);
            workingMemory.put(observation.id, tmp);
        }
    }

    /**
     * Löscht die bereits toten bzw. verschwundenen Preys
     */
    private void deleteIrrelevants() {
        workingMemory.keySet().removeIf(key -> currentState.observations().stream().noneMatch(obs -> key == obs.id));
    }

    /**
     * Berechnet die Länge eines Schrittes zwischen 2 {@link Observation}
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
     * Berechnet die Häufigkeit der einzelnen Schrittlängen
     *
     * @param stepSize
     * @param observation
     */
    private void addLengthMoves(int stepSize, Observation observation) {
        if (observation.type == PreyType.GOOD) {
            lengthMoveGood.put(stepSize, lengthMoveGood.getOrDefault(stepSize, 0.0) + 1);
            totalMovesGood++;
        } else if (observation.type == PreyType.POISONOUS) {
            lengthMovePoison.put(stepSize, lengthMovePoison.getOrDefault(stepSize, 0.0) + 1);
            totalMovesPoison++;
        } else {
            lengthMoveNeutral.put(stepSize, lengthMoveNeutral.getOrDefault(stepSize, 0.0) + 1);
            totalMovesNeutral++;
        }
    }

    /**
     * Berechnet den prozentualen Anteil der Schritte
     */
    private void addPercentage() {
        Map<Integer, Double> tmp = new HashMap<>();
        for (int steplength : lengthMoveGood.keySet()) {
            tmp.put(steplength, lengthMoveGood.getOrDefault(steplength, 0.0) / totalMovesGood);
            stepFrequency.put(PreyType.GOOD, tmp);
        }
        tmp = new HashMap<>();
        for (int steplength : lengthMovePoison.keySet()) {
            tmp.put(steplength, lengthMovePoison.getOrDefault(steplength, 0.0) / totalMovesPoison);
            stepFrequency.put(PreyType.POISONOUS, tmp);
        }
        tmp = new HashMap<>();
        for (int steplength : lengthMoveNeutral.keySet()) {
            tmp.put(steplength, lengthMoveNeutral.getOrDefault(steplength, 0.0) / totalMovesNeutral);
            stepFrequency.put(PreyType.NEUTRAL, tmp);
        }
    }

    /**
     * Berechnet den besten Prey (falls vorhanden) und liefert seine ID oder -1, wenn es keinen besten gibt
     *
     * @param probabilitiesSL
     * @return
     */
    private int argmax(Map<Integer, Triple<Double, Double, Double>> probabilitiesSL, Map<Integer, Triple<Double, Double, Double>> probabilitiesD) {
        Triple<Integer, Triple<Double, Double, Double>, Triple<Double, Double, Double>> bestPrey = null;
        for (int id : probabilitiesSL.keySet()) {
            double probabilityGood = (probabilitiesSL.get(id).fst * probabilitiesD.get(id).fst);
            double probabilityPoison = (probabilitiesSL.get(id).snd * probabilitiesD.get(id).snd);
            double probabilityNeutral = (probabilitiesSL.get(id).trd * probabilitiesD.get(id).trd);

            if (bestPrey != null && ((bestPrey.snd.fst * bestPrey.trd.fst) < probabilityGood) && probabilityGood > probabilityPoison && probabilityGood > 0 && probabilityPoison > 0) {
                bestPrey = triple(id, probabilitiesSL.get(id), probabilitiesD.get(id));
            } else if (probabilityGood > probabilityPoison && probabilityGood > 0 && probabilityPoison > 0) {
                bestPrey = triple(id, probabilitiesSL.get(id), probabilitiesD.get(id));
            }
        }
        return bestPrey == null ? -1 : bestPrey.fst;
    }

    public int getEatID() {
        return eatID;
    }

    public void addPoints() {
        points += 5;
    }

    public int getPoints() {
        return points;
    }

}

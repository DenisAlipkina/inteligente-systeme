package ReisezeitOptimierung;

import java.io.Serializable;
import java.util.Objects;

public class Triple<A, B, C> implements Serializable {

    public final A fst;
    public final B snd;
    public final C trd;

    @SuppressWarnings("checkstyle:HiddenField")
    public Triple(A fst, B snd, C trd) {
        this.fst = Objects.requireNonNull(fst);
        this.snd = Objects.requireNonNull(snd);
        this.trd = Objects.requireNonNull(trd);
    }

    /**
     * Liefert den ersten Inhalt.
     * @return Erster Inhalt
     */
    public A fst() {
        return this.fst;
    }

    /**
     * Liefert den zweiten Inhalt.
     * @return Zweiter Inhalt
     */
    public B snd() {
        return this.snd;
    }

    /**
     * Liefert den dritten Inhalt
     * @return
     */
    public C trd() {
        return this.trd;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,%s)", fst, snd, trd);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple))
            return false;
        else {
            @SuppressWarnings("rawtypes")
            Triple that = (Triple) o;
            return fst.equals(that.fst) && snd.equals(that.snd) && trd.equals(that.trd);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fst.hashCode();
        result = prime * result + snd.hashCode();
        result = prime * result + trd.hashCode();
        return result;
    }

    public Triple<B, A, C> swap() {
        return new Triple<>(snd, fst, trd);
    }

    public static <A, B, C> Triple<A, B, C> triple(A fst, B snd, C trd) {
        return new Triple<>(fst, snd, trd);
    }
}

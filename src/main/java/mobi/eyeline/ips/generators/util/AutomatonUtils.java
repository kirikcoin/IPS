package mobi.eyeline.ips.generators.util;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import mobi.eyeline.ips.generators.UnsupportedPatternException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class AutomatonUtils {

    public static Automaton toAutomaton(String regex) throws UnsupportedPatternException {
        try {
            return new RegExp(regex).toAutomaton();
        } catch (IllegalArgumentException e) {
            throw new UnsupportedPatternException(e);
        }
    }

    public static String[] asStateSymbols(Automaton a) throws UnsupportedPatternException {
        if (!a.isFinite()) {
            throw new UnsupportedPatternException("Infinite expression");
        }

        if (a.getAcceptStates().size() > 1) {
            throw new UnsupportedPatternException("Non-fixed length");
        }

        final List<String> options = new ArrayList<>(a.getNumberOfStates());

        State state = a.getInitialState();
        while (isNotEmpty(state.getTransitions())) {
            final Set<Transition> transitions = state.getTransitions();

            final Transition transition = transitions.iterator().next();
            final State next = transition.getDest();

            if (!eachLeadsTo(transitions, next)) {
                throw new UnsupportedPatternException();
            }

            final StringBuilder chars = new StringBuilder();
            for (Transition t : transitions) {
                CharUtils.collect(chars, t.getMin(), t.getMax());
            }
            options.add(chars.toString());

            state = next;
        }

        return options.toArray(new String[options.size()]);
    }

    private static boolean eachLeadsTo(Collection<Transition> transitions,
                                       State target) {
        for (Transition t : transitions) {
            if (!t.getDest().equals(target)) {
                return false;
            }
        }
        return true;
    }
}

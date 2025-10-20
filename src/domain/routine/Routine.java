package domain.routine;

import domain.profile.Step;

import java.util.List;
import java.util.Set;

public class Routine {
    private String name;
    private List<Step> steps;
    private Set<RoutineKind> routineKinds;

    public Routine(String name, List<Step> steps, Set<RoutineKind> routineKinds) {
        this.name = name;
        this.steps = List.copyOf(steps);
        this.routineKinds = Set.copyOf(routineKinds);
    }

    public String getName() {
        return name;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Set<RoutineKind> getRoutineKinds() {
        return routineKinds;
    }
}
package domain.routine;


import domain.day_plan.DayPlan;
import domain.profile.SkinType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WeeklyRoutinePlanner {

    private final RoutineLibrary routineLibrary;
    private final Random random;

    public WeeklyRoutinePlanner(RoutineLibrary routineLibrary) {
        this.routineLibrary = routineLibrary;
        this.random = new Random();
    }

    public List<DayPlan> planWeek(SkinType skinType) {
        List<DayPlan> weekPlan = new ArrayList<>(7);
        Routine prevPM = null;
        for (int i = 0; i < 7; i++) {
            Routine amRoutine = pick(routineLibrary.amCandidates(skinType));
            Routine pmRoutine = pickAvoiding(routineLibrary.pmCandidates(skinType), prevPM);
            weekPlan.add(new DayPlan(amRoutine, pmRoutine));
            prevPM = pmRoutine;
        }
        return weekPlan;
    }

    private Routine pick(List<Routine> routines) {
        return routines.get(random.nextInt(routines.size()));
    }

    private Routine pickAvoiding(List<Routine> routines, Routine prevRoutine) {
        if (prevRoutine == null) return pick(routines);
        List<Routine> filtered = routines
                .stream()
                .filter(routine -> Collections.disjoint(routine.getRoutineKinds(), prevRoutine.getRoutineKinds()))
                .toList();
        return pick(filtered);
    }
}
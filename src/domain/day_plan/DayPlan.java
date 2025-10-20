package domain.day_plan;

import domain.profile.TimeOfDay;
import domain.routine.Routine;

import java.util.HashMap;
import java.util.Map;

public class DayPlan {

    private final Map<TimeOfDay, Routine> map = new HashMap<>();

    public DayPlan(Routine morning, Routine night) {
        this.map.put(TimeOfDay.MORNING, morning);
        this.map.put(TimeOfDay.NIGHT, night);
    }

    public Routine get(TimeOfDay timeOfDay) {
        return map.get(timeOfDay);
    }
}
package domain.routine;


import domain.day_plan.DayPlan;
import domain.product.Product;
import domain.product.ProductCatalog;
import domain.product.ProductRecommender;
import domain.profile.SkinType;
import domain.profile.Step;
import domain.profile.TimeOfDay;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkincareService {

    private final WeeklyRoutinePlanner planner;
    private final ProductRecommender recommender;

    public SkincareService(WeeklyRoutinePlanner planner, ProductRecommender recommender) {
        this.planner = planner;
        this.recommender = recommender;
    }

    public static SkincareService defaultWired() {
        WeeklyRoutinePlanner weeklyRoutinePlanner = new WeeklyRoutinePlanner();
        ProductCatalog productCatalog = ProductCatalog.loadAllFromConstants();
        ProductRecommender productRecommender = new ProductRecommender(productCatalog);
        return new SkincareService(weeklyRoutinePlanner, productRecommender);
    }

    public List<DayPlan> buildWeeklyRoutine(SkinType skinType) {
        return planner.planWeek(skinType);
    }

    public Map<Routine, Map<Step, List<Product>>> recommendProductForWeek(List<DayPlan> weeklyPlans, int topN) {
        Map<String, Routine> uniqueRoutines = new LinkedHashMap<>();
        for (DayPlan dayPlan : weeklyPlans) {
            Routine morningRoutine = dayPlan.get(TimeOfDay.MORNING);
            Routine nightRoutine = dayPlan.get(TimeOfDay.NIGHT);
            uniqueRoutines.putIfAbsent(createRoutineKey(morningRoutine), morningRoutine);
            uniqueRoutines.putIfAbsent(createRoutineKey(nightRoutine), nightRoutine);
        }
        return uniqueRoutines.values()
                .stream()
                .collect(
                        Collectors.toMap(
                                routine -> routine,
                                routine -> recommender.recommend(routine, topN),
                                (existing, duplicate) -> existing,
                                LinkedHashMap::new
                        )
                );
    }

    private String createRoutineKey(Routine routine) {
        return routine.getName() + "_" + routine.getRoutineKinds().toString();
    }
}
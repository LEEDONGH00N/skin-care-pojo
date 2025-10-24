package domain.routine;

import domain.day_plan.DayPlan;
import domain.ingredient.IngredientService;
import domain.product.Product;
import domain.profile.SkinType;
import domain.profile.Step;
import view.ConsoleView;

import java.util.List;
import java.util.Map;

public class SkincareController {

    private final SkincareService routineService;
    private final IngredientService ingredientService;

    public SkincareController(SkincareService service, IngredientService ingredientService) {
        this.routineService = service;
        this.ingredientService = ingredientService;
    }

    public void run() {
        ConsoleView.showWelcome();
        SkinType skin = ConsoleView.askSkinTypeSelection(SkinType.values());
        if (skin == null) {
            ConsoleView.showError("잘못된 입력입니다. 프로그램을 종료합니다.");
            return;
        }
        List<DayPlan> week = routineService.buildWeeklyRoutine(skin);
        Map<Routine, Map<Step, List<Product>>> recs = routineService.recommendProductForWeek(week, 3);
        ConsoleView.renderWeekTable(week, skin);
        ConsoleView.renderRecommendations(recs);
    }
}
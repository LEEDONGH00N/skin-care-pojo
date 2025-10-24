package global;

import domain.ingredient.IngredientRepository;
import domain.ingredient.IngredientService;
import domain.product.ProductCatalog;
import domain.product.ProductRecommender;
import domain.routine.RoutineLibrary;
import domain.routine.SkincareService;
import domain.routine.WeeklyRoutinePlanner;

public class AppConfig {

    private SkincareService skincareService;
    private IngredientService ingredientService;

    public AppConfig() {
    }

    public AppConfig(SkincareService skincareService,
                     IngredientService ingredientService) {
        this.skincareService = skincareService;
        this.ingredientService = ingredientService;
    }
    public IngredientRepository ingredientRepository() {
        return new IngredientRepository();
    }

    public IngredientService ingredientService() {
        return new IngredientService(ingredientRepository());
    }

    public ProductCatalog productCatalog() {
        return ProductCatalog.loadAllFromConstants();
    }

    public ProductRecommender productRecommender() {
        return new ProductRecommender(productCatalog());
    }

    public RoutineLibrary routineLibrary() {
        return new RoutineLibrary();
    }

    public WeeklyRoutinePlanner weeklyRoutinePlanner() {
        return new WeeklyRoutinePlanner(routineLibrary());
    }

    public SkincareService skincareService() {
        return new SkincareService(
                weeklyRoutinePlanner(),
                productRecommender()
        );
    }

    public AppConfig load() {
        SkincareService skincareService = new SkincareService(weeklyRoutinePlanner(), productRecommender());
        IngredientService ingredientService = new IngredientService(ingredientRepository());
        return new AppConfig(skincareService, ingredientService);
    }
}

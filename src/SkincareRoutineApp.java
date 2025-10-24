
import domain.routine.SkincareController;
import global.AppConfig;

public class SkincareRoutineApp {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        config.load();
        SkincareController controller =
                new SkincareController(config.skincareService(), config.ingredientService());
        controller.run();
    }
}
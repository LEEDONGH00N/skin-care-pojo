import domain.routine.SkincareService;
import domain.routine.SkincareController;

public class SkincareRoutineApp {
    public static void main(String[] args) {
        SkincareController skincareController = new SkincareController(SkincareService.defaultWired());
        skincareController.run();
    }
}
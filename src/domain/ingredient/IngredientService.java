package domain.ingredient;

public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(domain.ingredient.IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
}

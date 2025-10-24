package domain.ingredient;

import domain.category.FunctionType;

import java.util.HashSet;
import java.util.Set;

public class Ingredient {
    private String inciEngName;
    private String inciKorName;
    private Set<FunctionType> functionTypes = new HashSet<>();

    public Ingredient(String inciEngName, String inciKorName) {
        this.inciEngName = inciEngName;
        this.inciKorName = inciKorName;
    }

    public Ingredient(String inciEngName, String inciKorName, Set<FunctionType> functionTypes) {
        this.inciEngName = inciEngName;
        this.inciKorName = inciKorName;
        this.functionTypes = functionTypes;
    }

    public String getInciEngName() {
        return inciEngName;
    }

    public String getInciKorName() {
        return inciKorName;
    }

    public Set<FunctionType> getFunctionNames() {
        return functionTypes;
    }

    public void addFunctionName(FunctionType functionType) {
        functionTypes.add(functionType);
    }
}

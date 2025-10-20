package domain.profile;

import domain.category.FunctionType;

import java.util.Set;

import static domain.category.FunctionType.*;

public enum SkinType {

    OILY(Set.of(ANTI_SEBUM, ASTRINGENT, ANTIMICROBIAL)),
    DRY(Set.of(HUMECTANT, OCCLUSIVE, MOISTURISING)),
    COMBINATION(Set.of(HUMECTANT, ANTI_SEBUM)),
    SENSITIVE(Set.of(SOOTHING, SKIN_PROTECTING, REFRESHING));

    private final Set<FunctionType> relatedFunctions;

    SkinType(Set<FunctionType> relatedFunctions) {
        this.relatedFunctions = relatedFunctions;
    }
}

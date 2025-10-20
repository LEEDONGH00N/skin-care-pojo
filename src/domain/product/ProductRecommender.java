package domain.product;

import domain.ingredient.KeywordBank;
import domain.profile.Step;
import domain.routine.Routine;
import domain.routine.RoutineKind;

import java.util.*;
import java.util.stream.Collectors;

public class ProductRecommender {

    private final ProductCatalog catalog;

    public ProductRecommender(ProductCatalog catalog) {
        this.catalog = catalog;
    }

    public Map<Step, List<Product>> recommend(Routine routine, int topN) {
        Map<Step, List<Product>> poolByStep = catalog.byStep();
        Set<RoutineKind> kinds = routine.getRoutineKinds();
        return routine.getSteps()
                .stream()
                .collect(Collectors.toMap(
                        step -> step,
                        step -> topMatches(poolByStep.getOrDefault(step, Collections.emptyList()), kinds, topN)
                ));
    }

    private List<Product> topMatches(List<Product> candidates, Set<RoutineKind> kinds, int topN) {
        List<String> keywords = kinds
                .stream()
                .flatMap(k -> KeywordBank.KEYWORDS.getOrDefault(k, Collections.emptyList()).stream())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .toList();
        return candidates.stream()
                .map(product -> new AbstractMap.SimpleEntry<>(product, matchScore(product, keywords)))
                .filter(entry -> entry.getValue() > 0)
                .sorted(Comparator.comparingInt((AbstractMap.SimpleEntry<Product, Integer> entry) -> entry.getValue()).reversed())
                .limit(topN)
                .map(AbstractMap.SimpleEntry::getKey)
                .toList();
    }

    private int matchScore(Product product, List<String> keywords) {
        String text = (product.name + " " + product.ingredients).toLowerCase(Locale.ROOT);
        int score = 0;
        for (String k : keywords) {
            if (text.contains(k)) score++;
        }
        return score;
    }
}
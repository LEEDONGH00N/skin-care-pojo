package domain.ingredient;


import domain.category.FunctionType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class IngredientRepository {

    private final Map<String, Ingredient> ingredients = new LinkedHashMap<>();

    public void save(Ingredient ingredient) {
        ingredients.put(ingredient.getInciEngName().toLowerCase(), ingredient);
    }

    public Collection<Ingredient> findAll() {
        return ingredients.values();
    }

    public void saveToCsv(String path) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write("inciEngName,iniciKorName,functionNames\n");
            for (Ingredient ing : ingredients.values()) {
                String functions = String.join("|",
                        ing.getFunctionNames().stream().map(Enum::name).toList());
                writer.write(String.format("%s,%s,%s\n",
                        ing.getInciEngName(), ing.getInciKorName(), functions));
            }
            System.out.println("CSV 저장 완료: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromCsv(String path) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // 헤더 스킵
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length < 2) continue;

                String eng = parts[0].trim();
                String kor = parts[1].trim();
                Set<FunctionType> functions = new HashSet<>();

                if (parts.length == 3 && !parts[2].isBlank()) {
                    for (String f : parts[2].split("\\|")) {
                        try {
                            functions.add(FunctionType.valueOf(f.trim()));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }

                Ingredient ing = new Ingredient(eng, kor, functions);
                save(ing);
            }

            System.out.println("CSV 로드 완료: " + ingredients.size() + "개");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package domain.ingredient;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IngredientLoader {

    private static final Pattern PAREN_PATTERN = Pattern.compile("\\([^)]*\\)");

    /** 괄호 제거 + 공백 정리 */
    private static String cleanName(String name) {
        if (name == null) return "";
        String cleaned = PAREN_PATTERN.matcher(name).replaceAll(""); // 괄호 제거
        cleaned = cleaned.replaceAll("\\s+", " ").trim(); // 공백 정리
        return cleaned;
    }

    public static Map<String, Ingredient> loadIngredients(String csvPath) {
        Map<String, Ingredient> ingredients = new LinkedHashMap<>();

        try (InputStream is = new FileInputStream(csvPath);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr);
             CSVReaderHeaderAware reader = new CSVReaderHeaderAware(br)) {

            Map<String, String> row;
            System.out.println("📂 MFDS CSV 읽기 시작...");
            while ((row = reader.readMap()) != null) {
                String kr = row.get("INGR_KOR_NAME");
                String en = row.get("INGR_ENG_NAME");
                if (en == null || en.isBlank()) continue;

                // ✅ 쉼표로 연결된 다중 성분 처리
                String[] enParts = en.split(",");
                String[] krParts = kr != null ? kr.split(",") : new String[enParts.length];

                for (int i = 0; i < enParts.length; i++) {
                    String eng = cleanName(enParts[i]);
                    if (eng.isBlank()) continue;

                    String kor = (i < krParts.length) ? cleanName(krParts[i]) : "";
                    Ingredient ingredient = new Ingredient(eng, kor);
                    ingredients.put(eng.toUpperCase(), ingredient);
                }
            }

            System.out.println("✅ 총 로드된 성분 수: " + ingredients.size());

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
    public static void main(String[] args) {
        // 1️⃣ MFDS CSV 파일 경로
        String path = "/Users/leedonghoon/Downloads/화장품원료성분정보조회2.csv";

        // 2️⃣ CSV → Map 로드
        Map<String, Ingredient> loaded = loadIngredients(path);

        // 3️⃣ Repository 저장
        IngredientRepository repo = new IngredientRepository();
        loaded.values().forEach(repo::save);

        // 4️⃣ CosIng API 기능 가져오기 + 저장
        FunctionUpdater updater = new FunctionUpdater(repo);
        updater.updateAllAndSave("/Users/leedonghoon/Desktop/ingredient/inci.csv");

        // 5️⃣ 결과 예시 출력
        repo.findAll().stream().limit(5).forEach(System.out::println);
    }
}
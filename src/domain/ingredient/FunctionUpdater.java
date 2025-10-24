package domain.ingredient;


import domain.category.FunctionType;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;


public class FunctionUpdater {
    private final IngredientRepository repo;

    public FunctionUpdater(IngredientRepository repo) {
        this.repo = repo;
    }

    /**
     * 모든 성분의 기능을 CosIng API에서 자동 갱신 후 CSV로 저장
     */
    public void updateAllAndSave(String savePath) {
        int count = 0;

        // 헤더 한 번만 작성
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(savePath), StandardCharsets.UTF_8))) {

            writer.write("id,inciEngName,iniciKorName,functionNames\n");

            int i = 0;
            for (Ingredient ingredient : repo.findAll()) {
                Set<FunctionType> fetched = CosingFunctionFetcher.fetchFunctions(ingredient.getInciEngName());

                if (!fetched.isEmpty()) {
                    fetched.forEach(ingredient::addFunctionName);
                    count++;
                    System.out.printf("✅ %s → %s%n", ingredient.getInciEngName(), fetched);
                } else {
                    System.out.printf("⚠️ %s → 기능 없음%n", ingredient.getInciEngName());
                }

                // 🔹 매번 바로 CSV에 한 줄씩 append
                String functions = String.join("|",
                        ingredient.getFunctionNames().stream().map(Enum::name).toList());

                writer.write(String.format("%s,%s,%s,%s\n",
                        String.valueOf(++i),
                        ingredient.getInciEngName(),
                        ingredient.getInciKorName(),
                        functions));
                writer.flush(); // 즉시 디스크 반영

                try {
                    Thread.sleep(1200);
                } catch (InterruptedException ignored) {
                } // API rate limit
            }

            System.out.printf("💾 업데이트 완료: %d개 성분 기능 갱신 및 저장 (%s)%n", count, savePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

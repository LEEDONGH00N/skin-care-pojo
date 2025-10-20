package view;


import domain.day_plan.DayPlan;
import domain.product.Product;
import domain.profile.SkinType;
import domain.profile.Step;
import domain.profile.TimeOfDay;
import domain.routine.Routine;

import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ConsoleView{

    private static final Scanner sc = new Scanner(System.in);
    private static final Map<SkinType, String> SKIN_LABEL = new HashMap<>() {{
        put(SkinType.DRY, "건성 피부");
        put(SkinType.OILY, "지성 피부");
        put(SkinType.COMBINATION, "복합성 피부");
        put(SkinType.SENSITIVE, "민감성 피부");
    }};

    public static void showWelcome() {
        banner("🌿 스킨케어 루틴 추천 시스템");
    }

    public static SkinType askSkinTypeSelection(SkinType[] options) {
        line();
        System.out.println("피부 타입을 선택하세요:");
        for (SkinType t : options) {
            System.out.printf("- %-12s (%s)%n", t.name(), SKIN_LABEL.get(t));
        }
        System.out.print("입력 ▶ ");
        String in = sc.nextLine();
        line();
        try {
            return SkinType.valueOf(in.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static void renderWeekTable(List<DayPlan> week, SkinType skinType) {
        line();
        System.out.printf("[%s] 1주일 루틴%n", SKIN_LABEL.get(skinType));
        String[] days = {"월","화","수","목","금","토","일"};
        line();
        System.out.printf("%-3s │ %-10s │ %-36s%n", "요일", "아침(루틴/스텝)", "저녁(루틴/스텝)");
        line();
        for (int i = 0; i < week.size(); i++) {
            Routine am = week.get(i).get(TimeOfDay.MORNING);
            Routine pm = week.get(i).get(TimeOfDay.NIGHT);
            System.out.printf("%-3s │ %-10s │ %-36s%n",
                    days[i],
                    am.getName(),
                    pm.getName());
        }
    }

    public static void renderRecommendations(Map<Routine, Map<Step, List<Product>>> recs) {
        banner("🛒 루틴별 추천 아이템(상위 3개)");
        for (Map.Entry<Routine, Map<Step, List<Product>>> entry : recs.entrySet()) {
            line();
            Routine r = entry.getKey();
            Map<Step, List<Product>> byStep = entry.getValue();

            System.out.printf("%s%n", r.getName());

            for (Step step : r.getSteps()) {
                List<Product> list = byStep.get(step);
                String line = (list == null || list.isEmpty())
                        ? "추천 대상 없음"
                        : list.stream()
                        .map(p -> String.format("%s | %,d원 | %s", shorten(p.name, 28), p.price, p.url))
                        .collect(Collectors.joining("  /  "));
                System.out.printf("  - %-10s : %s%n", step.name(), line);
            }
        }
    }

    public static void showError(String message) {
        System.out.println("⚠️ " + message);
    }

    private static void banner(String title) {
        line();
        System.out.println("✨ " + title + " ✨");
    }

    private static void line() {
        System.out.println("────────────────────────────────────────────────────────────────────────────");
    }

    private static String shorten(String s, int n) {
        if (s == null) return "";
        String t = s.replaceAll("\\s+"," ").trim();
        return t.length() > n ? t.substring(0, n) + "…" : t;
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }
}
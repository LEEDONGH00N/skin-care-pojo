package global.musinsa;


import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusinsaCrawler {

    private static final String UA =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0 Safari/537.36";

    // 목록 API(PLP)
    private static final String LIST_API =
            "https://api.musinsa.com/api2/dp/v1/plp/goods?gf=A&sortCode=SALE_ONE_YEAR_COUNT&category=%s&size=%d&caller=CATEGORY&page=%d";

    // 상품 고시정보(전성분)
    private static final String ESSENTIAL_API =
            "https://goods-detail.musinsa.com/api2/goods/%s/essential";

    public static void main(String[] args) throws Exception {
        // 예시: 패드 104001014002, 스킨/토너 104001014001
        String category = "104001013001";
        int startPage   = 1;
        int endPage     = 5;
        int pageSize    = 60;
        String outCsv   = "/Users/leedonghoon/Desktop/cosmetics/cream/cream.csv";

        List<Product> products = new ArrayList<>();

        // 1) 카테고리 목록 수집
        for (int page = startPage; page <= endPage; page++) {
            String url = String.format(LIST_API, category, pageSize, page);
            String json = httpGetJson(url);
            List<Product> pageItems = parseListJsonSafely(json);
            System.out.printf("📥 page %d → %d개 수집%n", page, pageItems.size());
            products.addAll(pageItems);
            sleep(800, 400);
        }
        System.out.println("총 수집 상품 수: " + products.size());

        // 2) 각 상품 전성분 수집
        for (Product p : products) {
            try {
                String api = String.format(ESSENTIAL_API, p.goodsNo);
                String json = httpGetJson(api);
                String ing = extractIngredientsFromEssential(json);
                p.ingredients = cleanIngredients(ing);
                System.out.printf("✅ %s | %s → 전성분 %s%n",
                        p.goodsNo, shorten(p.name, 40),
                        p.ingredients == null ? "없음" : shorten(p.ingredients, 60));
            } catch (Exception e) {
                System.out.printf("⚠️ %s | %s → essential 오류: %s%n",
                        p.goodsNo, shorten(p.name, 40), e.getMessage());
            }
            sleep(800, 400);
        }

        // 3) CSV 저장
        saveCsv(outCsv, products);
        System.out.println("💾 저장 완료: " + outCsv);
    }

    /* ========================= HTTP ========================= */

    private static String httpGetJson(String url) throws Exception {
        Connection conn = Jsoup.connect(url)
                .ignoreContentType(true)
                .userAgent(UA)
                .referrer("https://www.musinsa.com/")
                .timeout((int) Duration.ofSeconds(15).toMillis())
                .header("Accept", "application/json, text/plain, */*");
        return conn.execute().body();
    }

    private static void sleep(long baseMs, long jitterMs) {
        try {
            long ms = baseMs + (long) (Math.random() * jitterMs);
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    /* ========================= 목록 파싱 =========================
       정규식으로 JSON 전체를 훑지 않고,
       "list":[ ... ] 블록에서 { ... } 객체를 깊이 추적해서 안전 분리
     */

    private static List<Product> parseListJsonSafely(String json) {
        List<Product> out = new ArrayList<>();
        if (json == null || json.isBlank()) return out;

        int listStart = json.indexOf("\"list\"");
        if (listStart < 0) return out;

        int arrStart = json.indexOf('[', listStart);
        if (arrStart < 0) return out;

        int i = arrStart + 1;
        int depth = 0;
        int objStart = -1;

        while (i < json.length()) {
            char c = json.charAt(i);

            if (c == '{') {
                if (depth == 0) objStart = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && objStart >= 0) {
                    String block = json.substring(objStart, i + 1);
                    Product p = parseOneItemBlock(block);
                    if (p != null) out.add(p);
                    objStart = -1;
                }
            } else if (c == ']' && depth == 0) {
                break; // list 배열 종료
            }
            i++;
        }
        return out;
    }

    // 한 개 상품 블록에서 필드 추출
    private static Product parseOneItemBlock(String block) {
        String goodsNo = extract(block, "\"goodsNo\"\\s*:\\s*(\\d+)");
        String name    = cleanProductName(unescape(extract(block, "\"goodsName\"\\s*:\\s*\"([^\"]*)\"")));
        String url     = unescape(extract(block, "\"goodsLinkUrl\"\\s*:\\s*\"([^\"]*)\""));
        String price   = extract(block, "\"price\"\\s*:\\s*(\\d+)");
        String brand   = unescape(extract(block, "\"brandName\"\\s*:\\s*\"([^\"]*)\""));

        if (goodsNo == null || url == null) return null;
        if (name == null) name = "";
        if (brand == null) brand = "";
        if (price == null) price = "";

        return new Product(goodsNo, brand, name, price, url);
    }

    private static String extract(String text, String regex) {
        Matcher m = Pattern.compile(regex).matcher(text);
        return m.find() ? m.group(1) : null;
    }

    private static String unescape(String s) {
        if (s == null) return null;
        return s.replace("\\n", " ")
                .replace("\\r", " ")
                .replace("\\t", " ")
                .replace("\\\"", "\"")
                .replace("\\/", "/")
                .trim();
    }

    // 제품명 정제: [] / () 제거 + 공백 정리
    private static String cleanProductName(String name) {
        if (name == null) return "";
        return name.replaceAll("\\[.*?\\]", "")
                .replaceAll("\\(.*?\\)", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /* ========================= 전성분 추출 ========================= */

    // “화장품법에 따라 …” 라벨을 포함하는 항목의 value 추출
    private static String extractIngredientsFromEssential(String json) {
        if (json == null || json.isBlank()) return null;

        // name이 "화장품법에 따라"를 포함하는 항목의 value를 찾는다.
        Pattern p = Pattern.compile(
                "\\{[^\\{\\}]*?\"name\"\\s*:\\s*\"([^\"]*화장품법에 따라[^\"]*)\"[^\\{\\}]*?\"value\"\\s*:\\s*\"([\\s\\S]*?)\"[^\\{\\}]*?\\}",
                Pattern.DOTALL);
        Matcher m = p.matcher(json);
        if (m.find()) {
            return jsonUnescape(m.group(2));
        }

        // 보조: 정확히 "전성분" 라벨
        Pattern p2 = Pattern.compile(
                "\\{[^\\{\\}]*?\"name\"\\s*:\\s*\"전성분\"[^\\{\\}]*?\"value\"\\s*:\\s*\"([\\s\\S]*?)\"[^\\{\\}]*?\\}",
                Pattern.DOTALL);
        Matcher m2 = p2.matcher(json);
        if (m2.find()) {
            return jsonUnescape(m2.group(1));
        }
        return null;
    }

    private static String jsonUnescape(String s) {
        if (s == null) return null;
        return s.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", " ")
                .replace("\\r", " ")
                .replace("\\t", " ")
                .trim();
    }

    static String cleanIngredients(String raw) {
        if (raw == null) return null;
        String s = raw;

        // 0) HTML/엔티티 정리
        s = s.replaceAll("(?i)<br\\s*/?>", ", ");   // 줄바꿈 → 쉼표
        s = s.replaceAll("(?i)</?p>|</?div>|</?span>|</?br>", " ");
        s = s.replace("&nbsp;", " ");
        s = s.replaceAll("\\s+", " ").trim();

        // 1) “상세페이지 참조” 류면 저장 안 함 (null 반환)
        if (s.matches(".*(?i)(상세\\s*페이지|상세페이지|상세\\s*참조|상세참조|상세이미지).*")) {
            return null;
        }

        // 2) 앞머리 라벨 제거
        //   예) "■ 토너 : ", "• 에센스: ", "[스킨] ", "(전성분) ", "전성분 : "
        s = s.replaceFirst("^[\\p{Punct}\\p{InHangulJamo}\\p{IsPunctuation}\\s]*전\\s*성\\s*분\\s*[:：]\\s*", "");
        s = s.replaceFirst("^\\s*[\\[\\(][^\\]\\)]*[\\]\\)]\\s*", "");            // 선행 대괄호/소괄호 블록
        s = s.replaceFirst("^\\s*[■□◆▶▷•·\\-\\*]+\\s*[^:：]{1,30}[:：]\\s*", ""); // 기호 + 라벨:내용
        // 라벨이 여러 개 연달아 붙어있는 경우 대비: 2~3회 반복 제거
        for (int i = 0; i < 2; i++) {
            s = s.replaceFirst("^\\s*[\\[\\(][^\\]\\)]*[\\]\\)]\\s*", "");
            s = s.replaceFirst("^\\s*[■□◆▶▷•·\\-\\*]+\\s*[^:：]{1,30}[:：]\\s*", "");
        }

        // 3) 구분자 정규화 (· • ・ ㆍ / 、 ， 등을 쉼표로 통일)
        s = s.replaceAll("[·•・ㆍ/、，;]", ",");
        // 콜론이 성분 내부에서 쓰인 경우는 드물지만, 시작부 라벨 콜론 제거 후 남은 콜론을 쉼표로 완화
        s = s.replace(':', ',');
        // 쉼표 주변 공백 정리
        s = s.replaceAll("\\s*,\\s*", ", ");
        // 중복 쉼표 정리
        s = s.replaceAll("(,\\s*){2,}", ", ");
        // 남은 HTML/공백 정리
        s = s.replaceAll("\\s+", " ").trim();

        // 4) 여전히 라벨 문구가 맨 앞에 남았으면 최종 한 번 더 잘라내기 (예: "스킨 정제수, ...")
        s = s.replaceFirst("^[^,]{1,15}\\s*,\\s*", ""); // 첫 쉼표 전 15자 미만 라벨 컷

        return s.isBlank() ? null : s;
    }



    /* ========================= CSV 저장 ========================= */

    private static void saveCsv(String path, List<Product> items) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, StandardCharsets.UTF_8))) {
            bw.write("goodsNo,brand,productName,price,url,ingredients");
            bw.newLine();
            for (Product p : items) {
                bw.write(csv(p.goodsNo)); bw.write(",");
                bw.write(csv(p.brand));   bw.write(",");
                bw.write(csv(p.name));    bw.write(",");
                bw.write(csv(p.price));   bw.write(",");
                bw.write(csv(p.url));     bw.write(",");
                bw.write(csv(p.ingredients));
                bw.newLine();
            }
        }
    }

    private static String csv(String s) {
        if (s == null) return "";
        String v = s.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\n") || v.contains("\r")) return "\"" + v + "\"";
        return v;
    }

    private static String shorten(String s, int n) {
        if (s == null) return "";
        String t = s.replaceAll("\\s+", " ").trim();
        return t.length() > n ? t.substring(0, n) + "..." : t;
    }

    /* ========================= DTO ========================= */

    static class Product {
        final String goodsNo;
        final String brand;
        final String name;
        final String price;
        final String url;       // goodsLinkUrl
        String ingredients;     // 전성분(정제 후)

        Product(String goodsNo, String brand, String name, String price, String url) {
            this.goodsNo = goodsNo;
            this.brand = brand;
            this.name = name;
            this.price = price;
            this.url = url;
        }
    }
}
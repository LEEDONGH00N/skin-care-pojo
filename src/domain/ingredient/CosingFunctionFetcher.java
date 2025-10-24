package domain.ingredient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.category.FunctionType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class CosingFunctionFetcher {

    private static final String API_URL =
            "https://api.tech.ec.europa.eu/search-api/prod/rest/search" +
                    "?apiKey=285a77fd-1257-4271-8507-f0c6b2961203&text=*&pageSize=100&pageNumber=1";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Set<FunctionType> fetchFunctions(String ingredientName) {
        Set<FunctionType> result = new HashSet<>();

        try {
            // 🔹 검색용 쿼리는 슬래시를 공백으로만 치환 (원본은 보존)
            String searchQuery = normalizeForSearch(ingredientName);
            String jsonQuery = createJsonQuery(searchQuery);

            String boundary = "----WebKitFormBoundaryXYZ";
            String body = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"query\"; filename=\"blob\"\r\n" +
                    "Content-Type: application/json\r\n\r\n" +
                    jsonQuery + "\r\n" +
                    "--" + boundary + "--";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofSeconds(10))
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept", "application/json")
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("HTTP " + response.statusCode() + " for " + ingredientName);
                return result;
            }

            JsonNode root = MAPPER.readTree(response.body());
            JsonNode results = root.path("results");

            if (results.isArray() && results.size() > 0) {
                JsonNode best = chooseBestMatch(results, ingredientName); // ← 원본 표기로 매칭

                if (best != null) {
                    JsonNode fnList = best.path("metadata").path("functionName");
                    if (fnList.isArray()) {
                        for (JsonNode fn : fnList) {
                            FunctionType f = FunctionType.fromLabel(fn.asText());
                            System.out.println(f);
                            if (f != null) result.add(f);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching for: " + ingredientName + " → " + e.getMessage());
        }
        return result;
    }

    private static JsonNode chooseBestMatch(JsonNode results, String originalQuery) {
        String qStrict  = normalizeStrict(originalQuery);   // 슬래시 유지
        String qRelaxed = normalizeRelaxed(originalQuery);  // 슬래시→공백

        JsonNode best = null;
        int bestScore = -1;

        for (JsonNode node : results) {
            JsonNode md = node.path("metadata");
            String itemType = first(md, "itemType");
            String inci     = first(md, "inciName");
            if (inci == null || inci.isEmpty()) continue;

            String iStrict  = normalizeStrict(inci);
            String iRelaxed = normalizeRelaxed(inci);

            int score = 0;
            // 1) 글자(대소문자만 무시)까지 완전 동일 (슬래시 유지 버전)
            if (iStrict.equals(qStrict)) {
                score = 1200;
            }
            // 2) 슬래시→공백 치환한 버전끼리 완전 동일
            else if (iRelaxed.equals(qRelaxed)) {
                score = 1000;
            }
            // 3) 부분 일치(완전 일치가 없을 때만)
            else if (iRelaxed.contains(qRelaxed) || qRelaxed.contains(iRelaxed)) {
                score = 700;
            } else {
                continue; // 무관한 후보는 스킵
            }

            // 가산점: ingredient 우선, functionName 보유
            if ("ingredient".equalsIgnoreCase(itemType)) score += 100;
            JsonNode fnList = md.path("functionName");
            if (fnList.isArray() && fnList.size() > 0) score += 10;

            if (score > bestScore) {
                best = node;
                bestScore = score;
            }
        }
        return best;
    }

    private static String first(JsonNode md, String key) {
        JsonNode n = md.path(key);
        if (n.isArray() && n.size() > 0) return n.get(0).asText("");
        return n.asText("");
    }

    private static String normalizeForSearch(String q) {
        if (q == null) return "";
        return q.replace("/", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String normalizeStrict(String s) {
        if (s == null) return "";
        return s.toUpperCase()
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String normalizeRelaxed(String s) {
        if (s == null) return "";
        return s.toUpperCase()
                .replace("/", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String createJsonQuery(String searchQueryNormalized) {
        String phrase = "\"" + searchQueryNormalized + "\"";
        return """
                {
                  "bool": {
                    "must": [
                      {
                        "text": {
                          "query": %s,
                          "fields": [
                            "inciName.exact",
                            "inciUsaName",
                            "innName.exact",
                            "phEurName",
                            "chemicalName",
                            "chemicalDescription"
                          ],
                          "defaultOperator": "AND"
                        }
                      },
                      { "terms": { "itemType": ["ingredient","substance"] } }
                    ]
                  }
                }
                """.formatted(toJsonLiteral(phrase));
    }

    // 안전한 JSON 리터럴 변환
    private static String toJsonLiteral(String s) {
        try {
            return MAPPER.writeValueAsString(s);
        } catch (Exception e) {
            return "\"" + s.replace("\"", "\\\"") + "\"";
        }
    }
}
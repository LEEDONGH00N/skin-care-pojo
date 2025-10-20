package domain.product;


import domain.profile.Step;
import global.Csv;
import global.DataFileConstants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class ProductCatalog {

    private final List<Product> all;

    public ProductCatalog(List<Product> all) {
        this.all = all;
    }

    public Map<Step, List<Product>> byStep() {
        return all.stream()
                .collect(Collectors.groupingBy(p -> p.step));
    }

    public static List<Product> loadFile(String path, Step step) {
        List<Product> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String first = br.readLine();
            if (first == null) return out;
            if (!looksLikeData(first)) {
            } else {
                parseLine(first, step).ifPresent(out::add);
            }
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line, step).ifPresent(out::add);
            }
        } catch (Exception e) {
            throw new RuntimeException("파일 로드 실패 (" + path + "): " + e.getMessage(), e);
        }
        return out;
    }

    public static ProductCatalog loadAllFromConstants() {
        List<Product> all = new ArrayList<>();
        all.addAll(loadFile(DataFileConstants.MUSINSA_SKIN_TONER_CSV_PATH, Step.TONER));
        all.addAll(loadFile(DataFileConstants.MUSINSA_AMPOULE_SERUME_CSV_PATH, Step.ESSENCE));
        all.addAll(loadFile(DataFileConstants.MUSINSA_CREAM_CSV_PATH, Step.CREAM));
        return new ProductCatalog(all);
    }

    private static Optional<Product> parseLine(String line, Step step) {
        List<String> cols = splitSmart(line);
        if (cols.size() < 6) return Optional.empty();

        String goodsNo = cols.get(0).trim();
        String brand   = cols.get(1).trim();
        String name    = cols.get(2).trim();
        int price      =  Integer.parseInt(cols.get(3).replaceAll("[^0-9]", ""));
        String url     = cols.get(4).trim();
        String ingr    = cols.get(5).trim();
        if (goodsNo.isEmpty() || name.isEmpty()) return Optional.empty();

        return Optional.of(new Product(goodsNo, brand, name, price, url, ingr, step));
    }

    private static List<String> splitSmart(String line) {
        int tabs = count(line, '\t');
        int commas = count(line, ',');
        if (tabs > commas) {
            return Arrays.asList(line.split("\t", -1));
        } else {
            return Csv.minParse(line);
        }
    }

    private static int count(String s, char ch) {
        int c = 0;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == ch) c++;
        return c;
    }

    private static boolean looksLikeData(String line) {
        List<String> cols = splitSmart(line);
        if (cols.isEmpty()) return false;
        String first = cols.get(0).trim();
        return first.matches("\\d+");
    }
}
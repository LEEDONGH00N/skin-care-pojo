package global;
import java.util.ArrayList;
import java.util.List;

public class Csv {

    public static List<String> minParse(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQ = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQ && i + 1 < line.length() && line.charAt(i + 1) == '"') { sb.append('"'); i++; }
                else inQ = !inQ;
            } else if (c == ',' && !inQ) {
                out.add(sb.toString()); sb.setLength(0);
            } else sb.append(c);
        }
        out.add(sb.toString());
        return out;
    }
}
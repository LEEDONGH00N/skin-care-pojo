package domain.ingredient;

import domain.routine.RoutineKind;

import java.util.List;
import java.util.Map;

public class KeywordBank {
    public static final Map<RoutineKind, List<String>> KEYWORDS = Map.of(
            RoutineKind.SOOTHING, List.of("병풀", "마데카소사이드", "어성초", "시카", "판테놀"),
            RoutineKind.HYDRATION, List.of("히알루", "글리세", "베타인", "펜틸렌", "소듐피씨에이", "세라마이드", "우레아"),
            RoutineKind.BRIGHTENING, List.of("나이아신", "아스코", "비타민 C", "알부틴", "트라넥사믹", "레티놀", "레티날"),
            RoutineKind.EXFOLIATION, List.of("AHA", "BHA", "PHA", "살리실"),
            RoutineKind.SEBUM, List.of("살리실", "징크", "나이아신", "탄닌", "카올린", "벤토나이트"),
            RoutineKind.ANTIAGING, List.of("레티놀", "레티날", "펩타이드", "아데노신", "콜라겐", "유비퀴논", "바쿠치올")
    );
}
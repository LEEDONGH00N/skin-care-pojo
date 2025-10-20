package domain.routine;

import domain.profile.SkinType;
import domain.profile.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RoutineLibrary {

    private final Routine soothingAM = new Routine("진정 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM, Step.SUNSCREEN),
            Set.of(RoutineKind.SOOTHING)
    );

    private final Routine soothingPM = new Routine("진정 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM),
            Set.of(RoutineKind.SOOTHING)
    );

    private final Routine hydraAM = new Routine("보습 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM, Step.SUNSCREEN),
            Set.of(RoutineKind.HYDRATION)
    );

    private final Routine hydraPM = new Routine("보습 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM),
            Set.of(RoutineKind.HYDRATION)
    );

    // 미백은 오전 제외
    private final Routine brightPM = new Routine("미백 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM),
            Set.of(RoutineKind.BRIGHTENING)
    );

    private final Routine exfolPM = new Routine("각질 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM),
            Set.of(RoutineKind.EXFOLIATION)
    );

    private final Routine sebumAM = new Routine("모공/피지 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM, Step.SUNSCREEN),
            Set.of(RoutineKind.SEBUM)
    );

    private final Routine sebumPM = new Routine("모공/피지 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM),
            Set.of(RoutineKind.SEBUM)
    );

    private final Routine antiAM = new Routine("안티에이징 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM, Step.SUNSCREEN),
            Set.of(RoutineKind.ANTIAGING)
    );

    private final Routine antiPM = new Routine("안티에이징 루틴",
            List.of(Step.TONER, Step.ESSENCE, Step.CREAM),
            Set.of(RoutineKind.ANTIAGING)
    );

    public List<Routine> amCandidates(SkinType skinType) {
        List<Routine> list = new ArrayList<>();
        switch (skinType) {
            case DRY -> list.addAll(List.of(hydraAM, antiAM, soothingAM)); // 건성 -> 보습 + 안티에이징 중심
            case OILY -> list.addAll(List.of(sebumAM, soothingAM)); // 지성 -> 피지조절 + 진정
            case COMBINATION -> list.addAll(List.of(soothingAM, hydraAM, sebumAM)); // 복합성 -> 밸런스 중심
            case SENSITIVE -> list.addAll(List.of(soothingAM, hydraAM)); // 민감성 -> 자극 최소
        }
        return list;
    }

    public List<Routine> pmCandidates(SkinType skinType) {
        return skinType == SkinType.SENSITIVE
                ? List.of(soothingPM, hydraPM, sebumPM, antiPM)
                : List.of(soothingPM, hydraPM, brightPM, exfolPM, sebumPM, antiPM);
    }
}
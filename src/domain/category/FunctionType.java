package domain.category;

import java.util.Arrays;

public enum FunctionType {
    ABRASIVE("ABRASIVE", "피부 표면의 각질이나 오염을 제거하여 매끄럽게 함"),
    ABSORBENT("ABSORBENT", "피부 표면의 피지, 땀, 불순물 흡수"),
    ADHESIVE("ADHESIVE", "제품이 피부에 잘 밀착되도록 도움"),
    ANTI_SEBORRHEIC("ANTI_SEBORRHEIC", "지루성 피부 완화 및 피지 분비 억제"),
    ANTI_SEBUM("ANTI_SEBUM", "피지 분비 조절"),
    ANTICAKING("ANTICAKING", "분말 응집 방지"),
    ANTICORROSIVE("ANTICORROSIVE", "포장재 부식 억제"),
    ANTIFOAMING("ANTIFOAMING", "제품 내 거품 발생 억제"),
    ANTIMICROBIAL("ANTIMICROBIAL", "미생물 증식 억제"),
    ANTIOXIDANT("ANTIOXIDANT", "항산화 및 변질 방지"),
    ANTIPERSPIRANT("ANTIPERSPIRANT", "땀 분비 감소"),
    ANTIPLAQUE("ANTIPLAQUE", "치태 형성 방지"),
    ANTISTATIC("ANTISTATIC", "정전기 발생 억제"),
    ASTRINGENT("ASTRINGENT", "피부 수축 및 탄력 개선"),
    BINDING("BINDING", "고형 제형에서 결합력 제공"),
    BLEACHING("BLEACHING", "피부·모발 색소를 엷게 함"),
    BUFFERING("BUFFERING", "pH 안정화 및 변동 완화"),
    BULKING("BULKING", "제품의 부피 증가 및 희석용 비활성 고형물"),
    CHELATING("CHELATING", "금속 이온과 결합해 안정성 향상"),
    CLEANSING("CLEANSING", "피부 표면의 오염물 제거"),
    COLORANT("COLORANT", "제품 또는 피부에 색 부여"),
    DENATURANT("DENATURANT", "제품의 섭취 방지(에탄올 변성 등)"),
    DEODORANT("DEODORANT", "체취 억제 및 악취 방지"),
    DEPILATORY("DEPILATORY", "모발 제거(제모용)"),
    DETANGLING("DETANGLING", "엉킨 모발 빗질 용이"),
    DISPERSING_NON_SURFACTANT("DISPERSING_NON_SURFACTANT", "비계면활성제로 입자 분산 보조"),
    EMULSION_STABILISING("EMULSION_STABILISING", "유화 안정성 향상"),
    EPILATING("EPILATING", "모근 제거(왁싱 등)"),
    EXFOLIATING("EXFOLIATING", "각질 제거 및 피부 재생 촉진"),
    EYELASH_CONDITIONING("EYELASH_CONDITIONING", "속눈썹의 윤기·볼륨 개선"),
    FILM_FORMING("FILM_FORMING", "보호막 형성으로 수분 증발 억제"),
    FLAVOURING("FLAVOURING", "제품에 맛·향 부여"),
    FOAMING("FOAMING", "거품 형성 및 세정 보조"),
    FRAGRANCE("FRAGRANCE", "향 부여 또는 불쾌취 마스킹"),
    GEL_FORMING("GEL_FORMING", "젤상 질감 형성 및 쿨링감 부여"),
    HAIR_CONDITIONING("HAIR_CONDITIONING", "모발의 부드러움·윤기 개선"),
    HAIR_DYEING("HAIR_DYEING", "모발에 색 부여"),
    HAIR_FIXING("HAIR_FIXING", "헤어 스타일 고정"),
    HAIR_WAVING_OR_STRAIGHTENING("HAIR_WAVING_OR_STRAIGHTENING", "모발의 화학 구조 변환"),
    HUMECTANT("HUMECTANT", "피부에 수분을 끌어당겨 보습 유지"),
    KERATOLYTIC("KERATOLYTIC", "각질층 제거 및 매끄러운 피부 유도"),
    LIGHT_STABILIZER("LIGHT STABILIZER", "빛에 의한 제품 변질 방지"),
    LYTIC("LYTIC", "지질·단백질·다당류 분해 보조"),
    MOISTURISING("MOISTURISING", "피부 수분 함량 증가 및 장벽 강화"),
    NAIL_CONDITIONING("NAIL_CONDITIONING", "손발톱의 보습·광택·내구성 개선"),
    NAIL_SCULPTING("NAIL_SCULPTING", "손톱 구조 형성"),
    NOT_REPORTED("NOT_REPORTED", "보고된 기능 없음"),
    OCCLUSIVE("OCCLUSIVE", "수분 증발 차단막 형성"),
    OPACIFYING("OPACIFYING", "제품의 불투명화"),
    ORAL_CARE("ORAL_CARE", "구강 청결 및 보호"),
    OXIDISING("OXIDISING", "산화 작용을 통한 항균·톤 개선"),
    PEARLESCENT("PEARLESCENT", "진주광 효과 부여"),
    PERFUMING("PERFUMING", "향료로 사용"),
    PH_ADJUSTERS("PH_ADJUSTERS", "pH 조절"),
    PLASTICISER("PLASTICISER", "고분자 재료의 유연성 향상"),
    PRESERVATIVE("PRESERVATIVE", "미생물 성장 억제 및 보존제 역할"),
    PROPELLANT("PROPELLANT", "에어로졸 압력 생성 및 토출 보조"),
    REDUCING("REDUCING", "환원 작용으로 산화 방지"),
    REFATTING("REFATTING", "피부·모발의 지질 보충"),
    REFRESHING("REFRESHING", "쿨링감·청량감 부여"),
    SKIN_CONDITIONING("SKIN_CONDITIONING", "피부 상태 유지·개선"),
    SKIN_CONDITIONING_EMOLLIENT("SKIN_CONDITIONING_EMOLLIENT", "윤활막 형성으로 부드럽게 유지"),
    SKIN_CONDITIONING_HUMECTANT("SKIN_CONDITIONING_HUMECTANT", "각질층 수분 함량 증가"),
    SKIN_CONDITIONING_MISCELLANEOUS("SKIN_CONDITIONING_MISCELLANEOUS", "건조·손상 피부 개선"),
    SKIN_CONDITIONING_OCCLUSIVE("SKIN_CONDITIONING_OCCLUSIVE", "수분 증발 지연"),
    SKIN_PROTECTING("SKIN_PROTECTING", "외부 유해 요인으로부터 피부 보호"),
    SLIP_MODIFIER("SLIP_MODIFIER", "성분의 흐름성 개선"),
    SMOOTHING("SMOOTHING", "피부 표면의 거칠기 감소"),
    SOLVENT("SOLVENT", "성분 용해"),
    SOOTHING("SOOTHING", "피부 자극 완화 및 진정"),
    SURFACE_MODIFIER("SURFACE_MODIFIER", "성분의 표면특성(친수/소수성) 변경"),
    SURFACTANT("SURFACTANT", "서로 섞이지 않는 성분의 분산/용해 보조"),
    SURFACTANT_CLEANSING("SURFACTANT_CLEANSING", "세정 및 거품 형성 보조"),
    SURFACTANT_DISPERSING("SURFACTANT_DISPERSING", "불용성 고체 분산 보조"),
    SURFACTANT_EMULSIFYING("SURFACTANT_EMULSIFYING", "유화 보조 및 안정화"),
    SURFACTANT_FOAM_BOOSTING("SURFACTANT_FOAM BOOSTING", "거품 형성력 향상"),
    SURFACTANT_HYDROTROPE("SURFACTANT_HYDROTROPE", "수용화 능력 향상"),
    SURFACTANT_SOLUBILIZING("SURFACTANT_SOLUBILIZING", "불용성 성분의 용해 보조"),
    TANNING("TANNING", "피부를 어둡게 착색"),
    TONIC("TONIC", "피부에 활력과 생기 부여"),
    UV_ABSORBER("UV_ABSORBER", "제품을 자외선으로부터 보호"),
    UV_FILTER("UV_FILTER", "피부·모발을 자외선으로부터 보호"),
    VISCOSITY_CONTROLLING("VISCOSITY_CONTROLLING", "점도 조절 및 도포성 개선");

    private final String name;
    private final String description;

    FunctionType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static FunctionType fromLabel(String name) {
        if (name == null) return null;
        String norm = name.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(f -> f.name.toLowerCase().equals(norm))
                .findFirst()
                .orElse(null);
    }
}
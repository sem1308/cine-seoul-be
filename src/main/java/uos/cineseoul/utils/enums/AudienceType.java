package uos.cineseoul.utils.enums;

public enum AudienceType {
    G("일반", 0),
    Y("청소년", 0),
    E("경로", 0),
    D("우대(장애인)", 0);

    private String displayName;
    private Integer discountPrice;

    AudienceType(String displayName, Integer discountPrice) {
        this.displayName = displayName;
        this.discountPrice = discountPrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }
}
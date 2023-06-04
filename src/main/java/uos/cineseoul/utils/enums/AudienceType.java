package uos.cineseoul.utils.enums;

public enum AudienceType {
    GENERAL("일반"),
    YOUTH("청소년"),
    ELDERLY("경로"),
    DISABLED("우대(장애인)");

    private String displayName;

    AudienceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
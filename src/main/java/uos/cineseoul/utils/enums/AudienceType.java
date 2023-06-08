package uos.cineseoul.utils.enums;

public enum AudienceType {
    G("일반"),
    Y("청소년"),
    E("경로"),
    D("우대(장애인)");

    private String displayName;

    AudienceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
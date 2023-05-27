package uos.cineseoul.entity.domain;

public enum CharLength {
    RESIDENT(13),
    RELEASE(8),
    BOOL(1),
    CODE2(2),
    SEAT_ROW(1),
    CODE(4);
    private final int length;

    CharLength(int length) {
        this.length = length;
    }

    public String getLength() {
        return String.format("CHAR(%d)", length);
    }
}

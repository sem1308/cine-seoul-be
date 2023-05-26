package uos.cineseoul.entity.domain;

public enum VarcharLength {
    APPROVAL(30),
    PHONE(11),
    PASSWORD(500),
    NAME20(20),
    NAME100(100),
    MOVIE_INFO(4000),
    ID(100),
    SEAT_COL(2);

    private final int length;

    VarcharLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }
}

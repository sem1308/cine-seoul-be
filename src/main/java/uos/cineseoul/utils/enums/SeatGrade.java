package uos.cineseoul.utils.enums;

public enum SeatGrade {
    A(13000, "TOP_GRADE"),
    B(11000, "MIDDLE_GRADE"),
    C(9000, "LOW_GRADE");
    Integer price;
    String code;

    SeatGrade(int price, String code) {
        this.price = price;
        this.code = code;
    }

    public Integer getPrice(){
        return price;
    }
}
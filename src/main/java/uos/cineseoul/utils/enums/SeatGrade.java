package uos.cineseoul.utils.enums;

public enum SeatGrade {
    A(10000, "TOP_GRADE"),
    B(9000, "MIDDLE_GRADE"),
    C(7500, "LOW_GRADE");
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
package uos.cineseoul.utils.enums;

public enum GradeType {
    A(10000, "TOP_GRADE"),
    B(9000, "MIDDLE_GRADE"),
    C(7500, "LOW_GRADE");
    Integer price;
    String code;

    GradeType(int price, String code) {
        this.price = price;
        this.code = code;
    }

    public Integer getPrice(){
        return price;
    }
}
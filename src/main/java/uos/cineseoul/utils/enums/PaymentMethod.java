package uos.cineseoul.utils.enums;

public enum PaymentMethod {
    C00("카드"),
    A000("계좌");
    String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}
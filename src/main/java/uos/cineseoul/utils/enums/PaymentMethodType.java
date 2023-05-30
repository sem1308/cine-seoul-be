package uos.cineseoul.utils.enums;

public enum PaymentMethodType {
    C00("카드"),
    A000("계좌");
    String code;

    PaymentMethodType(String code) {
        this.code = code;
    }
}
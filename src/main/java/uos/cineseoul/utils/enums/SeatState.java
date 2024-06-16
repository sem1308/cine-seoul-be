package uos.cineseoul.utils.enums;

public enum SeatState {
    AVAILABLE("사용가능"),
    SELECTED("선택됨"),
    RESERVED("예약됨"),
    BOOKED("결제됨");
    final String state;

    SeatState(String state) {
        this.state = state;
    }
}
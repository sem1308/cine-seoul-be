package uos.cineseoul.utils.enums;

public enum TicketState {
    Y("발행 완료"),
    N("발행 전"),
    C("예매 취소");
    String state;

    TicketState(String state) {
        this.state = state;
    }
}
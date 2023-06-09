package uos.cineseoul.utils.enums;

public enum TicketState {
    I("발행 완료"),
    P("결제 완료"),
    N("결제 전"),
    C("예매 취소");
    String state;

    TicketState(String state) {
        this.state = state;
    }
}
package uos.cineseoul.utils.enums;

public enum PayState {
    Y("결제 완료"),
    C("결제 취소");
    String state;

    PayState(String state) {
        this.state = state;
    }
}
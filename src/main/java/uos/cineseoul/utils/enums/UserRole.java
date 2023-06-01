package uos.cineseoul.utils.enums;

public enum UserRole {
    M("회원"),
    N("비회원"),
    A("관리자");
    String description;

    UserRole(String description) {
        this.description = description;
    }
}
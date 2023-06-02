package uos.cineseoul.utils.enums.request;

public enum RunningType {
    showing("상영중"),
    upcomming("상영예정"),
    all("모든 영화");
    String description;

    RunningType(String description) {
        this.description = description;
    }
}

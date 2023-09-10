package study.datajpa.repository;

public interface NestedClosedProjections {

    String getUsername(); // username 까지는 최적화가 된다. (username 만 가져오도록 쿼리가 나간다)
    TeamInfo getTeam(); // 중첩 구조는 최적화가 되지 않는다. (team 은 team entity의 전체를 가져온다.)

    interface TeamInfo {
        String getName();
    }
}

package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    // @Value("#{target.username + ' ' + target.age}") // 이경우는 entity 가져와서 새로 만든다. 이게 없으면 단순히 username만 가져오는 쿼리가 수행된다.
    String getUsername();
}

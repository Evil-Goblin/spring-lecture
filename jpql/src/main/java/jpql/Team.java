package jpql;

import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id @GeneratedValue
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    @BatchSize(size = 100) // LazyJoin 시 N + 1 이 아닌 BatchSize 만큼 한번에 가져오도록 한다. (team 조회 이후 members 를 루프시마다 각각 쿼리가 수행되는 것을 방지한다. )
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }
}

### 영속 컨텍스트의 특징
- 캐싱
  - persist 를 통해 영속컨텍스트에 넣을 시 해당 값이 캐싱되어 find시에 db를 거치지 않고 캐시에서 가져온다.
  - select를 통해 조회한 데이터에 대해서 제차 select 하지 않고 캐시된 값을 사용한다.
  - 하지만 그렇게 큰영향을 가진 기능은 아니다.

- 변경 감지 (Dirty Checking)
  - 영속 컨텍스트의 캐시에 스냅샷도 같이 보관
  - 커밋 시점에 entity와 스냅샷을 비교하여 값이 변경된 경우 update 쿼리 수행

- flush
  - 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영
  - entityManager.flush() - 직접 호출
  - 트랜잭션 커밋 - 플러시 자동 호출
  - jpql 쿼리 실행 - 플러시 자동 호출
  - 플러시가 호출된다고 해서 영속성 컨텍스트 내의 캐시가 사라지지 않는다.

- 준영속
  - 영속 상태의 entity가 영속성 컨텍스트에서 분리(detached)
  - 영속성 컨텍스트의 기능을 사용하지 못한다.
  - entityManager.detach(entity)
  - entityManager.clear() - 영속성컨텍스트 초기화
  - entityManager.close() - 영속성컨텍스트 종료

### Entity Mapping
- 객체와 테이블 매핑
  - @Entity, @Table
- 필드와 칼럼 매핑
  - @Column
- 기본 키 매핑
  - @Id
- 연관관계 매핑
  - @ManyToOne, @JoinColumn

#### Mapping Annotation
- @Column
  - 칼럼 매핑
- @Temporal
  - 날짜 타입 매핑
- @Enumerated
  - enum 타입 매핑
- @Lob
  - BLOB, CLOB 매핑
- @Transient
  - 매핑 무시

#### @Entity
- 기본 생성자 필수
- final 클래스, enum, interface, inner클래스 사용 불가
- 저장할 필드에 final 사용 불가
- name 속성값
  - @Entity(name = "name")
  - JPA에서 사용할 엔티티 이름
  - 기본값: 클래스 이름 그대로 사용

#### @Table
- Entity와 매핑할 테이블 지정
- 속성
  - name: 매핑할 테이블 이름
  - catalog: 데이터베이스 catalog 매핑
  - schema: 데이터베이스 schema 매핑
  - uniqueConstraints: DDL 생성 시에 유니크 제약 조건 생성

#### @Column
- 제약조건 추가
  - nullable: null여부
  - length: 길이
  - unique: 유니크 제약조건 추가
- 속성
  - name: 필드와 매핑할 테이블 칼럼 이름
  - insertable, updatable: 등록, 변경 기능 여부 (jpa를 통해 변경여부) default True
  - nullable(DDL): not null 제약조건이 붙는다.
  - unique(DDL): @Table의 uniqueConstraints와 같지만 한 칼럼에 간단히 제약을 걸 때 사용
  - columnDefinition(DDL): 컬럼 정보를 직접 넣을 수 있다. (ex. `@Column(columnDefinition = "varchar(100) default 'EMPTY'")`)
  - length(DDL): 문자 길이 제약조건, String에만 사용
  - precision, scale(DDL): BigDecimal타입에 사용, 소수점 자릿수

#### @Enumerated
- value 속성
  - EnumType.ORDINAL: enum 순서를 데이터베이스에 저장(정수값)
  - EnumType.STRING: enum 이름을 데이터베이스에 저장(문자열)
- ORDINAL 사용시 ENUM 변경에 따라 저장된 값과 Enum의 값이 달라지기 때문에 STRING을 써야한다.

#### @Temporal
- LocalDate, LocalDateTime 사용시 생략 가능(최신 하이버네이트)
- value 속성
  - TemporalType.DATE: 날짜, 데이터베이스 date 타입과 매핑 (2013-10-11)
  - TemporalType.TIME: 시간, 데이터베이스 time 타입과 매핑 (11:11:11)
  - TemporalType.TIMESTAMP: 날짜와 시간, 데이터베이스 timestamp 타입과 매핑 (2013-10-11 11:11:11)

#### @Lob
- 데이터베이스 BLOB, CLOB 타입과 매핑
- @Lob 에는 속성이 없다.
- 매핑하는 필드 타입이 문자면 CLOB매핑, 나머지는 BLOB매핑
  - CLOB: String, char[], java.sql.CLOB
  - BLOB: byte[], java.sql.BLOB

### 데이터베이스 스키마 자동 생성
- hibernate.hbm2ddl.auto
  - create: 기존 테이블 삭제 후 다시 생성 (Drop + Create)
  - create-drop: create와 같으나 종료시점에 테이블 DROP
  - update: 변경분만 반영 (운영DB에는 사용하면 안됨) (추가된 부분은 추가되지만 삭제된 부분은 변경되지 않는다.)
  - validate: 엔티티와 테이블이 정상 매핑되었는지만 확인
  - none: 사용하지 않음

### 기본 키 매핑
#### 기본 키 매핑 어노테이션
- @Id
- @GeneratedValue

#### 기본 키 매핑 방법
- 직접 할당: @Id만 사용
- 자동 생성(@GeneratedValue)
  - IDENTITY: 데이터베이스에 위임, MYSQL
  - SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용, ORACLE
    - @SequenceGenerator 필요
    - ```java
      @Entity
      @SequenceGenerator(
          name = "MEMBER_SEQ_GENERATOR",
          sequenceName = "MEMBER_SEQ",
          initialValue = 1, allocationSize = 1)
      public class SeqMember {
          @Id
          @GeneratedValue(strategy = GenerationType.SEQUENCE,
              generator = "MEMBER_SEQ_GENERATOR")
          private Long id;
      }
      ```
  - TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용
    - @TableGenerator 필요
    - ```sql
      create table MY_SEQUENCES (
          sequence_name varchar(255) not null,
          next_val bigint,
          primary key ( sequence_name )
      )
      ```
    - ```java
      @Entity
      @TableGenerator(
          name = "MEMBER_SEQ_GENERATOR",
          table = "MY_SEQUENCES",
          pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
      public class TableMember {
          @Id
          @GeneratedValue(strategy = GenerationType.TABLE,
              generator = "MEMBER_SEQ_GENERATOR")
          private Long id;
      }
      ```
  - AUTO: db에 따라 자동 지정, 기본값

#### 자동생성 특징
- IDENTITY
  - id값을 null로 쿼리를 날리게 된다.
  - 하지만 영속성컨텍스트는 커밋시점에 쿼리가 날라가도록 되어있기 때문에 쿼리를 하기 전에 해당 id를 알지 못한다.
  - 때문에 자동생성의 경우 entityManager.persist()를 수행하는 순간 insert를 수행하고 id값을 가져오게 된다.
- SEQUENCE
  - SEQUENCE로 부터 id를 발급받기 때문에 persist() 수행시 id만을 발급받아온다.
  - 이후 commit시점에 발급받은 id를 이용해 쿼리를 수행한다.
  - SEQUENCE 의 allocationSize를 1이 아닌 큰 수로 잡을 시 미리 시퀀스값을 메모리로 가져와놓고 사용한다.
  - persist 수행시 두번의 sequence를 호출한다.
    - ex. 50으로 설정시
    - 최소 수행시 1, 51가져온다. (두번 수행)
    - 이후 persist 수행시 51까지는 메모리에서 계속증가시키며 사용한다. (추가로 db에 요청하지 않는다.)
  - allocationSize를 통해 성능 최적화를 노릴 수 있다.
- TABLE
  - SEQUENCE와 비슷하게 allocationSize를 사용할 수 있다.

### Entity간의 관계
- Foreign key를 통해 다른 테이블의 Primary Key에 매칭을 할 때
  - @ManyToOne @JoinColume(name = "<Primary key>") 를 통해 다대일 상관관계 구축
- Primary Key에 다른 테이블의 Foreign key 매핑
  - @OneToMany(mappedBy = "<Column>") 을 통해 일대다 매핑 (List 자료구조 사용)

#### 연관관계의 주인
- mappedBy 를 통해 매핑하는 객체는 연관관계의 주인이 아니다.
- 대체로는 다대일의 다에 해당하는 쪽이(Foreign key) 연관관계의 주인이다.
- 주인의 값을 변경하면 db에 영향이 가지만(쓰기) 주인이 아닌 쪽의 변경은 db에 영향이 가지 않는다.(읽기 전용)

#### 양방향 연관관계 주의점
- 주인의 값을 변경해야 적용이 되기 때문에 주인의 값을 설정하는 것이 중요하다.
- 하지만 반대쪽의 값을 설정하지 않으면 순수 자바객체로서의 접근시 문제가 생길 수 있다.
- 때문에 양쪽의 값을 모두 설정하는 것이 중요하다.
- 연관관계 편의 메소드를 생성할 필요가 있다.
  - ```java
    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
    ```
  - 양쪽의 값을 설정하는 것이 코드상 두줄을 차지하기 때문에 실수할 확률이 있다.
  - 이에 위와 같은 편의 메소드를 통해 한번에 세팅이 가능하다.

- toString(), lombok, JSON 생성 라이브러리 등 사용 주의
  - toString을 예로 들면 a의 toString에서 참조중인 b의 toString을 호출하고 b가 가진 a들의 toString을 호출하는 순환 참조가 발생한다.
  - JSON 의 경우 Controller 등의 매개변수로 받거나 반환하는 경우 문제가 될 수 있기 때문에 DTO를 이용하고 Entity는 절대 사용해선 안된다.

#### 양방향 매핑 정리
- 단방향 매핑만으로 이미 연관관계 매핑은 완료
- 양방향 매핑은 반대 방향으로 조회 기능이 추가된 것 뿐
- JPQL에서 역방향 탐색할 일이 많다.
- *단반향 매핑을 잘 하고 양방향 매핑은 필요할때 추가하면 된다.* (테이블에 영향을 주지 않는다.)

# *연관관계의 주인은 외래 키의 위치를 기준으로 정해야 한다.*

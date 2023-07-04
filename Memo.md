### Transactional
- 기본 동작은 해당 어노테이션 범위 내에서 트랜잭션을 유지해주고 에러가 발생하지 않을 시 커밋
- 테스트에서 적용시 커밋이 아닌 롤백
- 만약 테스트에서도 실제 커밋을 하고 싶다면 `@Commit`어노테이션이나 `@Rollback(false)`어노테이션을 추가로 사용하면 된다.

### Embedded Mode Db
- 아무 데이터소스 설정이 없다면 임베디드 디비를 사용한다.
- 이때 `dbname`은 유니크한 문자열으로 생성되는데 `spring.datasource.generate-unique-name=false`옵션을 토애 `testdb`라는 이름으로 생성된다.
- 최초 테이블이 없기 때문에 테이블 생성을 위해 `resource`에 `schema.sql`파일을 통해 `DDL`을 수행하도록 한다.

### MyBatis XML 파일 경로 수정
- 기본적으로 `MyBatis`의 XML파일은 `Mapper`인터페이스와 같은 경로에 있어야한다.
- 하지만 이렇게 되면 여러 파일에 대해서 XML파일이 여러곳에 배치되어 관리가 어렵게 되기 때문에 파일 경로를 고정시킬 수 있다.
- `application.properties`에 `mybatis.mapper-locations=classpath:mapper/**/*.xml` 이 옵션을 적용하면된다.
  - 해당 옵션 적용시 `resource/mapper`를 포함한 하위 디렉토리의 XML파일을 XML매핑파일로 인식한다.
  - 파일이름 또한 자유롭게 설정 가능하다.

### Gradle dependency 버전 변경
- `ext["hibernate.version"] = "x.x.x"`

### `Querydsl` 종속성 설치시 `java: java.lang.NoClassDefFoundError: javax/persistence/Entity` 에러
- 우선 모든 종속성을 최신으로 업데이트해주었다.
- 그럼에도 해결되지 않아 해당 문제를 검색해보니 특정 종속성 뒤에 `:jakarta`를 붙여야 한다고 하여
```Groovy
implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
annotationProcessor "com.querydsl:querydsl-apt:5.0.0:jakarta"
annotationProcessor 'jakarta.annotation:jakarta.annotation-api:2.1.1'
annotationProcessor 'jakarta.persistence:jakarta.persistence-api:3.1.0'
```
- 위와 같이 고쳐주었더니 정상동작하였다.
- 정확하게 어느지점이 문제였던 건지 다시 전체 업데이트 전으로 돌려 테스트해본 결과 `annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"` 이것만 변경해주면 되는 것을 확인하였다.

### `Querydsl` 종속성 설치시 `jakarta`와 `javax`사이에서 계속 문제가 발생
- `implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'`요것도 해주어야한다.
- 버전을 명시하지 않는 `implementation 'com.querydsl:querydsl-jpa:jakarta'` 이것이 되지 않기 때문에 이 친구는 상관 없지 않나 생각했는데 계속 `jakarta`를 불러야하는 곳에서 `javax`를 부르길래 확인해보니 이 친구가 `javax`라서 그렇더라....
- 해서 버전 특정해주고 `jakarta`로 변경하여 해결하였다.

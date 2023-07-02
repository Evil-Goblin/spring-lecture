###Transactional
- 기본 동작은 해당 어노테이션 범위 내에서 트랜잭션을 유지해주고 에러가 발생하지 않을 시 커밋
- 테스트에서 적용시 커밋이 아닌 롤백
- 만약 테스트에서도 실제 커밋을 하고 싶다면 `@Commit`어노테이션이나 `@Rollback(false)`어노테이션을 추가로 사용하면 된다.

### Embedded Mode Db
- 아무 데이터소스 설정이 없다면 임베디드 디비를 사용한다.
- 이때 `dbname`은 유니크한 문자열으로 생성되는데 `spring.datasource.generate-unique-name=false`옵션을 토애 `testdb`라는 이름으로 생성된다.
- 최초 테이블이 없기 때문에 테이블 생성을 위해 `resource`에 `schema.sql`파일을 통해 `DDL`을 수행하도록 한다.

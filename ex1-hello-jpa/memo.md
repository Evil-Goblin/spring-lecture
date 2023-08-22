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

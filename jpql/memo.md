## jpa 서브쿼리의 한계
- JPA 는 where, having 절에서만 서브쿼리 가능
- 하이버네이트에서는 select 절에서 가능하다.
- from 절의 서브쿼리는 현재 jpql에서 불가능하다.
  - join 으로 풀 수 있으면 풀어서 해결...

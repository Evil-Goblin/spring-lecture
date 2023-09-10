## SpringDataJpa 의 save
```java
@Transactional
@Override
public <S extends T> S save(S entity) {

    Assert.notNull(entity, "Entity must not be null");

    if (entityInformation.isNew(entity)) {
        em.persist(entity);
        return entity;
    } else {
        return em.merge(entity);
    }
}
```
- 새로운 Entity면 persist, 아니면 merge 가 수행된다.....
- isNew 의 동작 방식은 객체인 경우 null 여부, 기본 타입인 경우 0 으로 동작한다.
- 이에 만약 키값을 String 등으로 구성하고 Generated 를 사용하지 않아 직접 넣어주려고 하는 경우 persist 가 수행되지 않고 merge가 수행된다.
- 하지만 실제론 값이 없기 때문에 merge 를 위해 select 를 하고 값이 없어 insert 를 수행하게 되어 매우 비효율적이게 된다.
- Persistable 인터페이스 구현을 통해 해결 가능
  - Persistable 인터페이스의 isNew 메소드를 통해 해당 기능을 구현해야한다.
  - CreatedDate 와 함께 사용하여 해결할 수 있다.

package com.cevaris.authme.models.storage.dynamodb;

public interface DynamoDbRepository<A> {

  A create(A x);

  A get(Object key);

  Object delete(Object x);

}

package com.cevaris.authme.models.storage.dynamodb;

import com.amazonaws.services.dynamodbv2.document.Table;

public interface DynamoDbRepository<A> {

  Table getTable();

  A create(A x);

  A get(Object key);

  Object delete(Object x);

}

/*
 * Copyright 2016 Sai Pullabhotla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jmethods.catatumbo.impl;

import com.google.cloud.datastore.DatastoreException;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.DefaultDatastoreKey;
import com.jmethods.catatumbo.EntityAlreadyExistsException;
import com.jmethods.catatumbo.EntityManagerException;
import com.jmethods.catatumbo.EntityNotFoundException;
import com.jmethods.catatumbo.impl.IdentifierMetadata.DataType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods.
 *
 * @author Sai Pullabhotla
 */
class DatastoreUtils {

  /**
   * Error code from the Datastore when an update operation fails because entity with the specified
   * key does not exist.
   */
  private static final int ERROR_CODE_ENTITY_NOT_FOUND = 5;

  /**
   * Error code from the Datastore when an insert or put operation fails because another entity with
   * the same key already exists.
   */
  private static final int ERROR_CODE_ENTITY_ALREADY_EXISTS = 6;

  /** Hide the implicit constructor */
  private DatastoreUtils() {
    // Do nothing.
  }

  /**
   * Converts the given list of native entities to a list of model objects of given type, <code>
   * entityClass</code>.
   *
   * @param entityClass the entity class
   * @param nativeEntities native entities to convert
   * @return the list of model objects
   */
  static <E> List<E> toEntities(Class<E> entityClass, List<Entity> nativeEntities) {
    if (nativeEntities == null || nativeEntities.isEmpty()) {
      return new ArrayList<>();
    }
    List<E> entities = new ArrayList<>(nativeEntities.size());
    for (Entity nativeEntity : nativeEntities) {
      E entity = Unmarshaller.unmarshal(nativeEntity, entityClass);
      entities.add(entity);
    }
    return entities;
  }

  /**
   * Converts the given array of native entities to a list of model objects of given type, <code>
   * entityClass</code>.
   *
   * @param entityClass the entity class
   * @param nativeEntities native entities to convert
   * @return the list of model objects
   */
  static <E> List<E> toEntities(Class<E> entityClass, Entity[] nativeEntities) {
    if (nativeEntities == null || nativeEntities.length == 0) {
      return new ArrayList<>();
    }
    return toEntities(entityClass, Arrays.asList(nativeEntities));
  }

  /**
   * Converts the given list of model objects to an array of FullEntity objects.
   *
   * @param entities the model objects to convert.
   * @param entityManager the entity manager
   * @param intent the intent of marshalling
   * @return the equivalent FullEntity array
   */
  static FullEntity<?>[] toNativeFullEntities(
      List<?> entities, DefaultEntityManager entityManager, Marshaller.Intent intent) {
    FullEntity<?>[] nativeEntities = new FullEntity[entities.size()];
    for (int i = 0; i < entities.size(); i++) {
      nativeEntities[i] =
          (FullEntity<?>) Marshaller.marshal(entityManager, entities.get(i), intent);
    }
    return nativeEntities;
  }

  /**
   * Converts the given list of model objects to an array of native Entity objects.
   *
   * @param entities the model objects to convert.
   * @param entityManager the entity manager
   * @param intent the intent of marshalling
   * @return the equivalent Entity array
   */
  static Entity[] toNativeEntities(
      List<?> entities, DefaultEntityManager entityManager, Marshaller.Intent intent) {
    Entity[] nativeEntities = new Entity[entities.size()];
    for (int i = 0; i < entities.size(); i++) {
      nativeEntities[i] = (Entity) Marshaller.marshal(entityManager, entities.get(i), intent);
    }
    return nativeEntities;
  }

  /**
   * Increments the version property of the given entity by one.
   *
   * @param nativeEntity the target entity
   * @param versionMetadata the metadata of the version property
   * @return a new entity (copy of the given), but with the incremented version.
   */
  static Entity incrementVersion(Entity nativeEntity, PropertyMetadata versionMetadata) {
    String versionPropertyName = versionMetadata.getMappedName();
    long version = nativeEntity.getLong(versionPropertyName);
    return Entity.newBuilder(nativeEntity).set(versionPropertyName, ++version).build();
  }

  /**
   * Rolls back the given transaction, if it is still active.
   *
   * @param transaction the transaction to roll back.
   */
  static void rollbackIfActive(Transaction transaction) {
    try {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
    } catch (DatastoreException exp) {
      throw new EntityManagerException(exp);
    }
  }

  /**
   * Converts/wraps the given native keys into a list of {@link DatastoreKey} objects.
   *
   * @param nativeKeys the native keys
   * @return a list of {@link DatastoreKey} objects.
   */
  static List<DatastoreKey> toDatastoreKeys(List<Key> nativeKeys) {
    if (nativeKeys == null || nativeKeys.isEmpty()) {
      return new ArrayList<>(0);
    }
    List<DatastoreKey> datastoreKeys = new ArrayList<>(nativeKeys.size());
    for (Key nativeKey : nativeKeys) {
      datastoreKeys.add(new DefaultDatastoreKey(nativeKey));
    }
    return datastoreKeys;
  }

  /**
   * Converts/Unwraps the given list of {@link DatastoreKey} objects into an array of native {@link
   * Key}s.
   *
   * @param keys the list of {@link DatastoreKey} objects
   * @return the native keys
   */
  static Key[] toNativeKeys(List<DatastoreKey> keys) {
    Key[] nativeKeys = new Key[keys.size()];
    for (int i = 0; i < keys.size(); i++) {
      nativeKeys[i] = keys.get(i).nativeKey();
    }
    return nativeKeys;
  }

  /**
   * Validates if the given entity is valid for deferred ID allocation. Deferred ID allocation is
   * valid for entities using a numeric ID.
   *
   * @param entity the entity to validate
   * @throws EntityManagerException if the given entity does not use a numeric ID
   */
  static void validateDeferredIdAllocation(Object entity) {
    IdentifierMetadata identifierMetadata = EntityIntrospector.getIdentifierMetadata(entity);
    if (identifierMetadata.getDataType() == DataType.STRING) {
      throw new EntityManagerException(
          "Deferred ID allocation is not applicable for entities with String identifiers. ");
    }
  }

  /**
   * Wraps the given DatastoreException into an {@link EntityManagerException} or a subclass of
   * {@link EntityManagerException}.
   *
   * @param exp the DatastoreException
   * @return An {@link EntityManagerException} or a subclass of {@link EntityManagerException}.
   */
  static EntityManagerException wrap(DatastoreException exp) {
    switch (exp.getCode()) {
      case ERROR_CODE_ENTITY_NOT_FOUND:
        return new EntityNotFoundException(exp);
      case ERROR_CODE_ENTITY_ALREADY_EXISTS:
        return new EntityAlreadyExistsException(exp);
      default:
        return new EntityManagerException(exp);
    }
  }
}

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

package com.jmethods.catatumbo.mappers;

import com.google.cloud.datastore.EntityValue;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueBuilder;
import com.google.cloud.datastore.ValueType;
import com.jmethods.catatumbo.Indexer;
import com.jmethods.catatumbo.Mapper;
import com.jmethods.catatumbo.MappingException;
import com.jmethods.catatumbo.impl.ConstructorMetadata;
import com.jmethods.catatumbo.impl.EmbeddableIntrospector;
import com.jmethods.catatumbo.impl.EmbeddableMetadata;
import com.jmethods.catatumbo.impl.PropertyMetadata;

/**
 * An implementation of {@link Mapper} interface to map embedded objects.
 *
 * @author Sai Pullabhotla
 */
public class EmbeddedObjectMapper implements Mapper {

  /** The Embeddable class. */
  private final Class<?> clazz;

  /** Metadata of the Embeddable class. */
  private final EmbeddableMetadata metadata;

  /**
   * Creates a new instance of <code>EmbeddedObjectMapper</code>.
   *
   * @param clazz the Embeddable class
   */
  public EmbeddedObjectMapper(Class<?> clazz) {
    this.clazz = clazz;
    this.metadata = EmbeddableIntrospector.introspect(clazz);
  }

  @Override
  public ValueBuilder<?, ?, ?> toDatastore(Object input) {
    if (input == null) {
      return NullValue.newBuilder();
    }
    try {
      FullEntity.Builder<IncompleteKey> entityBuilder = FullEntity.newBuilder();
      for (PropertyMetadata propertyMetadata : metadata.getPropertyMetadataCollection()) {
        Object propertyValue = propertyMetadata.getReadMethod().invoke(input);
        if (propertyValue == null && propertyMetadata.isOptional()) {
          continue;
        }
        ValueBuilder<?, ?, ?> valueBuilder =
            propertyMetadata.getMapper().toDatastore(propertyValue);
        // ListValues cannot have indexing turned off. Indexing is turned on by
        // default, so we don't touch excludeFromIndexes for ListValues.
        if (valueBuilder.getValueType() != ValueType.LIST) {
          valueBuilder.setExcludeFromIndexes(!propertyMetadata.isIndexed());
        }
        Value<?> value = valueBuilder.build();
        entityBuilder.set(propertyMetadata.getMappedName(), value);
        Indexer indexer = propertyMetadata.getSecondaryIndexer();
        if (indexer != null) {
          entityBuilder.set(propertyMetadata.getSecondaryIndexName(), indexer.index(value));
        }
      }
      return EntityValue.newBuilder(entityBuilder.build());
    } catch (Throwable exp) {
      throw new MappingException(exp);
    }
  }

  @Override
  public Object toModel(Value<?> input) {
    if (input.getType() == ValueType.NULL) {
      return null;
    }
    try {
      FullEntity<?> entity = ((EntityValue) input).get();
      ConstructorMetadata constructorMetadata = metadata.getConstructorMetadata();
      Object embeddedObject = constructorMetadata.getConstructorMethodHandle().invoke();
      for (PropertyMetadata propertyMetadata : metadata.getPropertyMetadataCollection()) {
        String mappedName = propertyMetadata.getMappedName();
        if (entity.contains(mappedName)) {
          Value<?> propertyValue = entity.getValue(mappedName);
          Object fieldValue = propertyMetadata.getMapper().toModel(propertyValue);
          propertyMetadata.getWriteMethod().invoke(embeddedObject, fieldValue);
        }
      }
      if (constructorMetadata.isBuilderConstructionStrategy()) {
        embeddedObject =
            metadata.getConstructorMetadata().getBuildMethodHandle().invoke(embeddedObject);
      }
      return embeddedObject;
    } catch (Throwable exp) {
      throw new MappingException(exp);
    }
  }
}

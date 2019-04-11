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

import com.google.cloud.datastore.Blob;
import com.google.cloud.datastore.BlobValue;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueBuilder;
import com.jmethods.catatumbo.Mapper;

/**
 * An implementation of {@link Mapper} for mapping byte array types to/from Cloud Datastore.
 *
 * @author Sai Pullabhotla
 */
public class ByteArrayMapper implements Mapper {

  @Override
  public ValueBuilder<?, ?, ?> toDatastore(Object input) {
    if (input == null) {
      return NullValue.newBuilder();
    }
    return BlobValue.newBuilder(Blob.copyFrom((byte[]) input));
  }

  @Override
  public Object toModel(Value<?> input) {
    if (input instanceof NullValue) {
      return null;
    }
    return ((BlobValue) input).get().toByteArray();
  }
}

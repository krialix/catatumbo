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

package com.jmethods.catatumbo.custommappers;

import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueBuilder;
import com.jmethods.catatumbo.Mapper;
import com.jmethods.catatumbo.MappingException;

/**
 * An implementation of {@link Mapper} for mapping primitive and wrapper byte types to/from the
 * Cloud Datastore.
 * 
 * @author Sai Pullabhotla
 *
 */
public class ByteMapper implements Mapper {

  @Override
  public ValueBuilder<?, ?, ?> toDatastore(Object input) {
    if (input == null) {
      return NullValue.newBuilder();
    }
    return LongValue.newBuilder((byte) input);
  }

  @Override
  public Object toModel(Value<?> input) {
    if (input instanceof NullValue) {
      return null;
    }
    Long l = ((LongValue) input).get();
    if (l < Byte.MIN_VALUE || l > Byte.MAX_VALUE) {
      throw new MappingException(String.format("Value %d is out of range for byte type", l));
    }
    return l.byteValue();
  }

}

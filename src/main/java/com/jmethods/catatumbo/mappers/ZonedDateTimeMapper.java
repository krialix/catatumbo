/*
 * Copyright 2017 Sai Pullabhotla.
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

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueBuilder;
import com.jmethods.catatumbo.Mapper;
import com.jmethods.catatumbo.MappingException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of {@link Mapper} for mapping {@link ZonedDateTime} to/from Cloud Datastore.
 * {@link ZonedDateTime} types are mapped to DateTime type in the Cloud Datastore. This maximum
 * precision is capped to Microseconds to match with what the Datastore supports.
 *
 * @author Sai Pullabhotla
 */
public class ZonedDateTimeMapper implements Mapper {

  @Override
  public ValueBuilder<?, ?, ?> toDatastore(Object input) {
    if (input == null) {
      return NullValue.newBuilder();
    }
    ZonedDateTime zonedDateTime = (ZonedDateTime) input;
    long seconds = zonedDateTime.toEpochSecond();
    int nanos = zonedDateTime.getNano();
    long microseconds = TimeUnit.SECONDS.toMicros(seconds) + TimeUnit.NANOSECONDS.toMicros(nanos);
    return TimestampValue.newBuilder(Timestamp.ofTimeMicroseconds(microseconds));
  }

  @Override
  public Object toModel(Value<?> input) {
    if (input instanceof NullValue) {
      return null;
    }
    try {
      Timestamp ts = ((TimestampValue) input).get();
      long seconds = ts.getSeconds();
      int nanos = ts.getNanos();
      return ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanos), ZoneId.systemDefault());
    } catch (ClassCastException exp) {
      String pattern = "Expecting %s, but found %s";
      throw new MappingException(
          String.format(pattern, TimestampValue.class.getName(), input.getClass().getName()), exp);
    }
  }
}

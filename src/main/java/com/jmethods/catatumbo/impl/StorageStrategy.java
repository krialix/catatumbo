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

import com.jmethods.catatumbo.Embedded;

/**
 * Storage strategies for {@link Embedded} fields.
 *
 * @author Sai Pullabhotla
 */
public enum StorageStrategy {

  /** The object tree of {@link Embedded} field is exploded into individual properties. */
  EXPLODED,

  /**
   * The object tree of {@link Embedded} field is stored in a single property as an Embedded Entity.
   */
  IMPLODED
}

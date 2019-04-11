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

package com.jmethods.catatumbo;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies that a class is an EntityListener. Classes marked with this annotation should implement
 * one or more callback methods for processing entity lifecycle events. Each callback method must be
 * of the form <code>public void methodName(Object entity)</code>. The parameter type can be more
 * specific type than <code>Object</code>, as needed.
 *
 * <p>EntityListeners are registered with {@link Entity} classes and/or {@link MappedSuperClass}
 * classes using the {@link EntityListeners} annotation. For each lifecycle event of an entity, the
 * callback methods of the registered listeners will be invoked.
 *
 * <p>Callback methods must have one or more of these annotations:
 *
 * <ul>
 *   <li>{@link PreInsert}
 *   <li>{@link PostInsert}
 *   <li>{@link PreUpdate}
 *   <li>{@link PostUpdate}
 *   <li>{@link PreUpsert}
 *   <li>{@link PostUpsert}
 *   <li>{@link PreDelete}
 *   <li>{@link PostDelete}
 *   <li>{@link PostLoad}
 * </ul>
 *
 * <p>Entity Listener classes must conform to the following specification:
 *
 * <ul>
 *   <li>Must have a default (no-arg) constructor
 *   <li>Each callback method must be public, must not be static, abstract or final.
 *   <li>The callback method must be of the form <code>public void methodName(Object)</code>. The
 *       Object argument is the entity instance for which the callback method is invoked. The type
 *       may be more specific type than Object.
 *   <li>At most one method can exist for a given type of callback (e.g. PostUpdate)
 *   <li>A single method may handle more than one type of callback (e.g. A method can have both
 *       {@link PreInsert}, {@link PreUpdate} and {@link PreUpsert} annotations.
 * </ul>
 *
 * @author Sai Pullabhotla
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface EntityListener {
  // Marker
}

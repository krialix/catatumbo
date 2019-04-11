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

package com.jmethods.catatumbo.stats;

import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Property;
import java.io.Serializable;

/**
 * Base class for Summary Statistics - for the entire Datastore as well as for a specific namespace.
 *
 * @author Sai Pullabhotla
 */
@Entity(kind = StatConstants.STAT_NAMESPACE)
public class StatNamespace extends StatBase implements Serializable {

  /** Serial version UID */
  private static final long serialVersionUID = -6358059154078381425L;

  /** ID of the statistic entity */
  @Identifier(autoGenerated = false)
  private String id;

  /** Size of built-in indexes, in bytes */
  @Property(name = StatConstants.PROP_BUILTIN_INDEX_BYTES)
  private long builtinIndexBytes;

  /** Number of built-in indexes */
  @Property(name = StatConstants.PROP_BUILTIN_INDEX_COUNT)
  private long builtinIndexCount;

  /** Size of composite indexes, in bytes */
  @Property(name = StatConstants.PROP_COMPOSITE_INDEX_BYTES)
  private long compositeIndexBytes;

  /** Number of composite indexes */
  @Property(name = StatConstants.PROP_COMPOSITE_INDEX_COUNT)
  private long compositeIndexCount;

  /** Size of entities, in bytes */
  @Property(name = StatConstants.PROP_ENTITY_BYTES)
  private long entityBytes;

  /** Subject namespace */
  @Property(name = StatConstants.PROP_SUBJECT_NAMESPACE)
  private String subjectNamespace;

  /**
   * Returns the ID of this statistic entity.
   *
   * @return the ID of this statistic entity.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the ID of this statistic entity.
   *
   * @param id the ID of this statistic entity.
   */
  public void setId(String id) {
    this.id = id == null ? "" : id;
  }

  /**
   * Returns the size of built-in indexes in bytes.
   *
   * @return the size of built-in indexes in bytes.
   */
  public long getBuiltinIndexBytes() {
    return builtinIndexBytes;
  }

  /**
   * Sets the size of built-in indexes in bytes.
   *
   * @param builtinIndexBytes the size of built-in indexes in bytes.
   */
  public void setBuiltinIndexBytes(long builtinIndexBytes) {
    this.builtinIndexBytes = builtinIndexBytes;
  }

  /**
   * Returns the number of built-in indexes.
   *
   * @return the number of built-in indexes.
   */
  public long getBuiltinIndexCount() {
    return builtinIndexCount;
  }

  /**
   * Sets the number of built-in indexes.
   *
   * @param builtinIndexCount the number of built-in indexes.
   */
  public void setBuiltinIndexCount(long builtinIndexCount) {
    this.builtinIndexCount = builtinIndexCount;
  }

  /**
   * Returns the size of composite indexes in bytes.
   *
   * @return the size of composite indexes in bytes.
   */
  public long getCompositeIndexBytes() {
    return compositeIndexBytes;
  }

  /**
   * Sets the size of composite indexes in bytes.
   *
   * @param compositeIndexBytes the size of composite indexes in bytes.
   */
  public void setCompositeIndexBytes(long compositeIndexBytes) {
    this.compositeIndexBytes = compositeIndexBytes;
  }

  /**
   * Returns the number of composite indexes.
   *
   * @return the number of composite indexes.
   */
  public long getCompositeIndexCount() {
    return compositeIndexCount;
  }

  /**
   * Sets the number of composite indexes.
   *
   * @param compositeIndexCount the number of composite indexes.
   */
  public void setCompositeIndexCount(long compositeIndexCount) {
    this.compositeIndexCount = compositeIndexCount;
  }

  /**
   * Returns the size of entities in bytes.
   *
   * @return the size of entities in bytes.
   */
  public long getEntityBytes() {
    return entityBytes;
  }

  /**
   * Sets the size of entities in bytes.
   *
   * @param entityBytes the size of entities in bytes.
   */
  public void setEntityBytes(long entityBytes) {
    this.entityBytes = entityBytes;
  }

  /**
   * Returns the subject namespace to which these statistics belong to.
   *
   * @return the subject namespace to which these statistics belong to.
   */
  public String getSubjectNamespace() {
    return subjectNamespace;
  }

  /**
   * Sets the subject namespace to which these statistics belong to.
   *
   * @param subjectNamespace the subject namespace to which these statistics belong to.
   */
  public void setSubjectNamespace(String subjectNamespace) {
    this.subjectNamespace = subjectNamespace;
  }
}

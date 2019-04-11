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

import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.MappedSuperClass;
import com.jmethods.catatumbo.Property;
import java.io.Serializable;

/**
 * Base class for composite index statistics.
 *
 * @author Sai Pullabhotla
 */
@MappedSuperClass
public abstract class StatCompositeIndexBase extends StatBase implements Serializable {

  /** Serial version UID */
  private static final long serialVersionUID = -7965853149712013490L;

  /** Entity ID */
  @Identifier(autoGenerated = false)
  private String id;

  /** Index ID */
  @Property(name = StatConstants.PROP_INDEX_ID)
  private long indexId;

  /** Kind name */
  @Property(name = StatConstants.PROP_KIND_NAME)
  private String kindName;

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
    this.id = id;
  }

  /**
   * Returns the ID of the index to which this statistic belongs to.
   *
   * @return the ID of the index to which this statistic belongs to.
   */
  public long getIndexId() {
    return indexId;
  }

  /**
   * Sets the ID of the index to which this statistic belongs to.
   *
   * @param indexId the ID of the index to which this statistic belongs to.
   */
  public void setIndexId(long indexId) {
    this.indexId = indexId;
  }

  /**
   * Returns the name of the Kind on which this composite index was created.
   *
   * @return the name of the Kind on which this composite index was created.
   */
  public String getKindName() {
    return kindName;
  }

  /**
   * Sets the name of the Kind on which this composite index was created.
   *
   * @param kindName the name of the Kind on which this composite index was created.
   */
  public void setKindName(String kindName) {
    this.kindName = kindName;
  }
}

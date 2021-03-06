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
import java.io.Serializable;

/**
 * Statistics by Kind, across all namespaces.
 *
 * @author Sai Pullabhotla
 */
@Entity(kind = StatConstants.STAT_KIND)
public class StatKind extends StatKindBase implements Serializable {

  /** Serial version UID */
  private static final long serialVersionUID = 2549703734475617627L;
}

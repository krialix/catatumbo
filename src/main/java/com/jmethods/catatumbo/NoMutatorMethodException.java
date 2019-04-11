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

package com.jmethods.catatumbo;

/**
 * An Exception to indicate that a model class (e.g. Entity, Embeddable, etc.) is missing a required
 * public mutator method.
 *
 * @author Sai Pullabhotla
 */
public class NoMutatorMethodException extends EntityManagerException {

  /** Serial version UID */
  private static final long serialVersionUID = 7547518972406162160L;

  /** Creates a new instance of {@code NoMutatorMethodException}. */
  public NoMutatorMethodException() {}

  /**
   * Creates a new instance of {@code NoMutatorMethodException}.
   *
   * @param msg the detailed message.
   */
  public NoMutatorMethodException(String msg) {
    super(msg);
  }

  /**
   * Creates a new instance of {@code NoMutatorMethodException}.
   *
   * @param message the detailed message
   * @param cause the cause
   */
  public NoMutatorMethodException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new instance of {@code NoMutatorMethodException}.
   *
   * @param cause the cause.
   */
  public NoMutatorMethodException(Throwable cause) {
    super(cause);
  }
}

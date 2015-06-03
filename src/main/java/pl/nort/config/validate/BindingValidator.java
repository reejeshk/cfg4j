/*
 * Copyright 2015 Norbert Potocki (norbert.potocki@nort.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.nort.config.validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

/**
 * Configuration beans binding validator
 */
public class BindingValidator {

  /**
   * Validate if the {@code configurationBean} object was bound successfully
   *
   * @param configurationBean configurationBean to validate
   * @param type              interface used to access {@code configurationBean}
   * @param <T>               type of the validated bean
   * @throws IllegalStateException    when unable to access one of the methods
   * @throws NoSuchElementException   when an invocation of one of the {@code configurationBean} methods failed with an
   *                                  underlaying exception of type {@link NoSuchElementException}
   * @throws IllegalArgumentException when an invocation of one of the {@code configurationBean} methods failed with an
   *                                  underlaying exception of type {@link IllegalArgumentException}
   */
  public <T> void validate(T configurationBean, Class<T> type) {
    for (Method declaredMethod : type.getDeclaredMethods()) {
      try {

        declaredMethod.invoke(configurationBean);

      } catch (InvocationTargetException e) {

        if (e.getCause() != null) {
          if (e.getCause() instanceof NoSuchElementException) {
            throw (NoSuchElementException) e.getCause();
          }

          if (e.getCause() instanceof IllegalArgumentException) {
            throw (IllegalArgumentException) e.getCause();
          }
        }

        throw new IllegalStateException("Can't bind method " + declaredMethod.getName(), e);

      } catch (IllegalAccessException e) {
        throw new IllegalStateException("Can't bind method " + declaredMethod.getName(), e);
      }
    }
  }
}

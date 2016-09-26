package com.polydeucesys.validation.validators.string;/**
 * Copyright (c) 2016 Polydeuce-Sys Ltd
 * <p>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

import com.polydeucesys.validation.validators.Constraint;

import java.lang.annotation.*;

/**
 * An annotation for marking a String contrained by a matching regex.
 * By default it can match more than once. The onlyOnce flag can be set
 * to constrain to a single match
 * Created by kevinmclellan on 19/09/2016.
 */
@Constraint(validatedBy = StringMatchesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface StringMatches {
    String expression();
    boolean onlyOnce() default false;
}

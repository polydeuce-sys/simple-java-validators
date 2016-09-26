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
 * Annotation for marking a constraint that a value must be a hostname
 * or host and port. Allows validation of the hostname against a number of criteria
 * as descrived in the {@link Url} constraint
 * Created by kevinmclellan on 21/09/2016.
 */
@Constraint(validatedBy = HostValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface Host {
    boolean lookup() default false;
    String[] whitelist() default "";
    String[] blacklist() default "";
    String domain() default "";
}

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
 * Constraint for URL validation. Each component of the URL can be given a specific
 * validation criteria. Theres are specified as regex strings, however hostDomain
 * does allow the more idiomatic "*.company.classification" sytle. Additionally hostDomain
 * can be validated via dns lookup if the URL does not have the fully
 * qualified domain name for the host, however this requires that the
 * hostLookup flag be set to true.
 * Created by kevinmclellan on 20/09/2016.
 */
@Constraint(validatedBy = UrlValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface Url {
    String[] protocol() default {""};
    boolean hostLookup() default false;
    String[] hostWhitelist() default {""};
    String[] hostBlacklist() default {""};
    String hostDomain() default "";
    int[] portRange() default {0};
    String allowedPath() default "";
    String allowedQueryFormat() default "";
}

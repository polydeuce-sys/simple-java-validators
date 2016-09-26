package com.polydeucesys.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  Copyright 2016 Polydeuce-Sys Ltd
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 **/

/**
 * A validator is used to check the Field value on an object instance against the criteria specified
 * in an Annotation. IBeanGetterValidator subclasses are associated to Annotations by the Constraint
 * Annotation
 * Created by kevinmclellan on 15/09/2016.
 */
public interface IBeanGetterValidator<T extends Annotation> {
    /**
     * Check the value of the {@code beanGetter} on the {@code instance} against the
     * criteria in the {@code validationCriteria}. If validation fails a {@link ValidationException}
     * should be thrown
     * @param beanGetter
     * @param instance
     * @param validationCriteria
     * @throws ValidationException
     */
    void apply(Method beanGetter, Object instance, T validationCriteria) throws ValidationException;
}

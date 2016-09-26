package com.polydeucesys.validation.validators;/**
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

import java.lang.annotation.Annotation;

/**
 * The validator for values which cannot be null.
 * Created by kevinmclellan on 20/09/2016.
 */
public class NotNullValidator extends BaseValidator<NotNull, Object> {

    protected boolean doApply(Object o, NotNull validationCriteria) {
        return (o != null);
    }

    protected Object convertValueToValidatingType(Object value) {
        return value;
    }
}

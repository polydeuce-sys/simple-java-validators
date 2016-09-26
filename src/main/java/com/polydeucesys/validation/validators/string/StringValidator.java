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

import com.polydeucesys.validation.validators.BaseValidator;

import java.lang.annotation.Annotation;

/**
 * Abstract base class for validation of String types. Deals with convertion of
 * values to String
 * Created by kevinmclellan on 20/09/2016.
 */
public abstract class StringValidator<T extends Annotation> extends BaseValidator<T,String>{
    protected String convertValueToValidatingType(Object value ){
        return value.toString();
    }
}

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator which uses a Regular Expression to constrain the values of a field
 * Created by kevinmclellan on 19/09/2016.
 */
public class StringMatchesValidator extends StringValidator<StringMatches> {

    protected boolean doApply(String s, StringMatches validationCriteria) {
        Pattern pattern = Pattern.compile(validationCriteria.expression());
        Matcher m = pattern.matcher(s);
        // found and don't care how often, or not found again
        return m.find() && (!validationCriteria.onlyOnce() || !m.find());
    }
}

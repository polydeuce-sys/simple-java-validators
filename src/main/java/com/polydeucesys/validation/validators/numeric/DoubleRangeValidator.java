package com.polydeucesys.validation.validators.numeric;/**
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

/**
 * Validate a floating point range.
 * Created by kevinmclellan on 19/09/2016.
 */
public class DoubleRangeValidator extends DoubleValidator<DoubleRange>{
    protected boolean doApply(Double aDouble, DoubleRange validationCriteria) {
        return (validationCriteria.minInclusive()?aDouble>=validationCriteria.min():aDouble>validationCriteria.min()) &&
                (validationCriteria.maxInclusive()?aDouble<=validationCriteria.max():aDouble<validationCriteria.max());
    }
}

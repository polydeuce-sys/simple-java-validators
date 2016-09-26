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

import com.polydeucesys.validation.IBeanGetterValidator;
import com.polydeucesys.validation.ValidationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Provides an abtract superclass for validators to simplify implementation, dealing with
 * error messages and control flow
 * Created by kevinmclellan on 16/09/2016.
 */
public abstract class BaseValidator<T extends Annotation, V> implements IBeanGetterValidator<T> {

    // TODO: switch to resouce loading for languages/locales
    private static final String ILLEGAL_ACCESS_ERROR = "Illegal access to %s in %s : Validation may require different security manager settings";
    private static final String INVOCATION_TARGET_ERROR = "Invocation exception accessing %s in %s";

    private static final String VALIDATION_ERROR = "Invalid value \"%s\" in %s. Fails validation against %s";
    /**
     * Check the validity of the given value.
     * @param v - the parameter being checked
     * @param validationCriteria - The criteria being checked against
     * @return true if the value is valid
     */
    protected abstract boolean doApply(V v, T validationCriteria);

    /**
     * Ensure the value being checked is the same as the type expected in the constraint.
     * It may be that there is a String field for example which must actually be a number
     * in a certain range or other such odd constraint
     * @param value
     * @return
     */
    protected abstract V convertValueToValidatingType( Object value);

    private final V extractValue(Method beanGetter, Object instance) throws ValidationException{
        try{
            return convertValueToValidatingType(beanGetter.invoke(instance));
        }catch (IllegalAccessException e) {
            throw new ValidationException(String.format(ILLEGAL_ACCESS_ERROR,beanGetter.getName(),
                    beanGetter.getDeclaringClass()), e);
        } catch (InvocationTargetException e) {
            throw new ValidationException(String.format(INVOCATION_TARGET_ERROR,beanGetter.getName(),
                    beanGetter.getDeclaringClass()), e);
        }
    }

    /**
     * Apply the criteria defined in the validator annoation to the value.
     * @param beanGetter - Getter method for constrained value
     * @param instance - Instance of class defining the field
     * @param validationCriteria - Validation annotation object
     * @throws ValidationException - If the value does not meet the criteria defined in the annotation
     */
    public void apply(Method beanGetter, Object instance, T validationCriteria) throws ValidationException {
        if(!doApply(extractValue(beanGetter, instance), validationCriteria)){
            throw new ValidationException( String.format(VALIDATION_ERROR,  beanGetter.getName(),
                                                                            beanGetter.getDeclaringClass().toString(),
                                                                            validationCriteria.toString()));
        }
    }

}

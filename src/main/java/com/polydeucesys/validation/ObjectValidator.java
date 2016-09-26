package com.polydeucesys.validation;/**
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Walks up the class heriarchy of an object until it reaches specified
 * superclass. At each stage it will check the declared fields, and if they
 * are annotated with an annotation which has a contraint annotation,
 * it will apply the constraints implementation of {@link IBeanGetterValidator} to
 * the field and annotation for the object.
 * Created by kevinmclellan on 15/09/2016.
 */
public enum ObjectValidator implements IObjectValidator{

    INSTANCE;

    /**
     *
     * @return the singleton instance of the Object validator
     */
    public static final ObjectValidator getInstance(){
        return INSTANCE;
    }

    private void validateAtClass(Object obj, Class clazzLevel) throws ValidationException{
        Method[] declaredMethods = clazzLevel.getDeclaredMethods();
        for(Method target : declaredMethods){
            for(Annotation targetAnnotation : target.getAnnotations()){
                Constraint constraintAnnotation = targetAnnotation.annotationType().getAnnotation(Constraint.class);
                if(constraintAnnotation != null) {
                    IBeanGetterValidator validator = null;
                    try {
                        validator = (IBeanGetterValidator) constraintAnnotation.validatedBy().newInstance();
                    } catch (InstantiationException e) {
                        throw new IllegalStateException("Failed to instantiate constraint validator", e);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Failed to instantiate constraint validator", e);
                    }
                    validator.apply(target, obj, targetAnnotation);
                }
            }
        }
    }

    public void validate(Object obj, Class toParent) throws ValidationException{
        Class clazzLevel = obj.getClass();
        while(clazzLevel != toParent){
            validateAtClass(obj, clazzLevel);
            clazzLevel = clazzLevel.getSuperclass();
        }
    }

    public void validate(Object obj) throws ValidationException {
        validate(obj, Object.class);
    }
}

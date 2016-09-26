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

import com.polydeucesys.validation.ValidatableObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static junit.framework.TestCase.fail;

/**
 * Created by kevinmclellan on 16/09/2016.
 */
public class AnnotatedTestClass<T extends Annotation> extends ValidatableObject {

    public T getMyAnnotation(String methodName, Class annotation){
        try {
            Method f = this.getClass().getDeclaredMethod(methodName);
            T anno =  (T)f.getAnnotation(annotation);
            if(anno == null){
                throw new IllegalStateException("No such annotation " + annotation +
                        " on this test object. Check your test");
            }
            return anno;
        } catch (NoSuchMethodException e) {
            throw new Error("Missing Field? Rewrite your test!");
        }
    }
}

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

import com.polydeucesys.validation.ValidationException;
import com.polydeucesys.validation.validators.AnnotatedTestClass;
import com.polydeucesys.validation.validators.numeric.LongLowerBound;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by kevinmclellan on 20/09/2016.
 */
public class StringConstraintTests {
    // Test classes are public. We do not do anything like
    // caling setAccessable in the refletive parts. So you need to
    // validate what can be seen by others
    public static class TestSMAClass extends AnnotatedTestClass<StringMatches>{
        private String anyVal;
        private String onceVal;

        public TestSMAClass( String anyVal, String onceVal){
            this.anyVal = anyVal;
            this.onceVal = onceVal;
        }
        @StringMatches(expression="\\d\\d\\d\\d")
        public String getAnyVal(){
            return anyVal;
        }
        @StringMatches(expression="\\d\\d\\d\\d", onlyOnce = true)
        public String getOnceVal(){
            return onceVal;
        }

        public void setAnyVal(String val){
            anyVal = val;
        }

        public void setOnceVal( String val){
            onceVal = val;
        }
    }

    @Test
    public void testStringMatchesBounds(){
        StringMatchesValidator v = new StringMatchesValidator();
        TestSMAClass t1 = new TestSMAClass("SomeString", "SomeString");
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getAnyVal()),
                t1.getMyAnnotation("getAnyVal", StringMatches.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getOnceVal()),
                t1.getMyAnnotation("getOnceVal", StringMatches.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        t1.setAnyVal("September, 2016");
        t1.setOnceVal("September, 2016");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getAnyVal()),
                t1.getMyAnnotation("getAnyVal", StringMatches.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getOnceVal()),
                t1.getMyAnnotation("getOnceVal", StringMatches.class)));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        t1.setAnyVal("12345678");
        t1.setOnceVal("12345678");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getAnyVal()),
                t1.getMyAnnotation("getAnyVal", StringMatches.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getOnceVal()),
                t1.getMyAnnotation("getOnceVal", StringMatches.class)));
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){
        }
    }
}

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

import com.polydeucesys.validation.ValidationException;
import com.polydeucesys.validation.validators.AnnotatedTestClass;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static junit.framework.TestCase.*;

/**
 * Created by kevinmclellan on 16/09/2016.
 */
public class NumericBoundsTests {
    public static class TestLLBObject extends AnnotatedTestClass<LongLowerBound> {
        private int val;

        public TestLLBObject(int initVal ){
            val = initVal;
        }

        public void setValue(int value ){
            val = value;
        }

        @LongLowerBound(min=0)
        public int getValue(){
            return val;
        }
    }

    @Test
    public void testChecksLowerBound(){
        LongLowerBoundValidator v = new LongLowerBoundValidator();
        TestLLBObject t1 = new TestLLBObject(-15);
        LongLowerBound annotation = t1.getMyAnnotation("getValue", LongLowerBound.class);

        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        t1.setValue(5);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        t1.setValue(0);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
    }

    public static class TestLUBObject extends AnnotatedTestClass<LongUpperBound>{
        private int val;

        public TestLUBObject(int initVal ){
            val = initVal;
        }

        public void setValue(int value ){
            val = value;
        }

        @LongUpperBound(max=75)
        public int getValue(){
            return val;
        }
    }

    @Test
    public void testChecksUpperBound(){
        LongUpperBoundValidator v = new LongUpperBoundValidator();
        TestLUBObject t1 = new TestLUBObject(95);
        LongUpperBound annotation = t1.getMyAnnotation("getValue", LongUpperBound.class);

        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // countable bounds are inclusive
        t1.setValue(75);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
    }

    public static class TestLRngObject extends AnnotatedTestClass<LongRange>{
        private int val;

        public TestLRngObject(int initVal ){
            val = initVal;
        }

        public void setValue(int value ){
            val = value;
        }

        @LongRange(min=-10, max=75)
        public int getValue(){
            return val;
        }
    }

    @Test
    public void testChecksRange(){
        LongRangeValidator v = new LongRangeValidator();
        TestLRngObject t1 = new TestLRngObject(95);
        LongRange annotation = t1.getMyAnnotation("getValue", LongRange.class);

        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // countable bounds are inclusive
        t1.setValue(75);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        t1.setValue(-20);
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // countable bounds are inclusive
        t1.setValue(-5);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getValue()),
                annotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
    }

    public static class TestDLBObject extends AnnotatedTestClass<DoubleLowerBound>{
        private double valInclusive;
        private double valExclusive;


        public TestDLBObject(double initInVal, double initExVal ){
            valInclusive = initInVal;
            valExclusive = initExVal;
        }

        public void setInclusiveValue(double value ){
            valInclusive =  value;
        }

        public void setExclusiveValue(double value ){
            valExclusive =  value;
        }

        @DoubleLowerBound(min=-5.25)
        public double getInclusiveValue(){
            return valInclusive;
        }
        @DoubleLowerBound(min=-5.25, inclusive = false)
        public double getExclusiveValue(){
            return valExclusive;
        }
    }

    @Test
    public void testLowerDoubleBound(){
        DoubleLowerBoundValidator v = new DoubleLowerBoundValidator();
        TestDLBObject t1 = new TestDLBObject(-15.0, -15.0);
        DoubleLowerBound inclusiveAnnotation = t1.getMyAnnotation("getInclusiveValue", DoubleLowerBound.class);
        DoubleLowerBound exclusiveAnnotation = t1.getMyAnnotation("getExclusiveValue", DoubleLowerBound.class);

        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        t1.setInclusiveValue(5.0);
        t1.setExclusiveValue(5.0);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        // check boundry
        t1.setInclusiveValue(-5.25);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        t1.setExclusiveValue(-5.25);
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                    exclusiveAnnotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
    }

    public static class TestDUBObject extends AnnotatedTestClass<DoubleUpperBound>{
        private double valInclusive;
        private double valExclusive;

        public TestDUBObject(double initInVal, double initExVal ){
            valInclusive = initInVal;
            valExclusive = initExVal;
        }

        public void setInclusiveValue(double value ){
            valInclusive =  value;
        }

        public void setExclusiveValue(double value ){
            valExclusive =  value;
        }
        @DoubleUpperBound(max=-5.25)
        public double getInclusiveValue(){
            return valInclusive;
        }
        @DoubleUpperBound(max=-5.25, inclusive = false)
        public double getExclusiveValue(){
            return valExclusive;
        }
    }

    @Test
    public void testUpperDoubleBound(){
        DoubleUpperBoundValidator v = new DoubleUpperBoundValidator();
        TestDUBObject t1 = new TestDUBObject(55.0, 55.0);
        DoubleUpperBound inclusiveAnnotation = t1.getMyAnnotation("getInclusiveValue", DoubleUpperBound.class);
        DoubleUpperBound exclusiveAnnotation = t1.getMyAnnotation("getExclusiveValue", DoubleUpperBound.class);

        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        t1.setInclusiveValue(-15.0);
        t1.setExclusiveValue(-15.0);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        // check boundry
        t1.setInclusiveValue(-5.25);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        t1.setExclusiveValue(-5.25);
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
    }

    public static class TestDRngObject extends AnnotatedTestClass<DoubleRange>{
        private double valInclusive;
        private double valExclusive;


        public TestDRngObject(double initInVal, double initExVal ){
            valInclusive = initInVal;
            valExclusive = initExVal;
        }

        public void setInclusiveValue(double value ){
            valInclusive =  value;
        }

        public void setExclusiveValue(double value ){
            valExclusive =  value;
        }

        @DoubleRange(min=-10, max=75)
        public double getInclusiveValue(){
            return valInclusive;
        }
        @DoubleRange(min=-10, minInclusive = false, max=75, maxInclusive = false)
        public double getExclusiveValue(){
            return valExclusive;
        }
    }

    @Test
    public void testDoubleRange(){
        DoubleRangeValidator v = new DoubleRangeValidator();
        TestDRngObject t1 = new TestDRngObject(-11.0, -11.0);
        DoubleRange inclusiveAnnotation = t1.getMyAnnotation("getInclusiveValue", DoubleRange.class);
        DoubleRange exclusiveAnnotation = t1.getMyAnnotation("getExclusiveValue", DoubleRange.class);

        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        t1.setInclusiveValue(5.0);
        t1.setExclusiveValue(5.0);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        // check boundry
        t1.setInclusiveValue(-10.0);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        t1.setExclusiveValue(-10.0);
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        t1.setExclusiveValue(55.0);
        // check upper boundry
        t1.setInclusiveValue(75.0);
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getInclusiveValue()),
                inclusiveAnnotation));
        try{
            t1.validate();
        }catch(ValidationException e){
            fail("Object should be valid");
        }
        t1.setExclusiveValue(75.0);
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getExclusiveValue()),
                exclusiveAnnotation));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
    }
}


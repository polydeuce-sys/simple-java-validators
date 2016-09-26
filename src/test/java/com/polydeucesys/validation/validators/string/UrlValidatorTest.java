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
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

/**
 * Created by kevinmclellan on 26/09/2016.
 */
public class UrlValidatorTest {


    public static class UrlT1 extends AnnotatedTestClass<Url> {
        private String url;

        public UrlT1( String url ){
            this.url = url;
        }

        public void setUrl(String url){
            this.url = url;
        }

        @Url(protocol={"https"})
        public String getUrl(){
            return url;
        }
    }

    @Test
    public void testSecureOnly(){
        UrlT1 t1 = new UrlT1("http://mysvr.mycorp.com:8081/secured/service/%s");
        UrlValidator v = new UrlValidator();
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getUrl()),
                t1.getMyAnnotation("getUrl", Url.class)));
        try {
            t1.validate();
            fail("Only https");
        } catch (ValidationException e) {
        }
        t1.setUrl("https://mysvr.mycorp.com:8081/secured/service/%s");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getUrl()),
                t1.getMyAnnotation("getUrl", Url.class)));
        try {
            t1.validate();
        } catch (ValidationException e) {
            fail("Should be valid");
        }
    }

    public static class UrlT2 extends AnnotatedTestClass<Url> {
        private String url;

        public UrlT2( String url ){
            this.url = url;
        }

        public void setUrl(String url){
            this.url = url;
        }

        @Url(portRange={8081,9901})
        public String getUrl(){
            return url;
        }
    }

    @Test
    public void testPortRange(){
        UrlT2 t2 = new UrlT2("http://mysvr.mycorp.com/secured/service/%s");
        UrlValidator v = new UrlValidator();
        assertFalse(v.doApply(v.convertValueToValidatingType(t2.getUrl()),
                t2.getMyAnnotation("getUrl", Url.class)));
        try {
            t2.validate();
            fail("Only in port range");
        } catch (ValidationException e) {
        }
        t2.setUrl("http://mysvr.mycorp.com:8085/secured/service/%s");
        assertTrue(v.doApply(v.convertValueToValidatingType(t2.getUrl()),
                t2.getMyAnnotation("getUrl", Url.class)));
        try {
            t2.validate();
        } catch (ValidationException e) {
            fail("Should be valid");
        }
    }


    public static class UrlT3 extends AnnotatedTestClass<Url> {
        private String url;

        public UrlT3( String url ){
            this.url = url;
        }

        public void setUrl(String url){
            this.url = url;
        }

        @Url(allowedPath = "/dcd/(cusip|isin)/scsid")
        public String getUrl(){
            return url;
        }
    }

    @Test
    public void testAllowedPath(){
        UrlT3 t3 = new UrlT3("http://mysvr.mycorp.com/dcd/curveid/scsid");
        UrlValidator v = new UrlValidator();
        assertFalse(v.doApply(v.convertValueToValidatingType(t3.getUrl()),
                t3.getMyAnnotation("getUrl", Url.class)));
        try {
            t3.validate();
            fail("Only valid path");
        } catch (ValidationException e) {
        }
        t3.setUrl("http://mysvr.mycorp.com/dcd/isin/scsid");
        assertTrue(v.doApply(v.convertValueToValidatingType(t3.getUrl()),
                t3.getMyAnnotation("getUrl", Url.class)));
        try {
            t3.validate();
        } catch (ValidationException e) {
            fail("Should be valid");
        }
    }


    public static class UrlT4 extends AnnotatedTestClass<Url> {
        private String url;

        public UrlT4( String url ){
            this.url = url;
        }

        public void setUrl(String url){
            this.url = url;
        }

        // we should assum the url could actually be a format rather than
        // fully specc'ed
        @Url(allowedQueryFormat = "objId=%s&asOfDate=%s")
        public String getUrl(){
            return url;
        }
    }

    @Test
    public void testAllowedQuery(){
        UrlT4 t4 = new UrlT4("http://mysvr.mycorp.com/dcd/curveid/scsid?objectId=11224");
        UrlValidator v = new UrlValidator();
        assertFalse(v.doApply(v.convertValueToValidatingType(t4.getUrl()),
                t4.getMyAnnotation("getUrl", Url.class)));
        try {
            t4.validate();
            fail("Only valid path");
        } catch (ValidationException e) {
        }
        t4.setUrl("http://mysvr.mycorp.com/dcd/isin/scsid?objId=%s&asOfDate=%s");
        assertTrue(v.doApply(v.convertValueToValidatingType(t4.getUrl()),
                t4.getMyAnnotation("getUrl", Url.class)));
        try {
            t4.validate();
        } catch (ValidationException e) {
            fail("Should be valid");
        }
    }

    public static class UrlTestClass extends AnnotatedTestClass<Url> {
        private String url;

        public UrlTestClass( String url ){
            this.url = url;
        }

        @Url(protocol={"http", "https"}, allowedPath = "/dcd/security", allowedQueryFormat = "objId=%s&asOfDate=%s")
        public String getUrl(){
            return url;
        }
    }

    @Test
    public void generlTest(){
        UrlTestClass t1 = new UrlTestClass("http://svr1.mycorp.com/dcd/security?objId=%s&asOfDate=%s");
        try {
            t1.validate();
        } catch (ValidationException e) {
            fail("Should be valid");
        }
    }



}

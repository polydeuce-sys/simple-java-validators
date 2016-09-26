package com.polydeucesys.validation.validators.string;
/**
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

import static com.polydeucesys.validation.validators.string.HostValidator.NAME_RESOLVER_PROPERTY;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

/**
 *
 * Created by kevinmclellan on 22/09/2016.
 */
public class HostValidatorTest {

    public static class TestHostname extends AnnotatedTestClass<Host>{
        private String hostname = "";

        public void setHostname( String hostname ){
            this.hostname = hostname;
        }

        public TestHostname( String hostname ){
            this.hostname = hostname;
        }

        @Host()
        public String getHostname(){
            return hostname;
        }

        @Host(lookup=true)
        public String getHostnameWithLookup(){
            return hostname;
        }

        @Host(domain="*.testdomain.org")
        public String getHostnameWithDomain(){
            return hostname;
        }

        @Host(lookup=true, domain="*.testdomain.org")
        public String getHostnameWithLookupAndDomain(){
            return hostname;
        }

        @Host(whitelist={"myserver.mydomain.com", "myfailover.mydomain.com"})
        public String getHostnameWithWhitelist(){
            return hostname;
        }

        @Host(blacklist={"myprodserver.testdomain.org","myprodfailover.testdomain.org"})
        public String getHostnameWithBlacklist(){
            return hostname;
        }
    }

    @Test
    public void testSimpleHostname(){
        System.setProperty(NAME_RESOLVER_PROPERTY, MockResolver.class.getName());
        TestHostname t1 = new TestHostname("");
        HostValidator v = new HostValidator();
        // empty string never valid
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // simple hostname shoud be valid. DNS mock will also ensure fqdn gets tested
        MockResolver.addHost("myserver", "testdomain.org");
        MockResolver.addHost("my-server", "testdomain.org");
        MockResolver.addIpAddress("192.168.0.5", "myserver.testdomain.org");
        MockResolver.addIpAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "myprodfailover.testdomain.org");
        MockResolver.addHost("mybadserver", "evilcorp.com");
        t1.setHostname("myserver");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // test with port
        t1.setHostname("myserver:8080");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // test with bad port
        t1.setHostname("myserver:-8020");
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // test with -
        t1.setHostname("my-server.testdomain.org:9092");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid (whitelist)");

        }catch(ValidationException e){
        }
        // test ipv4
        t1.setHostname("192.168.0.5");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        // with no dns we have no domain in string form
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        // can't match whitelist to ip v 4 without lookup
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid (whitelist)");

        }catch(ValidationException e){
        }
        // test ipv6
        t1.setHostname("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        // with no dns we have no domain in string form
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        // can't match whitelist to ip v 4 without lookup
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid (whitelist)");

        }catch(ValidationException e){
        }
        // test numeric hostname
        t1.setHostname("8675309");
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertTrue(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // test invalid numeric
        t1.setHostname("192.168.0.321");
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostname()),
                t1.getMyAnnotation("getHostname", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithDomain()),
                t1.getMyAnnotation("getHostnameWithDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookup()),
                t1.getMyAnnotation("getHostnameWithLookup", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithLookupAndDomain()),
                t1.getMyAnnotation("getHostnameWithLookupAndDomain", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithWhitelist()),
                t1.getMyAnnotation("getHostnameWithWhitelist", Host.class)));
        assertFalse(v.doApply(v.convertValueToValidatingType(t1.getHostnameWithBlacklist()),
                t1.getMyAnnotation("getHostnameWithBlacklist", Host.class)));
        // check as ValidatableObject
        try{
            t1.validate();
            fail("Object should be invalid");
        }catch(ValidationException e){

        }
        // test bad format dots
        t1.setHostname("myserver.mydomain..org");
        // test bad char
        t1.setHostname("mys@rver.mydomain.org");
        // test trailing -
        t1.setHostname("my-server-.mydomain.org");
    }
}

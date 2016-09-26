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

import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Don't want to use real DNS. Just want to get back testable names for requests.
 * Created by kevinmclellan on 22/09/2016.
 */
public class MockResolver implements HostValidator.NameResolver {

    // not thread safe. not meant to be. test class only
    private static Map<String,String> dnsRegistry = new HashMap<String, String>();

    public static void addHost( String hostname, String domain ){
        String fqdn = hostname + "." + domain;
        dnsRegistry.put(hostname, fqdn);
        dnsRegistry.put(fqdn, fqdn);

    }

    public static void addIpAddress( String ip, String fqdn){
        dnsRegistry.put(ip, fqdn);
    }

    public String resolveHost(String host) throws UnknownHostException {
        String resolved = dnsRegistry.get(host);
        if(resolved == null) throw new UnknownHostException(host + " not found");
        return resolved;
    }
}

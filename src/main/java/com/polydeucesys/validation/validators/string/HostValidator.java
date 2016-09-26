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

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.Integer.parseInt;

/**
 * Validator for host critera. Checks host has valid format. Optionally
 * performs dns resolution, can use whitelist/blackist for validation,
 * can check domain is valid.
 * Created by kevinmclellan on 21/09/2016.
 */
public class HostValidator extends StringValidator<Host> {

    // For testing purposes offline, I don't want to have to use DNS to
    // resolve everything (I code on my commute and don't always have
    // network access). So, wrap up the DNS call with our own
    // interface and allow for a mock
    interface NameResolver{
        String resolveHost( String host ) throws UnknownHostException;
    }

    private static class DefaultResolver implements NameResolver{
        public String resolveHost(String host) throws UnknownHostException {
            InetAddress lookup = InetAddress.getByName(host);
            return lookup.getCanonicalHostName();
        }
    }


    private static final String MULTI_DOT_RE = ".*\\.\\.+.*";
    private static final String HAS_CHAR_RE = ".*[a-zA-Z]+.*";
    private static final String HOSTNAME_ELEMENT_RE = "^[a-zA-Z0-9][a-zA-Z0-9]*$|^[a-zA-Z0-9][a-zA-Z0-9\\-]+[a-zA-Z0-9]$";

    static final String NAME_RESOLVER_PROPERTY = "com.polydeucesys.validators.host.resolver";
    private final NameResolver resolver;

    public HostValidator(){
        String resolverClass = System.getProperty(NAME_RESOLVER_PROPERTY);
        if(resolverClass != null){
            try {
                resolver = (NameResolver)Class.forName(resolverClass).newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }else{
            resolver = new DefaultResolver();
        }
    }

    // name, dot.sep.name, name:port, dot.sep.name:port
    private boolean isValidFQDNHostname(String hostname){
        // at this point we have extracted ports and know
        // this is not ipv4 or ipv6
        // hostname is < 255
        // dot separated
        // <label> ::= <let-dig> [ [ <ldh-str> ] <let-dig> ]
        // hav to ssume all digit is ok
        boolean lenAndHasLetters = hostname.length() < 255 &&
                hostname.matches(HAS_CHAR_RE) &&
                !hostname.matches(MULTI_DOT_RE);
        boolean elementsValid = true;
        if(lenAndHasLetters) {
            String[] components = hostname.split("\\.");
            for (String element : components) {
                if(!elementsValid){
                    break;
                }else{
                    elementsValid = element.matches(HOSTNAME_ELEMENT_RE);
                }
            }
        }
        return lenAndHasLetters && elementsValid;
    }
    // We'll only allow the common w.x.y.z format
    private boolean isValidIpV4( String address ){
        String[] elements = address.split("\\.");
        boolean validByte = true;
        if(elements.length == 4){
            for(String element : elements){
                if(validByte){
                    try {
                        int b = Integer.parseInt(element);
                        validByte = (b >= 0 && b < 256);
                    }catch(NumberFormatException ne){
                        validByte = false;
                    }
                }
            }
        }
        return (elements.length == 4) && validByte;
    }

    private boolean isValidIpV6( String address ){
        //regex from Regular Expression Cookbook, 2nd Edition
        return address.matches("^(?:[a-fA-F0-9]{1,4}:){7}[a-fA-F0-9]{1,4}$");
    }

    boolean isInList(String item, String[] list){
        boolean isIn = false;
        for(int i = 0; i < list.length && !isIn;i++){
            String element = list[i];
            isIn = (item.equals(element)) ||
                    (element.startsWith("*") && item.endsWith(element.substring(1)));
        }
        return isIn;
    }

    private boolean isInWhitelist( String hostname, String[] whitelist){
        return isInList(hostname, whitelist);
    }

    private boolean isInBlacklist(String hostname, String[] blacklist ){
        return isInList(hostname, blacklist);
    }

    // hostname must be fqdn for this to work, so either this was specified
    // or the fqdn was determined from an IPAddress lookup
    private boolean isInDomain(String fqdn, String domain ){
        if(domain.startsWith("*.")){
            return fqdn.endsWith(domain.substring(1));
        }else{
            // strip up to first dot
            String hostDomain = fqdn.substring(fqdn.indexOf(".") + 1);
            return hostDomain.equals(domain);
        }
    }

    private boolean isHostShortName(String h ){
        // no dots, no *
        return h.matches(HOSTNAME_ELEMENT_RE);
    }

    private String[] validateList(String[] list, boolean lookupDns) throws ValidationException{
        String[] result = list;
        if(lookupDns){
            result = new String[ list.length];
            for(int i=0; i < list.length ; i++ ){
                String element = list[i];
                if(element.startsWith("*") || !isHostShortName(element)){
                    result[i] = element;
                }else{
                    try {
                        result[i] = resolver.resolveHost(element);
                    } catch (UnknownHostException e) {
                        throw new ValidationException("Unknown Host " + element, e);
                    }
                }
            }
        }
        return result;
    }

    // package level validation method to share with URL for host validation
    boolean isValidHost(String hostname, boolean lookupDns, String[] whitelist,
                        String blacklist[], String allowedDomain){
        String workingHostname = hostname;
        if(lookupDns){
            try {
                workingHostname =  resolver.resolveHost(hostname);
            } catch (UnknownHostException e) {
                return false;
            }
        }
        // options like whitelist, blacklist and domain
        boolean options = ((whitelist.length == 1 && whitelist[0].equals("")) || isInWhitelist(workingHostname, whitelist)) &&
                ((blacklist.length == 1 && blacklist[0].equals("")) || !isInBlacklist(workingHostname, blacklist)) &&
                (allowedDomain.equals("") || isInDomain(workingHostname, allowedDomain));
        return options &&
                (isValidIpV4(workingHostname) || isValidIpV6(workingHostname) || isValidFQDNHostname(workingHostname) ||
                (!lookupDns && workingHostname.matches(HOSTNAME_ELEMENT_RE)));
    }

    private String processPort(String hostAndPort){
        String host = hostAndPort;
        if(!isValidIpV6(hostAndPort)) {
            // if we have a :, then there is a port. Strip this, check it is > 0 && < 65535
            String[] check = hostAndPort.split(":");
            if (check.length == 2) {
                Short port = Short.parseShort(check[1]);
                if (port > 0 && port < Short.MAX_VALUE) {
                    host = check[0];
                } else {
                    host = "";
                }
            } else if (check.length > 2) {
                host = "";
            }
        }
        return host;
    }

    protected boolean doApply(String s, Host validationCriteria) {
        // check for port, strip port if required. Validate hostname
        String host = processPort(s);
        return host.length() > 0 && isValidHost(host, validationCriteria.lookup(), validationCriteria.whitelist(),
                validationCriteria.blacklist(),validationCriteria.domain());
    }
}

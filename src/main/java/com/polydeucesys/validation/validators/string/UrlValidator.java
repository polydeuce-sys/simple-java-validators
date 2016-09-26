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

import java.net.MalformedURLException;
import java.util.regex.Pattern;

/**
 * Simple URL validator. Just checks that Java thinks the URL is valid.
 * Could be any number of other issues with it when it attempts to connect
 * Created by kevinmclellan on 20/09/2016.
 */
public class UrlValidator extends StringValidator<Url> {
    private static final String URL_PATTERN = "";
    private final HostValidator hostValidator = new HostValidator();


    protected boolean doApply(String s, Url validationCriteria) {
        try {
            java.net.URL erl = new java.net.URL(s);
            boolean protocolValid = (validationCriteria.protocol().length == 1 && validationCriteria.protocol()[0].isEmpty()) ||
                    hostValidator.isInList(erl.getProtocol(), validationCriteria.protocol());
            boolean hostValid = hostValidator.isValidHost(erl.getHost(), validationCriteria.hostLookup(),
                    validationCriteria.hostWhitelist(), validationCriteria.hostBlacklist(),
                    validationCriteria.hostDomain());
            boolean portValid = (validationCriteria.portRange().length == 1 && validationCriteria.portRange()[0]== 0 ) ||
                    (validationCriteria.portRange()[0] <= erl.getPort() && validationCriteria.portRange()[1] >= erl.getPort());
            boolean pathValid = validationCriteria.allowedPath().isEmpty() ||
                    erl.getPath().matches(validationCriteria.allowedPath());
            boolean queryValid = validationCriteria.allowedQueryFormat().isEmpty() ||
                    erl.getQuery().matches(validationCriteria.allowedQueryFormat());


            return protocolValid && hostValid && portValid && pathValid && queryValid;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}

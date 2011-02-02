/*
 *  Nimble, an extensive application base for Grails
 *  Copyright (C) 2010 Bradley Beddoes
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package grails.plugins.nimble.auth

/**
 * Provides a generic WildcardPermission for Nimble applications to utilize so we can provide
 * additional functionality if required.
 *
 * @author Bradley Beddoes
 */
public class WildcardPermission extends org.apache.shiro.authz.permission.WildcardPermission {
    public WildcardPermission(String wildcardString)
    {
        super(wildcardString)
    }

    public WildcardPermission(String wildcardString, boolean caseSensitive)
    {
        super(wildcardString, caseSensitive)
    }
}
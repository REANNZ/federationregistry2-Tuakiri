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

import grails.plugins.nimble.core.*

/**
 * Provides generic, mostly UI related tags to the Nimble application
 *
 * @author Bradley Beddoes
 */
class NimbleHeaderTagLib {

    static namespace = "nh"

	// Provides js for core features
    def nimblecore = {attrs ->
        out << render(template: "/templates/header/" + grailsApplication.config.nimble.resources.jslibrary + "/nimblecore", contextPath: pluginContextPath, model:[nimblePath:pluginContextPath])
    }

	// Provides js for additional UI features
    def nimbleui = {attrs ->
        out << render(template: "/templates/header/" + grailsApplication.config.nimble.resources.jslibrary + "/nimbleui", contextPath: pluginContextPath, model:[nimblePath:pluginContextPath])
    }
}
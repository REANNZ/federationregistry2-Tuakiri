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
import grails.util.GrailsUtil

import org.apache.commons.lang.StringEscapeUtils

/**
 * Provides generic, mostly UI related tags to the Nimble application
 *
 * @author Bradley Beddoes
 */
class NimbleTagLib {

    static namespace = "n"

    def recaptchaService

    def baseJsResourcePath = {
      out << (grailsApplication.config.nimble.resources.usejsdev ? "dev" : "")
    }

    def baseCssResourcePath = {
      out << (grailsApplication.config.nimble.resources.usecssdev ? "dev" : "")
    }

    /**
     * Provides an inline output of the Grails application message in flash scope
     */
    def flashembed = {attrs, body ->
        out << render(template: "/templates/flashembed", contextPath: pluginContextPath, model:[nimblePath:pluginContextPath])
    }

    /**
     * Provides nimble session terminated message
     */
    def sessionterminated = {attrs, body ->
        out << render(template: "/templates/sessionterminated", contextPath: pluginContextPath)
    }  

    /**
     * provides markup to render grails error messages for beans
     */
    def errors = {attrs, body ->
        def bean = attrs['bean']
        if (bean)
        out << render(template: "/templates/errors", contextPath: pluginContextPath, model: [bean: bean])
        else
        out << render("Error: Details not supplied to generate error content")
    }

    /**
     * Provides markup to render a ReCaptcha instance. Supports the following attributes:
     *
     * theme - Can be one of 'red','white','blackglass','clean','custom'
     * lang  - Can be one of 'en','nl','fr','de','pt','ru','es','tr'
     * tabindex - Sets a tabindex for the ReCaptcha box
     * custom_theme_widget - Used when specifying a custom theme.
     */
    def recaptcha = {attrs ->
        def props = new Properties()
        attrs.each {
            if (attrs[it]) {
                props.setProperty(it, attrs[it])
            }
        }
        out << recaptchaService.createCaptcha(session, props)
    }

    /**
     * Renders body if captcha is currently required
     */
    def recaptcharequired = { attrs, body ->
        if (recaptchaService.enabled) {
            out << body()
        }
    }
 
    def javascript = { attrs ->
      out << "<script type=\"text/javascript\" src=\"" + resource(dir: pluginContextPath, file: baseJsResourcePath() +"/js/" + attrs.src ) + "\"></script>"
    }

    def css = { attrs ->
      out << """<link rel="stylesheet" href=\"""" + resource(dir: pluginContextPath, file: baseCssResourcePath() +"/css/" + attrs.src ) + "\"/>"
    }

	def button = { attrs -> 
		def type
		if(attrs.label && attrs.icon)
			type = 'ui-button-text-icon'
			else
				if(attrs.label)
					type = 'ui-button-text-only'
					else
						type = 'ui-button-icon-only'
		def id = ""
		if(attrs.id) 
			id = "id = ${attrs.id}"
	
		def onclick = ""
		if(attrs.onclick)
			onclick = "onClick= \"${attrs.onclick} return false;\" "
		
		def href = "href='#'"
		if(attrs.href)
			href = "href=\'${attrs.href}\'"
		
		def cssclass = ""
		if(attrs.class)
			cssclass = "class=\'${attrs.class}\'"
		
		if(attrs.plain) {
			out << "<a $href $onclick $id class='$cssclass'>${attrs.label.encodeAsHTML()}</a>"
		}
		else {
			out << "<a $href $onclick $id class='ui-button $type ui-widget ui-state-default ui-corner-all $cssclass'>"
			if(type.contains('icon'))
				out << "<span class='ui-button-icon-primary ui-icon ui-icon-${attrs.icon}'></span>"
			if(type.contains('text'))
				out << "<span class='ui-button-text'>${message(code:attrs.label).encodeAsHTML()}</span>"
			out << "</a>"
		}
	}
	
	def confirmaction = { attrs, body ->
		if(attrs.action == null || attrs.title == null || attrs.msg == null || attrs.accept == null || attrs.cancel == null)
			throwTagError("Confirm action tag requires action, title, msg, accept and cancel attributes")

		def btnAttrs = [:]
		btnAttrs.id = attrs.id
		btnAttrs.class = attrs.class
		btnAttrs.onclick = "confirmAction = function() { ${attrs.action} }; nimble.wasConfirmed('${StringEscapeUtils.escapeJavaScript(attrs.title.encodeAsHTML())}', '${StringEscapeUtils.escapeJavaScript(attrs.msg.encodeAsHTML())}', '${StringEscapeUtils.escapeJavaScript(attrs.accept.encodeAsHTML())}', '${StringEscapeUtils.escapeJavaScript(attrs.cancel.encodeAsHTML())}');"
		btnAttrs.label = attrs.label
		btnAttrs.icon = attrs.icon
		btnAttrs.plain = attrs.plain

		out << button(btnAttrs)
	}
	
}

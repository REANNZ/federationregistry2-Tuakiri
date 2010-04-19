package fedreg.workflow.engine

import grails.plugins.nimble.core.UserBase
import org.apache.shiro.SecurityUtils

class WorkflowEngineTagLib {

    static namespace = "workflow"
    
    def sortableColumn = { attrs ->
		def writer = out
		if(!attrs.property)
			throwTagError("Tag [sortableColumn] is missing required attribute [property]")

        if(!attrs.title && !attrs.titleKey) 
            throwTagError("Tag [sortableColumn] is missing required attribute [title] or [titleKey]")

        def property = attrs.remove("property") 
        def action = attrs.action ? attrs.remove("action") : (actionName ?: "list")

        def defaultOrder = attrs.remove("defaultOrder") 
        if(defaultOrder != "desc") 
            defaultOrder = "asc"

        // current sorting property and order 
        def sort = params.sort 
        def order = params.order

        // add sorting property and params to link params 
        def linkParams = [:] 
        if (params.id) 
            linkParams.put("id",params.id)
        
        if (attrs.params) 
            linkParams.putAll(attrs.remove("params")) 
        
        linkParams.sort = property

        // Pre & post href
        def preHref = (attrs.preHref) ? attrs.remove('preHref') : ''
        def postHref = (attrs.postHref) ? attrs.remove('postHref') : ''

        // determine and add sorting order for this column to link params 
        attrs.class = (attrs.class ? "${attrs.class} sortable" : "sortable") 
        if(property == sort) { 
            attrs.class = attrs.class + " sorted " + order 
            if(order == "asc") { 
                linkParams.order = "desc" 
            } else { 
                linkParams.order = "asc" 
            } 
        } else { 
            linkParams.order = defaultOrder
        }

        // determine column title 
        def title = attrs.remove("title") 
        def titleKey = attrs.remove("titleKey") 
        if(titleKey) { 
            if(!title) 
                title = titleKey
                    
            def messageSource = grailsAttributes.messageSource 
            def locale = RCU.getLocale(request) 
            title = messageSource.getMessage(titleKey, null, title, locale)
        }

        writer << "<th " 
        // process remaining attributes 
        attrs.each { k, v -> 
            writer << "${k}=\"${v.encodeAsHTML()}\" " 
        } 
        writer << "><a href=\"${preHref}${createLink(action: action, params: linkParams)}${postHref}\">${title}</a></th>"
    }
    
    def userinfo = { attr ->
        
        if (!attr.username)
            throwTagError("Tag [userinfo] is missing required attribute [username]")
        
        def user = UserBase.findByUsername(attr.username)
        if (user) {
            if (SecurityUtils.subject.hasRole("SYSTEM ADMINISTRATOR"))        
                out << link(controller: 'user', action: 'show', id: user.id) { "${user.profile.fullName} (${user.username})" }
            else
                out << "${user.profile.fullName} (${user.username})"
        } else {
            out << attr.username
        }
    }
    
    def userfullname = { attr ->
        
        if (!attr.username)
            throwTagError("Tag [userinfo] is missing required attribute [username]")
        
        def user = UserBase.findByUsername(attr.username)
        if (user) {
            if (SecurityUtils.subject.hasRole("SYSTEM ADMINISTRATOR"))        
                out << link(controller: 'user', action: 'show', id: user.id) { "${user.profile.fullName}" }
            else
                out << "${user.profile.fullName}"
        } else {
            out << attr.username
        }
    }
    
    
    def popupResources = {
        out << """
            <link rel="stylesheet" href="${resource(plugin: 'workflowEngine', dir: 'css', file: 'popup.css')}" type="text/css" media="screen" />
            <script src="${resource(plugin: 'workflowEngine', dir: 'js', file: 'jquery-1.2.6.min.js')}" type="text/javascript"></script>
            <script src="${resource(plugin: 'workflowEngine', dir: 'js', file: 'popup.js')}" type="text/javascript"></script>
            """
    }
    
    def taskMessage = { attr ->
        
        if (!attr.task)
            throwTagError("Tag [taskMessage] is missing required attribute [task]")
            
        if (!attr.preamble)
            attr.preamble = ""

        def task = attr.task
            
        if (task.message) {
            if (task.message != "") {
                
                out << """
                    &nbsp;&nbsp;<a href="javascript:loadPopup('task${task.id}');">
                    <img src="${resource(dir: 'images', file: 'note.png')}" border="0" alt="Message" style="vertical-align: middle;"></a>
                            
                    <div id="task${task.id}" class="popupMessage">  
                        <a href="javascript:disablePopup('task${task.id}');" class="popupMessageClose">x</a>  
                        <h1 class="popupMessage">Message</h1>
                        <p>
                            <div id="message" style="width: 400px;">
                                ${attr.preamble}
                                <center>
                                <textarea style="width: 380px; height: 300px;" readonly="true">${task.message}</textarea>
                                </center>
                            </div>
                        </p>
                    </div>
                    """
            }
        }
    }
    
    def confirmAction = { attr, body ->
    
        if (!attr.id)
            throwTagError("Tag [confirmAction] is missing required attribute [id]")

        if (!attr.uri)
            throwTagError("Tag [confirmAction] is missing required attribute [uri]")
        
        if (!attr.message)
            throwTagError("Tag [confirmAction] is missing required attribute [message]")
        
        out << """
            <a href="javascript:loadPopup('confirmAction${attr.id}')">${body()}</a>
            <div class="confirmAction" id="confirmAction${attr.id}">
                ${attr.message}
                <table style="border: 0; height: 100%;">
                    <tr>
                        <td style="text-align: center; vertical-align: middle;">
          	              <a href="javascript:confirmAction('${attr.uri}')">Yes</a>
                          <a href="javascript:disablePopup('confirmAction${attr.id}')">No</a>
                        </td>
                    </tr>
                </table>
                </center>
            </div>
            """
    }
}

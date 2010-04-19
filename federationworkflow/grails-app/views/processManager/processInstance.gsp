<html>
<head>
</title></title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.application}" />
<workflow:popupResources/>
</head>
<body>
<p>&nbsp;</p>
<g:if test="${process}">

<table style="width: 1000px; border: 0px;">
    <tr>
        <td>
        <h2>${process.name}</h2><br>


        <table style="width: 600px; border-collapse: collapse; border: 0;">
            <tr style="border: 1;">
                <th style="width: 100px; border-width: 1px; border-style: solid; border-color: #CCC;">ID</th>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b>${process.id}</b></td>
                <th style="width: 100px; border-width: 1px; border-style: solid; border-color: #CCC;">Process Version</th>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b>${process.definition.processVersion}</b></td>
            </tr>
            <tr>
                <g:set var="tasksCompleted" value="${(process.tasks.findAll { it.status == fedreg.workflow.engine.TaskStatus.COMPLETED }).size()}" />
                <th style="width: 100; border-width: 1px; border-style: solid; border-color: #CCC;">Tasks Completed</th>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b>${tasksCompleted}</b></td>
                
                <g:set var="tasksInProgress" value="${(process.tasks.findAll { it.status == fedreg.workflow.engine.TaskStatus.INPROGRESS }).size()}" />
                <th style="width: 100; border-width: 1px; border-style: solid; border-color: #CCC;">Still In Progress</th>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b>${tasksInProgress}</b></td>

            </tr>
            <tr>
                <th style="width: 100; border-width: 1px; border-style: solid; border-color: #CCC;">Status</th>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;">
                
                    <g:if test="${process.status == fedreg.workflow.engine.ProcessStatus.COMPLETED}"><img src="${resource(dir: 'images', file: 'ok.png')}" alt="Done" style="vertical-align: middle"></g:if>
                    <g:elseif test="${process.status == fedreg.workflow.engine.ProcessStatus.CANCELLED}"><img src="${resource(dir: 'images', file: 'cancel.png')}" alt="Cancelled" style="vertical-align: middle"></g:elseif>                    
                    <g:elseif test="${process.status == fedreg.workflow.engine.ProcessStatus.INPROGRESS}"><img src="${resource(dir: 'images', file: 'waiting.png')}" alt="Waiting" style="vertical-align: middle"></g:elseif>
                
                &nbsp;&nbsp;<b>${process.status}</b></td>
                
                <th style="width: 100; border-width: 1px; border-style: solid; border-color: #CCC;">Priority</th>

                <g:if test="${process.initiatedBy == authenticatedUser.username || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}">
                    <g:if test="${process.status == fedreg.workflow.engine.ProcessStatus.INPROGRESS}">
                        <td style="width: 200; border-width: 1px; border-style: solid; padding: 0px; border-spacing:0px;  border-color: #CCC;">
                        <g:form name="Priority" action="${params.action}" id="${params.id}">
                            &nbsp;<g:select name="priority" from="${fedreg.workflow.engine.ProcessPriority.collect{it}}" value="${process.priority}" onchange="submit();"/>
                        </g:form>
                        </td>
                    </g:if>
                    <g:else>
                        <td style="width: 200; border-width: 1px; border-style: solid;  border-color: #CCC;"><b>${process.priority}</b></td>
                    </g:else>
                </g:if>
                <g:else>
                <td style="width: 200; border-width: 1px; border-style: solid;  border-color: #CCC;"><b>${process.priority}</b></td>
                </g:else>
    
            </tr>
            <tr>

                <th style="width: 100; border-width: 1px; border-style: solid; border-color: #CCC;">Date Initiated</th>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b><g:formatDate format="EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateInitiated}"/></b></td>


                <th style="width: 100; border-width: 1px; border-style: solid; border-color: #CCC;">Initiated by</th>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b><workflow:userfullname username="${process.initiatedBy}"/></b></td>
            </tr>
            <tr>
                <th style="width: 100; border-width: 1px; border-style: solid; border-color: #CCC;">Date Completed</th>
                <g:if test="${process.dateCompleted}">
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b><g:formatDate format="EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateCompleted}"/></b></td>
                </g:if>
                <g:else>
                <td style="width: 200; border-width: 1px; border-style: solid; border-color: #CCC;"><b>n/a<b></td>
                </g:else>
                
                <td colspan="2"></td>    
            </tr>
            
        </table>
        
        <p>&nbsp;</p>
        <g:if test="${process.status == fedreg.workflow.engine.ProcessStatus.INPROGRESS}">
            <g:if test="${process.initiatedBy == authenticatedUser.username || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}">
                <workflow:confirmAction id="1" uri="${createLink(action: 'cancelProcess', id: process.id)}" message="Are you sure you want to cancel this process?">Cancel Process</workflow:confirmAction>
            </g:if>
        </g:if>
        
        <p>&nbsp;</p>
        <h3>Tasks associated with this process:</h3><br>
        <ul><ul>
        <table>
            <tr>
                <th style="width: 15px;"></th>
                <g:sortableColumn property="id" title="ID" style="width: 25px; text-align:center;"/>
                <g:sortableColumn property="name" title="Task"/>
                <g:sortableColumn property="status" title="Status" style="width: 80px;"/>
                <g:sortableColumn property="assignedTo" title="Assigned To" style="width: 100px;"/>
                <g:sortableColumn property="actionedBy" title="Actioned By" style="width: 120px;"/>
                <g:sortableColumn property="actionResult" title="Result" style="width: 100px;"/>
                <g:sortableColumn property="dateCompleted" title="Date Completed" style="width: 160px;"/>
            </tr>

            <g:set var="isOddNumber" value="${true}" />
            <g:each var="task" in="${process.tasks.sort{it.id}}">
            <g:if test="${isOddNumber}">
            <tr class="odd">
            <g:set var="isOddNumber" value="${false}" />
            </g:if>
            <g:else>
            <tr class="even">
            <g:set var="isOddNumber" value="${true}" />
            </g:else>
            
             
                <td style="vertical-align: middle;">
                    <g:if test="${task.status == fedreg.workflow.engine.TaskStatus.COMPLETED}"><img src="${resource(dir: 'images', file: 'ok.png')}" alt="Done"></g:if>
                    <g:elseif test="${task.status == fedreg.workflow.engine.TaskStatus.CANCELLED}"><img src="${resource(dir: 'images', file: 'cancel.png')}" alt="Cancelled"></g:elseif>
                    
                    <g:elseif test="${task.status == fedreg.workflow.engine.TaskStatus.PENDING}"><img src="${resource(dir: 'images', file: 'waiting.png')}" alt="Waiting"></g:elseif>

                    <g:elseif test="${task.status == fedreg.workflow.engine.TaskStatus.INPROGRESS}">
                        
                        <g:if test="${task.status == fedreg.workflow.engine.TaskStatus.INPROGRESS && !task.definition.automated && ((task.assignedTo == 'INITIATOR' && task.parentProcess.initiatedBy == authenticatedUser.username) || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' })}"><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}"></g:if>
                        
                        <g:if test="${task.parentProcess.priority == fedreg.workflow.engine.ProcessPriority.LOW}">
                            <img src="${resource(dir: 'images', file: 'task_low.png')}" border="0" alt="Low">
                        </g:if>
                        <g:elseif test="${task.parentProcess.priority == fedreg.workflow.engine.ProcessPriority.MEDIUM}">
                            <img src="${resource(dir: 'images', file: 'task_medium.png')}" border="0" alt="Medium">
                        </g:elseif>
                        <g:elseif test="${task.parentProcess.priority == fedreg.workflow.engine.ProcessPriority.HIGH}">
                            <img src="${resource(dir: 'images', file: 'task_high.png')}" border="0" alt="High">
                        </g:elseif>
                        <g:elseif test="${task.parentProcess.priority == fedreg.workflow.engine.ProcessPriority.CRITICAL}">
                            <img src="${resource(dir: 'images', file: 'task_critical.png')}" border="0" alt="Critical">
                        </g:elseif>

                        <g:if test="${task.status == fedreg.workflow.engine.TaskStatus.INPROGRESS && !task.definition.automated && authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}"></a></g:if>
                        
                    </g:elseif>
                </td>
            
                <td style="text-align:center; vertical-align: middle;">${task.id}</td>
                <td style="vertical-align: middle;">
                
                <g:if test="${task.status == fedreg.workflow.engine.TaskStatus.INPROGRESS && !task.definition.automated && ((task.assignedTo == 'INITIATOR' && task.parentProcess.initiatedBy == authenticatedUser.username) || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' })}"><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}">${task.name}</a></g:if>
                <g:else>${task.name}</g:else>
                <workflow:taskMessage task="${task}" preamble="This task was ${task.actionResult?.toUpperCase()} because:" />
                </td>
                
                <td style="vertical-align: middle;">${task.status}</td>

                <g:if test="${task.assignedTo}">
                <td style="vertical-align: middle;">${task.assignedTo}</td>
                </g:if>
                <g:else>
                <td style="vertical-align: middle;">n/a</td>
                </g:else>

                <g:if test="${task.actionedBy}">
                <td style="vertical-align: middle;"><workflow:userfullname username="${task.actionedBy}"/></td>
                </g:if>
                <g:else>
                <td style="vertical-align: middle;">n/a</td>
                </g:else>

                <g:if test="${task.actionResult}">
                <td style="vertical-align: middle;">${task.actionResult.toUpperCase()}</td>
                </g:if>
                <g:else>
                <td style="vertical-align: middle;">n/a</td>
                </g:else>

                <g:if test="${task.dateCompleted}">
                <td style="vertical-align: middle;"><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${task.dateCompleted}"/></td>
                </g:if>
                <g:else>
                <td style="vertical-align: middle;">n/a</td>
                </g:else>
            </tr>

        </g:each>
        </table>
        </ul>
        </ul>
        
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <h3>Messages:</h3><br>
        <ul><ul>
        <table>
            <tr>
                <td>
                    <g:if test="${process.messages}">
                    <g:each var="message" in="${process.messages.sort{ it.date}.reverse()}">
                    <img src="${resource(dir: 'images', file: 'comment.png')}" style="vertical-align:middle;"> <workflow:userfullname username="${message.postedBy}"/> at <g:formatDate format="h:mm:ssa, EEE dd MMM yyyy" date="${message.date}"/>
                    
                    <g:if test="${message.postedBy == authenticatedUser.username || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[ <a href="${createLink(action:'deleteMessage')}?processId=${process.id}&messageId=${message.id}"><img src="${resource(dir: 'images', file: 'comment_delete.png')}" border="0" style="vertical-align:middle;"> Delete</a> ]</g:if>
                    <p>&nbsp;</p>
                    <table style="border: 0px; border-collapse: collapse;">
                        <tr class="odd">
                            <td><br><pre><i>${message.message}</i></pre><br></td>
                        </tr>            
                    </table>       
                    <p>&nbsp;</p>
                    </g:each>
                    </g:if>
                    <g:else>
                    <i><pre>No messages.</pre></i>
                    </g:else>
                </td>
            </tr>
        </table>
        
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <b>Post Message:</b><br>
        <g:form action="postMessage" id="${params.id}">
        <g:textArea name="message" style="width:400px; height: 200px;"/><br>
        <table style="border: 0; width: 400px;">
            <tr>
                <td><g:submitButton name="post" value="Post Message"/></td>
                <td  style="vertical-align: middle;"><g:checkBox name="email" value="${true}"  style="vertical-align: middle;"/> Email message to associated parties</td>
            </tr>
        </table>
        </g:form>
        </ul></ul>
        
        </td>
    </tr>
</table>
</g:if>


</body>
</html>


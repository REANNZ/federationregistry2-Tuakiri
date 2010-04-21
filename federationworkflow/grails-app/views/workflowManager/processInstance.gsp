<html>
<head>
</title></title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.application}" />
<workflow:popupResources/>
</head>
<body>
<p>&nbsp;</p>
<g:if test="${process}">

<table >
    <tr>
        <td>
        <h2>${process.name}</h2><br>


        <table >
            <tr >
                <th >ID</th>
                <td ><b>${process.id}</b></td>
                <th >Process Version</th>
                <td ><b>${process.definition.processVersion}</b></td>
            </tr>
            <tr>
                <g:set var="tasksCompleted" value="${(process.tasks.findAll { it.status == fedreg.workflow.TaskStatus.COMPLETED }).size()}" />
                <th >Tasks Completed</th>
                <td ><b>${tasksCompleted}</b></td>
                
                <g:set var="tasksInProgress" value="${(process.tasks.findAll { it.status == fedreg.workflow.TaskStatus.INPROGRESS }).size()}" />
                <th >Still In Progress</th>
                <td ><b>${tasksInProgress}</b></td>

            </tr>
            <tr>
                <th >Status</th>
                <td >
                
                    <g:if test="${process.status == fedreg.workflow.ProcessStatus.COMPLETED}"><img src="${resource(dir: 'images', file: 'ok.png')}" alt="Done" ></g:if>
                    <g:elseif test="${process.status == fedreg.workflow.ProcessStatus.CANCELLED}"><img src="${resource(dir: 'images', file: 'cancel.png')}" alt="Cancelled" ></g:elseif>                    
                    <g:elseif test="${process.status == fedreg.workflow.ProcessStatus.INPROGRESS}"><img src="${resource(dir: 'images', file: 'waiting.png')}" alt="Waiting" ></g:elseif>
                
                &nbsp;&nbsp;<b>${process.status}</b></td>
                
                <th >Priority</th>

                <g:if test="${process.initiatedBy == authenticatedUser.username || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}">
                    <g:if test="${process.status == fedreg.workflow.ProcessStatus.INPROGRESS}">
                        <td >
                        <g:form name="Priority" action="${params.action}" id="${params.id}">
                            &nbsp;<g:select name="priority" from="${fedreg.workflow.ProcessPriority.collect{it}}" value="${process.priority}" onchange="submit();"/>
                        </g:form>
                        </td>
                    </g:if>
                    <g:else>
                        <td ><b>${process.priority}</b></td>
                    </g:else>
                </g:if>
                <g:else>
                <td ><b>${process.priority}</b></td>
                </g:else>
    
            </tr>
            <tr>

                <th >Date Initiated</th>
                <td ><b><g:formatDate format="EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateInitiated}"/></b></td>


                <th >Initiated by</th>
                <td ><b><workflow:userfullname username="${process.initiatedBy}"/></b></td>
            </tr>
            <tr>
                <th >Date Completed</th>
                <g:if test="${process.dateCompleted}">
                <td ><b><g:formatDate format="EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateCompleted}"/></b></td>
                </g:if>
                <g:else>
                <td ><b>n/a<b></td>
                </g:else>
                
                <td colspan="2"></td>    
            </tr>
            
        </table>
        
        <p>&nbsp;</p>
        <g:if test="${process.status == fedreg.workflow.ProcessStatus.INPROGRESS}">
            <g:if test="${process.initiatedBy == authenticatedUser.username || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}">
                <workflow:confirmAction id="1" uri="${createLink(action: 'cancelProcess', id: process.id)}" message="Are you sure you want to cancel this process?">Cancel Process</workflow:confirmAction>
            </g:if>
        </g:if>
        
        <p>&nbsp;</p>
        <h3>Tasks associated with this process:</h3><br>
        <ul><ul>
        <table>
            <tr>
                <th ></th>
                <g:sortableColumn property="id" title="ID" />
                <g:sortableColumn property="name" title="Task"/>
                <g:sortableColumn property="status" title="Status" />
                <g:sortableColumn property="assignedTo" title="Assigned To" />
                <g:sortableColumn property="actionedBy" title="Actioned By" />
                <g:sortableColumn property="actionResult" title="Result" />
                <g:sortableColumn property="dateCompleted" title="Date Completed" />
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
            
             
                <td >
                    <g:if test="${task.status == fedreg.workflow.TaskStatus.COMPLETED}"><img src="${resource(dir: 'images', file: 'ok.png')}" alt="Done"></g:if>
                    <g:elseif test="${task.status == fedreg.workflow.TaskStatus.CANCELLED}"><img src="${resource(dir: 'images', file: 'cancel.png')}" alt="Cancelled"></g:elseif>
                    
                    <g:elseif test="${task.status == fedreg.workflow.TaskStatus.PENDING}"><img src="${resource(dir: 'images', file: 'waiting.png')}" alt="Waiting"></g:elseif>

                    <g:elseif test="${task.status == fedreg.workflow.TaskStatus.INPROGRESS}">
                        
                        <g:if test="${task.status == fedreg.workflow.TaskStatus.INPROGRESS && !task.definition.automated && ((task.assignedTo == 'INITIATOR' && task.parentProcess.initiatedBy == authenticatedUser.username) || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' })}"><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}"></g:if>
                        
                        <g:if test="${task.parentProcess.priority == fedreg.workflow.ProcessPriority.LOW}">
                            <img src="${resource(dir: 'images', file: 'task_low.png')}" border="0" alt="Low">
                        </g:if>
                        <g:elseif test="${task.parentProcess.priority == fedreg.workflow.ProcessPriority.MEDIUM}">
                            <img src="${resource(dir: 'images', file: 'task_medium.png')}" border="0" alt="Medium">
                        </g:elseif>
                        <g:elseif test="${task.parentProcess.priority == fedreg.workflow.ProcessPriority.HIGH}">
                            <img src="${resource(dir: 'images', file: 'task_high.png')}" border="0" alt="High">
                        </g:elseif>
                        <g:elseif test="${task.parentProcess.priority == fedreg.workflow.ProcessPriority.CRITICAL}">
                            <img src="${resource(dir: 'images', file: 'task_critical.png')}" border="0" alt="Critical">
                        </g:elseif>

                        <g:if test="${task.status == fedreg.workflow.TaskStatus.INPROGRESS && !task.definition.automated && authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}"></a></g:if>
                        
                    </g:elseif>
                </td>
            
                <td >${task.id}</td>
                <td >
                
                <g:if test="${task.status == fedreg.workflow.TaskStatus.INPROGRESS && !task.definition.automated && ((task.assignedTo == 'INITIATOR' && task.parentProcess.initiatedBy == authenticatedUser.username) || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' })}"><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}">${task.name}</a></g:if>
                <g:else>${task.name}</g:else>
                <workflow:taskMessage task="${task}" preamble="This task was ${task.actionResult?.toUpperCase()} because:" />
                </td>
                
                <td >${task.status}</td>

                <g:if test="${task.assignedTo}">
                <td >${task.assignedTo}</td>
                </g:if>
                <g:else>
                <td >n/a</td>
                </g:else>

                <g:if test="${task.actionedBy}">
                <td ><workflow:userfullname username="${task.actionedBy}"/></td>
                </g:if>
                <g:else>
                <td >n/a</td>
                </g:else>

                <g:if test="${task.actionResult}">
                <td >${task.actionResult.toUpperCase()}</td>
                </g:if>
                <g:else>
                <td >n/a</td>
                </g:else>

                <g:if test="${task.dateCompleted}">
                <td ><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${task.dateCompleted}"/></td>
                </g:if>
                <g:else>
                <td >n/a</td>
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
                    <img src="${resource(dir: 'images', file: 'comment.png')}" > <workflow:userfullname username="${message.postedBy}"/> at <g:formatDate format="h:mm:ssa, EEE dd MMM yyyy" date="${message.date}"/>
                    
                    <g:if test="${message.postedBy == authenticatedUser.username || authenticatedUser.roles.find { it.name == 'SYSTEM ADMINISTRATOR' }}">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[ <a href="${createLink(action:'deleteMessage')}?processId=${process.id}&messageId=${message.id}"><img src="${resource(dir: 'images', file: 'comment_delete.png')}" border="0" > Delete</a> ]</g:if>
                    <p>&nbsp;</p>
                    <table >
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
        <g:textArea name="message" /><br>
        <table >
            <tr>
                <td><g:submitButton name="post" value="Post Message"/></td>
                <td  ><g:checkBox name="email" value="${true}"  /> Email message to associated parties</td>
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


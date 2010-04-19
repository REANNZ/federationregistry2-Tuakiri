<workflow:popupResources/>

<script type="text/javascript">

    function refreshTasks(url) {
    
        // code for IE7+, Firefox, Chrome, Opera, Safari
        if (window.XMLHttpRequest) { 
            xmlhttp=new XMLHttpRequest();
            
        // code for IE6, IE5
        } else { 
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        
        xmlhttp.onreadystatechange=function() {
            if(xmlhttp.readyState==4) {
                document.getElementById("tasks").innerHTML=xmlhttp.responseText
            }
        }
        xmlhttp.open("GET",url,true);
        xmlhttp.send(null);
    }
    
</script>

<div id="tasks">
<h2>Tasks to be completed by me</h2><br>

<g:if test="${flash.message}">
<div class="message">${flash.message}</div>
</g:if>

<table>
<tr>
    <th style="width: 15px;"></th>
    <workflow:sortableColumn property="task" title="Task" preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="assignedTo" title="Assigned To" style="width: 160px;" preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="messages" title="Messages" style="width: 70px; text-align: center;" preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="parentProcess.initiatedBy" title="Initiated by" style="width: 130px;" preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="parentProcess.priority" title="Priority" style="width: 60px; text-align: center;"  preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="parentProcess.dateInitiated" title="Date Initiated" style="width: 150px;" preHref="javascript:refreshTasks('" postHref="')"/>

</tr>

<g:set var="isOddNumber" value="${true}" />
<g:if test="${myTasks}">
<g:each var="task" in="${myTasks}">
<g:if test="${isOddNumber}">
<tr class="odd" style="${(task.definition.colour) ? 'background-color: '+task.definition.colour+';' : ''}">
<g:set var="isOddNumber" value="${false}" />
</g:if>
<g:else>
<tr class="even" style="${(task.definition.colour) ? 'background-color: '+task.definition.colour+';' : ''}">
<g:set var="isOddNumber" value="${true}" />
</g:else>
    <td style="vertical-align: middle;"><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}">
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
    </a></td>
    <td style="vertical-align: middle;"><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}">${task.name}</a> for <a href="${createLink(controller: 'processManager', action: 'processInstance', id: task.parentProcess.id)}">${task.parentProcess.name}</a>
    
    <g:each in="${task.calledBy}">
    <workflow:taskMessage task="${it}" preamble="The previous '${it.name}' task was ${it.actionResult?.toUpperCase()} because:"/>
    </g:each>
    </td>
    
    <td style="vertical-align: middle;">${task.assignedTo}</td>

    <td style="vertical-align: middle; text-align: center;">${task.parentProcess.messages.size()}</td>

    <td style="vertical-align: middle;"><workflow:userfullname username="${task.parentProcess.initiatedBy}"/></td>
    <td style="vertical-align: middle; text-align: center;">${task.parentProcess.priority}</td>
    <td style="vertical-align: middle;"><g:formatDate format="EEE dd/MMM/yyyy, h:mm:ssa" date="${task.parentProcess.dateInitiated}"/></td>

</tr>

</g:each>
</g:if>
<g:else>
<tr>
    <td colspan="8"><b>Nil.</b></td>
</tr>
</g:else>
</table>
</div>

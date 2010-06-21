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
<h3><g:message code="fedreg.workflow.views.tasks.heading"/></h3>

<g:if test="${flash.message}">
<div class="message">${flash.message}</div>
</g:if>

<g:if test="${myTasks}">
<table>
<tr>
    <th ></th>
    <workflow:sortableColumn property="task" title="Task" preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="assignedTo" title="Assigned To"  preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="messages" title="Messages"  preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="parentProcess.initiatedBy" title="Initiated by"  preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="parentProcess.priority" title="Priority"   preHref="javascript:refreshTasks('" postHref="')"/>
    <workflow:sortableColumn property="parentProcess.dateInitiated" title="Date Initiated"  preHref="javascript:refreshTasks('" postHref="')"/>

</tr>

<g:set var="isOddNumber" value="${true}" />

<g:each var="task" in="${myTasks}">
<g:if test="${isOddNumber}">
<tr class="odd" >
<g:set var="isOddNumber" value="${false}" />
</g:if>
<g:else>
<tr class="even" >
<g:set var="isOddNumber" value="${true}" />
</g:else>
    <td ><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}">
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
    </a></td>
    <td ><a href="${createLink(controller: 'processManager', action:'initiate', id: task.id)}">${task.name}</a> for <a href="${createLink(controller: 'processManager', action: 'processInstance', id: task.parentProcess.id)}">${task.parentProcess.name}</a>
    
    <g:each in="${task.calledBy}">
    <workflow:taskMessage task="${it}" preamble="The previous '${it.name}' task was ${it.actionResult?.toUpperCase()} because:"/>
    </g:each>
    </td>
    
    <td >${task.assignedTo}</td>

    <td >${task.parentProcess.messages.size()}</td>

    <td ><workflow:userfullname username="${task.parentProcess.initiatedBy}"/></td>
    <td >${task.parentProcess.priority}</td>
    <td ><g:formatDate format="EEE dd/MMM/yyyy, h:mm:ssa" date="${task.parentProcess.dateInitiated}"/></td>

</tr>

</g:each>
</table>

</g:if>
<g:else>
	<p><g:message code="fedreg.workflow.label.notasks" />
</g:else>

</div>

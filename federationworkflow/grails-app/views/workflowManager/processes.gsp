<%@ page import="fedreg.workflow.ProcessStatus" %>

<script type="text/javascript">

    function refreshProcesses(url) {
    
        // code for IE7+, Firefox, Chrome, Opera, Safari
        if (window.XMLHttpRequest) { 
            xmlhttp=new XMLHttpRequest();
            
        // code for IE6, IE5
        } else { 
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        
        xmlhttp.onreadystatechange=function() {
            if(xmlhttp.readyState==4) {
                document.getElementById("processes").innerHTML=xmlhttp.responseText
            }
        }
        xmlhttp.open("GET",url,true);
        xmlhttp.send(null);
    }
    
</script>


<div id="processes">
	<h3><g:message code="fedreg.workflow.views.processes.heading"/></h3>
	<g:if test="${myProcesses}">

		<table>
		<tr>
		    <th ></th>
		    <workflow:sortableColumn property="name" title="Process" preHref="javascript:refreshProcesses('" postHref="')"/>
		    <workflow:sortableColumn property="messages" title="Messages"  preHref="javascript:refreshProcesses('" postHref="')"/>
		    <th >Tasks Completed</th>
		    <workflow:sortableColumn property="priority" title="Priority"  preHref="javascript:refreshProcesses('" postHref="')"/>
		    <workflow:sortableColumn property="dateInitiated" title="Date Initiated"  preHref="javascript:refreshProcesses('" postHref="')"/>
		</tr>


		<g:set var="isOddNumber" value="${true}" />
			<g:each var="process" in="${myProcesses}">
			<g:if test="${isOddNumber}">
			<tr class="odd">
			<g:set var="isOddNumber" value="${false}" />
			</g:if>
			<g:else>
			<tr class="even">
			<g:set var="isOddNumber" value="${true}" />
			</g:else>

			    <g:if test="${process.status == ProcessStatus.COMPLETED}">
			        <td ><a href="javascript:refreshProcesses('${createLink(controller: 'processManager', action: 'myProcesses')}?clear=clear&processId=${process.id}')"><img src="${resource(dir: 'images', file: 'ok.png')}" border="0" alt="Clear"></a></td>
			    </g:if>
			    <g:elseif test="${process.status == ProcessStatus.CANCELLED}">
			        <td ><a href="javascript:refreshProcesses('${createLink(controller: 'processManager', action: 'myProcesses')}?clear=clear&processId=${process.id}')"><img src="${resource(dir: 'images', file: 'cancel.png')}" border="0" alt="Clear"></a></td>
			    </g:elseif>
			    <g:else>
			        <td ><img src="${resource(dir: 'images', file: 'waiting.png')}" border="0" alt="Waiting"></td>
			    </g:else>

			    <td ><a href="${createLink(controller: 'processManager', action: 'processInstance', id: process.id)}">${process.name}</a></td>
    
			    <td >${process.messages.size()}</td>

			    <g:set var="tasksCompleted" value="${(process.tasks.findAll { it.status == fedreg.workflow.TaskStatus.COMPLETED }).size()}" />
			    <td >${tasksCompleted}</td>
    
			    <td >${process.priority}</td>       
    
			    <td ><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateInitiated}"/></td>
			</tr>
			</g:each>
		</table>

	</g:if>
	<g:else>
		<p><g:message code="fedreg.workflow.label.noprocesses" />
	</g:else>

</div>

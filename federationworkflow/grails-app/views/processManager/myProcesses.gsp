<%@ page import="fedreg.workflow.engine.ProcessStatus" %>

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
<h2>My process requests currently in progress</h2><br>
<table>
<tr>
    <th style="width: 15px;"></th>
    <workflow:sortableColumn property="name" title="Process" preHref="javascript:refreshProcesses('" postHref="')"/>
    <workflow:sortableColumn property="messages" title="Messages" style="width: 70px; text-align: center;" preHref="javascript:refreshProcesses('" postHref="')"/>
    <th style="width: 90px; text-align:center; color: #333; font-size: 10px; text-decoration: none;">Tasks Completed</th>
    <workflow:sortableColumn property="priority" title="Priority" style="width: 80px; text-align: center;" preHref="javascript:refreshProcesses('" postHref="')"/>
    <workflow:sortableColumn property="dateInitiated" title="Date Initiated" style="width: 200px;" preHref="javascript:refreshProcesses('" postHref="')"/>
</tr>

<g:if test="${myProcesses}">
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
        <td style="text-align:center"><a href="javascript:refreshProcesses('${createLink(controller: 'processManager', action: 'myProcesses')}?clear=clear&processId=${process.id}')"><img src="${resource(dir: 'images', file: 'ok.png')}" border="0" alt="Clear"></a></td>
    </g:if>
    <g:elseif test="${process.status == ProcessStatus.CANCELLED}">
        <td style="text-align:center"><a href="javascript:refreshProcesses('${createLink(controller: 'processManager', action: 'myProcesses')}?clear=clear&processId=${process.id}')"><img src="${resource(dir: 'images', file: 'cancel.png')}" border="0" alt="Clear"></a></td>
    </g:elseif>
    <g:else>
        <td style="text-align:center"><img src="${resource(dir: 'images', file: 'waiting.png')}" border="0" alt="Waiting"></td>
    </g:else>

    <td style="vertical-align: middle;"><a href="${createLink(controller: 'processManager', action: 'processInstance', id: process.id)}">${process.name}</a></td>
    
    <td style="vertical-align: middle; text-align: center;">${process.messages.size()}</td>

    <g:set var="tasksCompleted" value="${(process.tasks.findAll { it.status == fedreg.workflow.engine.TaskStatus.COMPLETED }).size()}" />
    <td style="text-align:center; vertical-align: middle;">${tasksCompleted}</td>
    
    <td style="vertical-align: middle; text-align: center;">${process.priority}</td>       
    
    <td style="vertical-align: middle;"><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateInitiated}"/></td>
</tr>
</g:each>
</g:if>

<g:else>
<tr>
    <td colspan="6"><b>Nil.</b></td>
</tr>
</g:else>

</table>
</div>

<h2>Results</h2><br>
<table>
<tr>
    <th style="width: 15px;"></th>
    <g:sortableColumn property="id" title="ID" style="width: 15px; text-align:center" params="${params}"/>
    <g:sortableColumn property="process" title="Process" params="${params}"/>
    <g:sortableColumn property="version" title="Version" style="width: 30px; text-align:center" params="${params}"/>
    <g:sortableColumn property="initiatedBy" title="Initiated by" style="width: 130px; text-align:left" params="${params}"/>
    <th style="width: 90px; text-align:center; color: #333; font-size: 10px; text-decoration: none;">Tasks Completed</th>
    <g:sortableColumn property="dateInitiated" title="Date Initiated" style="width: 150px;" params="${params}"/>
    <g:sortableColumn property="dateCompleted" title="Date Complete" style="width: 150px;" params="${params}"/>
</tr>

<g:if test="${processes}">
<g:set var="isOddNumber" value="${true}" />
<g:each var="process" in="${processes}">
<g:if test="${isOddNumber}">
<tr class="odd">
<g:set var="isOddNumber" value="${false}" />
</g:if>
<g:else>
<tr class="even">
<g:set var="isOddNumber" value="${true}" />
</g:else>

    <g:if test="${process.status == fedreg.workflow.engine.ProcessStatus.COMPLETED}">
    <td style="text-align:center"><img src="${resource(dir: 'images', file: 'ok.png')}" border="0" alt="Clear"></td>
    </g:if>
    
    <g:elseif test="${process.status == fedreg.workflow.engine.ProcessStatus.CANCELLED}">
    <td style="text-align:center"><img src="${resource(dir: 'images', file: 'cancel.png')}" border="0" alt="Clear"></td>
    </g:elseif>
    
    <g:else>
    <td style="text-align:center"><img src="${resource(dir: 'images', file: 'waiting.png')}" border="0" alt="Waiting"></td>
    </g:else>

    <td style="text-align:center; vertical-align: middle;">${process.id}</td>

    <td style="vertical-align: middle;"><a href="${createLink(controller: 'processManager', action: 'processInstance', id: process.id)}" target="_top">${process.name}</a></td>
    
    <td style="text-align: center; vertical-align: middle;">${process.definition.processVersion}</td>
    <td style="text-align: left; vertical-align: middle;"><workflow:userfullname username="${process.initiatedBy}"/></td>
    
    <g:set var="tasksCompleted" value="${(process.tasks.findAll { it.status == fedreg.workflow.engine.TaskStatus.COMPLETED }).size()}" />
    <td style="text-align:center; vertical-align: middle;">${tasksCompleted}</td>
    
    <td style="vertical-align: middle;"><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateInitiated}"/></td>
    
   
    <g:if test="${process.dateCompleted}">
    <td style="vertical-align: middle;"><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateCompleted}"/></td>
    </g:if>
    <g:else>
    <td style="vertical-align: middle;">Still in progress</td>
    </g:else>
    

</tr>
</g:each>
</g:if>

<g:else>
<tr>
    <td colspan="7"><b>Nil.</b></td>
</tr>
</g:else>

</table>

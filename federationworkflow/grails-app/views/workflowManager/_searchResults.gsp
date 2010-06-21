<h2>Results</h2><br>
<table>
<tr>
    <th ></th>
    <g:sortableColumn property="id" title="ID"  params="${params}"/>
    <g:sortableColumn property="process" title="Process" params="${params}"/>
    <g:sortableColumn property="version" title="Version"  params="${params}"/>
    <g:sortableColumn property="initiatedBy" title="Initiated by"  params="${params}"/>
    <th >Tasks Completed</th>
    <g:sortableColumn property="dateInitiated" title="Date Initiated"  params="${params}"/>
    <g:sortableColumn property="dateCompleted" title="Date Complete"  params="${params}"/>
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

    <g:if test="${process.status == fedreg.workflow.ProcessStatus.COMPLETED}">
    <td ><img src="${resource(dir: 'images', file: 'ok.png')}" border="0" alt="Clear"></td>
    </g:if>
    
    <g:elseif test="${process.status == fedreg.workflow.ProcessStatus.CANCELLED}">
    <td ><img src="${resource(dir: 'images', file: 'cancel.png')}" border="0" alt="Clear"></td>
    </g:elseif>
    
    <g:else>
    <td ><img src="${resource(dir: 'images', file: 'waiting.png')}" border="0" alt="Waiting"></td>
    </g:else>

    <td >${process.id}</td>

    <td ><a href="${createLink(controller: 'processManager', action: 'processInstance', id: process.id)}" target="_top">${process.name}</a></td>
    
    <td >${process.definition.processVersion}</td>
    <td ><workflow:userfullname username="${process.initiatedBy}"/></td>
    
    <g:set var="tasksCompleted" value="${(process.tasks.findAll { it.status == fedreg.workflow.TaskStatus.COMPLETED }).size()}" />
    <td >${tasksCompleted}</td>
    
    <td ><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateInitiated}"/></td>
    
   
    <g:if test="${process.dateCompleted}">
    <td ><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${process.dateCompleted}"/></td>
    </g:if>
    <g:else>
    <td >Still in progress</td>
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

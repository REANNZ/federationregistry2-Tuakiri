<div id="monitors">
  <g:if test="${roleDescriptor.monitors.size() > 0}">
  	<table class="table borderless">
  		<thead>
  			<tr>
  				<th><g:message code="label.type"/></th>
  				<th><g:message code="label.url"/></th>
          <th><g:message code="label.node"/></th>
  				<th><g:message code="label.interval"/></th>
  				<th/>
  			</tr>	
  		</thead>
  		<tbody>
  			<g:each in="${roleDescriptor.monitors.sort{it.id}}" var="m">
  			<tr>
  				<td>${m.type.name}</td>
  				<td>${m.url}</td>
          <td>${m.node}</td>
  				<td>
  					<g:if test="${m.checkPeriod == 0}">
  						<g:message code="label.externallydefined"/>
  					</g:if>
  					<g:else>
  						${m.checkPeriod} <g:message code="label.seconds"/>
  					</g:else>
  				</td>
  				<td>
  					<fr:hasPermission target="descriptor:${roleDescriptor.id}:monitor:delete">
              <a class="confirm-delete-monitor btn btn-danger" data-monitorid="${m.id}"><g:message code="label.delete"/></a>
  					</fr:hasPermission>
  				</td>
  			</tr>
  			</g:each>
  		</tbody>
  	</table>
  </g:if>
  <g:else>
  	<p class="alert alert-message"><g:message code="fedreg.templates.monitor.none"/></p>
  </g:else>
</div>
<div id="monitors">
  <g:if test="${roleDescriptor.monitors.size() > 0}">
  	<table class="table borderless">
  		<thead>
  			<tr>
  				<th><g:message encodeAs="HTML" code="label.type"/></th>
  				<th><g:message encodeAs="HTML" code="label.url"/></th>
          <th><g:message encodeAs="HTML" code="label.node"/></th>
  				<th><g:message encodeAs="HTML" code="label.interval"/></th>
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
  						<g:message encodeAs="HTML" code="label.externallydefined"/>
  					</g:if>
  					<g:else>
  						${m.checkPeriod} <g:message encodeAs="HTML" code="label.seconds"/>
  					</g:else>
  				</td>
  				<td>
  					<fr:hasPermission target="federation:management:descriptor:${roleDescriptor.id}:monitor:delete">
              <a class="confirm-delete-monitor btn btn-mini" data-monitorid="${m.id}"><g:message encodeAs="HTML" code="label.delete"/></a>
  					</fr:hasPermission>
  				</td>
  			</tr>
  			</g:each>
  		</tbody>
  	</table>
  </g:if>
  <g:else>
  	<p class="alert alert-message"><g:message encodeAs="HTML" code="templates.fr.monitor.none"/></p>
  </g:else>
</div>

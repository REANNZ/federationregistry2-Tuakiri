<%@page import="fedreg.core.SamlURI" %>
<%@page import="fedreg.core.SamlURIType" %>

<script type="text/javascript">
  <njs:flashgrowl/>

	$(function() {		
		$(function(){
			$('.tip').tipTip({maxWidth: "auto", edgeOffset: 10, maxWidth:'200px', defaultPosition:"top"});
		});
	});
</script>

<div class="overlaycontent">
	<h4><g:message code="fedreg.templates.endpoints.edit.heading"/></h4>
	<form id="endpoint-edit-${endpoint.id}">
		<g:hiddenField name="id" value="${endpoint.id}" />
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.binding"/><th>
					<td>
						<g:select name="binding" from="${SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)}" optionKey="id" optionValue="uri" value="${endpoint.binding.id}"/>
						<fr:tooltip code='fedreg.help.endpoint.binding' />
					</td>
				</tr>
				<tr>
					<th><g:message code="label.location"/><th>
					<td>
						<input name="location" type="text" class="required url" size="60" value="${endpoint.location.uri}"/>
						<fr:tooltip code='fedreg.help.endpoint.location' />
					</td>
				</tr>
			</tbody>
		</table>
		<n:button onclick="if(\$('#endpoint-edit-${endpoint.id}').valid()) fedreg.endpoint_update('${endpoint.id}', '${endpointType}', '${containerID}');" label="${message(code:'label.update')}" class="update-button"/>
		<n:button onclick="fedreg.endpoint_list('${endpointType}', '${containerID}');" label="${message(code:'label.cancel')}" class="close-button"/>
	</form>
</div>
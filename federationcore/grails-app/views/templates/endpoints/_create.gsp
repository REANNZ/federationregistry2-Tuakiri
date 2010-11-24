<n:hasPermission target="descriptor:${descriptor.id}:endpoint:create">

	<%@page import="fedreg.core.SamlURI" %>
	<%@page import="fedreg.core.SamlURIType" %>

	<script type="text/javascript">
		$(function() {
			$("#new${endpointType}").hide();
			$("#new${endpointType}data").validate();
		});
	</script>

	<hr>

	<div id="add${endpointType}" class="searcharea">
		<n:button onclick="\$('#add${endpointType}').fadeOut(); \$('#new${endpointType}').fadeIn();" label="${message(code:'label.addendpoint')}" icon="plus"/>
	</div>
	
	<div id="new${endpointType}" class="searcharea">
		<h3><g:message code="fedreg.templates.endpoints.add.heading"/></h3>
		<form id="new${endpointType}data">
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.binding"/><th>
					<td>
						<g:select name="binding" from="${SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)}" optionKey="id" optionValue="uri"/>
					</td>
				</tr>
				<tr>
					<th><g:message code="label.location"/><th>
					<td>
						<input name="location" type="text" class="required url" size="60"/>
					</td>
				</tr>
				<g:if test="${resloc}">
				<tr>
					<th><g:message code="label.responselocation"/><th>
					<td>
						<input name="responselocation" type="text" class="easyinput" size="60"/>
					</td>
				</tr>
				</g:if>
				<tr>
					<th><g:message code="label.active"/><th>
					<td>
						<g:checkBox name="active" value="${true}" />
					</td>
				</tr>
			</tbody>
		</table>
		<n:button onclick="if(\$('#new${endpointType}data').valid()) fedreg.endpoint_create('${endpointType}', '${containerID}');" label="${message(code:'label.add')}" icon="plus"/>
		<n:button onclick="\$('#new${endpointType}').fadeOut(); \$('#add${endpointType}').fadeIn();" label="${message(code:'label.close')}" icon="close"/>
		</form>
	</div>
	
</n:hasPermission>
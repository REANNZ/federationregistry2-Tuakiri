<%@page import="fedreg.core.SamlURI" %>
<%@page import="fedreg.core.SamlURIType" %>

<script type="text/javascript">
	$(function() {
		$("#new${endpointType}").hide();
		$("#new${endpointType}data").validate();
	
		$("#add${endpointType}link").click(function() {
			$("#add${endpointType}").hide();
			$("#new${endpointType}").show('slide');
			$("#new${endpointType}[name]").focus();
		});
		
		$("#cancel${endpointType}link").click(function() {
			$("#new${endpointType}").hide();
			$("#add${endpointType}").show('slide');
		});
	});
</script>

<div id="add${endpointType}" class="searcharea">
	<fr:button id="add${endpointType}link" label="${message(code:'fedreg.label.addendpoint')}" icon="plus"/>
</div>
	
<div id="new${endpointType}" class="searcharea">
	<h3><g:message code="fedreg.template.endpoints.add.heading"/></h3>
	<form id="new${endpointType}data">
	<table>
		<tbody>
			<tr>
				<th><g:message code="fedreg.label.binding"/><th>
				<td>
					<g:select name="binding" from="${SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)}" optionKey="id" optionValue="uri"/>
				</td>
			</tr>
			<tr>
				<th><g:message code="fedreg.label.location"/><th>
				<td>
					<input name="location" type="text" class="required url" size="60"/>
				</td>
			</tr>
			<g:if test="${resloc}">
			<tr>
				<th><g:message code="fedreg.label.responselocation"/><th>
				<td>
					<input name="responselocation" type="text" class="easyinput" size="60"/>
				</td>
			</tr>
			</g:if>
			<tr>
				<th><g:message code="fedreg.label.active"/><th>
				<td>
					<g:checkBox name="active" value="${true}" />
				</td>
			</tr>
		</tbody>
	</table>
	<fr:button id="create${endpointType}link" onclick="if(\$('#new${endpointType}data').valid()) fedreg.endpoint_create('${endpointType}', '${containerID}');" label="${message(code:'fedreg.label.add')}" icon="plus"/>
	<fr:button id="cancel${endpointType}link" label="${message(code:'fedreg.label.close')}" icon="close"/>
	</form>
</div>
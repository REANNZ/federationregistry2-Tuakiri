<%@page import="fedreg.core.SamlURI" %>
<%@page import="fedreg.core.SamlURIType" %>

<script type="text/javascript">
	$(function() {
		$("#new${endpointType}").hide();
	
		$("#add${endpointType}link").click(function() {
			$("#add${endpointType}").hide();
			$("#new${endpointType}").show('slide');
		});
		
		$("#cancel${endpointType}link").click(function() {
			$("#new${endpointType}").hide();
			$("#add${endpointType}").show('slide');
		});
	});
</script>

<div id="add${endpointType}" class="searcharea">
	<a href="#" id="add${endpointType}link" class="button icon icon_add"><g:message code="fedreg.link.addendpoint"/></a>
</div>
	
<div id="new${endpointType}"  class="searcharea">
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
					<input name="location" type="text" class="easyinput" size="60"/>
				</td>
			</tr>
			<tr>
				<th><g:message code="fedreg.label.responselocation"/><th>
				<td>
					<input name="responselocation" type="text" class="easyinput" size="60"/>
				</td>
			</tr>
			<tr>
				<th><g:message code="fedreg.label.active"/><th>
				<td>
					<g:checkBox name="active" value="${true}" />
				</td>
			</tr>
		</tbody>
	</table>
	<a href="#" onclick="createEndpoint('${endpointType}', '${containerID}');" id="create${endpointType}link" class="button icon icon_add"><g:message code="fedreg.link.add"/></a>&nbsp;
	<a href="#" id="cancel${endpointType}link" class="button icon icon_cancel"><g:message code="fedreg.link.cancel"/></a>
	</form>
</div>
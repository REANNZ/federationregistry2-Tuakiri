
<n:hasPermission target="descriptor:${descriptor.id}:nameidformat:add">

	<%@page import="fedreg.core.SamlURI" %>
	<%@page import="fedreg.core.SamlURIType" %>

	<script type="text/javascript">
		$(function() {
			$("#newnameidformat").hide();
		});
	</script>

	<hr>

	<div id="addnameidformat" class="searcharea">
		<n:button onclick="\$('#addnameidformat').fadeOut(); \$('#newnameidformat').fadeIn();" label="${message(code:'label.addnameidformat')}" icon="plus"/>
	</div>
	
	<div id="newnameidformat"  class="searcharea">
		<h3><g:message code="fedreg.template.nameidformats.add.heading"/></h3>
		<form id="newnameidformatdata">
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.nameidformat"/><th>
					<td>
						<g:select name="formatID" from="${SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)}" optionKey="id" optionValue="uri"/>
					</td>
				</tr>
			</tbody>
		</table>
		<n:button onclick="fedreg.nameIDFormat_add('${containerID}');" id="createnameidformatlink" label="${message(code:'label.add')}" icon="plus"/>
		<n:button onclick="\$('#newnameidformat').fadeOut(); \$('#addnameidformat').fadeIn();" label="${message(code:'label.close')}" icon="close"/>
		</form>
	</div>
	
</n:hasPermission>
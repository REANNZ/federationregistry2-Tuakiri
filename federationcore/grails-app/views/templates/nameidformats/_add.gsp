<%@page import="fedreg.core.SamlURI" %>
<%@page import="fedreg.core.SamlURIType" %>

<script type="text/javascript">
	$(function() {
		$("#newnameidformat").hide();
	
		$("#addnameidformatlink").click(function() {
			$("#addnameidformat").hide();
			$("#newnameidformat").show('slide');
		});
		
		$("#cancelnameidformatlink").click(function() {
			$("#newnameidformat").hide();
			$("#addnameidformat").show('slide');
		});
	});
</script>

<div id="addnameidformat" class="searcharea">
	<fr:button id="addnameidformatlink" label="${message(code:'fedreg.label.addnameidformat')}" icon="plus"/>
</div>
	
<div id="newnameidformat"  class="searcharea">
	<h3><g:message code="fedreg.template.nameidformats.add.heading"/></h3>
	<form id="newnameidformatdata">
	<table>
		<tbody>
			<tr>
				<th><g:message code="fedreg.label.nameidformat"/><th>
				<td>
					<g:select name="formatID" from="${SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)}" optionKey="id" optionValue="uri"/>
				</td>
			</tr>
		</tbody>
	</table>
	<fr:button onclick="fedreg.nameIDFormat_add('${containerID}');" id="createnameidformatlink" label="${message(code:'fedreg.label.add')}" icon="plus"/>
	<fr:button id="cancelnameidformatlink" label="${message(code:'fedreg.label.close')}" icon="close"/>
	</form>
</div>
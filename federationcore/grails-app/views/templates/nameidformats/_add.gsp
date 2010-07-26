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
	<n:button id="addnameidformatlink" label="${message(code:'label.addnameidformat')}" icon="plus"/>
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
	<n:button id="cancelnameidformatlink" label="${message(code:'label.close')}" icon="close"/>
	</form>
</div>
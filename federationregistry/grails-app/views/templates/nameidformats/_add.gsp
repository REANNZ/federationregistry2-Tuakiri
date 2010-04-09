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
	<a href="#" id="addnameidformatlink" class="button icon icon_add"><g:message code="fedreg.link.addnameidformat"/></a>
</div>
	
<div id="newnameidformat"  class="searcharea">
	<h3><g:message code="fedreg.template.nameidformats.add.heading"/></h3>
	<form id="newnameidformatdata">
	<g:hiddenField name="containerID" value="myValue" />
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
	<a href="#" onclick="addNameIDFormat('${containerID}');" id="createnameidformatlink" class="button icon icon_add"><g:message code="fedreg.link.add"/></a>&nbsp;
	<a href="#" id="cancelnameidformatlink" class="button icon icon_cancel"><g:message code="fedreg.link.cancel"/></a>
	</form>
</div>
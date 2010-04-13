<%@page import="fedreg.core.Attribute" %>

<script type="text/javascript">
	$(function() {
		$("#newattribute").hide();
	
		$("#addattributelink").click(function() {
			$("#addattribute").hide();
			$("#newattribute").show('slide');
		});
		
		$("#cancelattributelink").click(function() {
			$("#newattribute").hide();
			$("#addattribute").show('slide');
		});
	});
</script>

<div id="addattribute" class="searcharea">
	<a href="#" id="addattributelink" class="button icon icon_add"><g:message code="fedreg.link.addattribute"/></a>
</div>
	
<div id="newattribute"  class="searcharea">
	<h3><g:message code="fedreg.template.attributes.add.heading"/></h3>
	<p>
		<g:message code="fedreg.template.attributes.add.${type}.details"/>
	</p>
	<form id="newattributedata">
	<table>
		<tbody>
			<tr>
				<th><g:message code="fedreg.label.attribute"/><th>
				<td>
					<g:select name="attributeID" from="${Attribute.list()}" optionKey="id" optionValue="${{ it.friendlyName + ' ( ' + it.oid + ' )' }}" />
				</td>
			</tr>
		</tbody>
	</table>
	<a href="#" onclick="fedreg.attribute_add('${containerID}');" id="createattributelink" class="button icon icon_add"><g:message code="fedreg.link.add"/></a>&nbsp;
	<a href="#" id="cancelattributelink" class="button icon icon_cancel"><g:message code="fedreg.link.close"/></a>
	</form>
</div>
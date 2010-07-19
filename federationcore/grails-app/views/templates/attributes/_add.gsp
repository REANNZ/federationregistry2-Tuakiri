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
	<fr:button id="addattributelink" label="${message(code:'fedreg.label.addattribute')}" icon="plus"/>
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
	<fr:button id="createattributelink" onclick="fedreg.attribute_add('${containerID}');" label="${message(code:'fedreg.link.add')}" icon="plus"/>
	<fr:button id="cancelattributelink" label="${message(code:'fedreg.label.close')}" icon="cancel"/>
	</form>
</div>
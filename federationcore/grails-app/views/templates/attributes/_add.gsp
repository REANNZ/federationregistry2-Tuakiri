<%@page import="fedreg.core.Attribute" %>

<script type="text/javascript">
	$(function() {
		$("#newattribute").hide();
	});
</script>

<div id="addattribute" class="searcharea">
	<n:button onclick="\$('#addattribute').fadeOut(); \$('#newattribute').fadeIn();" label="${message(code:'label.addattribute')}" icon="plus"/>
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
				<th><g:message code="label.attribute"/><th>
				<td>
					<g:select name="attributeID" from="${Attribute.list()}" optionKey="id" optionValue="${{ it.friendlyName + ' ( ' + it.oid + ' )' }}" />
				</td>
			</tr>
		</tbody>
	</table>
	<n:button id="createattributelink" onclick="fedreg.attribute_add('${containerID}');" label="${message(code:'label.add')}" icon="plus"/>
	<n:button onclick="\$('#newattribute').fadeOut(); \$('#addattribute').fadeIn();" label="${message(code:'label.close')}" icon="cancel"/>
	</form>
</div>
<fr:hasPermission target="descriptor:${descriptor.id}:attribute:add">
	<%@page import="aaf.fr.foundation.AttributeBase" %>

	<script type="text/javascript">
		$(function() {
			$("#newattribute").hide();
		});
	</script>

	<hr>

	<div id="addattribute" class="searcharea">
		<n:button onclick="\$('#addattribute').fadeOut(); \$('#newattribute').fadeIn();" label="${message(code:'label.addattribute')}" class="add-button"/>
	</div>
	
	<div id="newattribute"  class="searcharea">
		<h3><g:message code="fedreg.templates.attributes.add.heading"/></h3>
		<p>
			<g:message code="fedreg.templates.attributes.add.${type}.details"/>
		</p>
		<form id="newattributedata">
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.attribute"/><th>
					<td>
						<g:select name="attributeID" from="${AttributeBase.list().sort{it.name}}" optionKey="id" optionValue="${{ it.name + ' ( ' + it.oid + ' )' }}" />
					</td>
				</tr>
			</tbody>
		</table>
		<n:button id="createattributelink" onclick="fedreg.attribute_add('${containerID}');" label="${message(code:'label.add')}" class="add-button"/>
		<n:button onclick="\$('#newattribute').fadeOut(); \$('#addattribute').fadeIn();" label="${message(code:'label.close')}" class="close-button"/>
		</form>
	</div>
</fr:hasPermission>
<n:hasPermission target="descriptor:${descriptor.id}:category:add">

	<%@page import="fedreg.core.ServiceCategory" %>
	<script type="text/javascript">
		$(function() {
			$("#newcategory").hide();
		});
	</script>

	<hr>

	<div id="addcategory" class="searcharea">
		<n:button onclick="\$('#addcategory').fadeOut(); \$('#newcategory').fadeIn();" label="${message(code:'label.addcategory')}" class="add-button"/>
	</div>
	
	<div id="newcategory"  class="searcharea">
		<h3><g:message code="fedreg.templates.servicecategories.add.heading"/></h3>
		<form id="newservicecategorydata">
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.category"/><th>
					<td>
						<g:select name="categoryID" from="${ServiceCategory.list()}" optionKey="id" optionValue="name"/>
					</td>
				</tr>
			</tbody>
		</table>
		<n:button onclick="fedreg.serviceCategory_add('${containerID}');" id="createcategorylink" label="${message(code:'label.add')}" class="add-button"/>
		<n:button onclick="\$('#newcategory').fadeOut(); \$('#addcategory').fadeIn();" label="${message(code:'label.close')}" class="close-button"/>
		</form>
	</div>
	
</n:hasPermission>
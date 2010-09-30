
	<script type="text/javascript">
		$(function() {
			$("#searchfulladministrator").hide();
			$("#availablefulladministrators").hide();
		});
	</script>
	<hr>
	<div id="addfulladministrator" class="searcharea">
		<n:button onclick="\$('#addfulladministrator').hide(); \$('#searchfulladministrator').fadeIn(); \$('#email').focus();" label="${message(code:'label.addadministrator')}" icon="plus"/>
	</div>

	<div id="searchfulladministrator">
		<h3><g:message code="fedreg.templates.organization.full.administrators.search.heading"/></h3>
		<table>
			<tbody>
				<tr>
					<td><input type="text" id="q" name="q"/></td>
					<td>
						<n:button href="#" onclick="fedreg.organization_fulladministrator_search(${organization.id});" label="${message(code:'label.search')}" icon="search"/>
						<n:button onclick="\$('#searchfulladministrator').hide(); \$('#availablefulladministrators').fadeOut(); \$('#addfulladministrator').fadeIn(); \$('#availablefulladministrators').empty();" label="${message(code:'label.close')}" icon="close"/>
		            </td>
				</tr>
			</tbody>
		</table>		

		<div id="availablefulladministrators">
		</div>
	</div>


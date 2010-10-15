<n:hasPermission target="descriptor:${descriptor.id}:monitor:create">

	<%@page import="fedreg.core.MonitorType" %>

	<script type="text/javascript">
		$(function() {
			$("#newmonitor").hide();
			$("#newmonitordata").validate();
		});
	</script>

	<hr>

	<div id="addmonitor" class="searcharea">
		<n:button onclick="\$('#addmonitor').fadeOut(); \$('#newmonitor').fadeIn();" label="${message(code:'label.addmonitor')}" icon="plus"/>
	</div>
	
	<div id="newmonitor" class="searcharea">
		<h3><g:message code="fedreg.template.monitor.add.heading"/></h3>
		<form id="newmonitordata">
			<g:hiddenField name="interval" value="0" />
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.monitortype"/><th>
					<td>
						<g:select name="type" from="${MonitorType.list()}" optionKey="id" optionValue="name"/>
					</td>
				</tr>
				<tr>
					<th><g:message code="label.location"/><th>
					<td>
						<input name="url" type="text" class="required" size="60"/>
					</td>
				</tr>
			</tbody>
		</table>
		<n:button onclick="if(\$('#newmonitordata').valid()) fedreg.monitor_create();" label="${message(code:'label.add')}" icon="plus"/>
		<n:button onclick="\$('#newmonitor').fadeOut(); \$('#addmonitor').fadeIn();" label="${message(code:'label.close')}" icon="close"/>
		</form>
	</div>
	
</n:hasPermission>
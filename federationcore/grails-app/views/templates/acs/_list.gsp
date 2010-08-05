<%@page import="fedreg.core.Attribute" %>
<g:if test="${attributeConsumingServices}">
<table>
	<tbody>
	<g:each in="${attributeConsumingServices}" status="i" var="acs">
		<tr>
			<td colspan="2"><h4><g:message code="label.attributeconsumingservice"/> ${i+1}</h4></td>
		</tr>
		<tr>
			<th><g:message code="label.name" /></th>
			<td>${acs.serviceNames.asList()?.get(0)?.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="label.description" /></th>
			<td>${acs.serviceDescriptions.asList()?.get(0)?.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="label.requestedattributes" /></th>
			<td>
				<div id="acsreqattr${i}">
					<g:render template="/templates/acs/listrequestedattributes" plugin="federationcore" model='[requestedAttributes:acs.requestedAttributes, containerID:"acsreqattr${i}"]' />
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<script type="text/javascript">
					$(function() {
						$("#newattribute${i}").hide();
					});
				</script>

				<div id="addattribute${i}" class="searcharea">
					<n:button onclick="\$('#addattribute${i}').fadeOut(); \$('#newattribute${i}').fadeIn();" label="${message(code:'label.addattribute')}" icon="plus"/>
				</div>

				<div id="newattribute${i}"  class="searcharea">
					<h3><g:message code="fedreg.template.acs.reqattributes.add.heading"/></h3>
					<p>
						<g:message code="fedreg.template.acs.reqattributes.add.details"/>
					</p>
					<form id="newattributedata${i}">
						<table>
							<tbody>
								<tr>
									<th><g:message code="label.attribute"/><th>
									<td>
										<g:select name="attrid" from="${Attribute.list().sort{it.friendlyName}}" optionKey="id" optionValue="${{ it.friendlyName + ' ( ' + it.oid + ' )' }}" class="required"/>
									</td>
								</tr>
								<tr>
									<th><g:message code="label.reason"/><th>
									<td>
										<input name="reasoning" type="text" class="required" size="60"/>
									</td>
								</tr>
								<tr>
									<th><g:message code="label.required"/><th>
									<td>
										<g:checkBox name="isrequired" />
									</td>
								</tr>
							</tbody>
						</table>
						<n:button onclick="if(\$('#newattributedata${i}').valid()) fedreg.acs_reqattribute_add(${acs.id}, 'newattributedata${i}', 'acsreqattr${i}' );" label="${message(code:'label.add')}" icon="plus"/>
						<n:button onclick="\$('#newattribute${i}').fadeOut(); \$('#addattribute${i}').fadeIn();" label="${message(code:'label.close')}" icon="cancel"/>
					</form>
				</div>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
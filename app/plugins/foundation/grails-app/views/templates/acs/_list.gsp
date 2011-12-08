<%@page import="aaf.fr.foundation.AttributeBase" %>
<g:if test="${attributeConsumingServices}">
<table>
	<tbody>
	<g:each in="${attributeConsumingServices}" status="i" var="acs">
		<tr>
			<td colspan="2"><h4><g:message code="label.attributeconsumingservice"/> ${i+1}</h4></td>
		</tr>
		<tr>
			<th><g:message code="label.requestedattributes" /></th>
			<td>
				<div id="acsreqattr${i}">
					<g:render template="/templates/acs/listrequestedattributes" plugin="federationcore" model='[acs:acs, requestedAttributes:acs.sortedAttributes(), containerID:"acsreqattr${i}"]' />
				</div>
			</td>
		</tr>
		<tr>
			<td/>
			<td colspan="2" class="contentaddition">
				<script type="text/javascript">
					$(function() {
						$("#newattribute${i}").hide();
					});
				</script>

				<n:hasPermission target="descriptor:${acs.descriptor.id}:attribute:add">
					<div id="addattribute${i}">
						<n:button onclick="\$('#addattribute${i}').fadeOut(); \$('#newattribute${i}').fadeIn();" label="${message(code:'label.addattribute')}" class="add-button"/>
					</div>

					<div id="newattribute${i}">
						<h3><g:message code="fedreg.templates.acs.reqattributes.add.heading"/></h3>
						<p>
							<g:message code="fedreg.templates.acs.reqattributes.add.details"/>
						</p>
						<form id="newattributedata${i}">
							<table>
								<tbody>
									<tr>
										<th><g:message code="label.attribute"/><th>
										<td>
											<g:select name="attrid" from="${availableAttributes.sort{it.name}}" optionKey="id" optionValue="${{ it.name + ' ( ' + it.oid + ' )' }}" class="required"/>
										</td>
									</tr>
									<tr>
										<th><g:message code="label.reason"/><th>
										<td>
											<input name="reasoning" type="text" class="required" size="60"/>
											<fr:tooltip code='fedreg.help.acs.reason' />
										</td>
									</tr>
									<tr>
										<th><g:message code="label.required"/><th>
										<td>
											<g:checkBox name="isrequired" />
											<fr:tooltip code='fedreg.help.acs.isrequired' />
										</td>
									</tr>
								</tbody>
							</table>
							<n:button onclick="if(\$('#newattributedata${i}').valid()) fedreg.acs_reqattribute_add(${acs.id}, 'newattributedata${i}', 'acsreqattr${i}' );" label="${message(code:'label.add')}" class="add-button"/>
							<n:button onclick="\$('#newattribute${i}').fadeOut(); \$('#addattribute${i}').fadeIn();" label="${message(code:'label.close')}" class="close-button"/>
						</form>
					</div>
				</n:hasPermission>
			</td>
		</tr>
		<tr>
			<td/>
			<td>
				<div id="acsspecattributes">
					<g:render template="/templates/acs/listspecifiedattributes" plugin="federationcore" model='[acs:acs, requestedAttributes:acs.requestedAttributes, specificationAttributes:specificationAttributes]' />
				</div>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
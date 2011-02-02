<table>
	<tbody>
		<g:each in="${specificationAttributes}" status="j" var="specAttr">
			<g:each in="${requestedAttributes}" status="k" var="ra">
				<g:if test="${ra.base == specAttr}">
					<tr>
						<th><g:message code="label.requestedvaluesfor" args="[specAttr.friendlyName]"/></th>
						<td colspan="2">
							<div id="acsspecattr${j}">
								<table>
									<tbody>
										<tr>
											<td colspan="2">
												<div id="acsspecattrvals${j}">
													<g:render template="/templates/acs/listspecifiedattributevalues" plugin="federationcore" model='[requestedAttribute:ra, containerID:"acsspecattrvals${j}"]' />
												</div>
											</td>
										</tr>
										<n:hasPermission target="descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:value:add">
											<tr>
												<td colspan="2" class="contentaddition">
													<script type="text/javascript">
														$(function() {
															$("#newspecattributeval${j}").hide();
														});
													</script>
													<div id="addnewspecattributeval${j}">
														<n:button onclick="\$('#addnewspecattributeval${j}').fadeOut(); \$('#newspecattributeval${j}').fadeIn();" label="${message(code:'label.addvalue')}" class="add-button"/>
													</div>
													<div id="newspecattributeval${j}">
														<h3><g:message code="fedreg.templates.acs.specattributes.add.heading"/></h3>
														<p>
															<g:message code="fedreg.templates.acs.specattributes.add.details"/>
														</p>
														<form id="newspecattributedata${j}">
															<table>
																<tbody>
																	<tr>
																		<th><g:message code="label.value"/><th>
																		<td>
																			<input name="value" type="text" class="required" size="60"/>
																			<fr:tooltip code='fedreg.help.acs.specvalue' />
																		</td>
																	</tr>
																</tbody>
															</table>
														</form>
														<n:button onclick="if(\$('#newspecattributedata${j}').valid()) fedreg.acs_specattribute_add(${ra.id}, 'newspecattributedata${j}', 'acsspecattrvals${j}' );" label="${message(code:'label.add')}" class="add-button"/>
														<n:button onclick="\$('#newspecattributeval${j}').fadeOut(); \$('#addnewspecattributeval${j}').fadeIn();" label="${message(code:'label.close')}" class="close-button"/>
													</div>
												</td>
											</tr>
										</n:hasPermission>
									</tbody>
								</table>
							</div>
						</td>
					</tr>
				</g:if>
			</g:each>
		</g:each>
	</tbody>
</table>
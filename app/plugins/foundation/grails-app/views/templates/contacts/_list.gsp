
	<g:if test="${host.contacts.size() > 0}">
  	<table class="borderless">
  		<thead>
  			<tr>
  				<th><g:message code="label.name" /></th>
  				<th><g:message code="label.email" /></th>
  				<th><g:message code="label.type" /></th>
  				<th/>
  			</tr>
  		</thead>
  		<tbody>
  			<g:each in="${host.contacts?.sort{it.contact.surname}}" var="contactPerson" status="i">
  				<tr>
  					<td>${contactPerson.contact.givenName?.encodeAsHTML()} ${contactPerson.contact.surname?.encodeAsHTML()}</td>
  					<td><a href="mailto:${contactPerson.contact.email?.encodeAsHTML()}">${contactPerson.contact.email?.encodeAsHTML()}</a></td>
  					<td>${contactPerson.type.displayName.encodeAsHTML()}</td>
  					<td>
  						<a href="${createLink(controller:'contacts', action:'show', id: contactPerson.contact.id)}" class="btn"><g:message code='label.view'/></a>
  						<fr:hasPermission target="organization:${owner.id}:contact:remove">
                <a class="confirm-delete-contact btn" data-contact="${contactPerson.id}"><g:message code='label.delete'/></a>
  						</fr:hasPermission>
  					</td>
  				</tr>
  			</g:each>
  		</tbody>
  	</table>
	</g:if>
	<g:else>
		<p><g:message code="fedreg.templates.contacts.noresults" /></p>
	</g:else>
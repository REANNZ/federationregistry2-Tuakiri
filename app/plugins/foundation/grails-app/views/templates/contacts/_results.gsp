
  <h4>Contacts matching search query</h4>
	<g:if test="${contacts}">
		<table class="table borderless">
			<thead>
				<tr>
					<th><g:message code="label.givenname" /></th>
					<th><g:message code="label.surname" /></th>
					<th><g:message code="label.email" /></th>
					<th><g:message code="label.organization" /></th>
					<th/>
				</tr>
			</thead>
			<tbody>
			<g:each in="${contacts}" var="contact" status="i">
				<tr>
					<td>${contact.givenName?.encodeAsHTML()}</td>
					<td>${contact.surname?.encodeAsHTML()}</td>
					<td>${contact.email?.encodeAsHTML()}</td>
					<td>${contact.organization?.displayName?.encodeAsHTML()}</td>
					<td>						
						<a class="btn confirm-link-contact" data-contact='${contact.id}' data-name='${contact.givenName} ${contact.surname}' data-email='${contact.email}'><g:message code="label.add" /></a>
					</td>
				</tr>
			</g:each>
			</tbody>
		</table>

    <div class="form-actions">
      <a class="btn search-for-contact"><g:message code="label.refinesearch" /></a>
      <a class="btn cancel-search-for-contact"><g:message code="label.close" /></a>
    </div>
	</g:if>
	<g:else>
		<p class=""><g:message code="label.noresults"/></p>
		<div>
      <a class="btn search-for-contact"><g:message code="label.refinesearch" /></a>
      <a href="${createLink(controller:'contacts', action:'create')}" class="btn"><g:message code="label.newcontact" /></a>
      <a class="btn cancel-search-for-contact"><g:message code="label.close" /></a>
    </div>
	</g:else>
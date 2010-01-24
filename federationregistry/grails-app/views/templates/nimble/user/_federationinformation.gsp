<h3><g:message code="nimble.view.user.show.federated.heading" /></h3>
<table>
	<tbody>
		<tr>
			<th><g:message code="nimble.label.provider" /></th>
			<td valign="top">
			<a href="${user.federationProvider.details?.url?.location}" alt="${user.federationProvider.details?.url?.altText}">${user?.federationProvider?.details?.displayName.encodeAsHTML()}</a>
		</td>
		</tr>
		<tr>
			<th><g:message code="nimble.label.description" /></th>
			<td>${user?.federationProvider?.details?.description.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.providingentity" /></th>
			<td>${user?.entityDescriptor?.entityID.encodeAsHTML()}</td>
		</tr>
  </tbody>
</table>
<table class="table borderless">
  <thead>
    <tr>
      <th></th>
      <th><g:message encodeAs="HTML" code="label.total" /></th>
      <th><g:message encodeAs="HTML" code="label.active" /></th>
      <th><g:message encodeAs="HTML" code="label.inactive" /></th>
      <th><g:message encodeAs="HTML" code="label.approved" /></th>
      <th><g:message encodeAs="HTML" code="label.unapproved" /></th>
    </tr>
  </thead>
  <tbody>
    <tr class="odd">
      <th><g:message encodeAs="HTML" code="label.entitydescriptors" /></th>
      <td>${registrations.entityDescriptors.total.encodeAsHTML()}</td>
      <td>${registrations.entityDescriptors.activeEntityDescriptors.encodeAsHTML()}</td>
      <td>${registrations.entityDescriptors.inactiveEntityDescriptors.encodeAsHTML()}</td>
      <td>${registrations.entityDescriptors.approvedEntityDescriptors.encodeAsHTML()}</td>
      <td>${registrations.entityDescriptors.unapprovedEntityDescriptors.encodeAsHTML()}</td>
    </tr>
    <tr>
      <th><g:message encodeAs="HTML" code="label.identityproviders" /></th>
      <td>${registrations.idpSSODescriptors.total.encodeAsHTML()}</td>
      <td>${registrations.idpSSODescriptors.activeIDPSSODescriptors.encodeAsHTML()}</td>
      <td>${registrations.idpSSODescriptors.inactiveIDPSSODescriptors.encodeAsHTML()}</td>
      <td>${registrations.idpSSODescriptors.approvedIDPSSODescriptors.encodeAsHTML()}</td>
      <td>${registrations.idpSSODescriptors.unapprovedIDPSSODescriptors.encodeAsHTML()}</td>
    </tr>
    <tr>
      <th><g:message encodeAs="HTML" code="label.serviceproviders" /></th>
      <td>${registrations.spSSODescriptors.total.encodeAsHTML()}</td>
      <td>${registrations.spSSODescriptors.activeSPSSODescriptors.encodeAsHTML()}</td>
      <td>${registrations.spSSODescriptors.inactiveSPSSODescriptors.encodeAsHTML()}</td>
      <td>${registrations.spSSODescriptors.approvedSPSSODescriptors.encodeAsHTML()}</td>
      <td>${registrations.spSSODescriptors.unapprovedSPSSODescriptors.encodeAsHTML()}</td>
    </tr>
  </tbody>
</table>  

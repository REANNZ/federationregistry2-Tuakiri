<fr:hasAnyPermission in='["federation:management:organization:${owner.id}:contact:add", "federation:management:contacts"]'>
  <hr>
  
  <div class="search-contacts">
    <div class="add-contact">
      <a class="btn search-for-contact"><g:message code="label.addcontact" /></a>
    </div>

    <div class="potential-contacts revealable">
    </div>

    <div class="search-contacts-form revealable">
      <h4><g:message code="template.fr.contactmanager.searchforcontacts.heading"/></h4>
      <form>
        <fieldset>
          <input type="hidden" name="id" value="${owner.id}" />

          <label for="givenName"><g:message code="label.givenname"/></label>
          <input type="text" id="givenName" name="givenName" class="x-large" />

          <label for="surname"><g:message code="label.surname"/></label>
          <input type="text" id="surname" name="surname" class="x-large"/>

          <label for="email"><g:message code="label.email"/></label>
          <input type="text" id="email" name="email" class="x-large"/>

          <div class="form-actions">
            <a class="submit-search-for-contact btn btn-info"><g:message code="label.search" /></a>
            <a class="cancel-search-for-contact btn"><g:message code="label.cancel" /></a>
          </div>
        </fieldset>
      </form>
    </div>
  </div>

  <div id="link-contact-modal" class="modal hide fade">
    <div class="modal-header">
      <a class="close close-modal">&times;</a>
      <h3><g:message code="template.fr.contacts.confirmaddition.title"/></h3>
    </div>
    <div class="modal-body">
      <p><g:message code="template.fr.contacts.confirmaddition"/></p>
      <strong><g:message code="template.fr.contacts.selecttype"/></strong>
      <div class="controls">
        <g:select id="contactselectedtype" name="contactType" from="${contactTypes}" optionKey="name" optionValue="displayName"/>
      </div>
    </div>
    <div class="modal-footer">
      <a class="btn close-modal"><g:message code="label.cancel" /></a>
      <a class="btn btn-success link-contact"><g:message code="label.accept" /></a>
    </div>
  </div>

  <div id="unlink-contact-modal" class="modal hide fade">
    <div class="modal-header">
      <a class="close close-modal">&times;</a>
      <h3><g:message code="template.fr.contacts.remove.confirm.title"/></h3>
    </div>
    <div class="modal-body">
      <p><g:message code="template.fr.contacts.remove.confirm.descriptive"/></p>
    </div>
    <div class="modal-footer">
      <a class="btn close-modal"><g:message code="label.cancel" /></a>
      <a class="btn btn-danger delete-contact"><g:message code="label.delete" /></a>
    </div>
  </div>

</fr:hasAnyPermission>

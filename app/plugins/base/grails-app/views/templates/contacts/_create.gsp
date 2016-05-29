
<fr:hasAnyPermission in='["federation:management:${hostType}:${host.id}:contact:add", "federation:management:contacts"]'>
  <hr>

  <div class="search-contacts">
    <div class="add-contact">
      <a class="btn search-for-contact"><g:message encodeAs="HTML" code="label.addcontact" /></a>
    </div>

    <div class="potential-contacts revealable">
    </div>

    <div class="search-contacts-form revealable">
      <h4><g:message encodeAs="HTML" code="templates.fr.contactmanager.searchforcontacts.heading"/></h4>
      <form>
        <fieldset>
          <input type="hidden" name="id" value="${host.id}" />

          <label for="givenName"><g:message encodeAs="HTML" code="label.givenname"/></label>
          <input type="text" id="givenName" name="givenName" class="x-large" />

          <label for="surname"><g:message encodeAs="HTML" code="label.surname"/></label>
          <input type="text" id="surname" name="surname" class="x-large"/>

          <label for="email"><g:message encodeAs="HTML" code="label.email"/></label>
          <input type="text" id="email" name="email" class="x-large"/>

          <div class="form-actions">
            <a class="submit-search-for-contact btn btn-info"><g:message encodeAs="HTML" code="label.search" /></a>
            <a class="cancel-search-for-contact btn"><g:message encodeAs="HTML" code="label.cancel" /></a>
          </div>
        </fieldset>
      </form>
    </div>
  </div>

  <div id="link-contact-modal" class="modal hide fade">
    <div class="modal-header">
      <a class="close close-modal">&times;</a>
      <h3><g:message encodeAs="HTML" code="templates.fr.contacts.confirmaddition.title"/></h3>
    </div>
    <div class="modal-body">
      <p><span id="contactnameconfirmation"> </span> (<span id="contactemailconfirmation"> </span>)</p>
      <p><g:message encodeAs="HTML" code="templates.fr.contacts.confirmaddition"/></p>
      <strong><g:message encodeAs="HTML" code="templates.fr.contacts.selecttype"/></strong>
      <div class="controls">
        <g:select id="contactselectedtype" name="contactType" from="${contactTypes.sort{it.displayName}}" optionKey="${{it.name?.encodeAsHTML()}}" optionValue="${{it.displayName?.encodeAsHTML()}}"/>
      </div>
    </div>
    <div class="modal-footer">
      <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>
      <a class="btn btn-success link-contact"><g:message encodeAs="HTML" code="label.accept" /></a>
    </div>
  </div>

  <div id="unlink-contact-modal" class="modal hide fade">
    <div class="modal-header">
      <a class="close close-modal">&times;</a>
      <h3><g:message encodeAs="HTML" code="templates.fr.contacts.remove.confirm.title"/></h3>
    </div>
    <div class="modal-body">
      <p><g:message encodeAs="HTML" code="templates.fr.contacts.remove.confirm.descriptive"/></p>
    </div>
    <div class="modal-footer">
      <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>
      <a class="btn btn-danger delete-contact"><g:message encodeAs="HTML" code="label.delete" /></a>
    </div>
  </div>

</fr:hasAnyPermission>

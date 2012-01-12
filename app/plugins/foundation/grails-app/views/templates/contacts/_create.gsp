<fr:hasPermission target="organization:${owner.id}:contact:add">

  <div class="search-contacts search-box">
  	<div class="add-contact span16">
  		<a class="btn info search-for-contact"><g:message code="label.addcontact" /></a>
  	</div>

    <div class="potential-contacts hidden">
    </div>

    <div class="search-contacts-form hidden">
      <h4><g:message code="fedreg.templates.contactmanager.searchforcontacts.heading"/></h4>
      <form class="form-stacked">
  		  <fieldset>
          <input type="hidden" name="id" value="${owner.id}" />

          <div class="clearfix">
            <label for="givenName"><g:message code="label.givenname"/></label>
            <div class="input">
              <input type="text" id="givenName" name="givenName" class="x-large" />
            </div>
          </div>

          <div class="clearfix">
        		<label for="surname"><g:message code="label.surname"/></label>
            <div class="input">
              <input type="text" id="surname" name="surname" class="x-large"/>
            </div>
          </div>

          <div class="clearfix">
        		<label for="email"><g:message code="label.email"/></label>
            <div class="input">
        		  <input type="text" id="email" name="email" class="x-large"/>
            </div>
          </div>

          <div>
      		  <a class="btn primary submit-search-for-contact"><g:message code="label.search" /></a>
      		  <a class="btn cancel-search-for-contact"><g:message code="label.cancel" /></a>
          </div>
  		  </fieldset>
      </form>
    </div>
  </div>

  <div id="link-contact-modal" class="modal hide fade">
    <div class="modal-header">
      <a href="#" class="close">×</a>
      <h3><g:message code="fedreg.templates.contacts.confirmaddition.title"/></h3>
    </div>
    <div class="modal-body">
      <p><g:message code="fedreg.templates.contacts.confirmaddition"/></p>
      <strong><g:message code="fedreg.templates.contacts.selecttype"/></strong>
      <div class="input">
        <g:select id="contactselectedtype" name="contactType" from="${contactTypes}" optionKey="name" optionValue="displayName"/>
      </div>
    </div>
    <div class="modal-footer">
      <a class="btn close-modal"><g:message code="label.cancel" /></a>
      <a class="btn success link-contact"/><g:message code="label.accept" /></a>
    </div>
  </div>

  <div id="unlink-contact-modal" class="modal hide fade">
    <div class="modal-header">
      <a href="#" class="close">×</a>
      <h3><g:message code="fedreg.templates.contacts.remove.confirm.title"/></h3>
    </div>
    <div class="modal-body">
      <p><g:message code="fedreg.templates.contacts.remove.confirm.descriptive"/></p>
    </div>
    <div class="modal-footer">
      <a class="btn close-modal"><g:message code="label.cancel" /></a>
      <a class="btn danger delete-contact"/><g:message code="label.delete" /></a>
    </div>
  </div>

</fr:hasPermission>

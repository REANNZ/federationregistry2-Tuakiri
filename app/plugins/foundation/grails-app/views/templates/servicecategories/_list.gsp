
<p><g:message code="template.fr.servicecategories.descriptive" /></p>
  <g:if test="${categories}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.description" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
    <g:each in="${ categories.sort{it.id} }" status="i" var="cat">
      <tr>
        <td>${cat.name.encodeAsHTML()}</td>
        <td>${cat.description?.encodeAsHTML()}</td>
        <td>
          <fr:hasPermission target="descriptor:${descriptor.id}:category:remove">
            <a class="confirm-unlink-category btn btn-small" data-category="${cat.id}"><g:message code='label.delete'/></a>
          </fr:hasPermission>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
  </g:if>
  <g:else>
    <div>
      <p class="alert alert-message"><g:message code="template.fr.servicecategories.noresults"/></p>
    </div>
  </g:else>

<div id="unlink-category-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="template.fr.servicecategories.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="template.fr.servicecategories.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>
    <a class="btn btn-danger unlink-category"><g:message code="label.delete" /></a>
  </div>
</div>
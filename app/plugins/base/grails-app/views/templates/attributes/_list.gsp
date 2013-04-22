<div id="supported-attributes">
  <g:if test="${attrs}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message encodeAs="HTML" code="label.attribute" /></th>
        <th><g:message encodeAs="HTML" code="label.category" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
    <g:each in="${attrs}" status="i" var="attr">
      <tr>
        <td>
          <strong class="highlight">${fieldValue(bean: attr, field: "base.name")}</strong><br>
            <code>oid:${fieldValue(bean: attr, field: "base.oid")}</code>
            <br><br><em>${fieldValue(bean: attr, field: "base.description")}</em>
        </td>
        <td>${attr.base.category.name.encodeAsHTML()}</td>
        <td>
          <fr:hasPermission target="federation:management:descriptor:${descriptor.id}:attribute:remove">
            <a class="confirm-delete-attribute btn btn-mini" data-attrid="${attr.id}"><g:message encodeAs="HTML" code="label.remove"/></a>
          </fr:hasPermission>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
  </g:if>
  <g:else>
    <div>
      <p class="alert alert-message"><g:message encodeAs="HTML" code="templates.fr.attributes.noresults"/></p>
    </div>
  </g:else>
</div>

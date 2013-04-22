
<table class="table table-sortable borderless">
  <thead>
    <tr>
      <th><g:message encodeAs="HTML" code="label.name" default="Name"/></th>
      <th><g:message encodeAs="HTML" code="label.principal" default="Principal"/></th>
      <th/>
    </tr>
  </thead>
  <tbody>
    <g:each in="${subjects}" var="subject">
      <g:if test="${subject.enabled}">
        <tr>
          <td><g:fieldValue bean="${subject}" field="cn"/></td>
          <td><g:fieldValue bean="${subject}" field="principal"/></td>
          <td>
            <g:form controller="organization" action="grantFullAdministration">
              <g:hiddenField name="id" value="${organization.id}" />
              <g:hiddenField name="subjectID" value="${subject.id}" />
              <fr:hasPermission target="app:administration">
                <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:subject.id, absolute:true)}" >
                  <g:message encodeAs="HTML" code="label.quickview" default="Quick View"/>
                </a>
              </fr:hasPermission>
              <g:submitButton name="submit" class="btn" value="${message(code: 'label.grant', default: 'Grant Access')}" />
            </g:form>
          </td>
        </tr>
      </g:if>
    </g:each>
  </tbody>
</table>

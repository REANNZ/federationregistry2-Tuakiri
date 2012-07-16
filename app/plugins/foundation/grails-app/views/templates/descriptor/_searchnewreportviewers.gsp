
<table class="table table-sortable borderless">
  <thead>
    <tr>
      <th><g:message code="label.id" default="ID"/></th>
      <th><g:message code="label.name" default="Name"/></th>
      <th><g:message code="label.principal" default="Principal"/></th>
      <th/>
    </tr>
  </thead>
  <tbody>
    <g:each in="${subjects.sort{it.id}}" var="subject">
      <g:if test="${subject.enabled}">
        <tr>
          <td><g:fieldValue bean="${subject}" field="id"/></td>
          <td><g:fieldValue bean="${subject}" field="cn"/></td>
          <td><g:fieldValue bean="${subject}" field="principal"/></td>
          <td>
            <g:form controller="descriptorAdministration" action="grantReportAdministration">
              <g:hiddenField name="id" value="${descriptor.id}" />
              <g:hiddenField name="subjectID" value="${subject.id}" />
              <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:subject.id, absolute:true)}" ><g:message code="label.quickview" default="Quick View"/></a>
              <g:submitButton name="submit" class="btn" value="${message(code: 'label.grant', default: 'Grant')}" />
            </g:form>
          </td>
        </tr>
      </g:if>
    </g:each>
  </tbody>
</table>
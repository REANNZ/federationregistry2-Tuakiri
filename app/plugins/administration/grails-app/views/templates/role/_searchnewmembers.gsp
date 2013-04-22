<h4><g:message encodeAs="HTML" code="label.addmember" default="Add Member"/></h4>
<table class="table borderless table-admin-sortable">
  <thead>
    <tr>
      <th><g:message encodeAs="HTML" code="label.id" default="ID"/></th>
      <th><g:message encodeAs="HTML" code="label.name" default="Name"/></th>
      <th><g:message encodeAs="HTML" code="label.principal" default="Principal"/></th>
      <th/>
    </tr>
  </thead>
  <tbody>
    <g:each in="${subjects}" var="subject">
      <tr>
        <td><g:fieldValue bean="${subject}" field="id"/></td>
        <td><g:fieldValue bean="${subject}" field="cn"/></td>
        <td><g:fieldValue bean="${subject}" field="principal"/></td>
        <td>
          <g:form method="post">
            <g:hiddenField name="id" value="${role?.id}" />
            <g:hiddenField name="version" value="${role?.version}" />
            <g:hiddenField name="subjectID" value="${subject?.id}" />
            <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:subject.id, absolute:true)}" ><g:message encodeAs="HTML" code="label.quickview" default="Quick View"/></a>
            <g:link controller="subject" action="show" id="${subject.id}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view" default="View"/></g:link>
            <g:actionSubmit action="addmember" class="btn btn-small" value="${message(code: 'label.add', default: 'Add')}" />
          </g:form>
        </td>
    </g:each>
  </tbody>
</table>

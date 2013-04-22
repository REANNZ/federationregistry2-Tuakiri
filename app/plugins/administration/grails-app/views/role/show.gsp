<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
  </head>
  <body>
    <h2><g:message encodeAs="HTML" code="aaf.fr.admin.role.show.heading" default="Role - {0}" args="[role.name]"/></h2>

    <g:render template="/templates/flash" />

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message encodeAs="HTML" code="label.overview" /></a></li>
      <li><a href="#tab-members" data-toggle="tab"><g:message encodeAs="HTML" code="label.members" default="Members"/></a></li>
      <li><a href="#tab-permissions" data-toggle="tab"><g:message encodeAs="HTML" code="label.permissions" default="Permissions"/></a></li>
    </ul>

    <div class="tab-content">
      <div id="tab-overview" class="tab-pane active">
        <div id="overview-role">
          <table class="table borderless">
            <tbody>
              <tr>
                <th><g:message encodeAs="HTML" code="role.id.label" default="id" /></th>
                <td><g:fieldValue bean="${role}" field="id"/></td>
              </tr>
              <tr>
                <th><g:message encodeAs="HTML" code="role.name.label" default="Name" /></th>
                <td><g:fieldValue bean="${role}" field="name"/></td>
              </tr>
              <tr>
                <th><g:message encodeAs="HTML" code="role.description.label" default="Description" /></th>
                <td><g:fieldValue bean="${role}" field="description"/></td>
              </tr>
              <tr>
                <th><g:message encodeAs="HTML" code="role.protect.label" default="Protect" /></th>
                <td><g:formatBoolean boolean="${role?.protect}" /></td>
              </tr>
            </tbody>
          </table>
          

          <g:form>
            <fieldset>
              <g:hiddenField name="id" value="${role?.id}" />
              <a class="show-edit-role btn btn-info"><g:message encodeAs="HTML" code="label.edit" default="Edit" /></a>
              <g:if test="${!role.protect}">
                <a class="confirm-delete-role btn"><g:message encodeAs="HTML" code="label.delete" default="Delete" /></a>
              </g:if>
            </fieldset>
          </g:form>
        </div>
        <div id="editor-role" class="revealable">
          <h3>Editing Role</h3>

          <g:hasErrors bean="${role}">
          <ul class="clean alert alert-error">
            <g:eachError bean="${role}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message encodeAs="HTML" error="${error}"/></li>
            </g:eachError>
          </ul>
          </g:hasErrors>
          <g:form method="post" class="form form-horizontal">
            <g:hiddenField name="id" value="${role?.id}" />
            <g:hiddenField name="version" value="${role?.version}" />
            <fieldset class="form">
              <g:render template="form"/>
            </fieldset>
            <fieldset>
              <div class="form-actions">
                <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'label.update', default: 'Update')}" />
                <a class="cancel-edit-role btn"><g:message encodeAs="HTML" code="label.cancel" default="Cancel" /></a>
              </div>
            </fieldset>
          </g:form>
        </div>
      </div>

      <div id="tab-members" class="tab-pane">
        <g:if test="${role.subjects}">
          <table class="table borderless">
            <thead>
              <tr>
                <th><g:message encodeAs="HTML" code="label.id" default="ID"/></th>
                <th><g:message encodeAs="HTML" code="label.name" default="Name"/></th>
                <th><g:message encodeAs="HTML" code="label.principal" default="Principal"/></th>
                <th/>
              </tr>
            </thead>
            <tbody>
              <g:each in="${role.subjects.sort{it.id}}" var="subject">
                <tr>
                  <td><g:fieldValue bean="${subject}" field="id"/></td>
                  <td><g:fieldValue bean="${subject}" field="cn"/></td>
                  <td><g:fieldValue bean="${subject}" field="principal"/></td>
                  <td>
                    <g:form method="post" class="form">
                      <g:hiddenField name="id" value="${role?.id}" />
                      <g:hiddenField name="subjectID" value="${subject.id}" />
                      <g:link controller="subject" action="show" id="${subject.id}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view" default="View"/></g:link>
                      <g:actionSubmit action="removemember" class="btn btn-small" value="${message(code: 'label.remove', default: 'Remove')}" />
                    </g:form>
                  </td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </g:if>
        <g:else>
          <p class="alert alert-info">This role currently has no members.</p>
        </g:else>

        <a href="#" id="show-manage-role-members" class="btn"><g:message encodeAs="HTML" code="label.addmembers"/></a>
        <div id="manage-role-members" class="revealable span11"></div>
      </div>

      <div id="tab-permissions" class="tab-pane">
        <g:if test="${role.permissions}">
          <table class="table borderless">
            <thead>
              <tr>
                <th><g:message encodeAs="HTML" code="label.type" default="Type"/></th>
                <th><g:message encodeAs="HTML" code="label.target" default="Target"/></th>
                <th><g:message encodeAs="HTML" code="label.managed" default="Managed"/></th>
                <th/>
              </tr>
            </thead>
            <tbody>
              <g:each in="${role.permissions.sort{it.id}}" var="perm">
                <tr>
                  <td><g:fieldValue bean="${perm}" field="displayType"/></td>
                  <td><g:fieldValue bean="${perm}" field="target"/></td>
                  <td><g:fieldValue bean="${perm}" field="managed"/></td>
                  <td>
                    <g:form method="post" class="form">
                      <g:hiddenField name="id" value="${role?.id}" />
                      <g:hiddenField name="permID" value="${perm.id}" />
                      <g:actionSubmit action="deletepermission" class="btn btn-small" value="${message(code: 'label.delete', default: 'Delete')}" />
                    </g:form>
                  </td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </g:if>
        <g:else>
          <p class="alert alert-info">This role currently has no associated permissions.</p>
        </g:else>
        <a href="#" class="show-manage-permissions btn"><g:message encodeAs="HTML" code="label.addpermissions" default="Add Permissions"/></a>
        <div class="manage-role-permissions revealable row-spacer">
          <hr>
          <h3><g:message encodeAs="HTML" code="label.addpermission" default="Add Permission"/></h3>
          <g:form method="post" class="form validating">
            <g:hiddenField name="id" value="${role?.id}" />
            <g:hiddenField name="version" value="${role?.version}" />
            <fieldset class="form">
              <label><g:message encodeAs="HTML" code="label.target" default="Target"/></label>
              <input name="target" class="span4 required" placeholder="target:must:be:colon:seperated:use:*:for:matchall"></input>
            </fieldset>
            <fieldset>
              <g:actionSubmit action="createpermission" class="btn btn-success" value="${message(code: 'label.create', default: 'Create')}" />
            </fieldset>
          </g:form>
        </div>
      </div>
    </div>

    <div id="delete-role-modal" class="modal hide fade">
      <div class="modal-header">
        <a class="close close-modal">&times;</a>
        <h3><g:message encodeAs="HTML" code="aaf.fr.identity.role.delete.confirm.title" default="Delete Role?"/></h3>
      </div>
      <div class="modal-body">
        <p><g:message encodeAs="HTML" code="aaf.fr.identity.role.delete.confirm.title" default="Are you sure you wish to delete this role?. Removing permissions may impact user access"/></p>
      </div>
      <div class="modal-footer">
        <g:form method="post" class="form">
          <g:hiddenField name="id" value="${role?.id}" />
          <g:hiddenField name="version" value="${role?.version}" />
          <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>
          <g:actionSubmit action="delete" class="btn btn-danger" value="${message(code: 'label.delete', default: 'Delete')}" />
        </g:form>
      </div>
    </div>

    <r:script>
      var searchNewMembersEndpoint = "${createLink(controller:'role', action:'searchNewMembers', id:role.id)}";
    </r:script>

  </body>
</html>

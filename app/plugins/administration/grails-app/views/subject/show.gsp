<html>
  <head>
    <meta name="layout" content="admin" />  
  </head>
  <body>
      
    <h2><g:message code="aaf.fr.admin.subject.show.heading" default="Subject {0}" args="[subject.principal]"/></h2>

    <g:render template="/templates/flash" />

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" default="Overview" /></a></li>
      <li><a href="#tab-roles" data-toggle="tab"><g:message code="label.roles" default="Roles"/></a></li>
      <li><a href="#tab-permissions" data-toggle="tab"><g:message code="label.permissions" default="Permissions" /></a></li>
      <li><a href="#tab-sessions" data-toggle="tab"><g:message code="label.sessions" default="Sessions"/></a></li>
    </ul>

    <div class="tab-content">
      <div id="tab-overview" class="tab-pane active">
        <table class="table borderless fixed">
          <tbody>
            <tr>
              <th><g:message code="label.id" default="ID"/></th>
              <td>${fieldValue(bean: subject, field: "id")}</td>
            </tr>
            <tr>
              <th><g:message code="label.principal" default="Principal"/></th>
              <td>${fieldValue(bean: subject, field: "principal")}</td>
            </tr>
            <tr>
              <th><g:message code="label.displayname" default="Display Name"/></th>
              <td>${fieldValue(bean: subject, field: "displayName")}</td>
            </tr>
            <tr>
              <th><g:message code="label.email" default="Email"/></th>
              <td>${fieldValue(bean: subject, field: "email")}</td>
            </tr>
            <tr>
              <th><g:message code="label.enabled" default="Enabled"/></th>
              <td>${fieldValue(bean: subject, field: "enabled")}</td>
            </tr>
          </tbody>
        </table>
          <g:if test="${subject.enabled}">
            <g:form action="disablesubject">
              <g:hiddenField name="id" value="${subject?.id}" />
              <g:submitButton name="submit" value="Disable" class="btn" />
            </g:form>
          </g:if>
          <g:else>
            <g:form action="enablesubject">
              <g:hiddenField name="id" value="${subject?.id}" />
              <g:submitButton name="submit" value="Enable" class="btn" />
            </g:form>
          </g:else>
      </div>

      <div id="tab-roles" class="tab-pane">
        <g:if test="${subject.roles}">
          <table class="table borderless table-sortable">
            <thead>
              <tr>
                <th><g:message code="label.name" default="Name"/></th>
                <th><g:message code="label.description" default="Description"/></th>
                <th/>
              </tr>
            </thead>
            <tbody>
              <g:each in="${subject.roles}" status="i" var="role">
                <tr>
                  <td>${fieldValue(bean: role, field: "name")}</td>
                  <td>${fieldValue(bean: role, field: "description")}</td>
                  <td><g:link controller="role" action="show" id="${role.id}" class="btn btn-small"><g:message code="label.view" default="View"/></g:link></td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </g:if>
        <g:else>
          <p class="alert alert-info">This subject currently has no role membership.</p>
        </g:else>
      </div>

      <div id="tab-permissions" class="tab-pane">
        <g:if test="${subject.permissions}">
          <table class="table borderless table-sortable">
            <thead>
              <tr>
                <th><g:message code="label.type" default="Type"/></th>
                <th><g:message code="label.target" default="Target"/></th>
                <th/>
              </tr>
            </thead>
            <tbody>
              <g:each in="${subject.permissions}" status="i" var="perm">
                <tr>
                  <td>${fieldValue(bean: perm, field: "displayType")}</td>
                  <td>${fieldValue(bean: perm, field: "target")}</td>
                  <td>
                      <g:form method="post" class="form">
                        <g:hiddenField name="id" value="${subject?.id}" />
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
          <p class="alert alert-info">This subject currently has no directly associated permissions.</p>
        </g:else>
        <a href="#" class="show-manage-subject-permissions btn"><g:message code="label.addpermissions" default="Add Permissions"/></a>
        <div class="manage-subject-permissions revealable row-spacer">
          <hr>
          <h3><g:message code="label.addpermission" default="Add Permission"/></h3>
          <g:form method="post" class="form validating">
            <g:hiddenField name="id" value="${subject?.id}" />
            <g:hiddenField name="version" value="${subject?.version}" />
            <fieldset class="form">
              <label><g:message code="label.target" default="Target"/></label>
              <input name="target" class="span4 required" placeholder="target:must:be:colon:seperated:use:*:for:matchall"></input>
            </fieldset>
            <fieldset>
              <g:actionSubmit action="createpermission" class="btn btn-success" value="${message(code: 'label.create', default: 'Create')}" />
            </fieldset>
          </g:form>
        </div>
      </div>

      <div id="tab-sessions" class="tab-pane">
        <table class="table borderless table-sortable">
          <thead>
            <tr>
              <th><g:message code="label.credential" default="Credential"/></th>
              <th><g:message code="label.remotehost" default="Remote Host"/></th>
              <th><g:message code="label.useragent" default="User Agent"/></th>
              <th><g:message code="label.datecreated" default="Date"/></th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${subject.sessionRecords.sort{it.dateCreated}.reverse()}" status="i" var="session">
              <tr  >
                <td>${fieldValue(bean: session, field: "credential")}</td>
                <td>${fieldValue(bean: session, field: "remoteHost")}</td>
                <td>${fieldValue(bean: session, field: "userAgent")}</td>
                <td>${fieldValue(bean: session, field: "dateCreated")}</td>
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>

    </div>
  </body>
</html>

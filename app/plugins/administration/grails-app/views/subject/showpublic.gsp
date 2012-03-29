<html>
  <head>
    <r:require modules="bootstrap, bootstrap-datepicker, validate, datatables, alphanumeric, zenbox, app"/>
    <r:layoutResources/>
  </head>
  <body class="plain">
    <div class="container">
      <div class="row">
        <div class="span12">
          <h2><g:message code="aaf.fr.admin.subject.show.heading" default="Subject {0}" args="[subject.principal]"/></h2>

          <ul class="nav nav-tabs">
            <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" default="Overview" /></a></li>
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
            </div>

            <div id="tab-sessions" class="tab-pane">
              <table class="table borderless">
                <thead>
                  <tr>
                    <th><g:message code="label.credential" default="Credential"/></th>
                    <th><g:message code="label.remotehost" default="Remote Host"/></th>
                    <th><g:message code="label.useragent" default="User Agent"/></th>
                    <th><g:message code="label.datecreated" default="Date"/></th>
                  </tr>
                </thead>
                <tbody>
                  <g:each in="${subject.sessionRecords.sort{it.dateCreated}.reverse().subList(0, subject.sessionRecords.size() > 9 ? 10 : subject.sessionRecords.size() - 1)}" status="i" var="session">
                    <tr>
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
        </div>
      </div>
    </div>

    <r:layoutResources/>

  </body>
</html>

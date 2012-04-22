<div id="descriptorfulladministratorlist">
  <h3><g:message code="templates.fr.descriptor.full.administrators" /></h3>
  <g:if test="${administrators}">
    <p><strong><g:message code="templates.fr.descriptor.report.administrators.detail" default="The following users have complete administrative control for this descriptor."/></strong>
    <table class="table borderless">
      <thead>
        <tr>
          <th><g:message code="label.name" default="Name"/></th>
          <th><g:message code="label.organization" default="Organisation"/></th>
          <th/>
        </tr>
      </thead>
      <tbody>
        <g:each in="${administrators.sort{it.principal}}" var="admin" status="i">
          <tr>
            <td>${fieldValue(bean: admin, field: "cn")}</td>
            <td><g:link controller='organization' action='show' id="${admin.contact?.organization?.id}">${fieldValue(bean: admin, field: "contact.organization.displayName")}</g:link></td>
            <td>
              <fr:hasPermission target="descriptor:${descriptor.id}:manage:administrators">
                <g:form controller="descriptorAdministration" action="revokeFullAdministration" method="DELETE">
                  <g:hiddenField name="id" value="${descriptor.id}" />
                  <g:hiddenField name="subjectID" value="${admin.id}" />
                  <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:admin.id, absolute:true)}" ><g:message code="label.quickview" default="Quick View"/></a>
                  <g:submitButton name="submit" value="${message(code: 'label.revoke', default: 'Revoke')}" class="btn" />
                </g:form>
              </fr:hasPermission>
            </td>
          </tr>
        </g:each>
      </tbody>
    </table>
  </g:if>
  <g:else>
    <h2 class="alert alert-error">No current administrators</h2>
    <div class="span5">
      <p><g:message code="templates.fr.descriptor.administrator.justregistered" default="Have you recently registered this provider? If so you will have recieved an email from Federation Registry stating registration was completed. This email contains within a unique code (10 characters a mix of letters and numbers) that when entered will give you administrative rights. Please look for and enter this code now." /></p>
    </div>
    <div class="offset1 span5">
      <g:form controller="descriptorAdministration" action="grantFullAdministrationToken" method="POST">
        <g:hiddenField name="id" value="${descriptor.id}" />
        <div class="input-prepend">
          <span class="add-on"><strong>CODE</strong> </span><input class="span2" id="token" name="token" size="16" type="text">
        </div>
        <g:submitButton name="submit" value="${message(code: 'label.submitcode', default: 'Submit Code')}" class="btn btn-success btn-large" />
      </g:form>
    </div>
    <div class="span11 row-spacer">
      <p><strong><g:message code="templates.fr.descriptor.administrator.nocode" default="If you have not been provided a code or cannot locate it please log a support request using the links above." /></strong><br><br><br></p>
    </div>
  </g:else>
  <fr:hasPermission target="descriptor:${descriptor.id}:manage:administrators">
    <a href="#" class="show-manage-members btn"><g:message code="label.addadministrator" default="Add Administrator"/></a>
    <div class="manage-role-members revealable row-spacer">
      <g:if test="${subjects && subjects.size() > 0}">
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
                    <g:form controller="descriptorAdministration" action="grantFullAdministration">
                      <g:hiddenField name="id" value="${descriptor.id}" />
                      <g:hiddenField name="subjectID" value="${subject.id}" />
                      <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:subject.id, absolute:true)}" ><g:message code="label.quickview" default="Quick View"/></a>
                      <g:submitButton name="submit" class="btn" value="${message(code: 'label.grant', default: 'Grant Access')}" />
                    </g:form>
                  </td>
                </tr>
              </g:if>
            </g:each>
          </tbody>
        </table>
      </g:if>
      <g:else>
        <p class="alert alert-info"><g:message code="templates.fr.descriptor.administrator.alladded" default="All subjects are currently administrators" /></p>
      </g:else>
    </div>
  </fr:hasPermission>
</div>
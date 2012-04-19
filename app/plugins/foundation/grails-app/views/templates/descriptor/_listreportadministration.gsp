<div id="descriptorreportadministratorlist">
  <h3><g:message code="template.fr.descriptor.report.administrators" default="Report Viewers"/></h3>
  <g:if test="${reportAdministrators}">
    <p><g:message code="template.fr.descriptor.report.administrators.detail" default="The following users are able to view reports for this descriptor but otherwise have no administrative control."/>
    <p><strong><g:message code="template.fr.descriptor.reportadministrator.obtain" default="To obtain access to reports for this descriptor please contact one of the administrators listed below directly." /></strong></p>
    <table class="table borderless">
      <thead>
        <tr>
          <th><g:message code="label.name" default="Name"/></th>
          <th><g:message code="label.organization" default="Organisation"/></th>
          <th/>
        </tr>
      </thead>
      <tbody>
        <g:each in="${reportAdministrators.sort{it.principal}}" var="admin" status="i">
          <tr>
            <td>${fieldValue(bean: admin, field: "cn")}</td>
            <td><g:link controller='organization' action='show' id="${admin.contact?.organization?.id}">${fieldValue(bean: admin, field: "contact.organization.displayName")}</g:link></td>
            <td>
              <fr:hasPermission target="descriptor:${descriptor.id}:manage:administrators">
                <g:form controller="descriptorAdministration" action="revokeReportAdministration" method="DELETE">
                  <g:hiddenField name="id" value="${descriptor.id}" />
                  <g:hiddenField name="subjectID" value="${admin.id}" />
                  <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:admin.id, absolute:true)}" ><g:message code="label.quickview" default="Quick View"/></a>
                  <g:submitButton name="submit" value="${message(code: 'label.revoke', default: 'Grant')}" class="btn" />
                </g:form>
              </fr:hasPermission>
            </td>
          </tr>
        </g:each>
      </tbody>
    </table>
  </g:if>
  <g:else>
    <p class="alert alert-info"><g:message code="template.fr.descriptor.reportadministrator.noresults" default="No specific users have been granted report access at the time" /></p>
  </g:else>

  <fr:hasPermission target="descriptor:${descriptor.id}:manage:administrators">
    <a href="#" class="show-manage-reportadmins btn"><g:message code="label.addreportmembers" default="Add Viewer"/></a>
    <div class="manage-reportadmins revealable row-spacer">
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
      </g:if>
      <g:else>
        <p class="alert alert-info"><g:message code="template.fr.descriptor.administrator.alladded" default="All subjects are currently administrators" /></p>
      </g:else>
    </div>
  </fr:hasPermission>
</div>
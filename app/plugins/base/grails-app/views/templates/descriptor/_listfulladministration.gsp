<div id="descriptorfulladministratorlist">
  <h3><g:message encodeAs="HTML" code="templates.fr.descriptor.full.administrators" /></h3>
  <g:if test="${administrators}">
    <p><strong><g:message encodeAs="HTML" code="templates.fr.descriptor.report.administrators.detail" default="The following users have complete administrative control for this descriptor."/></strong>
    <table class="table borderless">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="label.name" default="Name"/></th>
          <th><g:message encodeAs="HTML" code="label.organization" default="Organisation"/></th>
          <th/>
        </tr>
      </thead>
      <tbody>
        <g:each in="${administrators.sort{it.principal}}" var="admin" status="i">
          <tr>
            <td>${fieldValue(bean: admin, field: "cn")}</td>
            <td><g:link controller='organization' action='show' id="${admin.contact?.organization?.id}">${fieldValue(bean: admin, field: "contact.organization.displayName")}</g:link></td>
            <td>
              <fr:hasPermission target="federation:management:descriptor:${descriptor.id}:manage:administrators">
                <g:form controller="descriptorAdministration" action="revokeFullAdministration" method="DELETE">
                  <g:hiddenField name="id" value="${descriptor.id}" />
                  <g:hiddenField name="subjectID" value="${admin.id}" />
                  <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:admin.id, absolute:true)}" ><g:message encodeAs="HTML" code="label.quickview" default="Quick View"/></a>
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
      <p><g:message encodeAs="HTML" code="templates.fr.descriptor.administrator.justregistered" /></p>
    </div>
    <div class="offset1 span5">
      <form id="addfulladministrator" method="POST" class="validating" action='${g.createLink(controller:"descriptorAdministration", action:"grantFullAdministrationToken")}'>
        <g:hiddenField name="id" value="${descriptor.id}" />
        <div class="input-prepend">
          <span class="add-on"><strong>CODE</strong> </span><input class="span2 required" id="token" name="token" size="16" type="text">
        </div>
        <input type="submit" name="submit" value="${message(code: 'label.submitcode', default: 'Submit Code')}" class="btn btn-success btn-large" />
      </form>
    </div>
    <div class="span11 row-spacer">
      <p><strong><g:message encodeAs="HTML" code="templates.fr.descriptor.administrator.nocode" default="If you have not been provided a code or cannot locate it please log a support request using the links above." /></strong><br><br><br></p>
    </div>
  </g:else>
  <fr:hasPermission target="federation:management:descriptor:${descriptor.id}:manage:administrators">
      <a href="#" class="show-manage-members btn"><g:message encodeAs="HTML" code="label.addadministrator" default="Add Administrator"/></a>
      <div id="manage-role-members" class="revealable span11"></div>
  </fr:hasPermission>
</div>

<r:script>
    $('#addfulladministrator').validate({
      ignore: ":disabled",
      keyup: false,
    });
</r:script>

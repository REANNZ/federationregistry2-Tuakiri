<%! import aaf.fr.foundation.Contact %>
<html>
  <head>  
    <meta name="layout" content="workflow" />
    <title><g:message encodeAs="HTML" code="views.fr.workflow.approval.title" /></title>
  </head>
  <body>
    <h2><g:message encodeAs="HTML" code="views.fr.workflow.approval.heading" /></h2>

    <g:if test="${tasks}">
      <p><g:message encodeAs="HTML" code="views.fr.workflow.approval.descriptive" /></p>
      <table class="table">
        <thead>
          <tr>
            <th><g:message encodeAs="HTML" code="label.name" /></th>
            <th><g:message encodeAs="HTML" code="label.created" /></th>
            <th><g:message encodeAs="HTML" code="label.creator" /></th>
            <th><g:message encodeAs="HTML" code="label.processinstance" /></th>
            <th><g:message encodeAs="HTML" code="label.action" /></th>
          </tr>
        </thead>
        <tbody>
          <g:each in="${tasks}" status="i" var="instance">
            <tr>
              <td>${fieldValue(bean: instance, field: "task.name")}</td>
              <td>${fieldValue(bean: instance, field: "dateCreated")}</td>
              <td>
                <g:if test="${instance.processInstance.params.creator}">
                  <g:set var="contact" value="${Contact.get(instance.processInstance.params.creator)}" />
                  <g:link controller="contacts" action="show" id="${instance.processInstance.params.creator}">${fieldValue(bean: contact, field: "givenName")} ${fieldValue(bean: contact, field: "surname")}</g:link>
                </g:if>
                <g:else>
                  <g:message encodeAs="HTML" code="label.publiccreation" />
                </g:else>
              <td>
                ${fieldValue(bean: instance, field: "processInstance.description")}
                <br><br>
                <g:if test="${instance.processInstance.params.containsKey('identityProvider')}">
                  <g:link controller='identityProvider' action='show' id="${instance.processInstance.params.identityProvider}" class="btn"><g:message encodeAs="HTML" code="label.view"/> <g:message encodeAs="HTML" code="label.identityprovider"/></g:link><br><br>
                </g:if>
                <g:else>
                  <g:if test="${instance.processInstance.params.containsKey('serviceProvider')}">
                    <g:link controller='serviceProvider' action='show' id="${instance.processInstance.params.serviceProvider}" class="btn"><g:message encodeAs="HTML" code="label.view"/> <g:message encodeAs="HTML" code="label.serviceprovider"/></g:link><br><br>
                  </g:if>
                  <g:else>
                    <g:if test="${instance.processInstance.params.containsKey('organization') && instance.processInstance.params.organization.isNumber()}">
                      <g:link controller='organization' action='show' id="${instance.processInstance.params.organization}" class="btn"><g:message encodeAs="HTML" code="label.view"/> <g:message encodeAs="HTML" code="label.organization"/></g:link><br><br>
                    </g:if>
                  </g:else>
                </g:else>  
              </td>
              <td>
                <g:form action="approve" id="${instance.id}" name="submitapproval${i}" style="margin-bottom:24px; padding:0;">
                  <g:submitButton name="submit" class="btn btn-success" value="${g.message(encodeAs:"HTML", code:'label.approve')}"/>
                </g:form>
                <g:each in="${instance.task.rejections}" var="rej">
                    <g:form action="reject" id="${instance.id}" name="submitrejection${i}" style="margin:1px; padding:0;">
                      <g:hiddenField name="rejection" value="${rej.key}" />
                      <g:submitButton name="submit" class="btn btn-danger" value="${rej.value.name}"/>
                    </g:form>
                </g:each>
              </td>
            </tr>
            <g:if test="${i+1 != tasks.size()}">
            <tr>
              <td colspan="4"><hr></td>
            </tr>
            </g:if>
          </g:each>
        </tbody>
      </table>
    </g:if>
    <g:else>
      <p><g:message encodeAs="HTML" code="views.fr.workflow.approval.nothing" /></p>
    </g:else>

  </body>
</html>


<%@ page import="aaf.fr.foundation.ServiceCategory" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'serviceCategory.label', default: 'ServiceCategory')}" />
  </head>
  <body>

    <div id="show-serviceCategory" class="content scaffold-show" role="main">
      <h3>Viewing ServiceCategory</h3>
      <ul class="property-list serviceCategory clean">

        <g:if test="${serviceCategoryInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message encodeAs="HTML" code="serviceCategory.id.label" default="id" /></span></strong>:

            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${serviceCategoryInstance}" field="id"/></span>

        </li>
        </g:if>
        <g:if test="${serviceCategoryInstance?.name}">
        <li class="fieldcontain">
          <strong><span id="name-label" class="property-label"><g:message encodeAs="HTML" code="serviceCategory.name.label" default="Name" /></span></strong>:

            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${serviceCategoryInstance}" field="name"/></span>

        </li>
        </g:if>

        <g:if test="${serviceCategoryInstance?.description}">
        <li class="fieldcontain">
          <strong><span id="description-label" class="property-label"><g:message encodeAs="HTML" code="serviceCategory.description.label" default="Description" /></span></strong>:

            <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${serviceCategoryInstance}" field="description"/></span>

        </li>
        </g:if>
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${serviceCategoryInstance?.id}" />
          <g:link class="edit" action="edit" id="${serviceCategoryInstance?.id}" class="btn btn-info"><g:message encodeAs="HTML" code="label.edit" default="Edit" /></g:link>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>

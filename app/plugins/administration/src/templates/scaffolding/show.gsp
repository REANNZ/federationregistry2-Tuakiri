<% import grails.persistence.Event %>
<%=packageName%>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  </head>
  <body>

    <div id="show-${domainClass.propertyName}" class="content scaffold-show" role="main">
      <h3>Viewing ${domainClass.name}</h3>
      <ul class="property-list ${domainClass.propertyName} clean">
      <%  excludedProps = Event.allEvents.toList() << 'version'
        allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated' << 'id'
        props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) }
        Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
        props.each { p -> %>
        <g:if test="\${${propertyName}?.${p.name}}">
        <li class="fieldcontain">
          <strong><span id="${p.name}-label" class="property-label"><g:message encodeAs="HTML" code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></span></strong>: 
          <%  if (p.isEnum()) { %>
            <span class="property-value" aria-labelledby="${p.name}-label"><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></span>
          <%  } else if (p.oneToMany || p.manyToMany) { %>
            <g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
            <span class="property-value" aria-labelledby="${p.name}-label"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></span>
            </g:each>
          <%  } else if (p.manyToOne || p.oneToOne) { %>
            <span class="property-value" aria-labelledby="${p.name}-label"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link></span>
          <%  } else if (p.type == Boolean || p.type == boolean) { %>
            <span class="property-value" aria-labelledby="${p.name}-label"><g:formatBoolean boolean="\${${propertyName}?.${p.name}}" /></span>
          <%  } else if (p.type == Date || p.type == java.sql.Date || p.type == java.sql.Time || p.type == Calendar) { %>
            <span class="property-value" aria-labelledby="${p.name}-label"><g:formatDate date="\${${propertyName}?.${p.name}}" /></span>
          <%  } else if(!p.type.isArray()) { %>
            <span class="property-value" aria-labelledby="${p.name}-label"><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></span>
          <%  } %>
        </li>
        </g:if>
      <%  } %>
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="\${${propertyName}?.id}" />
          <g:link class="edit" action="edit" id="\${${propertyName}?.id}" class="btn btn-info"><g:message encodeAs="HTML" code="label.edit" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="\${message(code: 'label.delete', default: 'Delete')}" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>

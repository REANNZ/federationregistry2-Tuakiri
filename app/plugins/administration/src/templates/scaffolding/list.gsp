<% import grails.persistence.Event %>
<%=packageName%>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  </head>
  <body>

    <div id="list-${domainClass.propertyName}" class="scaffold-list">
      <h3>List of ${domainClass.name}</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
          <%  excludedProps = Event.allEvents.toList() << 'id' << 'version'
            allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
            props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && it.type != null && !Collection.isAssignableFrom(it.type) }
            Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
            props.eachWithIndex { p, i ->
              if (i < 6) { %>
            <th><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></th>
          <%  }   } %>
          <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="\${${propertyName}List}" status="i" var="${propertyName}">
          <tr class="\${(i % 2) == 0 ? 'even' : 'odd'}">
          <%  props.eachWithIndex { p, i ->
              if (i == 0) { %>
            <td>\${fieldValue(bean: ${propertyName}, field: "${p.name}")}</td>
          <%      } else if (i < 6) {
                if (p.type == Boolean || p.type == boolean) { %>
            <td><g:formatBoolean boolean="\${${propertyName}.${p.name}}" /></td>
          <%          } else if (p.type == Date || p.type == java.sql.Date || p.type == java.sql.Time || p.type == Calendar) { %>
            <td><g:formatDate date="\${${propertyName}.${p.name}}" /></td>
          <%          } else { %>
            <td>\${fieldValue(bean: ${propertyName}, field: "${p.name}")}</td>
          <%  }   }   } %>
            <td><g:link action="show" id="\${${propertyName}.id}" class="btn btn-small"><g:message code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>

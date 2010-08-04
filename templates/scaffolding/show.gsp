<% import grails.persistence.Event %>
<%=packageName%>
<html>
    <head>
        
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="\${${propertyName}?.id}" class="icon icon_edit icon_edit_${domainClass.propertyName}"><g:message code="${domainClass.propertyName}.edit.label"/></g:link>
					</li>
					<n:hasPermission target="${domainClass.propertyName}:delete:\${${propertyName}.id}">
					<li>
						<g:form action="delete" name="delete${domainClass.propertyName}">
							<g:hiddenField name="id" value="\${${propertyName}?.id}"/>
							<n:confirmaction action="document.delete${domainClass.propertyName}.submit()" title="\${message(code: 'delete.confirm.title')}" msg="\${message(code: '${domainClass.propertyName}.delete.confirm.msg')}" accept="\${message(code: 'default.button.accept.label')}" cancel="\${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_${domainClass.propertyName}"><g:message code="${domainClass.propertyName}.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
					                    <%  excludedProps = Event.allEvents.toList() << ['version', 'id']
					                        props = domainClass.properties.findAll { !excludedProps.contains(it.name) }
					                        Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
					                        props.each { p -> %>
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></td>
					                            <%  if (p.isEnum()) { %>
					                            <td valign="top" class="value">\${${propertyName}?.${p.name}?.encodeAsHTML()}</td>
					                            <%  } else if (p.oneToMany || p.manyToMany) { %>
					                            <td valign="top"  class="value">
					                                <ul>
					                                <g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
					                                    <li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            <%  } else if (p.manyToOne || p.oneToOne) { %>
					                            <td valign="top" class="value"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link></td>
					                            <%  } else if (p.type == Boolean.class || p.type == boolean.class) { %>
					                            <td valign="top" class="value"><g:formatBoolean boolean="\${${propertyName}?.${p.name}}" /></td>
					                            <%  } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
					                            <td valign="top" class="value"><g:formatDate date="\${${propertyName}?.${p.name}}" /></td>
					                            <%  } else { %>
					                            <td valign="top" class="value">\${fieldValue(bean: ${propertyName}, field: "${p.name}")}</td>
					                            <%  } %>
					                        </tr>
					                    <%  } %>
					                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>

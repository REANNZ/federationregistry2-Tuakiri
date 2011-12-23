<%@page import="aaf.fr.foundation.AttributeBase" %>
<g:if test="${attributeConsumingServices}">
  <g:each in="${attributeConsumingServices}" status="i" var="acs">
        <div id="acsreqattr${i}">
          <g:render template="/templates/acs/listrequestedattributes" plugin="foundation" model='[acs:acs, requestedAttributes:acs.sortedAttributes(), containerID:"acsreqattr${i}"]' />
        </div>

        <fr:hasPermission target="descriptor:${acs.descriptor.id}:attribute:add">
          <div id="addattribute${i}">
            <n:button onclick="\$('#addattribute${i}').fadeOut(); \$('#newattribute${i}').fadeIn();" label="${message(code:'label.addattribute')}" class="add-button"/>
          </div>

          <div id="newattribute${i}">
            <h3><g:message code="fedreg.templates.acs.reqattributes.add.heading"/></h3>
            <p>
              <g:message code="fedreg.templates.acs.reqattributes.add.details"/>
            </p>
            <form id="newattributedata${i}">
              
              <g:message code="label.attribute"/>
                      <g:select name="attrid" from="${availableAttributes.sort{it.name}}" optionKey="id" optionValue="${{ it.name + ' ( ' + it.oid + ' )' }}" class="required"/>

                      <g:message code="label.reason"/><th>
                      <input name="reasoning" type="text" class="required" size="60"/>
                      <fr:tooltip code='fedreg.help.acs.reason' />
                    
                    <g:message code="label.required"/><th>
                    <g:checkBox name="isrequired" />
                    <fr:tooltip code='fedreg.help.acs.isrequired' />

              <n:button onclick="if(\$('#newattributedata${i}').valid()) fedreg.acs_reqattribute_add(${acs.id}, 'newattributedata${i}', 'acsreqattr${i}' );" label="${message(code:'label.add')}" class="add-button"/>
              <n:button onclick="\$('#newattribute${i}').fadeOut(); \$('#addattribute${i}').fadeIn();" label="${message(code:'label.close')}" class="close-button"/>
            </form>
          </div>
        </fr:hasPermission>

        <div id="acsspecattributes">
          <g:render template="/templates/acs/listspecifiedattributes" plugin="foundation" model='[acs:acs, requestedAttributes:acs.requestedAttributes, specificationAttributes:specificationAttributes]' />
        </div>

  </g:each>
</g:if>
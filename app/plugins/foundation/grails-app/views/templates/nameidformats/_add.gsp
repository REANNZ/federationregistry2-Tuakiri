
<fr:hasPermission target="descriptor:${descriptor.id}:nameidformat:add">

  <%@page import="aaf.fr.foundation.SamlURI" %>
  <%@page import="aaf.fr.foundation.SamlURIType" %>

  <div id="addnameidformat" class="searcharea">
    <a class="show-add-nameid btn info"><g:message code="label.addnameidformat"/></a>
  </div>
  
  <div id="newnameidformat" class="hidden">
    <h4><g:message code="fedreg.templates.nameidformats.add.heading"/></h4>
    <form id="newnameidformatdata">
      <div class="clearfix">
        <label for="formatID"><g:message code="label.nameidformat"/></label>
        <div class="input">
          <g:select name="formatID" from="${SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)}" optionKey="id" optionValue="uri"/>
        </div>
      </div>

      <div class="input">
        <a class="add-nameid btn success"><g:message code="label.add"/></a>
        <a class="cancel-add-nameid btn"><g:message code="label.close"/></a>
      </div>
    </form>
  </div>
  
</fr:hasPermission>
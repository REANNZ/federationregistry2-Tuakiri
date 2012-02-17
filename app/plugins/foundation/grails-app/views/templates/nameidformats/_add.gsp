
<fr:hasPermission target="descriptor:${descriptor.id}:nameidformat:add">

  <%@page import="aaf.fr.foundation.SamlURI" %>
  <%@page import="aaf.fr.foundation.SamlURIType" %>

  <div id="addnameidformat" class="searcharea">
    <a class="show-add-nameid btn"><g:message code="label.addnameidformat"/></a>
  </div>
  
  <div id="newnameidformat" class="revealable">
    <h4><g:message code="fedreg.templates.nameidformats.add.heading"/></h4>
    <form id="newnameidformatdata" class="form-horizontal">
      <div class="control-group">
        <label for="formatID"><g:message code="label.nameidformat"/></label>
        <div class="controls">
          <g:select name="formatID" from="${SamlURI.findAllWhere(type:SamlURIType.NameIdentifierFormat)}" optionKey="id" optionValue="uri" class="span4"/>
        </div>
      </div>

      <div class="form-actions">
        <a class="add-nameid btn btn-success"><g:message code="label.add"/></a>
        <a class="cancel-add-nameid btn"><g:message code="label.cancel"/></a>
      </div>
    </form>
  </div>
  
</fr:hasPermission>
<div id="delete-nameid-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.nameidformats.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.nameidformats.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>
    <a class="btn btn-danger delete-nameid"><g:message code="label.delete" /></a>
  </div>
</div>
<%@page import="aaf.fr.foundation.ServiceCategory" %>
<fr:hasPermission target="federation:management:descriptor:${descriptor.id}:category:add">
  <hr>

  <div id="addcategory" class="">
    <a class="show-addnew-servicecategory btn"><g:message encodeAs="HTML" code="label.addcategory"/></a>
  </div>
  
  <div id="newcategory"  class="revealable">
    <h4><g:message encodeAs="HTML" code="templates.fr.servicecategories.add.heading"/></h4>
    <form id="newservicecategorydata" class="form-horizontal">
      <fieldset>
        <input type="hidden" name="id" value="${descriptor.id}"/>
        
        <div class="control-group">
          <label class="control-label" for="categoryID"><g:message encodeAs="HTML" code="label.category"/></label>
          <div class="controls">
                <g:select name="categoryID" from="${ServiceCategory.list()}" optionKey="id" optionValue="${{ it.name?.encodeAsHTML() }}"/>
          </div>
        </div>

        <div class="form-actions">
          <a class="link-new-category btn btn-success" id="createcategorylink"><g:message encodeAs="HTML" code="label.add"/></a>
          <a class="hide-addnew-servicecategory btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
        </div>
      </fieldset>
    </form>
  </div>
  
</fr:hasPermission>

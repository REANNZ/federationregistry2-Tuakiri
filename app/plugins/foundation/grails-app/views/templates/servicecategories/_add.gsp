<fr:hasPermission target="descriptor:${descriptor.id}:category:add">

  <%@page import="aaf.fr.foundation.ServiceCategory" %>

  <div id="addcategory" class="">
    <a onclick="$('#addcategory').fadeOut(); $('#newcategory').fadeIn(); return false;" class="btn info"><g:message code="label.addcategory"/></a>
  </div>
  
  <div id="newcategory"  class="hidden">
    <h3><g:message code="fedreg.templates.servicecategories.add.heading"/></h3>
    <form id="newservicecategorydata">
      <fieldset>
        <input type="hidden" name="id" value="${descriptor.id}"/>
        
        <div class="clearfix">
          <label for="categoryID"><g:message code="label.category"/></label>
          <div class="input">
                <g:select name="categoryID" from="${ServiceCategory.list()}" optionKey="id" optionValue="name"/>
          </div>
        </div>

        <div class="input">
          <a class="link-new-category btn success" id="createcategorylink"><g:message code="label.add"/></a>
          <a onclick="$('#newcategory').fadeOut(); $('#addcategory').fadeIn(); return false;" class="btn"><g:message code="label.close"/></a>
        </div>
      </fieldset>
    </form>
  </div>
  
</fr:hasPermission>
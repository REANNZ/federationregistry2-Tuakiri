<!DOCTYPE html>

<html>
  <head>
    <title><g:message code='fr.branding.title' default='Federation Registry'/></title>
    <r:require modules="modernizr, bootstrap, bootstrap-datepicker, validate, datatables, alphanumeric, zenbox, app"/>
    <r:layoutResources/>
    <g:layoutHead />
  </head>

  <body>

    <div class="container">
      <header>
        <div class="row">
          <div class="span12">
            <g:render template='/templates/frheader' />
          </div>
        </div>
      </header>

      <nav>
        <div class="row">
          <div class="span12">
            <g:render template='/templates/layouts/administration_nav'/>      
          </div>
        </div>
      </nav>

      <section>
        <g:if test="${controllerName == 'adminDashboard' || controllerName == 'adminConsole' || controllerName == 'role' || controllerName == 'subject'}">
          <div>
            <g:layoutBody/>
          </div>
        </g:if>
        <g:else>
          <div class="row">
            <div class="span2">
              <g:if test="${['attributeBase', 'attributeCategory', 'CAKeyInfo', 'contactType', 'monitorType', 'organizationType', 'samlURI'].contains(controllerName)}">
                <g:render template='/templates/object_navigation' plugin='administration'/>
              </g:if>
            </div>
            <div class="span9">
              <g:layoutBody/>
            </div>
          </div>
        </g:else>
      </section>

      <footer>
        <div class="row">
          <div class="span12">
            <g:render template='/templates/frfooter' />
          </div>
        </div>
      </footer>
    </div>
    
    <g:render template="/templates/ajaxload-modal" />
    <r:layoutResources/>

  </body>

</html>

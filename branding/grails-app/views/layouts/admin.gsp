<!DOCTYPE html>

<html>
  <head>
    <title><g:message encodeAs="HTML"  code='fr.branding.title' default='Federation Registry'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <r:require modules="modernizr, bootstrap, bootstrap-responsive-css, bootstrap-notify, bootstrap-datepicker, validate, datatables, alphanumeric, jreject, app"/>
    <g:render template='/templates/frbrowsercheck' />
    <r:layoutResources/>
    <g:layoutHead />
  </head>

  <body>

    <header>
      <div class="container">
        <div class="row">
          <div class="span12">
            <g:render template='/templates/frheader' />
          </div>
        </div>
      </div>
    </header>

    <nav>
      <div class="container">
        <div class="row">
          <div class="span12">
            <g:render template='/templates/layouts/administration_nav' />  
          </div>
        </div>
      </div>
    </nav>

    <section>
      <div class="container">
        <div class='notifications top-right'></div>
        <g:if test="${controllerName == 'adminDashboard' || controllerName == 'adminConsole' || controllerName == 'role' || controllerName == 'subject'}">
          <g:layoutBody/>
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
      </div>
    </section>

    <footer>
      <div class="container">
        <div class="row">
          <div class="span12">
            <g:render template='/templates/frfooter' />
          </div>
        </div>
      </div>
    </footer>
    
    <g:render template="/templates/ajaxload-modal" />
    <r:layoutResources/>

  </body>

</html>

<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
    <r:require modules="bootstrap, datatables, zenbox, app"/>
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
            <g:render template='/templates/frtopnavigation'/>      
          </div>
        </div>
      </nav>

      <section>
        <div class="row">
          <div class="span2">
            <ul class="well nav nav-list">
              <li class="nav-header">
                Attributes
              </li>
              <li class="${controllerName == 'attributeBase' && actionName == 'list' ? 'active' : ''}">
                <g:link controller="attributeBase" action="list">List</g:link></li>
              <li class="${controllerName == 'attributeBase' && actionName == 'create' ? 'active' : ''}">
                <g:link controller="attributeBase" action="create">Create</g:link></li>

              <li class="nav-header">
                Organisation Types
              </li>
              <li class="${controllerName == 'organizationType' && actionName == 'list' ? 'active' : ''}">
                <g:link controller="organizationType" action="list">List</g:link></li>
              <li class="${controllerName == 'organizationType' && actionName == 'create' ? 'active' : ''}">
                <g:link controller="organizationType" action="create">Create</g:link></li>


              <li class="nav-header">
                Monitor Types
              </li>
              <li class="${controllerName == 'monitorType' && actionName == 'list' ? 'active' : ''}">
                <g:link controller="monitorType" action="list">List</g:link></li>
              <li class="${controllerName == 'monitorType' && actionName == 'create' ? 'active' : ''}">
                <g:link controller="monitorType" action="create">Create</g:link></li>

              <li class="nav-header">
                CA Key Info / Certs
              </li>
              <li class="${controllerName == 'CAKeyInfo' && actionName == 'list' ? 'active' : ''}">
                <g:link controller="CAKeyInfo" action="list">List</g:link></li>
              <li class="${controllerName == 'CAKeyInfo' && actionName == 'create' ? 'active' : ''}">
                <g:link controller="CAKeyInfo" action="create">Create</g:link></li>

              <li class="nav-header">
                SAML URI
              </li>
              <li class="${controllerName == 'samlURI' && actionName == 'list' ? 'active' : ''}">
                <g:link controller="samlURI" action="list">List</g:link></li>
              <li class="${controllerName == 'samlURI' && actionName == 'create' ? 'active' : ''}">
                <g:link controller="samlURI" action="create">Create</g:link></li>

              <li class="nav-header nav-sectionheader">
                Other
              </li>
              <li class="${controllerName == 'adminConsole' ? 'active' : ''}">
                <g:link controller="adminConsole" action="index"><g:message code="fedreg.navigation.admin.console" /></g:link>
              </li>

            </ul>
          </div>
          <div class="span9">
            <g:layoutBody/>
          </div>
        </div>
      </section>

      <footer>
        <div class="row">
          <div class="span12">
            <g:render template='/templates/frfooter' />
          </div>
        </div>
      </footer>
    </div>
    
    <r:layoutResources/>

  </body>

</html>

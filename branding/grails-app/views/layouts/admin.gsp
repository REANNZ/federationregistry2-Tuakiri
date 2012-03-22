<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
    <r:require modules="bootstrap, zenbox, app"/>
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
                Objects
              </li>

              <li class="nav-header">
                Access Control
              </li>

              <li class="nav-header">
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

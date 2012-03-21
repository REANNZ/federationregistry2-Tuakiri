<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /></title>
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
            <ul class="level1">
              <li class="${controllerName == 'dashboard' ? 'directactive':''}">
                <g:link controller="auth"><g:message code="fedreg.navigation.dashboard" /></g:link>
              </li>
              <li class="${controllerName == 'bootstrap' && ['organization', 'saveorganization','organizationregistered'].contains(actionName) ? 'directactive':''}">
                <g:link controller="bootstrap" action="organization"><g:message code="fedreg.navigation.registerorganization" /></g:link>
              </li>
              <li class="${controllerName == 'bootstrap' && ['idp', 'saveidp','idpregistered'].contains(actionName) ? 'directactive':''}">
                <g:link controller="bootstrap" action="idp"><g:message code="fedreg.navigation.registeridentityprovider" /></g:link>
              </li>
              <li class="${controllerName == 'bootstrap' && ['sp', 'savesp','spregistered'].contains(actionName) ? 'directactive':''}">
                <g:link controller="bootstrap" action="sp"><g:message code="fedreg.navigation.registerserviceprovider" /></g:link>
              </li>

              <li><a style="color: #fff;" href="http://support.aaf.edu.au/forums"><g:message code="fedreg.navigation.help" /></a></li>
              <li><a style="color: #fff;" href="#" onClick="script: Zenbox.show(); return false;"><g:message code="fedreg.navigation.support" /></a></li>
            </ul>
          </div>
        </div>
      </nav>

      <section>
        <g:layoutBody/>
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

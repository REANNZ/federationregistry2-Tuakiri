<!DOCTYPE html>

<html>
  <head>
    <title><g:message code='fr.branding.title' default='Federation Registry'/></title>
    <r:require modules="modernizr, bootstrap, zenbox, app"/>
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
            <g:if test="${controllerName == 'metadata'}">
              <ul class="level2a">
                <li class="${actionName == 'view' ? 'active':''}"><g:link controller="metadata" action="view"><g:message code="fr.branding.nav.currentmetadata" default="Current"/></g:link></li>
                <li class="${actionName == 'viewall' ? 'active':''}"><g:link controller="metadata" action="viewall"><g:message code="fr.branding.nav.allmetadata" default="All"/></g:link></li>
              </ul>
            </g:if>
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

<!DOCTYPE html>

<html>
  <head>
    <title><g:message encodeAs="HTML"  code='fr.branding.title' default='Federation Registry'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <r:require modules="modernizr, bootstrap, bootstrap-responsive-css, bootstrap-notify, jreject, app"/>
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
            <g:render template='/templates/frtopnavigation'/>
            <g:if test="${controllerName == 'metadata'}">
              <ul class="level2a">
                <li class="${actionName == 'view' ? 'active':''}"><g:link controller="metadata" action="view"><g:message encodeAs="HTML"  code="fr.branding.nav.currentmetadata" default="Current"/></g:link></li>
                <li class="${actionName == 'viewall' ? 'active':''}"><g:link controller="metadata" action="viewall"><g:message encodeAs="HTML"  code="fr.branding.nav.allmetadata" default="All"/></g:link></li>
              </ul>
            </g:if>
          </div>
        </div>
      </div>
    </nav>

    <section>
      <div class="container">
        <div class='notifications top-right'></div>
        <g:layoutBody/>
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

    <r:layoutResources/>
  </body>
</html>

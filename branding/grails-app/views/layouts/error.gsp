<!DOCTYPE html>

<html>
  <head>
    <title><g:message encodeAs="HTML"  code='fr.branding.title' default='Federation Registry'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <r:require modules="modernizr, bootstrap, bootstrap-responsive-css, bootstrap-notify, app"/>
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
            <ul class="level1">
              <li class="${controllerName == 'dashboard' ? 'directactive':''}">
                <g:link controller="dashboard"><g:message encodeAs="HTML"  code="fr.branding.nav.dashboard" default="Dashboard"/></g:link>
              </li>
              <li><a style="color: #fff;" href="#" onClick="script: Zenbox.show(); return false;"><g:message encodeAs="HTML"  code="fr.branding.nav.support" default="Support"/></a></li>
            </ul>
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

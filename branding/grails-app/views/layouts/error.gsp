<!DOCTYPE html>

<html>
  <head>
    <title><g:message encodeAs="HTML"  code='fr.branding.title' default='Federation Registry'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <r:require modules="modernizr, bootstrap, bootstrap-notify, zenbox, app"/>
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
                <g:link controller="dashboard"><g:message encodeAs="HTML"  code="fr.branding.nav.dashboard" default="Dashboard"/></g:link>
              </li>
              <li><a style="color: #fff;" href="http://support.aaf.edu.au/forums"><g:message encodeAs="HTML"  code="fr.branding.nav.help" default="Help"/></a></li>
              <li><a style="color: #fff;" href="#" onClick="script: Zenbox.show(); return false;"><g:message encodeAs="HTML"  code="fr.branding.nav.support" default="Support"/></a></li>
            </ul>
          </div>
        </div>
      </nav>

      <section>
        <div class='notifications top-right'></div>
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

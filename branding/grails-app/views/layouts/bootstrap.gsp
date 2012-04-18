<!DOCTYPE html>

<html>
  <head>
    <title><g:message code='fr.branding.title' default='Federation Registry'/></title>
    <r:require modules="html5, jquery-ui, blockui, tiptip, jgrowl, datatables, validate, zenbox, alphanumeric, formwizard, zenbox, app"/>
    <r:layoutResources/>
    <g:layoutHead />
  </head>

  <body>
    <header>
      <g:render template='/templates/frheader' />
    </header>

    <nav>
      <ul class="level1">
        <li class="${controllerName == 'initialBootstrap' ? 'directactive':''}">
          <g:link controller="initialBootstrap"><g:message code="fr.branding.nav.bootstrap" Default="Bootstrap"/></g:link>
        </li>
      </ul>
    </nav>

    <section>
      <section>
        <g:layoutBody/>
      </section>
    </section>
    
    <footer>
      <g:render template='/templates/frfooter' />
    </footer>
    
  </body>

</html>
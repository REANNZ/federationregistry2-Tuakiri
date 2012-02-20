<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
    <r:require modules="bootstrap, validate, protovis, modernizr, app"/>
    <r:layoutResources/>
    <g:layoutHead />
  </head>

  <body>

    <header>
      <g:render template='/templates/frheader' />
    </header>
  
    <nav>
      <div class="row">
        <div class="span12">
          <g:render template='/templates/layouts/reporting_nav' />  
        </div>
      </div>
    </nav>

    <section>
      <g:layoutBody/>
    </section>

    <footer>
      <g:render template='/templates/frfooter' />
    </footer>
    
    <r:layoutResources/>
  </body>
</html>

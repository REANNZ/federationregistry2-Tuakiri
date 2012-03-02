<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
    <r:require modules="bootstrap, bootstrap-datepicker, validate, modernizr, highcharts, app"/>
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
            <g:render template='/templates/layouts/reporting_nav' />  
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

<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
    <r:require modules="bootstrap, bootstrap-datepicker, validate, datatables, alphanumeric, zenbox, app"/>
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
            <g:render template='/templates/layouts/workflow_nav' />  
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
    
    <g:render template="/templates/ajaxload-modal" />
    <r:layoutResources/>

  </body>

</html>

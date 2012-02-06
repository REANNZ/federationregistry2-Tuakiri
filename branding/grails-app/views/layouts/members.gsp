<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
    <r:use modules="bootstrap, validate, protovis, modernizr, app"/>
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
            <g:render template='/templates/layouts/members_nav' />  
          </div>
        </div>
      </nav>

      <section>

            <g:if test="${flash.type == 'info'}">
              <div class="alert alert-message info">
                <strong><g:message code="label.info"/></strong>
                <p>${flash.message}<p>
              </div>
            </g:if>
            <g:if test="${flash.type == 'success'}">
              <div class="alert alert-message success">
                <strong><g:message code="label.success"/></strong>
                <p>${flash.message}<p>
              </div>
            </g:if>
            <g:if test="${flash.type == 'error'}">
              <div class="alert alert-message error">
                <strong><g:message code="label.error"/></strong>
                <p>${flash.message}<p>
              </div>
            </g:if>

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

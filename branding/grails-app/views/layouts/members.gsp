<!DOCTYPE html>

<html>
  <head>
    <title><g:message encodeAs="HTML"  code='fr.branding.title' default='Federation Registry'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <r:require modules="modernizr, bootstrap, bootstrap-responsive-css, bootstrap-notify, bootstrap-datepicker, validate, datatables, alphanumeric, app"/>
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
            <g:render template='/templates/layouts/members_nav' />  
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

    
    <g:render template="/templates/ajaxload-modal" />
    <r:layoutResources/>

  </body>

</html>

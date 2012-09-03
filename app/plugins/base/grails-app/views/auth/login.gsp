<html>
  <head>
    <meta name="layout" content="public" />
  </head>
  
  <body>

    <div class="hero-unit">
      <h1>Select login account</h1>
      <p>Please choose either federated or local account login for development purposes. For each local account in development mode different access levels are provided.</p>
      <div class="row">
        <div class="span5">
          <h2>Federated Login</h2>
          <a href="" class="btn btn-info btn-large">Federated Login</a>
        </div>
        <div class="span5">
          <h2>Local Accounts</h2>
          <div class="">
            <h3>Fred Bloggs</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1</p>
              <g:form action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://vho.aaf.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!8698dde3-d9cd-4c44-928c-2b6836bcc4bb" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.sharedToken" value="LGW3wpNaPgwnLoYYsghGbzXXXXXXXX" />
              <g:hiddenField name="attributes.cn" value="Fred Bloggs" />
              <g:hiddenField name="attributes.email" value="fredbloggs@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://vho.aaf.edu.au/idp/shibboleth" />

              <g:submitButton name="Login" class="btn btn-success btn-large"/>
            </g:form>
          </div>
          <div class="">
            <h3>Joe Schmoe</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2</p>
              <g:form action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.sharedToken" value="LGW3wpNaPgwnLoYYsghGbzSSSSSSS" />
              <g:hiddenField name="attributes.cn" value="Joe Schmoe" />
              <g:hiddenField name="attributes.email" value="joeschmoe@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://idp.one.edu.au/idp/shibboleth" />

              <g:submitButton name="Login" class="btn btn-warning btn-large"/>
            </g:form>
          </div>
          <div class="">
            <h3>Max Mustermann</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3</em></strong></p>
              <g:form action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.sharedToken" value="LGW3wpNaPgwnLoYYsghGbzZZZZZZZZ" />
              <g:hiddenField name="attributes.cn" value="Max Mustermann" />
              <g:hiddenField name="attributes.email" value="maxmustermann@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://idp.one.edu.au/idp/shibboleth" />

              <g:submitButton name="Login" class="btn btn-inverse btn-large"/>
            </g:form>
          </div>
      </div>
    </div>

  </body>
</html>
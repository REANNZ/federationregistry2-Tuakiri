<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
  </head>
  
  <body>
    <div style="text-align: center;"><h2>Select login account</h2><br><br></div>

    <h3 style="text-align: center;"><a href="${spsession_url}">Login via Federation</a></h3>
    <br><br>
    <h3 style="text-align: center;">OR</h3> 
    <br><br>
    <table style="border: 0;">
      <tbody>
        <tr>
          <td style="text-align: center;">
            <h3>Fred Bloggs</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1</p>
              <g:form action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.givenName" value="Fred" />
              <g:hiddenField name="attributes.surname" value="Bloggs" />
              <g:hiddenField name="attributes.email" value="fredbloggs@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://vho.aaf.edu.au/idp/shibboleth" />
              <g:hiddenField name="attributes.homeOrganization" value="one.edu.au" />
              <g:hiddenField name="attributes.homeOrganizationType" value="university:australia" />
              <br><br>
              <g:submitButton name="Login" class="save-button"/>
            </g:form>
          </td>
          <td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>OR</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td style="text-align: center;">
            <h3>Joe Schmoe</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2</p>
              <g:form action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.givenName" value="Joe" />
              <g:hiddenField name="attributes.surname" value="Schmoe" />
              <g:hiddenField name="attributes.email" value="joeschmoe@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://vho.aaf.edu.au/idp/shibboleth" />
              <g:hiddenField name="attributes.homeOrganization" value="one.edu.au" />
              <g:hiddenField name="attributes.homeOrganizationType" value="university:australia" />
              <br><br>
              <g:submitButton name="Login" class="save-button"/>
            </g:form>
          </td>
          <td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>OR</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td style="text-align: center;">
            <h3>Max Mustermann</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3</em></strong></p>
              <g:form action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.displayName" value="Max Mustermann" />
              <g:hiddenField name="attributes.email" value="maxmustermann@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://idp.one.edu.au/idp/shibboleth" />
              <g:hiddenField name="attributes.homeOrganization" value="one.edu.au" />
              <g:hiddenField name="attributes.homeOrganizationType" value="university:australia" />
              <br><br>
              <g:submitButton name="Login" class="save-button"/>
            </g:form>
          </td>
        </tr>
      </tbody>
    </table>

  </body>
</html>
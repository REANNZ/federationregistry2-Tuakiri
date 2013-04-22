<html>
  <head>      
      <meta name="layout" content="public" />
  </head>

  <body>
    <div class="hero-unit">
      <h2>Welcome to the AAF Federation Registry</h2>
      <p>Here you can view, manage and report on the Organisations, Identity Providers and Service Providers which make up the AAF.</p>

      <div class="row-spacer">
        <h3>Are you already able to login to AAF connected services?</h3>
      </div>

      <div class="row row-spacer">
        <div class="span5">
          <h4>Yes I can!</h4>
          <g:link class="btn btn-success btn-large" controller="auth">Welcome - Please Login</g:link>
          <br><br>
          <a class="show-problems-logging-on small" href="#problems">Problems logging on?</a>
        </div>

        <div class="span5">
          <h4>Not yet :(</h4>
          <p>You can create your organisation, identity provider or service provider with the AAF below without needing to login. Once you submit your details we'll be in touch, easy.</p>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="organization">Create an Organisation</g:link>
          <br><br>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="idp">Create an Identity Provider</g:link>
          <br><br>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="sp">Create a Service Provider</g:link>
        </div>
      </div>
    </div>

    <div class="problems-logging-on hidden">
      <hr>
      <a name="problems"></a>
      <h2>Problems logging on?</h2>
      <p>Here are some common things you might need to configure at your Identity Provider in order to successfully login.</p>
      <h3>1. Metadata</h3>
      <p>Ensure you've correctly configured AAF metadata for use with your Identity Provider. Take a look at <a href="http://support.aaf.edu.au/entries/22529592-AAF-Metadata">our helpful Metadata guide</a> for more information.
      <h3>2. Time Synchronization</h3>
      <p>
        Ensure your Identity Provider server is synced to an upstream time server, this is critically important for federated authentication.
      </p>
      <h3>3. Required Attributes</h3>
      <p>
        In order to get access to the Federation Registry, you need an account provided by an Identity Provider that is active within the federation. Your Identity Provider <strong>must be configured to release the following attributes</strong> to this service.
      </p>
      <ul>
        <li>commonName</li>
        <li>mail</li>
        <li>eduPersonTargetedID</li>
        <li>auEduPersonSharedToken</li>
      </ul>
      <p>
        Within the AAF we recommend automating this process. Take a look at <a href="http://support.aaf.edu.au/entries/22545567-Automating-Attribute-Release">our helpful Attribute Release guide</a> for more information.
      </p>
    </div>
  </body>
</html>

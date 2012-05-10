<html>
  <head>      
      <meta name="layout" content="public" />
  </head>

  <body>
    <div class="hero-unit">
      <h1>Welcome</h1>
      <p>Welcome to the AAF management tool, Federation Registry. Here you can view, manage and report on the Organisations, Identity Providers and Service Providers which make up the federation.</p>

      <div class="row-spacer">
        <h2>Are you already able to login to AAF connected services?</h2>
      </div>

      <div class="row row-spacer">
        <div class="span5">
          <p><strong>Yes I can!</strong></p>
          <g:link class="btn btn-success btn-large" controller="auth">Welcome - Please Login</g:link>
          <br><br>
          <a class="show-problems-logging-on small" href="#problems">Problems logging on?</a>
        </div>

        <div class="span5">
          <p><strong>Not yet :(</strong>.</p>
          <p>You can create your organisation, identity provider or service provider with the AAF below without needing to login. Once you submit your details we'll be in touch, easy.</p>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="organization">Create an Organisation</g:link>
          <br><br>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="idp">Create an Identity Provider</g:link>
          </a>
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
      <p>Ensure you've correctly configured AAF metadata for use with your Identity Provider. Double check by taking a look at this guide <a href="http://support.aaf.edu.au/entries/338216-three-versions-of-the-aaf-metadata-available">http://support.aaf.edu.au/entries/338216-three-versions-of-the-aaf-metadata-available</a>.
      <h3>2. Time Synchronization</h3>
      <p>
        Ensure your Identity Provider server is synced to an upstream time server, this is critically important for federated authentication.
      </p>
      <h3>3. Required Attributes</h3>
      <p>
        In order to get access to the Federation Registry, you need an account provided by an Identity Provider that is active within the federation. Your Identity Provider <strong>must be configured to release the following attributes</strong> to this service:
        <ul>
          <li>commonName</li>
          <li>mail</li>
          <li>eduPersonTargetedID</li>
          <li>auEduPersonSharedToken</li>
        </ul>
      </p>
      <p>
        Within the AAF we recommend automating this process, take a look at this guide for more information <a href="http://support.aaf.edu.au/entries/321600-automating-attribute-release">http://support.aaf.edu.au/entries/321600-automating-attribute-release</a>. Don't know the value for [YOUR UNIQUE URL]? Get in touch with AAF support who'll be able to help you out.
      </p>
    </div>
  </body>
</html>

<html>
  <head>
    <meta name="layout" content="dashboard" />
  </head>
  <body>

    <g:if test="${tasks}">
      <div class="row">
        <div class="span11">
          <p class="alert alert-block alert-info"><strong>Outstanding Worflows</strong><a class="close" data-dismiss="alert" href="#">&times;</a><br>
          <g:link controller="workflowApproval" action="list">You have ${tasks.size()} remaining to be approved.</g:link></p>
        </div>
      </div>
    </g:if>

    <div class="row row-spacer">
      <div class="span3 offset1 well">
        <h3>My Organisations</h3>
        <g:if test="${organizations}">
          <ul class="dashlist clean">
            <g:each in="${organizations}" var="org">
              <li><a href="${createLink(controller:'organization', action:'show', id:org?.id)}">${fieldValue(bean: org, field: 'displayName')} </a></li>
            </g:each>
          </ul>
        </g:if>
        <g:else>
          <p>You're not managing any organisations.</p>
        </g:else>
        <hr>
        <a href="${createLink(controller:'organization', action:'list')}" class="btn btn-small btn-info">View All</a>
      </div>

      <div class="span3 well">
        <h3>My Identity Providers</h3>
        <g:if test="${identityProviders}">
          <ul class="dashlist clean">
            <g:each in="${identityProviders}" var="idp">
              <li><a href="${createLink(controller:'identityProvider', action:'show', id:idp.id)}">${fieldValue(bean: idp, field: 'displayName')}</a></li>
            </g:each>
          </ul>
        </g:if>
        <g:else>
          <p>You're not managing any Identity Providers</p> 
        </g:else>
        <hr>
        <a href="${createLink(controller:'identityProvider', action:'list')}" class="btn btn-small btn-info">View All</a>
      </div>

      <div class="span3 well">
        <h3>My Service Providers</h3>
        <g:if test="${serviceProviders}">
          <ul class="dashlist clean">
            <g:each in="${serviceProviders}" var="sp">
              <li><a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}">${fieldValue(bean: sp, field: 'displayName')}</a></li>
            </g:each>
          </ul>
        </g:if>
        <g:else>
          <p>You're not managing any Service Providers</p> 
        </g:else>
        <hr>
        <a href="${createLink(controller:'serviceProvider', action:'list')}" class="btn btn-small btn-info">View All</a>
      </div>
    </div>


    <div class="row">
      <div class="span11 offset1">
        <h2>Federation Statistics</h2>
      </div>
      <div class="span3 offset1 well centered">
        <strong class="dashboard-wow">${orgCount}</strong><hr><h4>Organisations</h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${idpCount}</strong><hr><h4>Identity Providers</h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${spCount}</strong><hr><h4>Service Providers</h4>
      </div>
    </div>

    <div class="row row-spacer">
      <div class="span10 offset1">
        <div id="sessions">
          <div class="span9 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        </div>
      </div>
    </div>

    <div class="row row-spacer">
      <div class="span10 offset1">
        <div id="registrations">
          <div class="span9 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        </div>
      </div>
    </div>

    <r:script>
      var summaryregistrationsEndpoint = "${createLink(controller:'federationReports', action:'reportsummaryregistrations')}"
      var summarysessionsEndpoint = "${createLink(controller:'federationReports', action:'reportsummarysessions')}"
      $(document).ready(function() {
        fr.summary_registrations_report('registrations');
        fr.summary_sessions_report('sessions');
      });
    </r:script>

  </body>

</html>

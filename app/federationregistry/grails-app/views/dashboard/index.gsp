
<html>
	<head>
		<meta name="layout" content="dashboard" />
		<title><g:message code="fedreg.view.dashboard.title" /></title>
	</head>
	<body>

			<h2><g:message code="fedreg.view.dashboard.welcome" args="[subject.displayName]" /></h2>
			<p><g:message code="fedreg.view.dashboard.welcome.descriptive"/></p>

      <g:if test="${tasks}">
        
        <p class="alert alert-block alert-info"><strong><g:message code="label.outstandingworkflow"/></strong><a class="close" data-dismiss="alert" href="#">&times;</a><br>
        <g:link controller="workflowApproval" action="list"><g:message code="fedreg.view.dashboard.outstanding.tasks.required" args="[tasks.size()]"/></g:link></p>
      </g:if>

      <div class="row">
        <div class="span3 offset1">
          <h3><g:message code="fedreg.view.dashboard.myorganizations"/></h3>
          <g:if test="${organizations}">
            <ul  class="dashlist">
              <g:each in="${organizations}" var="org">
                <li><a href="${createLink(controller:'organization', action:'show', id:org?.id)}">${fieldValue(bean: org, field: 'displayName')} </a></li>
              </g:each>
            </ul>
          </g:if>
          <g:else>
            <p><g:message code="fedreg.view.dashboard.myorganizations.none"/></p>
          </g:else>
        </div>

        <div class="span3">
          <h3><g:message code="fedreg.view.dashboard.myidentityproviders"/></h3>
          <g:if test="${identityProviders}">
            <ul class="dashlist">
              <g:each in="${identityProviders}" var="idp">
                <li><a href="${createLink(controller:'identityProvider', action:'show', id:idp.id)}">${fieldValue(bean: idp, field: 'displayName')}</a></li>
              </g:each>
            </ul>
          </g:if>
          <g:else>
            <p><g:message code="fedreg.view.dashboard.myidentityproviders.none"/></p> 
          </g:else>
        </div>
        <div class="span3">
          <h3><g:message code="fedreg.view.dashboard.myserviceproviders"/></h3>
          <g:if test="${serviceProviders}">
            <ul class="dashlist">
              <g:each in="${serviceProviders}" var="sp">
                <li><a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}">${fieldValue(bean: sp, field: 'displayName')}</a></li>
              </g:each>
            </ul>
          </g:if>
          <g:else>
            <p><g:message code="fedreg.view.dashboard.myserviceproviders.none"/></p> 
          </g:else>
        </div>
      </div>

      <hr>
      
      <div class="row">
        <div class="span5 offset1">
          <h3><g:message code="fedreg.view.dashboard.federation.statistics"/></h3>
          <p><strong>${orgCount}</strong> <g:message code="label.organizations"/> - <g:link controller="organization" action="list" class="btn btn-small"><g:message code="label.view"/></g:link></p>
          <p><strong>${idpCount}</strong> <g:message code="label.identityproviders"/> - <g:link controller="identityProvider" action="list" class="btn btn-small"><g:message code="label.view"/></g:link></p>
          <p><strong>${spCount}</strong> <g:message code="label.serviceproviders"/> - <g:link controller="serviceProvider" action="list" class="btn btn-small"><g:message code="label.view"/></g:link></p>
          <p><strong>${endpointCount}</strong> <g:message code="label.activeendpoints"/></p>
          <p><strong>${certCounts}</strong> <g:message code="label.activecertificates"/></p>   
        </div>
        
        <div class="span5">
          <h3><fr:principalName /></h3>
          <h4><g:message code="fedreg.view.dashboard.targetedid"/></h4>
          ${fieldValue(bean: subject, field: 'principal')}
          <h4><g:message code="fedreg.view.dashboard.email"/></h4>
          ${fieldValue(bean: subject, field: 'email')}
        </div>  
      </div> 

	</body>

</html>

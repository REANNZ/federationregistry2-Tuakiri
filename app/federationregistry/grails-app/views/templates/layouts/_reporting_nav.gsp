<g:render template='/templates/frtopnavigation'/>

<ul class="level2">
  <fr:hasPermission target="federation:management:reporting">
    <li class="${controllerName == 'federationReports' ? 'active':''}"><g:link controller="federationReports" action="summary"><g:message code="fedreg.navigation.federationreports"/></g:link></li>
  </fr:hasPermission>
  <li class="${controllerName == 'complianceReports' ? 'active':''}"><g:link controller="complianceReports" action="attributesupport"><g:message code="fedreg.navigation.compliance"/></g:link></li>
  <fr:hasPermission target="federation:management:reporting">
    <li class="${controllerName == 'identityProviderReports' ? 'active':''}"><g:link controller="identityProviderReports" action="sessions"><g:message code="fedreg.navigation.idpreports"/></g:link></li>
    <li class="${controllerName == 'serviceProviderReports' ? 'active':''}"><g:link controller="serviceProviderReports" action="sessions"><g:message code="fedreg.navigation.spreports"/></g:link></li>
  </fr:hasPermission>
</ul>

<g:if test="${controllerName in ['complianceReports']}" >
  <ul class="level3a">
    <li class="${actionName == 'attributesupport' ? 'active':''}">
      <g:link controller="complianceReports" action="attributesupport"><g:message code="fedreg.navigation.reporting.compliance.idpsummaryattributesupport" /></g:link>
    </li>
    <li class="${actionName == 'detailedattributesupport' ? 'active':''}">
      <g:link controller="complianceReports" action="detailedattributesupport"><g:message code="fedreg.navigation.reporting.compliance.idpdetailedattributesupport" /></g:link>
    </li>
    <li class="${actionName == 'idpprovidingattribute' ? 'active':''}">
      <g:link controller="complianceReports" action="idpprovidingattribute"><g:message code="fedreg.navigation.reporting.compliance.idpprovidingattribute" /></g:link>
    </li>
    <li class="${actionName == 'compatability' ? 'active':''}">
      <g:link controller="complianceReports" action="compatability"><g:message code="fedreg.navigation.reporting.compliance.compatability" /></g:link>
    </li>
    <li class="${actionName == 'causage' ? 'active':''}">
      <g:link controller="complianceReports" action="causage"><g:message code="fedreg.navigation.reporting.compliance.causage" /></g:link>
    </li>
  </ul>
</g:if>

<fr:hasPermission target="federation:management:reporting">
  <g:if test="${controllerName in ['federationReports']}" >
    <ul class="level3a">
      <li class="${controllerName == 'federationReports' && actionName == 'summary' ? 'active':''}">
        <g:link controller="federationReports" action="summary"><g:message code="fedreg.navigation.reporting.summary" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'registrations' ? 'active':''}">
        <g:link controller="federationReports" action="registrations"><g:message code="fedreg.navigation.reporting.registrations" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'growth' ? 'active':''}">
        <g:link controller="federationReports" action="growth"><g:message code="fedreg.navigation.reporting.growth" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'sessions' ? 'active':''}">
        <g:link controller="federationReports" action="sessions"><g:message code="fedreg.navigation.reporting.sessions" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'idputilization' ? 'active':''}">
        <g:link controller="federationReports" action="idputilization"><g:message code="fedreg.navigation.reporting.idputilization" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'serviceutilization' ? 'active':''}">
        <g:link controller="federationReports" action="serviceutilization"><g:message code="fedreg.navigation.reporting.serviceutilization" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'dsutilization' ? 'active':''}">
        <g:link controller="federationReports" action="dsutilization"><g:message code="fedreg.navigation.reporting.dsutilization" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'demand' ? 'active':''}">
        <g:link controller="federationReports" action="demand"><g:message code="fedreg.navigation.reporting.demand" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'exportonly' ? 'active':''}">
        <g:link controller="federationReports" action="exportonly"><g:message code="fedreg.navigation.reporting.exportonly" /></g:link>
      </li>
    </ul> 
  </g:if>

  <g:if test="${controllerName in ['identityProviderReports']}" >
    <ul class="level3a">
      <li class="${controllerName == 'identityProviderReports' && actionName == 'sessions' ? 'active':''}">
        <g:link controller="identityProviderReports" action="sessions"><g:message code="fedreg.navigation.reporting.idp.sessions" /></g:link>
      </li>
      <li class="${controllerName == 'identityProviderReports' && actionName == 'utilization' ? 'active':''}">
        <g:link controller="identityProviderReports" action="utilization"><g:message code="fedreg.navigation.reporting.idp.utilization" /></g:link>
      </li>
      <li class="${controllerName == 'identityProviderReports' && actionName == 'demand' ? 'active':''}">
        <g:link controller="identityProviderReports" action="demand"><g:message code="fedreg.navigation.reporting.idp.demand" /></g:link>
      </li>
      <li class="${controllerName == 'identityProviderReports' && actionName == 'connections' ? 'active':''}">
        <g:link controller="identityProviderReports" action="connections"><g:message code="fedreg.navigation.reporting.idp.connections" /></g:link>
      </li>
    </ul> 
  </g:if>

  <g:if test="${controllerName in ['serviceProviderReports']}" >
    <ul class="level3a">
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'sessions' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="sessions"><g:message code="fedreg.navigation.reporting.idp.sessions" /></g:link>
      </li>
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'utilization' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="utilization"><g:message code="fedreg.navigation.reporting.idp.utilization" /></g:link>
      </li>
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'demand' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="demand"><g:message code="fedreg.navigation.reporting.idp.demand" /></g:link>
      </li>
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'connections' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="connections"><g:message code="fedreg.navigation.reporting.idp.connections" /></g:link>
      </li>
    </ul> 
  </g:if>
</fr:hasPermission>
<g:render template='/templates/frtopnavigation'/>

<ul class="level2">
  <fr:hasPermission target="federation:management:reporting">
    <li class="${controllerName == 'federationReports' ? 'active':''}"><g:link controller="federationReports" action="summary"><g:message code="fr.branding.nav.reports.federation" default="Federation"/></g:link></li>
  </fr:hasPermission>
  <li class="${controllerName == 'complianceReports' ? 'active':''}"><g:link controller="complianceReports" action="attributesupport"><g:message code="fr.branding.nav.reports.compliance" default="Compliance"/></g:link></li>
  <fr:hasPermission target="federation:management:reporting">
    <li class="${controllerName == 'identityProviderReports' ? 'active':''}"><g:link controller="identityProviderReports" action="sessions"><g:message code="fr.branding.nav.reports.idpreports" default="Identity Provider"/></g:link></li>
    <li class="${controllerName == 'serviceProviderReports' ? 'active':''}"><g:link controller="serviceProviderReports" action="sessions"><g:message code="ffr.branding.nav.reports.spreports" default="Service Provider"/></g:link></li>
  </fr:hasPermission>
</ul>

<g:if test="${controllerName in ['complianceReports']}" >
  <ul class="level3a">
    <li class="${actionName == 'attributesupport' ? 'active':''}">
      <g:link controller="complianceReports" action="attributesupport"><g:message code="fr.branding.nav.reports.compliance.idpsummaryattributesupport" default="Attribute Support"/></g:link>
    </li>
    <li class="${actionName == 'detailedattributesupport' ? 'active':''}">
      <g:link controller="complianceReports" action="detailedattributesupport"><g:message code="fr.branding.nav.reports.compliance.idpdetailedattributesupport" default="Detailed Attribute Support"/></g:link>
    </li>
    <li class="${actionName == 'idpprovidingattribute' ? 'active':''}">
      <g:link controller="complianceReports" action="idpprovidingattribute"><g:message code="fr.branding.nav.reports.compliance.idpprovidingattribute" default="Providing Attribute"/></g:link>
    </li>
    <li class="${actionName == 'compatability' ? 'active':''}">
      <g:link controller="complianceReports" action="compatability"><g:message code="fr.branding.nav.reports.compliance.compatability" default="Compatibility"/></g:link>
    </li>
    <fr:hasPermission target="federation:management:reporting">
      <li class="${actionName == 'causage' ? 'active':''}">
        <g:link controller="complianceReports" action="causage"><g:message code="fr.branding.nav.reports.compliance.causage" default="CA Usage"/></g:link>
      </li>
    </fr:hasPermission>
  </ul>
</g:if>

<fr:hasPermission target="federation:management:reporting">
  <g:if test="${controllerName in ['federationReports']}" >
    <ul class="level3a">
      <li class="${controllerName == 'federationReports' && actionName == 'summary' ? 'active':''}">
        <g:link controller="federationReports" action="summary"><g:message code="fr.branding.nav.reports.summary"  default="Summary"/></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'registrations' ? 'active':''}">
        <g:link controller="federationReports" action="registrations"><g:message code="fr.branding.nav.reports.registrations" default="Registrations" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'growth' ? 'active':''}">
        <g:link controller="federationReports" action="growth"><g:message code="fr.branding.nav.reports.growth" default="Growth" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'sessions' ? 'active':''}">
        <g:link controller="federationReports" action="sessions"><g:message code="fr.branding.nav.reports.sessions" default="Sessions" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'idputilization' ? 'active':''}">
        <g:link controller="federationReports" action="idputilization"><g:message code="fr.branding.nav.reports.idputilization" default="IdP Utilisation" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'serviceutilization' ? 'active':''}">
        <g:link controller="federationReports" action="serviceutilization"><g:message code="fr.branding.nav.reports.serviceutilization" default="SP Utilisation"/></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'dsutilization' ? 'active':''}">
        <g:link controller="federationReports" action="dsutilization"><g:message code="fr.branding.nav.reports.dsutilization" default="DS Utilisation" /></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'demand' ? 'active':''}">
        <g:link controller="federationReports" action="demand"><g:message code="fr.branding.nav.reports.demand" default="Demand"/></g:link>
      </li>
      <li class="${controllerName == 'federationReports' && actionName == 'exportonly' ? 'active':''}">
        <g:link controller="federationReports" action="exportonly"><g:message code="fr.branding.nav.reports.exportonly" default="Exportable"/></g:link>
      </li>
    </ul> 
  </g:if>

  <g:if test="${controllerName in ['identityProviderReports']}" >
    <ul class="level3a">
      <li class="${controllerName == 'identityProviderReports' && actionName == 'sessions' ? 'active':''}">
        <g:link controller="identityProviderReports" action="sessions"><g:message code="fr.branding.nav.reports.idp.sessions"  default="Sessions"/></g:link>
      </li>
      <li class="${controllerName == 'identityProviderReports' && actionName == 'utilization' ? 'active':''}">
        <g:link controller="identityProviderReports" action="utilization"><g:message code="fr.branding.nav.reports.idp.utilization" default="Utilisation" /></g:link>
      </li>
      <li class="${controllerName == 'identityProviderReports' && actionName == 'demand' ? 'active':''}">
        <g:link controller="identityProviderReports" action="demand"><g:message code="fr.branding.nav.reports.idp.demand" default="Demand" /></g:link>
      </li>
      <li class="${controllerName == 'identityProviderReports' && actionName == 'connections' ? 'active':''}">
        <g:link controller="identityProviderReports" action="connections"><g:message code="fr.branding.nav.reports.idp.connections" default="Connections" /></g:link>
      </li>
    </ul> 
  </g:if>

  <g:if test="${controllerName in ['serviceProviderReports']}" >
    <ul class="level3a">
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'sessions' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="sessions"><g:message code="fr.branding.nav.reports.sp.sessions" default="Sessions" /></g:link>
      </li>
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'utilization' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="utilization"><g:message code="fr.branding.nav.reports.sp.utilization" default="Utilisation" /></g:link>
      </li>
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'demand' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="demand"><g:message code="fr.branding.nav.reports.sp.demand" default="Demand" /></g:link>
      </li>
      <li class="${controllerName == 'serviceProviderReports' && actionName == 'connections' ? 'active':''}">
        <g:link controller="serviceProviderReports" action="connections"><g:message code="fr.branding.nav.reports.sp.connections" default="Connections" /></g:link>
      </li>
    </ul> 
  </g:if>
</fr:hasPermission>
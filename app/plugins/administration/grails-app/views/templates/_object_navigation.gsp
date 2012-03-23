<ul class="well nav nav-list">

  <li class="nav-header">
    Attributes
  </li>
  <li class="${controllerName == 'attributeBase' && actionName == 'list' ? 'active' : ''}">
    <g:link controller="attributeBase" action="list">List</g:link></li>
  <li class="${controllerName == 'attributeBase' && actionName == 'create' ? 'active' : ''}">
    <g:link controller="attributeBase" action="create">Create</g:link></li>

  <li class="nav-header">
    Attribute Category
  </li>
  <li class="${controllerName == 'attributeCategory' && actionName == 'list' ? 'active' : ''}">
    <g:link controller="attributeCategory" action="list">List</g:link></li>
  <li class="${controllerName == 'attributeCategory' && actionName == 'create' ? 'active' : ''}">
    <g:link controller="attributeCategory" action="create">Create</g:link></li>

  <li class="nav-header">
    Organisation Types
  </li>
  <li class="${controllerName == 'organizationType' && actionName == 'list' ? 'active' : ''}">
    <g:link controller="organizationType" action="list">List</g:link></li>
  <li class="${controllerName == 'organizationType' && actionName == 'create' ? 'active' : ''}">
    <g:link controller="organizationType" action="create">Create</g:link></li>
  
  <li class="nav-header">
    Contact Type
  </li>
  <li class="${controllerName == 'contactType' && actionName == 'list' ? 'active' : ''}">
    <g:link controller="contactType" action="list">List</g:link></li>
  <li class="${controllerName == 'contactType' && actionName == 'create' ? 'active' : ''}">
    <g:link controller="contactType" action="create">Create</g:link></li>

  <li class="nav-header">
    Monitor Types
  </li>
  <li class="${controllerName == 'monitorType' && actionName == 'list' ? 'active' : ''}">
    <g:link controller="monitorType" action="list">List</g:link></li>
  <li class="${controllerName == 'monitorType' && actionName == 'create' ? 'active' : ''}">
    <g:link controller="monitorType" action="create">Create</g:link></li>

  <li class="nav-header">
    CA Key Info / Certs
  </li>
  <li class="${controllerName == 'CAKeyInfo' && actionName == 'list' ? 'active' : ''}">
    <g:link controller="CAKeyInfo" action="list">List</g:link></li>
  <li class="${controllerName == 'CAKeyInfo' && actionName == 'create' ? 'active' : ''}">
    <g:link controller="CAKeyInfo" action="create">Create</g:link></li>

  <li class="nav-header">
    SAML URI
  </li>
  <li class="${controllerName == 'samlURI' && actionName == 'list' ? 'active' : ''}">
    <g:link controller="samlURI" action="list">List</g:link></li>
  <li class="${controllerName == 'samlURI' && actionName == 'create' ? 'active' : ''}">
    <g:link controller="samlURI" action="create">Create</g:link></li>

</ul>
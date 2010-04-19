<html>
<head>
<title>Process Manager | Process Definition | ${processDefinition.name}</title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.application}" />
</head>

<body>

<h1>${processDefinition.name}</h1>
<table style="width: 550px;">
    <tr>
        <th style="width: 120px;">Description:</th>
        <td colspan="3">
            <g:if test="${processDefinition.description}">
                ${processDefinition.description}
            </g:if>
            <g:else>
                Nil.
            </g:else>
        </td>
    </tr>
    <tr>
        <th style="width: 120px;">Process Version:</th>
        <td style="width: 50px">${processDefinition.processVersion}</td>
        <th style="width: 120px;">Uploaded by:</th>
        <td><g:if test="${processDefinition.uploadedBy}">
                <workflow:userfullname username="${processDefinition.uploadedBy}"/>
            </g:if>
            <g:else>
                Plugin (during installation)
            </g:else>   
        </td>
    </tr>
    <tr>
        <th style="width: 120px;">Date Uploaded:</th>
        <td colspan="3"><g:formatDate format=" EEE dd/MMM/yyyy, h:mm:ssa" date="${processDefinition.dateCreated}"/></td>
    </tr>
</table>

<p>&nbsp;</p>
<g:if test="${saved}">
<div class="message">${processDefinition.name} saved as version ${processDefinition.processVersion}</div>
</g:if>
<p>&nbsp;</p>

<h3>Process Definition:</h3><br>
<g:render template="loadDSL"/>
</body>
</html>

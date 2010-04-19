<html>
<head>
<title>Process Manager for ${authenticatedUser.profile.fullName}</title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.application}" />
</head>

<body>
<table style="width:1000px; border: 0px;">
    <tr>
        <td>
        <h1>Process Manager for ${authenticatedUser.profile.fullName}</h1>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <g:include controller="processManager" action="myTasks" />
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <g:include controller="processManager" action="myProcesses" />
        </td>
    </tr>
</table>

</body>
</html>

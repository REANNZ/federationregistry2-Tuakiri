<html>
<head>
<title>Process Manager</title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.administration}" />
<calendar:resources lang="en" theme="blue"/>

</head>

<body>
<table >
<tr>
<td>

<h1>Process Manager</h1>

<p>&nbsp;</p>
<p>&nbsp;</p>
<h2>Search Criteria</h2><br>

<g:form name="search" action="${params.action}">
<table >
    <tr>
        <td ><b>General Search</b></td>
        <td ><a href="${createLink(action: 'dateSearch')}">Date Search</a></td>
        <td >&nbsp;</td>
    </tr>
</table>
<table >
    <tr>
        <td>
            <table >
                <tr>
                    <td ><i><b>Process ID:</b></i></td>
                    <td ><input type="text" name="processId" size="5" value="${adminCommand.processId}"></td>
                    <td ><i>OR...</i></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td><hr size="1" ></td>
    </tr>
    <tr>
        <td>
            <table >
                <tr>
                        <td ><i><b>Process Name:</b></i></td>
                    <td colspan="3" ><input type="text" name="processName" size="30" value="${adminCommand.processName}"></td>
                </tr>
                <tr>
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <td ><i><b>Status:</b></i></td>
                    <td ><g:select name="status" from="${[''] + fedreg.workflow.ProcessStatus.collect { it }}" value="${adminCommand.status}" /></td>

                    <td ><i><b>Initiated by:</b></i></td>
                    <td ><g:textField name="initiatedBy" value="${adminCommand.initiatedBy}" /></td>
                </tr>
            </table>
        </td>
    </tr>    
    <tr>
        <td><hr size="1" ></td>
    </tr>
    <tr>
        <td>
            <table >
                <tr>
                    <td><g:submitButton name="search" value="Search" /></td>
                </tr>
            </table>
       </td>
    </tr>
</table>
</g:form>

<p>&nbsp;</p>
<p>&nbsp;</p>

<g:render template="searchResults"/>
</td>
</tr>
</table>

</body>
</html>

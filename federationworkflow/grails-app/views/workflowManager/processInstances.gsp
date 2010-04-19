<html>
<head>
<title>Process Manager</title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.administration}" />
<calendar:resources lang="en" theme="blue"/>

</head>

<body>
<table style="width: 1000px; border: 0;">
<tr>
<td>

<h1>Process Manager</h1>

<p>&nbsp;</p>
<p>&nbsp;</p>
<h2>Search Criteria</h2><br>

<g:form name="search" action="${params.action}">
<table style="width: 500px; border:0; border-collapse;">
    <tr>
        <td style="width: 100px; border-width: 1px; border-style: solid; border-color: #CCC; background-color: #555;"><b>General Search</b></td>
        <td style="width: 100px; border-width: 1px; border-style: solid; border-color: #CCC; background-color: #EEE"><a href="${createLink(action: 'dateSearch')}">Date Search</a></td>
        <td style="border-width: 0px;">&nbsp;</td>
    </tr>
</table>
<table style="width: 500px">
    <tr>
        <td>
            <table style="width: 100%; border: 0; border-collapse:collapse;">
                <tr>
                    <td style="width: 20%; vertical-align: middle; padding:0px;"><i><b>Process ID:</b></i></td>
                    <td style="width: 15%; vertical-align: middle; padding:0px;"><input type="text" name="processId" size="5" value="${adminCommand.processId}"></td>
                    <td style="width: 65%; vertical-align: middle; padding:0px;"><i>OR...</i></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td><hr size="1" style="color: #AAA;"></td>
    </tr>
    <tr>
        <td>
            <table style="width: 100%; border: 0; border-collapse:collapse;">
                <tr>
                        <td style="width: 20%; vertical-align: middle; padding:0px;"><i><b>Process Name:</b></i></td>
                    <td colspan="3" style="width: 45%; vertical-align: middle; padding:0px;"><input type="text" name="processName" size="30" value="${adminCommand.processName}"></td>
                </tr>
                <tr>
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <td style="width: 10%; vertical-align: middle; padding:0px;"><i><b>Status:</b></i></td>
                    <td style="width: 30%; vertical-align: middle; padding:0px;"><g:select name="status" from="${[''] + fedreg.workflow.engine.ProcessStatus.collect { it }}" value="${adminCommand.status}" /></td>

                    <td style="width: 15%; vertical-align: middle; padding:0px;"><i><b>Initiated by:</b></i></td>
                    <td style="width: 45%; vertical-align: middle; padding:0px;"><g:textField name="initiatedBy" value="${adminCommand.initiatedBy}" /></td>
                </tr>
            </table>
        </td>
    </tr>    
    <tr>
        <td><hr size="1" style="color: #AAA;"></td>
    </tr>
    <tr>
        <td>
            <table style="width: 100%; border: 0; border-collapse:collapse;">
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

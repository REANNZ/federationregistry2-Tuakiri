<html>
<head>
<title>Process Manager</title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.administration}" />
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
        <td style="width: 100px; border-width: 1px; border-style: solid; border-color: #CCC; background-color: #EEE;"><a href="${createLink(action: 'processInstances')}">General Search</a></td>
        <td style="width: 100px; border-width: 1px; border-style: solid; border-color: #CCC; background-color: #555"><b>Date Search</b></td>
        <td style="border-width: 0px;">&nbsp;</td>
    </tr>
</table>
<table style="width: 500px">

    <tr>
        <td>
            <table style="width: 100%; border: 0; border-collapse:collapse;">
                <tr>
                    <td style="width: 15%; vertical-align: middle; padding:0px;"><i><b>Status:</b></i></td>
                    <td style="width: 35%; vertical-align: middle; padding:0px;"><g:select name="status" from="${[''] + fedreg.workflow.engine.ProcessStatus.collect { it }}" value="${adminCommand.status}" /></td>
                    <td style="width: 50%; vertical-align: middle; padding:0px;"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table style="width: 100%; border: 0; border-collapse:collapse;">
                <tr>
                    <td colspan="5" style="vertical-align: middle; padding:0px;"><i><b>Date Initiated:</b></i></td>
                </tr>
                <tr>
                    <td style="width: 6%;  vertical-align: middle; padding:0px;">&nbsp;</td>
                    <td style="width: 8%; vertical-align: middle; padding:0px;">Start:</td>
                    <td style="width: 40%; vertical-align: middle; padding:0px;"><calendar:datePicker name="startDateInitiated" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.startDateInitiated}"/></td>
                    <td style="width: 8%; vertical-align: middle; padding:0px;">End:</td>
                    <td style="width: 40%; vertical-align: middle; padding:0px;"><calendar:datePicker name="endDateInitiated" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.endDateInitiated}"/></td>
                </tr>
                
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table style="width: 100%; border: 0; border-collapse:collapse;">
                <tr>
                    <td colspan="5" style="vertical-align: middle; padding:0px;"><i><b>Date Required:</b></i></td>
                </tr>
                
                <tr>
                    <td style="width: 6%;  vertical-align: middle; padding:0px;">&nbsp;</td>
                    <td style="width: 8%; vertical-align: middle; padding:0px;">Start:</td>
                    <td style="width: 40%; vertical-align: middle; padding:0px;"><calendar:datePicker name="startDateRequired" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.startDateRequired}"/></td>
                    <td style="width: 8%; vertical-align: middle; padding:0px;">End:</td>
                    <td style="width: 40%; vertical-align: middle; padding:0px;"><calendar:datePicker name="endDateRequired" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.endDateRequired}"/></td>
                </tr>

            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table style="width: 100%; border: 0; border-collapse:collapse;">
                <tr>
                    <td colspan="5" style="vertical-align: middle; padding:0px;"><i><b>Date Completed:</b></i></td>
                </tr>
                
                <tr>
                    <td style="width: 6%;  vertical-align: middle; padding:0px;">&nbsp;</td>
                    <td style="width: 8%; vertical-align: middle; padding:0px;">Start:</td>
                    <td style="width: 40%; vertical-align: middle; padding:0px;"><calendar:datePicker name="startDateCompleted" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.startDateCompleted}"/></td>
                    <td style="width: 8%; vertical-align: middle; padding:0px;">End:</td>
                    <td style="width: 40%; vertical-align: middle; padding:0px;"><calendar:datePicker name="endDateCompleted" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.endDateCompleted}"/></td>
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

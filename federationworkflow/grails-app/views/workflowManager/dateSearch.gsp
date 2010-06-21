<html>
<head>
<title>Process Manager</title>
<meta name="layout" content="${grailsApplication.config.workflowEngine.layout.administration}" />
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
        <td ><a href="${createLink(action: 'processInstances')}">General Search</a></td>
        <td ><b>Date Search</b></td>
        <td >&nbsp;</td>
    </tr>
</table>
<table >

    <tr>
        <td>
            <table >
                <tr>
                    <td ><i><b>Status:</b></i></td>
                    <td ><g:select name="status" from="${[''] + fedreg.workflow.ProcessStatus.collect { it }}" value="${adminCommand.status}" /></td>
                    <td ></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table >
                <tr>
                    <td colspan="5" ><i><b>Date Initiated:</b></i></td>
                </tr>
                <tr>
                    <td >&nbsp;</td>
                    <td >Start:</td>
                    <td ><calendar:datePicker name="startDateInitiated" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.startDateInitiated}"/></td>
                    <td >End:</td>
                    <td ><calendar:datePicker name="endDateInitiated" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.endDateInitiated}"/></td>
                </tr>
                
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table >
                <tr>
                    <td colspan="5" ><i><b>Date Required:</b></i></td>
                </tr>
                
                <tr>
                    <td >&nbsp;</td>
                    <td >Start:</td>
                    <td ><calendar:datePicker name="startDateRequired" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.startDateRequired}"/></td>
                    <td >End:</td>
                    <td ><calendar:datePicker name="endDateRequired" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.endDateRequired}"/></td>
                </tr>

            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table >
                <tr>
                    <td colspan="5" ><i><b>Date Completed:</b></i></td>
                </tr>
                
                <tr>
                    <td >&nbsp;</td>
                    <td >Start:</td>
                    <td ><calendar:datePicker name="startDateCompleted" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.startDateCompleted}"/></td>
                    <td >End:</td>
                    <td ><calendar:datePicker name="endDateCompleted" dateFormat="%d/%m/%Y %I:%M:%S%P" showTime="true" defaultValue="${adminCommand.endDateCompleted}"/></td>
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

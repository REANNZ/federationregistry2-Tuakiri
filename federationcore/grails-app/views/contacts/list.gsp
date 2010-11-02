
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.list.title" /></title>
		<script type="text/javascript">
			$(function() {
				$('#contactlist').dataTable( {
						"sPaginationType": "full_numbers",
						"bLengthChange": false,
						"iDisplayLength": 10,
						"aaSorting": [[0, "asc"]],
						"oLanguage": {
							"sSearch": "${g.message(code:'label.filter')}"
						}
					} );
			});
		</script>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.contacts.list.heading" /></h2>

			<table id="contactlist">
				<thead>
					<tr>
						<th><g:message code='label.givenname' /></th>
						<th><g:message code='label.surname' /></th>
						<th><g:message code='label.organization' /></th>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${contactList}" status="i" var="contact">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: contact, field: "givenName")}</td>
						<td>${fieldValue(bean: contact, field: "surname")}</td>
						<td>${fieldValue(bean: contact, field: "organization.displayName")}</td>
						<td><n:button href="${createLink(action:'show', id:contact.id)}" label="label.view"  icon="arrowthick-1-ne"/></td>
					</tr>
				</g:each>
				</tbody>
			</table>
			
		</section>
	</body>
</html>

<script type="text/javascript">
	$(function() {
		contactDialogInit();
		$("#searchcontact").hide();
		$("#availablecontacts").hide();
		
		$("#addcontactlink").click(function() {
			$("#addcontact").hide();
			$("#searchcontact").show('slide');
			$("#email").focus();
		});
		
		$("#closesearchcontactlink").click(function() {
			$("#searchcontact").hide('slide');
			$("#availablecontacts").hide();
			$("#addcontact").show('slide');
			$("#availablecontacts").empty();
		});
	});
	
	function contactDialogInit() {
		$("#contactconfirmationdialog").dialog({
			bgiframe: true,
			resizable: false,
			modal: true,
			autoOpen: false,
			width: 400,
			overlay: {
				backgroundColor: '#000',
				opacity: 0.5
			}
		});
	}
	
	function searchContacts(id) {
		$("#working").trigger("fedreg.working");
		$("#availablecontacts").hide('slide').html('');
		var dataString = "givenName=" + $('#givenName').val() + '&surname=' + $('#surname').val() + '&email=' + $('#email').val()
		$.ajax({
			type: "POST",
			url: contactSearchEndpoint,
			data: dataString,
			success: function(res) {
				$("#availablecontacts").empty();
				$("#availablecontacts").append(res);
				$("#availablecontacts").show('slide');
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function confirmAndChooseContactType(contactID, name, email) {
		activeContact = contactID;
		$("#contactnameconfirmation").html(name);
		$("#contactemailconfirmation").html(email)
		$("#contactconfirmationdialog").dialog( "option", "hide", "drop" );
		$("#contactconfirmationdialog").dialog('open');
	}
	
	function linkContact(contactType) {
		$("#working").trigger("fedreg.working");
		var dataString = "contactID=" + activeContact + "&contactType=" + $('#contactselectedtype').val()
		$.ajax({
			type: "POST",
			url: linkContactEndpoint,
			data: dataString,
			success: function(res) {
				reloadContacts();
				$("#contactconfirmationdialog").dialog('close');
				growl('success', res);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function unlinkContact(contactID) {
		$("#working").trigger("fedreg.working");
		var dataString = "id=" + contactID;
		$.ajax({
			type: "POST",
			url: unlinkContactEndpoint,
			data: dataString,
			success: function(res) {
				reloadContacts();
				growl('success', res);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function reloadContacts() {
		$.ajax({
			type: "GET",
			url: listContactsEndpoint,
			success: function(res) {
				$("#contacts").html(res);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
</script>

<div id="contactmanagement">
	<div id="addcontact" class="searcharea">
		<a id="addcontactlink" href="#" class="button icon icon_add"><g:message code="fedreg.label.addcontact"/></a>
	</div>
	<div id="searchcontact" class="searcharea">
		<h3><g:message code="fedreg.templates.contactmanager.searchforcontacts.heading"/></h3>
		<table class="datatable buttons">
			<thead>
				<tr>
					<th><g:message code="fedreg.label.givenname"/></th>
					<th><g:message code="fedreg.label.surname"/></th>
					<th><g:message code="fedreg.label.email"/></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" id="givenName" name="givenName" class="enhancedinput"/></td>
					<td><input type="text" id="surname" name="surname" class="enhancedinput"/></td>
					<td><input type="text" id="email" name="email" class="enhancedinput"/></td>
					<td>
						<a href="#" onClick="searchContacts(${descriptor.id});" class="button icon icon_magnifier"><g:message code="fedreg.link.search" /></a>
		                <a href="#" id="closesearchcontactlink" class="button icon icon_cross"><g:message code="fedreg.link.close" /></a>
		            </td>
				</tr>
			</tbody>
		</table>		

		<div id="availablecontacts">
		</div>
	</div>

	<div id="contactconfirmationdialog" title="Add Contact">
		<div class="popup">
			<p><g:message code="fedreg.template.contacts.confirmaddition"/></p>
			<p><g:message code="fedreg.template.contacts.selecttype"/></p>
			<g:select id="contactselectedtype" name="contactType" from="${contactTypes}" optionKey="name" optionValue="displayName"/>
			<div class="buttons">
				<a href="#" class="modal_close button icon icon_accept" onClick="linkContact();">Accept</a>
				<a href="#" onClick="$('#contactconfirmationdialog').dialog('close');" class="modal_close button icon icon_cancel">Cancel</a>    
			</div>
		</div>
	</div>
</div>
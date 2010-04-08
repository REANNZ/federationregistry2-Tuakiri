
// CERTIFICATES
function certificateDialogInit() {
	$("#certificateconfirmationdialog").dialog({
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

function verifyNewCertificateData() {
	$("#working").trigger("fedreg.working");
	var dataString = "cert=" + $("#newcertificatedata").val();
	$.ajax({
		type: "POST",
		url: certificateValidationEndpoint,
		data: dataString,
		success: function(res) {
			$("#newcertificatedetails").html(res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

function createNewCertificate() {
	$("#working").trigger("fedreg.working");
	var dataString = "cert=" + $("#newcertificatedata").val() + "&certname=" + $("#newcertificatename").val();
	$.ajax({
		type: "POST",
		url: certificateCreationEndpoint,
		data: dataString,
		success: function(res) {
			$("#newcertificatedata").val('');
			$("#newcertificatedetails").html('');
			$("#newcertificate").hide('slide');
			$("#addcertificate").show('slide');
			growl('success', res);
			listCertificates();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

function removeKeyDescriptor(id) {
	$("#working").trigger("fedreg.working");
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: certificateDeleteEndpoint,
		data: dataString,
		success: function(res) {
			growl('success', res);
			listCertificates();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

function listCertificates() {
	$.ajax({
		type: "GET",
		url: certificateListEndpoint,
		success: function(res) {
			$("#certificates").html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

// CONTACTS
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


// ENDPOINTS

function removeEndpoint(id, endpointType, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: endpointDeleteEndpoint,
		data: dataString,
		success: function(res) {
			growl('success', res);
			listEndpoints(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

function listEndpoints(endpointType, containerID) {
	var dataString = "endpointType=" + endpointType + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		url: endpointListEndpoint,
		data: dataString,
		success: function(res) {
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

function createEndpoint(endpointType, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "endpointType=" + endpointType + "&containerID=" + containerID + "&" + $("#new" + endpointType + "data").serialize();
	$.ajax({
		type: "POST",
		url: endpointCreationEndpoint,
		data: dataString,
		success: function(res) {
			$(':input', "#new" + endpointType + "data")
			 	.not(':button, :submit, :reset, :hidden, select[name=binding]')
			 	.val('')
			listEndpoints(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

function toggleEndpointState(id, endpointType, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: endpointToggleStateEndpoint,
		data: dataString,
		success: function(res) {
			growl('success', res);
			listEndpoints(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

// NAME ID Formats
function removeNameIDFormat(formatID, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "formatID=" + formatID;
	$.ajax({
		type: "POST",
		url: nameIDFormatDeleteEndpoint,
		data: dataString,
		success: function(res) {
			growl('success', res);
			listNameIDFormats(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}

function listNameIDFormats(containerID) {
	var dataString = "containerID=" + containerID
	$.ajax({
		type: "GET",
		url: nameIDFormatListEndpoint,
		data: dataString,
		success: function(res) {
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			growl('error', xhr.responseText);
	    }
	});
}
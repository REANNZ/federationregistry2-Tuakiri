
window.fedreg = window.fedreg || {};
var fedreg = window.fedreg;

//Growl
fedreg.growl = function(type, msg, period) {
    if(!period) period = 2000;

    if (type == 'success')
      $.jGrowl(msg, { life: period, header: '<span class=\'icon icon_tick\'>&nbsp;</span>' });

    if (type == 'error')
      $.jGrowl(msg, { life: period, header: '<span class=\'icon icon_cross\'>&nbsp;</span>' });

    if (type == 'info')
      $.jGrowl(msg, { life: period, header: '<span class=\'icon icon_information\'>&nbsp;</span>' });

    if (type == 'help')
      $.jGrowl(msg, { life: period, header: '<span class=\'icon icon_cross\'>&nbsp;</span>' });

    if (type == 'flagred')
      $.jGrowl(msg, { life: period, header: '<span class=\'icon icon_flag_red\'>&nbsp;</span>' });

    if (type == 'flaggreen')
      $.jGrowl(msg, { life: period, header: '<span class=\'icon icon_flag_green\'>&nbsp;</span>' });

    if (type == 'flagblue')
      $.jGrowl(msg, { life: period, header: '<span class=\'icon icon_flag_blue\'>&nbsp;</span>' });
};

// Dialog support
$(function() {
	$(	'<div id="confirmationdialog" title="" style="display:none" class="popup">'+
		'<p id="confirmationcontent">&nbsp;</p>'+
		'<div class="buttons">'+
		'<a id="confirmaccept" href="#" class="ui-button ui-button-text-icon ui-widget ui-state-default ui-corner-all" onClick="confirmAction(); $(\'#confirmationdialog\').dialog(\'close\');">' +
		'<span class="ui-button-icon-primary ui-icon ui-icon-check"></span>' +
		'<span class="ui-button-text"></span>' +
		'</a>' +
		'<a id="confirmcancel" href="#" class="ui-button ui-button-text-icon ui-widget ui-state-default ui-corner-all" onClick="$(\'#confirmationdialog\').dialog(\'close\');">' +
		'<span class="ui-button-icon-primary ui-icon ui-icon-cancel"></span>' +
		'<span class="ui-button-text"></span>' +
		'</a>' +	 
		'</div>'+
		'</div>').appendTo(document.body);

	$("#confirmationdialog").dialog({
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
});

fedreg.wasConfirmed = function(title, msg, accept, cancel) {
	$("#confirmationtitle").html(title);
	$("#confirmationcontent").html(msg); 
	$("#confirmaccept>.ui-button-text").html(accept);
	$("#confirmcancel>.ui-button-text").html(cancel);
	
	$("#confirmationdialog").dialog('option', 'title', title);
	$("#confirmationdialog").dialog('open');		
};

// Key Descriptor
fedreg.keyDescriptor_verify = function() {
	$("#working").trigger("fedreg.working");
	var dataString = "cert=" + $("#newcertificatedata").val();
	newCertificateValid = false;
	$.ajax({
		async: false,
		type: "POST",
		url: certificateValidationEndpoint,
		data: dataString,
		success: function(res) {
			$("#newcertificatedetails").html(res);
			newCertificateValid = true;
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			$("#newcertificatedetails").html(xhr.responseText);
			newCertificateValid = false;
	    }
	});
};

fedreg.keyDescriptor_create = function() {
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
			fedreg.growl('success', res);
			fedreg.keyDescriptor_list()
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.keyDescriptor_list = function() {
	$.ajax({
		type: "GET",
		url: certificateListEndpoint,
		success: function(res) {
			$("#certificates").html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.keyDescriptor_delete = function(id) {
	$("#working").trigger("fedreg.working");
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: certificateDeleteEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			fedreg.keyDescriptor_list()
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};


// Contacts
fedreg.contact_dialogInit = function() {
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
};

fedreg.contact_search = function(id) {
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
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_confirm = function(contactID, name, email) {
	activeContact = contactID;
	$("#contactnameconfirmation").html(name);
	$("#contactemailconfirmation").html(email)
	$("#contactconfirmationdialog").dialog( "option", "hide", "drop" );
	$("#contactconfirmationdialog").dialog('open');
};

fedreg.contact_create = function(contactType) {
	$("#working").trigger("fedreg.working");
	var dataString = "contactID=" + activeContact + "&contactType=" + $('#contactselectedtype').val()
	$.ajax({
		type: "POST",
		url: contactCreateEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.contact_list();
			$("#contactconfirmationdialog").dialog('close');
			fedreg.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_delete = function(contactID) {
	$("#working").trigger("fedreg.working");
	var dataString = "id=" + contactID;
	$.ajax({
		type: "POST",
		url: contactDeleteEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.contact_list();
			fedreg.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_list = function() {
	$.ajax({
		type: "GET",
		url: contactListEndpoint,
		success: function(res) {
			$("#contacts").html(res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};


// Endpoint
fedreg.endpoint_delete = function(id, endpointType, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: endpointDeleteEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_list = function(endpointType, containerID) {
	var dataString = "endpointType=" + endpointType + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		url: endpointListEndpoint,
		data: dataString,
		success: function(res) {
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_create = function(endpointType, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "endpointType=" + endpointType + "&" + $("#new" + endpointType + "data").serialize();
	$.ajax({
		type: "POST",
		url: endpointCreationEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			$(':input', "#new" + endpointType + "data")
			 	.not(':button, :submit, :reset, :hidden, select[name=binding]')
			 	.val('')
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_toggle = function(id, endpointType, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: endpointToggleStateEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

// Name ID Formats
fedreg.nameIDFormat_remove = function(formatID, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "formatID=" + formatID;
	$.ajax({
		type: "POST",
		url: nameIDFormatRemoveEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			fedreg.nameIDFormat_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.nameIDFormat_list = function(containerID) {
	var dataString = "containerID=" + containerID
	$.ajax({
		type: "GET",
		url: nameIDFormatListEndpoint,
		data: dataString,
		success: function(res) {
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.nameIDFormat_add = function(containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = $("#newnameidformatdata").serialize();
	$.ajax({
		type: "POST",
		url: nameIDFormatAddEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			fedreg.nameIDFormat_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

// Attributes
fedreg.attribute_remove = function(attributeID, containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = "attributeID=" + attributeID;
	$.ajax({
		type: "POST",
		url: attributeRemoveEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			fedreg.attribute_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.attribute_list = function(containerID) {
	var dataString = "containerID=" + containerID
	$.ajax({
		type: "GET",
		url: attributeListEndpoint,
		data: dataString,
		success: function(res) {
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

fedreg.attribute_add = function(containerID) {
	$("#working").trigger("fedreg.working");
	var dataString = $("#newattributedata").serialize();
	$.ajax({
		type: "POST",
		url: attributeAddEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.growl('success', res);
			fedreg.attribute_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			fedreg.growl('error', xhr.responseText);
	    }
	});
};

window.nimble = window.nimble || {};
var nimble = window.nimble;
nimble.endpoints = nimble.endpoints || {};

window.fedreg = window.fedreg || {};
var fedreg = window.fedreg;

$(function() {
	applyBehaviourTo(document);
});

applyBehaviourTo = function(e) {
	$(document).ajaxStop($.unblockUI);
	
	$(".tabs").tabs();
	$(".revealable").hide();
	
	if((Modernizr.svg))
		$('.reportingunsupported').hide();
	else
		$('.reportingsupported').hide();
	
	$('.sortable-table').each(function(index) {
		$(this).dataTable( {
			"sPaginationType": "full_numbers",
			"bLengthChange": false,
			"iDisplayLength": 10,
			"aaSorting": [[0, "asc"]],
			"oLanguage": {
				"sSearch": "Filter: ",
				"sZeroRecords": "No matches found",
				"sInfo": "Showing _START_ to _END_ of _TOTAL_ records",
				"sInfoEmpty": "Showing 0 to 0 of 0 records",
				"sInfoFiltered": "(filtered from _MAX_ total records)",
				"oPaginate": {
					"sFirst": "First",
					"sLast": "Last",
					"sNext": "Next",
					"sPrevious": "Previous"
				}
			},
			"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) { fedreg.stylebuttons(nRow); return nRow; }
		});
	});
	
	fedreg.stylebuttons(e); 
            
	$(e).find('.buttonset').buttonset();
	$(e).find('.tip').tipTip({activation:"hover", maxWidth: "auto", edgeOffset: 10, maxWidth:'200px', defaultPosition:"top"});
}

fedreg.workingOverlay = function() {
	$.blockUI({fadeIn:100, fadeOut:100, centerX: true, centerY: true, message:" working ", css : {border: '0px', padding: '3px', color:'#fff', backgroundColor: '#000'}});
}

fedreg.stylebuttons = function(e) {
	$(e).find('.button').button();
    $.each({
        '.add-button': 'ui-icon-plusthick',
        '.back-button': 'ui-icon-arrowreturnthick-1-w',
        '.save-button': 'ui-icon-check',
		'.update-button' : 'ui-icon-check',
        '.delete-button': 'ui-icon-trash',
        '.edit-button': 'ui-icon-pencil',
        '.view-button': 'ui-icon-circle-arrow-e',
        '.search-button': 'ui-icon-search',
        '.prev-button': 'ui-icon-arrowthick-1-w',
        '.next-button': 'ui-icon-arrowthick-1-e',
        '.download-button': 'ui-icon-arrowreturnthick-1-s',
        '.approve-button': 'ui-icon-check',
        '.redo-button': 'ui-icon-arrowrefresh-1-e',
		'.search-button': 'ui-icon-search',
		'.close-button': 'ui-icon-close',
		'.grant-button': 'ui-icon-circle-plus',
		'.revoke-button': 'ui-icon-circle-minus',
		'.toggle-button': 'ui-icon-power'
    }, function(selector, icon) {
        $(e).find(selector).button({'icons': {'primary': icon}});
    });
};

fedreg.getParameterByName = function(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)')
                    .exec(window.location.search);
 
    return match ? decodeURIComponent(match[1].replace(/\+/g, ' ')) : null;
}

// Descriptor Metadata
fedreg.descriptor_metadata = function() {
//	fedreg.workingOverlay();
	$.ajax({
		type: "GET",
		cache: false,
		url: descriptorMetadataEndpoint,
		success: function(res) {
			var target = $("#descriptormetadata");
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

// Organization Administrators
fedreg.organization_fulladministrator_grant = function(userID) {
	fedreg.workingOverlay();
	var dataString = "userID=" + userID
	$.ajax({
		async: false,
		type: "POST",
		url: organizationFullAdministratorGrantEndpoint,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			fedreg.organization_fulladministrator_list();
			fedreg.organization_fulladministrator_search();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.organization_fulladministrator_revoke = function(userID) {
	fedreg.workingOverlay();
	var dataString = "userID=" + userID
	$.ajax({
		async: false,
		type: "POST",
		url: organizationFullAdministratorRevokeEndpoint,
		data: dataString  + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.organization_fulladministrator_list();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.organization_fulladministrator_list = function() {
	$.ajax({
		type: "GET",
		cache: false,
		url: organizationFullAdministratorListEndpoint,
		success: function(res) {
			var target = $("#organizationfulladministratorlist");
			target.empty();
			target.append(res);
			applyBehaviourTo(target);
			target.fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.organization_fulladministrator_search = function() {
	fedreg.workingOverlay();
	var dataString = "q=" + $('#q').val()
	$.ajax({
		type: "GET",
		cache: false,
		url: organizationFullAdministratorSearchEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#availablefulladministrators");
			target.html(res);
			applyBehaviourTo(target);
			target.fadeIn();
			
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

// Descriptor Administrators
fedreg.descriptor_fulladministrator_grant = function(userID) {
	fedreg.workingOverlay();
	var dataString = "userID=" + userID
	$.ajax({
		async: false,
		type: "POST",
		url: descriptorFullAdministratorGrantEndpoint,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			fedreg.descriptor_fulladministrator_list();
			fedreg.descriptor_fulladministrator_search();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

fedreg.descriptor_fulladministrator_revoke = function(userID) {
	fedreg.workingOverlay();
	var dataString = "userID=" + userID
	$.ajax({
		async: false,
		type: "POST",
		url: descriptorFullAdministratorRevokeEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.descriptor_fulladministrator_list();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

fedreg.descriptor_fulladministrator_list = function() {
	$.ajax({
		type: "GET",
		cache: false,
		url: descriptorFullAdministratorListEndpoint,
		success: function(res) {
			var target = $("#descriptorfulladministratorlist")
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.descriptor_fulladministrator_search = function() {
	fedreg.workingOverlay();
	var dataString = "q=" + $('#q').val()
	$.ajax({
		type: "GET",
		cache: false,
		url: descriptorFullAdministratorSearchEndpoint,
		data: dataString,
		success: function(res) {	
			var target = $("#availablefulladministrators");
			target.html(res);
			applyBehaviourTo(target);
			target.fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

// Key Descriptor
fedreg.keyDescriptor_verify = function(entity) {
	fedreg.workingOverlay();
	var dataString = $("#cert").serialize() + "&entity=" + entity;
	newCertificateValid = false;
	$.ajax({
		async: false,
		type: "POST",
		url: certificateValidationEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#newcertificatedetails")
			target.html(res);
			applyBehaviourTo(target);
			newCertificateValid = true;
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			$("#newcertificatedetails").html(xhr.responseText);
			newCertificateValid = false;
	    }
	});
};

fedreg.keyDescriptor_create = function() {
	fedreg.workingOverlay();
	var dataString = $("#newcryptoform").serialize();
	$.ajax({
		type: "POST",
		url: certificateCreationEndpoint,
		data: dataString,
		success: function(res) {
			$("#newcertificatedata").val('');
			$("#newcertificatedetails").html('');
			$("#newcertificate").fadeOut();
			$("#addcertificate").fadeIn();
			nimble.growl('success', res);
			fedreg.keyDescriptor_list();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.keyDescriptor_list = function() {
	$.ajax({
		type: "GET",
		cache: false,
		url: certificateListEndpoint,
		success: function(res) {
			var target = $("#certificates");
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.keyDescriptor_delete = function(id) {
	fedreg.workingOverlay();
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: certificateDeleteEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.keyDescriptor_list()
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

// Monitors
fedreg.monitor_create = function() {
	fedreg.workingOverlay();
	var dataString = $("#newmonitordata").serialize();
	$.ajax({
		type: "POST",
		url: monitorCreateEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.monitor_list();
			nimble.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.monitor_delete = function(monitorID) {
	fedreg.workingOverlay();
	var dataString = "id=" + monitorID;
	$.ajax({
		type: "POST",
		url: monitorDeleteEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			fedreg.monitor_list();
			nimble.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.monitor_list = function(containerID) {
	$.ajax({
		type: "GET",
		cache: false,
		url: monitorListEndpoint,
		success: function(res) {
			var target = $("#monitors");
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
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
	fedreg.workingOverlay();
	var dataString = "givenName=" + $('#givenName').val() + '&surname=' + $('#surname').val() + '&email=' + $('#email').val()
	$.ajax({
		type: "GET",
		cache: false,
		url: contactSearchEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#availablecontacts");
			target.html(res);
			applyBehaviourTo(target);
			target.fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_confirm = function(contactID, name, email) {
	activeContact = contactID;
	$("#contactnameconfirmation").html(name);
	$("#contactemailconfirmation").html(email);
	
	var target = $("#contactconfirmationdialog")
	target.dialog('open');
};

fedreg.contact_create = function(contactType) {
	fedreg.workingOverlay();
	var dataString = "contactID=" + activeContact + "&contactType=" + $('#contactselectedtype').val()
	$.ajax({
		type: "POST",
		url: contactCreateEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.contact_list();
			$("#contactconfirmationdialog").dialog('close');
			nimble.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_delete = function(contactID) {
	fedreg.workingOverlay();
	var dataString = "id=" + contactID;
	$.ajax({
		type: "POST",
		url: contactDeleteEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			fedreg.contact_list();
			nimble.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_list = function() {
	$.ajax({
		type: "GET",
		cache: false,
		url: contactListEndpoint,
		success: function(res) {
			var target = $("#contacts");
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

// Organization contacts
fedreg.orgcontact_search = function(id) {
	fedreg.workingOverlay();
	var dataString = "givenName=" + $('#givenName').val() + '&surname=' + $('#surname').val() + '&email=' + $('#email').val()
	$.ajax({
		type: "GET",
		cache: false,
		url: contactSearchEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#availablecontacts");
			target.html(res);
			applyBehaviourTo(target);
			target.fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.orgcontact_confirm = function(contactID, name, email) {
	activeContact = contactID;
	$("#contactnameconfirmation").html(name);
	$("#contactemailconfirmation").html(email);
	
	var target = $("#contactconfirmationdialog")
	target.dialog('open');
};

fedreg.orgcontact_create = function(contactType) {
	fedreg.workingOverlay();
	var dataString = "contactID=" + activeContact + "&contactType=" + $('#contactselectedtype').val()
	$.ajax({
		type: "POST",
		url: contactCreateEndpoint,
		data: dataString,
		success: function(res) {
			fedreg.contact_list();
			$("#contactconfirmationdialog").dialog('close');
			nimble.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.orgcontact_delete = function(contactID) {
	fedreg.workingOverlay();
	var dataString = "id=" + contactID;
	$.ajax({
		type: "POST",
		url: contactDeleteEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			fedreg.contact_list();
			nimble.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.orgcontact_list = function() {
	$.ajax({
		type: "GET",
		cache: false,
		url: contactListEndpoint,
		success: function(res) {
			var target = $("#contacts");
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};


// Endpoint
fedreg.endpoint_edit = function(id, endpointType, containerID) {
	fedreg.workingOverlay();
	$("#endpoint-"+id).fadeOut();
	var dataString = "id=" + id + "&endpointType=" + endpointType + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		cache: false,
		url: endpointEditEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#endpoint-"+id);
			target.empty();
			target.html(res);
			applyBehaviourTo(target);
			target.fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

fedreg.endpoint_update = function(id, endpointType, containerID) {
	fedreg.workingOverlay();
	var dataString =  $("#endpoint-edit-" + id).serialize();
	$.ajax({
		type: "POST",
		url: endpointUpdateEndpoint,
		data: dataString + "&_method=put",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

fedreg.endpoint_delete = function(id, endpointType, containerID) {
	fedreg.workingOverlay();
	var dataString = "id=" + id + "&endpointType=" + endpointType;
	$.ajax({
		type: "POST",
		url: endpointDeleteEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_list = function(endpointType, containerID) {
	var dataString = "endpointType=" + endpointType + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		cache: false,
		url: endpointListEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#"+containerID);
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_create = function(endpointType, containerID) {
	fedreg.workingOverlay();
	var dataString = "endpointType=" + endpointType + "&" + $("#new" + endpointType + "data").serialize();
	$.ajax({
		type: "POST",
		url: endpointCreationEndpoint,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			$("#new" + endpointType + "data").each(function(){ this.reset(); });
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_toggle = function(id, endpointType, containerID) {
	fedreg.workingOverlay();
	var dataString = "id=" + id;
	$.ajax({
		type: "POST",
		url: endpointToggleStateEndpoint,
		data: dataString + "&_method=put",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_makedefault = function(id, endpointType, containerID) {
	fedreg.workingOverlay();
	var dataString = "id=" + id + "&endpointType=" + endpointType;
	$.ajax({
		type: "POST",
		url: endpointMakeDefaultEndpoint,
		data: dataString + "&_method=put",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

// Attribute Consuming Services
fedreg.acs_reqattribute_add = function(acsID, formID, containerID) {
	fedreg.workingOverlay();
	var dataString = "id=" + acsID + "&" + $("#" + formID).serialize();
	$.ajax({
		type: "POST",
		url: acsAddAttr,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			$(':input[name=reasoning]').val('')
			fedreg.acs_reqattribute_list(acsID, containerID);
			fedreg.acs_specattributes_list(acsID, 'acsspecattributes');
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.acs_reqattribute_remove = function(raID, acsID, containerID) {
	fedreg.workingOverlay();
	var dataString = "raid=" + raID;
	$.ajax({
		type: "POST",
		url: acsRemoveAttr,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.acs_reqattribute_list(acsID, containerID);
			fedreg.acs_specattributes_list(acsID, 'acsspecattributes');
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.acs_reqattribute_update = function(acsID, id, reason, required, containerID) {
	fedreg.workingOverlay();
	var dataString = "id=" + id + "&reasoning=" + reason
	if(required)
		dataString = dataString + "&required=" + required;
	$.ajax({
		type: "POST",
		url: acsUpdateAttr,
		data: dataString + "&_method=put",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.acs_reqattribute_list(acsID, containerID);
			fedreg.acs_specattributes_list(acsID, 'acsspecattributes');
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.acs_reqattribute_list = function(acsID, containerID) {
	var dataString = "id=" + acsID + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		cache: false,
		url: acsListAttr,
		data: dataString,
		success: function(res) {
			var target = $("#"+containerID);
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.acs_specattribute_add = function(id, formID, containerID) {
	fedreg.workingOverlay();
	var dataString = "id=" + id + "&" + $("#" + formID).serialize();
	$.ajax({
		type: "POST",
		url: acsAddSpecAttrVal,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			$(':input', "#" + formID)
			 	.not(':button, :submit, :reset, :hidden, select[name=binding]')
			 	.val('')
			fedreg.acs_specattribute_list(id, containerID)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

fedreg.acs_specattribute_remove = function(id, valueID, containerID) {
	fedreg.workingOverlay();
	var dataString = "id=" + id + "&valueid=" + valueID;
	$.ajax({
		type: "POST",
		url: acsRemoveSpecAttrVal,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.acs_specattribute_list(id, containerID)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

fedreg.acs_specattribute_list = function(id, containerID) {
	var dataString = "id=" + id + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		cache: false,
		url: acsListSpecAttrVal,
		data: dataString,
		success: function(res) {
			var target = $("#"+containerID);
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.acs_specattributes_list = function(id, containerID) {
	var dataString = "id=" + id + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		cache: false,
		url: acsListSpecAttrsVal,
		data: dataString,
		success: function(res) {
			var target = $("#"+containerID);
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

// Name ID Formats
fedreg.nameIDFormat_remove = function(formatID, containerID) {
	fedreg.workingOverlay();
	var dataString = "formatID=" + formatID;
	$.ajax({
		type: "POST",
		url: nameIDFormatRemoveEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.nameIDFormat_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.nameIDFormat_list = function(containerID) {
	var dataString = "containerID=" + containerID
	$.ajax({
		type: "GET",
		cache: false,
		url: nameIDFormatListEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#"+containerID);
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.nameIDFormat_add = function(containerID) {
	fedreg.workingOverlay();
	var dataString = $("#newnameidformatdata").serialize();
	$.ajax({
		type: "POST",
		url: nameIDFormatAddEndpoint,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			fedreg.nameIDFormat_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

// Service Categories
fedreg.serviceCategory_list = function(containerID) {
	var dataString = "containerID=" + containerID
	$.ajax({
		type: "GET",
		cache: false,
		url: serviceCategoryListEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#"+containerID);
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.serviceCategory_add = function(containerID) {
	fedreg.workingOverlay();
	var dataString = $("#newservicecategorydata").serialize();
	$.ajax({
		type: "POST",
		url: serviceCategoryAddEndpoint,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			fedreg.serviceCategory_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.serviceCategory_remove = function(categoryID, containerID) {
	fedreg.workingOverlay();
	var dataString = "categoryID=" + categoryID;
	$.ajax({
		type: "POST",
		url: serviceCategoryRemoveEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.serviceCategory_list(containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

// Attributes
fedreg.attribute_remove = function(attributeID, containerID) {
	fedreg.workingOverlay();
	var dataString = "attributeID=" + attributeID;
	$.ajax({
		type: "POST",
		url: attributeRemoveEndpoint,
		data: dataString + "&_method=delete",
		success: function(res) {
			nimble.growl('success', res);
			fedreg.attribute_list(containerID);
			$('#attrfilpolood').show();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.attribute_list = function(containerID) {
	var dataString = "containerID=" + containerID
	$.ajax({
		type: "GET",
		cache: false,
		url: attributeListEndpoint,
		data: dataString,
		success: function(res) {
			var target = $("#"+containerID);
			target.html(res);
			applyBehaviourTo(target);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.attribute_add = function(containerID) {
	fedreg.workingOverlay();
	var dataString = $("#newattributedata").serialize();
	$.ajax({
		type: "POST",
		url: attributeAddEndpoint,
		data: dataString,
		success: function(res) {
			nimble.growl('success', res);
			fedreg.attribute_list(containerID);
			$('#attrfilpolood').show();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.attributefilter_refresh = function() {
	fedreg.workingOverlay();
	editor.setCode('working...');
	$.ajax({
		type: "GET",
		cache: false,
		url: attributeFilterEndpoint,
		dataType: "text",
		success: function(res) {
			editor.setCode(res);
			$('#attrfilpolood').hide();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.validateCertificate = function() {
	$('#newcertificatedata').removeClass('error');
	fedreg.keyDescriptor_verify($('#entity\\.identifier').val());
	if(!newCertificateValid) {
		$('#newcertificatedata').addClass('error');
	}
};

fedreg.configureIdentityProviderSAML = function(host) {
	$(".samloptional").val("");
	if(host.length > 0) {
		host = host.toLowerCase();
		
		$('#entity\\.identifier').val( knownIDPImpl[currentImpl].entitydescriptor.replace('$host', host));
		$('#idp\\.post\\.uri').val( knownIDPImpl[currentImpl].post.uri.replace('$host', host) );
		$('#idp\\.redirect\\.uri').val( knownIDPImpl[currentImpl].redirect.uri.replace('$host', host) );
		$('#idp\\.artifact\\.uri').val( knownIDPImpl[currentImpl].artifact.uri.replace('$host', host) );
		$('#idp\\.artifact\\.index').val( knownIDPImpl[currentImpl].artifact.index );
		$('#aa\\.attributeservice\\.uri').val( knownIDPImpl[currentImpl].attributeservice.uri.replace('$host', host) );
	}
};

fedreg.configureServiceProviderSAML = function(host) {
	$(".samloptional").val("");
	if(host.length > 0) {
		host = host.toLowerCase();
		
		$('#entity\\.identifier').val( knownSPImpl[currentImpl].entitydescriptor.replace('$host', host) );
		
		$('#sp\\.acs\\.post\\.uri').val( knownSPImpl[currentImpl].acs.post.uri.replace('$host', host) );
		$('#sp\\.acs\\.post\\.index').val( knownSPImpl[currentImpl].acs.post.index );
		
		$('#sp\\.acs\\.artifact\\.uri').val( knownSPImpl[currentImpl].acs.artifact.uri.replace('$host', host) );
		$('#sp\\.acs\\.artifact\\.index').val( knownSPImpl[currentImpl].acs.artifact.index );
		
		if(knownSPImpl[currentImpl].drs) {
			$('#sp\\.drs\\.uri').val( knownSPImpl[currentImpl].drs.uri.replace('$host', host) );
			$('#sp\\.drs\\.index').val( knownSPImpl[currentImpl].drs.index );
		}
		
		if( knownSPImpl[currentImpl].slo ) {
			if( knownSPImpl[currentImpl].slo.artifact )
				$('#sp\\.slo\\.artifact\\.uri').val( knownSPImpl[currentImpl].slo.artifact.uri.replace('$host', host) );
		
			if( knownSPImpl[currentImpl].slo.redirect )
				$('#sp\\.slo\\.redirect\\.uri').val( knownSPImpl[currentImpl].slo.redirect.uri.replace('$host', host) );
		
			if( knownSPImpl[currentImpl].slo.soap )
				$('#sp\\.slo\\.soap\\.uri').val( knownSPImpl[currentImpl].slo.soap.uri.replace('$host', host) );
		
			if( knownSPImpl[currentImpl].slo.post )
				$('#sp\\.slo\\.post\\.uri').val( knownSPImpl[currentImpl].slo.post.uri.replace('$host', host) );
		}
		
		if( knownSPImpl[currentImpl].mnid ) {
			if( knownSPImpl[currentImpl].mnid.artifact )
				$('#sp\\.mnid\\.artifact\\.uri').val( knownSPImpl[currentImpl].mnid.artifact.uri.replace('$host', host) );
		
			if( knownSPImpl[currentImpl].mnid.redirect )
				$('#sp\\.mnid\\.redirect\\.uri').val( knownSPImpl[currentImpl].mnid.redirect.uri.replace('$host', host) );
		
			if( knownSPImpl[currentImpl].mnid.soap )
				$('#sp\\.mnid\\.soap\\.uri').val( knownSPImpl[currentImpl].mnid.soap.uri.replace('$host', host) );
		
			if( knownSPImpl[currentImpl].mnid.post )
				$('#sp\\.mnid\\.post\\.uri').val( knownSPImpl[currentImpl].mnid.post.uri.replace('$host', host) );
		}
	}
};

// Reporting
fedreg.openRefinement = function() {
	$(".reportrefinementopen").hide();
	$(".reportrefinementinput").slideDown();
	
	return false;
};

fedreg.closeRefinement = function() {
	$(".reportrefinementinput").slideUp();
	$(".reportrefinementopen").show();
	
	return false;
};

fedreg.renderIdPReport = function() {
	if (Modernizr.svg) {
		fedreg.workingOverlay();
		fedreg.closeRefinement();
		$(".revealable").hide();

		var dataString = $("#reportrequirements").serialize() + fedreg.includeRobotsInReporting(true);
	
		if( $(".reporttype option:selected").val() == 'connections') {
			$.ajax({url: idpReportsConnectivityEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderIdPConnectivity(data, false);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'sessions') {
			$.ajax({url: idpReportsSessionsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderIdPSessions(data, false);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'totals') {
			$.ajax({url: idpReportsTotalsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderIdPTotals(data, false);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'logins') {
			$.ajax({url: idpReportsLoginsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderIdPLogins(data);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	} else {
		fedreg.toggleReportingContent(false);
	}
};

fedreg.refineIdPReport = function(refinement) {
	if (Modernizr.svg) {
		fedreg.workingOverlay();
		fedreg.closeRefinement();
	
		var dataString = $("#reportrequirements").serialize() + "&" + refinement.serialize() + fedreg.includeRobotsInReporting(true);
	
		if( $(".reporttype option:selected").val() == 'connections') {
			$.ajax({url: idpReportsConnectivityEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderIdPConnectivity(data, true);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'totals') {
			$.ajax({url: idpReportsTotalsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderIdPTotals(data, true);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	} else {
		fedreg.toggleReportingContent(false);
	}
};

fedreg.renderSPReport = function() {
	if (Modernizr.svg) {
		fedreg.workingOverlay();
		fedreg.closeRefinement();
		$(".revealable").hide();
	
		var dataString = $("#reportrequirements").serialize() + fedreg.includeRobotsInReporting(true);
	
		if( $(".reporttype option:selected").val() == 'connections') {
			$.ajax({url: spReportsConnectivityEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderSPConnectivity(data, false);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'sessions') {
			$.ajax({url: spReportsSessionsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderSPSessions(data, false);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'totals') {
			$.ajax({url: spReportsTotalsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderSPTotals(data, false);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'logins') {
			$.ajax({url: spReportsLoginsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderSPLogins(data);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	} else {
		fedreg.toggleReportingContent(false);
	}
};

fedreg.refineSPReport = function(refinement) {
	if (Modernizr.svg) {
		fedreg.workingOverlay();
		fedreg.closeRefinement();
	
		var dataString = $("#reportrequirements").serialize() + "&" + refinement.serialize() + fedreg.includeRobotsInReporting(true);
	
		if( $(".reporttype option:selected").val() == 'connections') {
			$.ajax({url: spReportsConnectivityEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderSPConnectivity(data, true);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( $(".reporttype option:selected").val() == 'totals') {
			$.ajax({url: spReportsTotalsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderSPTotals(data, true);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	} else {
		fedreg.toggleReportingContent(false);
	}
};

fedreg.renderFederationReport = function(type) {
	if (Modernizr.svg) {
		fedreg.workingOverlay();
		fedreg.closeRefinement();
		$(".revealable").hide();
	
		var dataString = $("#reportrequirements").serialize() + fedreg.includeRobotsInReporting(true);
	
		if( type == 'logins') {
			$.ajax({url: federationReportsLoginsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderFederationLogins(data);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( type == 'sessions') {
			$.ajax({url: federationReportsSessionsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderFederationSessions(data);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	
		if( type == 'sessiontotals') {
			$.ajax({url: federationReportsSessionTotalsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderFederationServices(data);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
		
		if( type == 'registrations') {
			if( $("#registrationstype").val() == 'organization') {
				$.ajax({url: federationReportsRegistrationOrganizationsEndpoint, 
					data: dataString,
					dataType: 'json',
					async:true, 
					success: function(data){
						fedreg.renderFederationRegistrations(data, true);
					},
					error: function (xhr, ajaxOptions, thrownError) {
						nimble.growl('error', xhr.responseText);
					}
				});
			}
		}
		
	} else {
		fedreg.toggleReportingContent(false);
	}
};

fedreg.refineFederationReport = function(type, refinement) {
	if (Modernizr.svg) {
		fedreg.workingOverlay();
		fedreg.closeRefinement();
	
		var dataString = $("#reportrequirements").serialize() + "&" + refinement.serialize() + fedreg.includeRobotsInReporting(true);
	
		if( type == 'sessiontotals') {
			$.ajax({url: federationReportsSessionTotalsEndpoint, 
				data: dataString,
				dataType: 'json',
				async:true, 
				success: function(data){
					fedreg.renderFederationServices(data);
				},
				error: function (xhr, ajaxOptions, thrownError) {
					nimble.growl('error', xhr.responseText);
				}
			});
		}
	} else {
		fedreg.toggleReportingContent(false);
	}
};

fedreg.renderFederationSummaryReport = function(type) {
	if (Modernizr.svg) {
		fedreg.toggleReportingContent(true);
		fedreg.workingOverlay();
	
		var dataString = fedreg.includeRobotsInReporting(false);
	
		$.ajax({url: federationReportsSummaryEndpoint, 
			data: dataString,
			dataType: 'json',
			async:true, 
			success: function(data){
				fedreg.renderCreationSummary(data);
			},
			error: function (xhr, ajaxOptions, thrownError) {
				nimble.growl('error', xhr.responseText);
			}
		});
	} else {
		fedreg.toggleReportingContent(false);
	}
};

fedreg.toggleReportingContent = function(supported) {
	if(supported == true) {
		$('.reportingunsupported').hide();
		$('.reportingsupported').show();
	} else {
		$('.reportingsupported').hide();
		$('.reportingunsupported').show();
	}
};


fedreg.includeRobotsInReporting = function(append) {
	var robot = fedreg.getParameterByName('robots');
	return robot == 'true' ? (append ? "&robot=true" : "robot=true") : "" ;
};

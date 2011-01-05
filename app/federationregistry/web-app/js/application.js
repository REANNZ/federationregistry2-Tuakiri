
window.nimble = window.nimble || {};
var nimble = window.nimble;
nimble.endpoints = nimble.endpoints || {};

window.fedreg = window.fedreg || {};
var fedreg = window.fedreg;

// AJAX indicator
$(function() {
	$("#working").hide();
	
	$("#working").bind("fedreg.working", function(){
		if( $(this).is(':hidden') ) {
			$(this).css({left: $(window).scrollLeft() + 6, top: $(window).scrollTop() + 6})
			$(this).fadeIn();
		}
	 }).bind("ajaxComplete", function(){
		if( $(this).is(':visible') ) {
			$(this).fadeOut();
		}
	 });
	
	$("form").bind("keypress", function(e) {
		if (e.keyCode == 13) {
			return false;
		}
	});
	
	nimble.createTabs('tabs');
	nimble.createTabs('tabs2');

	$(function(){
		$('.tip').tipTip({maxWidth: "auto", edgeOffset: 10, maxWidth:'200px', defaultPosition:"top"});
	});
});

// Organization Administrators
fedreg.organization_fulladministrator_grant = function(userID) {
	$("#working").trigger("fedreg.working");
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
}

fedreg.organization_fulladministrator_revoke = function(userID) {
	$("#working").trigger("fedreg.working");
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
}

fedreg.organization_fulladministrator_list = function() {
	$.ajax({
		type: "GET",
		cache: false,
		url: organizationFullAdministratorListEndpoint,
		success: function(res) {
			$("#organizationfulladministratorlist").html(res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.organization_fulladministrator_search = function() {
	$("#working").trigger("fedreg.working");
	$("#availablefulladministrators").fadeOut().empty();
	var dataString = "q=" + $('#q').val()
	$.ajax({
		type: "GET",
		url: organizationFullAdministratorSearchEndpoint,
		data: dataString,
		success: function(res) {
			$("#availablefulladministrators").empty();
			$("#availablefulladministrators").append(res);
			$("#availablefulladministrators").fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

// Descriptor Administrators
fedreg.descriptor_fulladministrator_grant = function(userID) {
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
			$("#descriptorfulladministratorlist").html(res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.descriptor_fulladministrator_search = function() {
	$("#working").trigger("fedreg.working");
	$("#availablefulladministrators").fadeOut().empty();
	var dataString = "q=" + $('#q').val()
	$.ajax({
		type: "GET",
		url: descriptorFullAdministratorSearchEndpoint,
		data: dataString,
		success: function(res) {
			$("#availablefulladministrators").empty();
			$("#availablefulladministrators").append(res);
			$("#availablefulladministrators").fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

// Key Descriptor
fedreg.keyDescriptor_verify = function(entity) {
	$("#working").trigger("fedreg.working");
	var dataString = $("#cert").serialize() + "&entity=" + entity;
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
			$("#certificates").html(res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.keyDescriptor_delete = function(id) {
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
			$("#monitors").html(res);
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
	$("#working").trigger("fedreg.working");
	$("#availablecontacts").fadeOut().html('');
	var dataString = "givenName=" + $('#givenName').val() + '&surname=' + $('#surname').val() + '&email=' + $('#email').val()
	$.ajax({
		type: "GET",
		url: contactSearchEndpoint,
		data: dataString,
		success: function(res) {
			$("#availablecontacts").empty();
			$("#availablecontacts").append(res);
			$("#availablecontacts").fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_confirm = function(contactID, name, email) {
	activeContact = contactID;
	$("#contactnameconfirmation").html(name);
	$("#contactemailconfirmation").html(email)
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
			nimble.growl('success', res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.contact_delete = function(contactID) {
	$("#working").trigger("fedreg.working");
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
			$("#contacts").html(res);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};


// Endpoint
fedreg.endpoint_edit = function(id, endpointType, containerID) {
	$("#working").trigger("fedreg.working");
	$("#endpoint-"+id).fadeOut();
	var dataString = "id=" + id + "&endpointType=" + endpointType + "&containerID=" + containerID;
	$.ajax({
		type: "GET",
		url: endpointEditEndpoint,
		data: dataString,
		success: function(res) {
			$("#endpoint-"+id).html(res).fadeIn();
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
}

fedreg.endpoint_update = function(id, endpointType, containerID) {
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
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
			nimble.growl('success', res);
			$(':input', "#new" + endpointType + "data")
			 	.not(':button, :submit, :reset, :hidden, select[name=binding]')
			 	.val('')
			fedreg.endpoint_list(endpointType, containerID);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.endpoint_toggle = function(id, endpointType, containerID) {
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.acs_specattribute_add = function(id, formID, containerID) {
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
			$("#"+containerID).html(res)
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
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
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
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
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
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
	    }
	});
};

fedreg.serviceCategory_add = function(containerID) {
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
	$("#working").trigger("fedreg.working");
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
			$("#"+containerID).html(res)
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
			nimble.growl('error', xhr.responseText);
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
	$("#working").trigger("fedreg.working");
	editor.setCode('working...');
	$.ajax({
		type: "GET",
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
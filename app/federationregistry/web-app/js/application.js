
window.fr = window.fr || {};
var fr = window.fr;
var btn;

fr.escapeHtml = function(str) {
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(str));
    return div.innerHTML;
};

$(function() {
  applyBehaviourTo(document);
});

applyBehaviourTo = function(scope) {
  $("[rel=twipsy]", scope).tooltip({offset:3});
  $("[rel=tooltip]", scope).tooltip({offset:3});

  if($('form.validating').validate) {
    $('form.validating', scope).validate({
      ignore: ":disabled",
      keyup: false,
    });
  }

  $('.revealable', scope).hide();

  if($('.table-sortable').dataTable) {
    $('.table-sortable', scope).dataTable( {
      "sDom": "<'row'<'span5'l><'span6'f>r>t<'row'<'span6'i><'span5'p>>",
      "sPaginationType": "bootstrap",
      "oLanguage": {
        "sLengthMenu": "_MENU_ records per page"
      }
    });
  }

  if($('.table-admin-sortable').dataTable) {
    $('.table-admin-sortable', scope).dataTable( {
      "sDom": "<'row'<'span5'l><'span6'f>r>t<'row'<'span6'i><'span5'p>>",
      "sPaginationType": "bootstrap",
      "oLanguage": {
        "sLengthMenu": "_MENU_ records per page"
      }
    });
  }

  var url = document.location.toString();
  if (url.match('#')) {
      $('.nav-tabs a[href=#'+url.split('#')[1]+']').tab('show') ;
  }
};

$('.modal-content').modal({keyboard:true, backdrop:'static'});
$(".close-modal").click(function () {
  fr.hide_modals();
  fr.reset_button();
});
$(".modal").on('hidden', function(){
  fr.reset_button();
});
$('.modal').bind('shown', function () {
  $('.btn', $(this)).removeAttr('disabled');
});
fr.hide_modals = function() {
  $(".modal.in").modal('hide');
};

fr.getParameterByName = function(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match ? decodeURIComponent(match[1].replace(/\+/g, ' ')) : null;
};

fr.set_button = function(b) {
  btn = b;
  btn.button('loading');
  $('.btn').attr('disabled', '');
};

fr.reset_button = function() {
  $('.btn').button('reset').removeAttr('disabled');
};

fr.showspinner= function() {
  $('.spinner').show();
};

fr.hidespinner= function() {
  $('.spinner').hide();
};

fr.showlocalspinner= function(scope) {
  $(scope + ' .spinner').show();
};

fr.hidelocalspinner= function(scope) {
  $(scope + ' .spinner').hide();
};

fr.popuperror= function() {
  $('.top-right').notify({type:'error', closable:true,
    message: { html: '<b>Error</b><br>An error occured communicating with the server.<br>This has been logged for review.<br>If you continue to have problems please <b>contact support</b>.' },
    fadeOut: { enabled: true, delay: 5000 },
    }).show();
};

$(document).ajaxComplete(function(event, request, settings){
   fr.reset_button();
});

$(document).on('click', '.show-problems-logging-on', function() {
  $('.problems-logging-on').removeClass('hidden');
});

// Descriptor Metadata
$(document).on('click', '.load-descriptor-metadata', function() {
  var target = $("#descriptormetadata");
  target.addClass('hidden');
  fr.set_button($(this));
  $.ajax({
    type: "GET",
    cache: false,
    url: descriptorMetadataEndpoint,
    dataType: "text",
    success: function(res) {
      target.text(res);
      target.removeClass('hidden');
      fr.reset_button($(this));
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.reset_button($(this));
      fr.popuperror();
    }
  });

});

// Organization Administrators
fr.organization_fulladministrator_grant = function(userID) {
  var data = "userID=" + userID
  $.ajax({
    async: false,
    type: "POST",
    url: organizationFullAdministratorGrantEndpoint,
    data: data,
    success: function(res) {
      fr.organization_fulladministrator_list();
      fr.organization_fulladministrator_search();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

fr.organization_fulladministrator_revoke = function(userID) {
  var data = "userID=" + userID
  $.ajax({
    async: false,
    type: "POST",
    url: organizationFullAdministratorRevokeEndpoint,
    data: data  + "&_method=delete",
    success: function(res) {
      fr.organization_fulladministrator_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

fr.organization_fulladministrator_list = function() {
  $.ajax({
    type: "GET",
    cache: false,
    url: organizationFullAdministratorListEndpoint,
    success: function(res) {
      var target = $("#organizationfulladministratorlist");
      target.empty();
      target.html(res);
      applyBehaviourTo(target);
      target.fadeIn();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

fr.organization_fulladministrator_search = function() {
  var data = "q=" + $('#q').val()
  $.ajax({
    type: "GET",
    cache: false,
    url: organizationFullAdministratorSearchEndpoint,
    data: data,
    success: function(res) {
      var target = $("#availablefulladministrators");
      target.html(res);
      applyBehaviourTo(target);
      target.fadeIn();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
}

// Descriptor Administrators
fr.descriptor_fulladministrator_grant = function(userID) {
  var data = "userID=" + userID
  $.ajax({
    async: false,
    type: "POST",
    url: descriptorFullAdministratorGrantEndpoint,
    data: data,
    success: function(res) {
      fr.descriptor_fulladministrator_list();
      fr.descriptor_fulladministrator_search();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
}

fr.descriptor_fulladministrator_revoke = function(userID) {
  var data = "userID=" + userID
  $.ajax({
    async: false,
    type: "POST",
    url: descriptorFullAdministratorRevokeEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.descriptor_fulladministrator_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
}

fr.descriptor_fulladministrator_list = function() {
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
      fr.popuperror();
    }
  });
};

fr.descriptor_fulladministrator_search = function() {
  var data = "q=" + $('#q').val()
  $.ajax({
    type: "GET",
    cache: false,
    url: descriptorFullAdministratorSearchEndpoint,
    data: data,
    success: function(res) {
      var target = $("#availablefulladministrators");
      target.html(res);
      applyBehaviourTo(target);
      target.fadeIn();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
}

$(document).on('click', '.ajax-modal', function() {
  $.get($(this).attr('data-load'),function(d){
    $("#show-ajaxload-modal .modal-body").html(d);
    $("#show-ajaxload-modal").modal('show');
  });
});

// Organization
$(document).on('click', '.show-edit-organization', function() {
  $("#overview-organization").hide();
  $("#editor-organization").fadeIn();
});

$(document).on('click', '.cancel-edit-organization', function() {
  $("#editor-organization").hide();
  $("#overview-organization").fadeIn();
});

$(document).on('click', '.confirm-unarchive-organization', function() {
  fr.set_button($(this));
  $("#unarchive-organization-modal").modal('show');
});

$(document).on('click', '.confirm-archive-organization', function() {
  fr.set_button($(this));
  $("#archive-organization-modal").modal('show');
});

$(document).on('click', '.confirm-delete-organization', function() {
  fr.set_button($(this));
  $("#delete-organization-modal").modal('show');
});

// Entity Descriptor
$(document).on('click', '.confirm-archive-entitydescriptor', function() {
  fr.set_button($(this));
  $("#archive-entitydescriptor-modal").modal('show');
});

$(document).on('click', '.confirm-unarchive-entitydescriptor', function() {
  fr.set_button($(this));
  $("#unarchive-entitydescriptor-modal").modal('show');
});

$(document).on('click', '.confirm-delete-entitydescriptor', function() {
  fr.set_button($(this));
  $("#delete-entitydescriptor-modal").modal('show');
});

$(document).on('click', '.show-edit-entitydescriptor', function() {
  $("#overview-entitydescriptor").hide();
  $("#editor-entitydescriptor").fadeIn();
});

$(document).on('click', '.show-migrate-organisation', function() {
  $("#overview-entitydescriptor").hide();
  $("#editor-entitydescriptor-migrateorg").fadeIn();
});

$(document).on('click', '.cancel-edit-entitydescriptor', function() {
  $("#editor-entitydescriptor").hide();
  $("#overview-entitydescriptor").fadeIn();
});

$(document).on('click', '.cancel-entitydescriptor-migrateorg', function() {
  $("#editor-entitydescriptor-migrateorg").hide();
  $("#overview-entitydescriptor").fadeIn();
});

// Identity Provider
$(document).on('click', '.show-edit-identityprovider', function() {
  $("#overview-identityprovider").hide();
  $("#editor-identityprovider").fadeIn();
});

$(document).on('click', '.cancel-edit-identityprovider', function() {
  $("#editor-identityprovider").hide();
  $("#overview-identityprovider").fadeIn();
});

// Service Provider
$(document).on('click', '.show-edit-serviceprovider', function() {
  $("#overview-serviceprovider").hide();
  $("#editor-serviceprovider").fadeIn();
});

$(document).on('click', '.cancel-edit-serviceprovider', function() {
  $("#editor-serviceprovider").hide();
  $("#overview-serviceprovider").fadeIn();
});

// Key Descriptor
$(document).on('click', '.show-addnew-certificate', function(entity) {
  $('#addcertificate').hide();
  $('#newcertificate').fadeIn();
});

$(document).on('click', '.hide-addnew-certificate', function(entity) {
  $('#newcertificate').hide();
  $('#addcertificate').fadeIn();
});

$(document).on('click', '.add-new-certificate', function(entity) {
  fr.set_button($(this));
  var entity = $(this).attr('data-entity');
  fr.keyDescriptor_verify(entity);
  if(valid_certificate){
    var data = $("#newcryptoform").serialize();
    $.ajax({
      type: "POST",
      url: certificateCreationEndpoint,
      data: data,
      success: function(res) {
        $("#newcertificatedata").val('');
        $("#newcertificatedetails").html('');
        $("#newcertificate").hide();
        $('#validcertificate').hide();
        $("#addcertificate").fadeIn();
        fr.keyDescriptor_list();
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
    });
  }
});

$(document).on('click', '.confirm-delete-certificate', function() {
  fr.set_button($(this));
  delete_certificate = $(this).attr('data-certificate');
  $("#delete-certificate-modal").modal('show');
});

$(document).on('click', '.delete-certificate', function() {
  fr.hide_modals();
  var data = "id=" + delete_certificate;
  $.ajax({
    type: "POST",
    url: certificateDeleteEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.keyDescriptor_list()
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

fr.keyDescriptor_verify = function(entity) {
  var data = $("#cert").serialize() + "&entity=" + entity;
  valid_certificate = false;
  $.ajax({
    async: false,
    type: "POST",
    url: certificateValidationEndpoint,
    data: data,
    success: function(res) {
      var target = $("#newcertificatedetails")
      target.html(res);
      applyBehaviourTo(target);
      valid_certificate = true;
      },
    error: function (xhr, ajaxOptions, thrownError) {
      $("#newcertificatedetails").html(xhr.responseText);
      valid_certificate = false;
    }
  });
};

fr.keyDescriptor_list = function() {
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

      }
  });
};

fr.validateCertificate = function() {
  $('#newcertificatedata').removeClass('error');
  fr.keyDescriptor_verify($('#entity\\.identifier').val());
  if(!valid_certificate) {
    $('#newcertificatedata').addClass('error');
  }
};

// Contacts
var delete_contact;
var link_contact;

$(document).on('click', '.search-for-contact', function() {
  var p = $(this).parents('.search-contacts');
  p.children('.add-contact').hide();
  p.children('.potential-contacts').hide();
  p.children('.search-contacts-form').fadeIn();
});

$(document).on('click', '.cancel-search-for-contact', function() {
  var p = $(this).parents('.search-contacts');
  p.children('.potential-contacts').hide();
  p.children('.search-contacts-form').hide();
  p.children('.add-contact').fadeIn();
});

$('.submit-search-for-contact').click(function() {
  fr.set_button($(this));
  var p = $(this).closest('.search-contacts');
  var data = p.find('.search-contacts-form > form').serialize();

  $.ajax({
    type: "GET",
    cache: false,
    url: contactSearchEndpoint,
    data: data,
    success: function(res) {
      var target = p.children('.potential-contacts');
      target.html(res);
      applyBehaviourTo(target);
      p.children('.search-contacts-form').hide();
      target.fadeIn();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.confirm-link-contact', function(contactID, name, email) {
  fr.set_button($(this));
  link_contact = $(this).attr('data-contact');
  $("#contactnameconfirmation").html($(this).attr('data-name'));
  $("#contactemailconfirmation").html($(this).attr('data-email'));
  $("#link-contact-modal").modal('show');
});

$(document).on('click', '.link-contact', function(contactType) {
  fr.hide_modals();
  var data = "contactID=" + link_contact + "&contactType=" + $('#contactselectedtype').val()
  $.ajax({
    type: "POST",
    url: contactCreateEndpoint,
    data: data,
    success: function(res) {
      fr.contact_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.confirm-delete-contact', function() {
  fr.set_button($(this));
  delete_contact = $(this).attr('data-contact');
  $("#unlink-contact-modal").modal('show');
});

$(document).on('click', '.delete-contact', function() {
  fr.hide_modals();
  var data = "id=" + delete_contact;
  $.ajax({
    type: "POST",
    url: contactDeleteEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.contact_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

fr.contact_list = function() {
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
      fr.popuperror();
    }
  });
};

// Endpoint
$(document).on('click', '.confirm-makedefault-endpoint', function() {
  fr.set_button($(this));
  makedefault_endpoint = $(this).attr('data-id');
  container_endpoint = $(this).attr('data-container');
  type_endpoint = $(this).attr('data-type');
  $("#makedefault-endpoint-modal").modal('show');
});

$(document).on('click', '.confirm-toggle-endpoint', function() {
  fr.set_button($(this));
  toggle_endpoint = $(this).attr('data-id');
  $("#toggle-endpoint-modal").modal('show');
});

$(document).on('click', '.confirm-delete-endpoint', function() {
  fr.set_button($(this));
  delete_endpoint = $(this).attr('data-id');
  type_endpoint = $(this).attr('data-type');
  $("#delete-endpoint-modal").modal('show');
});

$(document).on('click', '.toggle-endpoint', function() {
  fr.hide_modals();
  var data = "id=" + toggle_endpoint;
  $.ajax({
    type: "POST",
    url: endpointToggleStateEndpoint,
    data: data + "&_method=put",
    success: function(res) {
      var active = $('#endpoint-' + toggle_endpoint + '-active');
      var inactive = $('#endpoint-' + toggle_endpoint + '-inactive');
      if(active.is(":hidden")) {
        inactive.hide();
        active.show();
      } else {
        active.hide();
        inactive.show();
      }
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.delete-endpoint', function() {
  fr.hide_modals();
  var data = "id=" + delete_endpoint + "&endpointType=" + type_endpoint;
  $.ajax({
    type: "POST",
    url: endpointDeleteEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.endpoint_list(type_endpoint);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.makedefault-endpoint', function() {
  fr.hide_modals();
  var data = "id=" + makedefault_endpoint + "&endpointType=" + type_endpoint;
  $.ajax({
    type: "POST",
    url: endpointMakeDefaultEndpoint,
    data: data + "&_method=put",
    success: function(res) {
      fr.endpoint_list(type_endpoint, container_endpoint);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.show-create-endpoint', function() {
  type_endpoint = $(this).attr('data-type');

  $('#add-' + type_endpoint).hide();
  $('#new-' + type_endpoint).fadeIn();
});

$(document).on('click', '.create-endpoint', function() {
  fr.set_button($(this));
  type_endpoint = $(this).attr('data-type');
  var target_form = $('#new' + type_endpoint + 'data')
  if(target_form.valid()) {
    var data = target_form.serialize();
    $.ajax({
      type: "POST",
      url: endpointCreationEndpoint,
      data: data,
      success: function(res) {
        fr.endpoint_list(type_endpoint);
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
    });
  } else { fr.reset_button(); }
});

$(document).on('click', '.cancel-create-endpoint', function() {
  type_endpoint = $(this).attr('data-type');
  $('#new-' + type_endpoint).hide();
  $('#add-' + type_endpoint).fadeIn();
});

$(document).on('click', '.edit-endpoint', function() {
  fr.set_button($(this));
  edit_endpoint = $(this).attr('data-id');
  type_endpoint = $(this).attr('data-type');
  var data = "id=" + edit_endpoint + "&endpointType=" + type_endpoint;
  $.ajax({
    type: "GET",
    cache: false,
    url: endpointEditEndpoint,
    data: data,
    success: function(res) {
      var target = $("#endpoint-"+edit_endpoint);
      target.hide();
      target.after(res);
      applyBehaviourTo($("#endpoint-edit-" + edit_endpoint));
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.cancel-edit-endpoint', function() {
  $('#endpoint-'+edit_endpoint+'-editor').remove();
  $("#endpoint-"+edit_endpoint).fadeIn();
});

$(document).on('click', '.update-endpoint', function() {
  fr.set_button($(this));
  update_endpoint = $(this).attr('data-id');
  type_endpoint = $(this).attr('data-type');
  var target_form = $("#endpoint-edit-" + update_endpoint)
  if(target_form.valid()) {
    var data =  target_form.serialize();
    $.ajax({
      type: "POST",
      url: endpointUpdateEndpoint,
      data: data + "&_method=put",
      success: function(res) {
        fr.endpoint_list(type_endpoint);
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
    });
  } else { fr.reset_button(); }
});

fr.endpoint_list = function(endpointType) {
  var data = "endpointType=" + endpointType;
  $.ajax({
    type: "GET",
    cache: false,
    url: endpointListEndpoint,
    data: data,
    success: function(res) {
      var target = $("#list-"+endpointType);
      target.html(res);
      applyBehaviourTo(target);
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
  });
};

// Attribute Consuming Services
$(document).on('click', '.edit-ra', function() {
  fr.set_button($(this));
  raID = $(this).attr('data-raid');
  manage_ra = $('.manage-ra[data-raid=' + raID +']');
  update_ra = $('.manage-update-ra[data-raid=' + raID +']');
  editor_ra = $('.editor-ra[data-raid=' + raID +']');
  show_ra = $('.show-ra[data-raid=' + raID +']');

  manage_ra.hide();
  show_ra.hide();
  editor_ra.fadeIn();
  update_ra.fadeIn();

  fr.reset_button();
});

$(document).on('click', '.cancel-edit-ra', function() {
  fr.close_ra_editor();
});

$(document).on('click', '.update-ra', function() {
  fr.set_button($(this));
  raID = $(this).attr('data-raid');
  acsID = $(this).attr('data-acsid');
  reason_ra = $('.reason-ra[data-raid=' + raID +']');
  required_ra = $('.required-ra[data-raid=' + raID +']');

  if(reason_ra.parent().valid()) {
    fr.acs_reqattribute_update(acsID, raID, reason_ra.val(), required_ra.is(':checked'));
  }
});

$(document).on('click', '.show-create-ra', function() {
  acsID = $(this).attr('data-acsid');

  $('#addattribute' + acsID).hide();
  $('#newattribute' + acsID).fadeIn();
});

$(document).on('click', '.create-ra', function() {
  fr.set_button($(this));

  acsID = $(this).attr('data-acsid');
  var target_form = $('#newattributedata' + acsID)

  if(target_form.valid()) {
    var data = "id=" + acsID + "&" + $("#newattributedata" + acsID).serialize();
    $.ajax({
      type: "POST",
      url: acsAddAttr,
      data: data,
      success: function(res) {
        $(':input[name=reasoning]').val('')
        fr.acs_reqattribute_list(acsID, "#acsreqattr" + acsID);
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
    });
  } else { fr.reset_button(); }
});

$(document).on('click', '.cancel-create-ra', function() {
  acsID = $(this).attr('data-acsid');

  $('#newattribute' + acsID).hide();
  $('#addattribute' + acsID).fadeIn();
});

$(document).on('click', '.confirm-delete-ra', function() {
  fr.set_button($(this));
  acsID = $(this).attr('data-acsid');
  raID = $(this).attr('data-raid');

  $("#delete-ra-modal").modal('show');
});

$(document).on('click', '.delete-ra', function() {
  fr.hide_modals();
  var data = "raid=" + raID;
  $.ajax({
    type: "POST",
    url: acsRemoveAttr,
    data: data + "&_method=delete",
    success: function(res) {
      fr.acs_reqattribute_list(acsID);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.add-ra-value', function() {
  fr.set_button($(this));

  raID = $(this).attr('data-raid');
  acsID = $(this).attr('data-acsid');

  var target_form = $("#newspecattributedata" + raID);

  if(target_form.valid()) {
    var data = target_form.serialize();
    $.ajax({
      type: "POST",
      url: acsAddSpecAttrVal,
      data: data,
      success: function(res) {
        $(':input', target_form)
        .not(':button, :submit, :reset, :hidden, select[name=binding]')
        .val('')
        fr.acs_reqattribute_list_values(raID);
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
    });
  } else { fr.reset_button(); }
});

$(document).on('click', '.show-add-ra-value', function() {
  raID = $(this).attr('data-raid');
  manage_ra = $('.manage-ra[data-raid=' + raID +']');

  manage_ra.hide();
  $("#newspecattributeval" + raID).fadeIn();
});

$(document).on('click', '.close-add-ra-value', function() {
  raID = $(this).attr('data-raid');
  $("#newspecattributeval" + raID).hide();
  manage_ra.fadeIn();
});

$(document).on('click', '.confirm-delete-ra-value', function() {
  fr.set_button($(this));
  acsID = $(this).attr('data-acsid');
  raID = $(this).attr('data-raid');
  raValueID = $(this).attr('data-ravalueid');

  $("#delete-ra-value-modal").modal('show');
});

$(document).on('click', '.delete-ra-value', function() {
  fr.hide_modals();
  var data = "id=" + raID + "&valueid=" + raValueID;
  $.ajax({
    type: "POST",
    url: acsRemoveSpecAttrVal,
    data: data + "&_method=delete",
    success: function(res) {
      fr.acs_reqattribute_list_values(raID);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

fr.close_ra_editor = function() {
  editor_ra.hide();
  update_ra.hide();
  manage_ra.show();
  show_ra.fadeIn();
};

fr.acs_reqattribute_update = function(acsID, raID, reason, required) {
  var data = "id=" + raID + "&reasoning=" + encodeURIComponent(reason) + "&required=" + required;
  $.ajax({
    type: "POST",
    url: acsUpdateAttr,
    data: data + "&_method=put",
    success: function(res) {
      fr.close_ra_editor();
      //TODO: when time permits clean this up further to stop total reload of content
      fr.acs_reqattribute_list(acsID);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

fr.acs_reqattribute_list = function(acsID) {
  var data = "id=" + acsID;
  $.ajax({
    type: "GET",
    cache: false,
    url: acsListAttr,
    data: data,
    success: function(res) {
      var target = $("#acsreqattr" + acsID);
      target.html(res);
      applyBehaviourTo(target);
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
  });
};

fr.acs_reqattribute_list_values = function(raID) {
  var data = "id=" + raID;
  $.ajax({
    type: "GET",
    cache: false,
    url: acsListSpecAttrValues,
    data: data,
    success: function(res) {
      var target = $("#ra-values-" + raID);
      target.html(res);
      applyBehaviourTo(target);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

// Name ID Formats
$(document).on('click', '.show-add-nameid', function() {
  $("#addnameidformat").hide();
  $("#newnameidformat").fadeIn();
});

$(document).on('click', '.cancel-add-nameid', function() {
  $("#newnameidformat").hide();
  $("#addnameidformat").fadeIn();
});

$(document).on('click', '.add-nameid', function() {
  var data = $("#newnameidformatdata").serialize();
  $.ajax({
    type: "POST",
    url: nameIDFormatAddEndpoint,
    data: data,
    success: function(res) {
      fr.nameIDFormat_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.confirm-delete-nameid', function() {
  fr.set_button($(this));
  formatID = $(this).attr('data-formatid');

  $("#delete-nameid-modal").modal('show');
});

$(document).on('click', '.delete-nameid', function() {
  fr.hide_modals();
  var data = "formatID=" + formatID;
  $.ajax({
    type: "POST",
    url: nameIDFormatRemoveEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.nameIDFormat_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

fr.nameIDFormat_list = function() {
  $.ajax({
    type: "GET",
    cache: false,
    url: nameIDFormatListEndpoint,
    success: function(res) {
      var target = $("#nameidformats");
      target.html(res);
      applyBehaviourTo(target);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

// Service Categories
var delete_category;

$(document).on('click', '.show-addnew-servicecategory', function(entity) {
  $('#addcategory').hide();
  $('#newcategory').fadeIn();
});

$(document).on('click', '.hide-addnew-servicecategory', function(entity) {
  $('#newcategory').hide();
  $('#addcategory').fadeIn();
});

$(document).on('click', '.link-new-category', function() {
  fr.set_button($(this));
  var data = $("#newservicecategorydata").serialize();
  $.ajax({
    type: "POST",
    url: serviceCategoryAddEndpoint,
    data: data,
    success: function(res) {
      fr.serviceCategory_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.confirm-unlink-category', function() {
  fr.set_button($(this));
  delete_category = $(this).attr('data-category');
  $("#unlink-category-modal").modal('show');
});

$(document).on('click', '.unlink-category', function() {
  fr.hide_modals();
  var data = "categoryID=" + delete_category;
  $.ajax({
    type: "POST",
    url: serviceCategoryRemoveEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.serviceCategory_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

fr.serviceCategory_list = function() {
  $.ajax({
    type: "GET",
    cache: false,
    url: serviceCategoryListEndpoint,
    success: function(res) {
      var target = $("#categories");
      target.html(res);
      applyBehaviourTo(target);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

// Attributes
$(document).on('click', '.show-add-attribute', function() {
  $('#add-attribute').hide();
  $('#new-attribute').fadeIn();
});

$(document).on('click', '.cancel-add-attribute', function() {
  $('#new-attribute').hide();
  $('#add-attribute').fadeIn();
});

$(document).on('click', '.add-attribute', function() {
  fr.set_button($(this));
  var data = $("#newattributedata").serialize();
  $.ajax({
    type: "POST",
    url: attributeAddEndpoint,
    data: data,
    success: function(res) {
      fr.attribute_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

$(document).on('click', '.confirm-delete-attribute', function() {
  fr.set_button($(this));
  delete_attrid = $(this).attr('data-attrid');
  $("#delete-attribute-modal").modal('show');
});

$(document).on('click', '.delete-attribute', function() {
  fr.hide_modals();
  var data = "attrid=" + delete_attrid;
  $.ajax({
    type: "POST",
    url: attributeRemoveEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.attribute_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

fr.attribute_list = function() {
  $.ajax({
    type: "GET",
    cache: false,
    url: attributeListEndpoint,
    success: function(res) {
      var target = $("#supported-attributes");
      target.html(res);
      applyBehaviourTo(target);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
};

// Attribute Filter
$(document).on('click', '.load-descriptor-attrfilter', function() {
  $("#descriptorattributefilter").addClass('hidden');
  fr.set_button($(this));
  $.ajax({
    type: "GET",
    cache: false,
    url: attributeFilterEndpoint,
    dataType: "text",
    success: function(res) {
      $("#descriptorattributefilter").text(res);
      $("#descriptorattributefilter").removeClass('hidden');
      fr.reset_button($(this));
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.reset_button($(this));
      fr.popuperror();
    }
  });
});

// Monitors
$(document).on('click', '.show-add-monitor', function() {
  $('#addmonitor').hide();
  $('#newmonitor').fadeIn();
});

$(document).on('click', '.cancel-add-monitor', function() {
  $('#newmonitor').hide();
  $('#addmonitor').fadeIn();
});

$(document).on('click', '.add-monitor', function() {
  var target_form = $("#newmonitordata")

  if(target_form.valid()) {
    var data = target_form.serialize();
    $.ajax({
      type: "POST",
      url: monitorCreateEndpoint,
      data: data,
      success: function(res) {
        fr.monitor_list();
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
    });
  }
});

$(document).on('click', '.confirm-delete-monitor', function() {
  fr.set_button($(this));
  monitorID = $(this).attr('data-monitorID');
  $("#delete-monitor-modal").modal('show');
});

$(document).on('click', '.delete-monitor', function() {
  fr.hide_modals();

  var data = "id=" + monitorID;
  $.ajax({
    type: "POST",
    url: monitorDeleteEndpoint,
    data: data + "&_method=delete",
    success: function(res) {
      fr.monitor_list();
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.popuperror();
    }
  });
});

fr.monitor_list = function(containerID) {
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
      fr.popuperror();
    }
  });
};

// Registration
fr.configureIdentityProviderSAML = function(host) {
  $(".samloptional").val("");
  if(host.length > 0) {
    host = host.toLowerCase();

    $('#entity\\.identifier').val( knownIDPImpl[currentImpl].entitydescriptor.replace('$host', host));
    $('#idp\\.post').val( knownIDPImpl[currentImpl].post.uri.replace('$host', host) );
    $('#idp\\.redirect').val( knownIDPImpl[currentImpl].redirect.uri.replace('$host', host) );
    $('#idp\\.ecp').val( knownIDPImpl[currentImpl].ecp.uri.replace('$host', host) );
    $('#idp\\.artifact').val( knownIDPImpl[currentImpl].artifact.uri.replace('$host', host) );
    $('#idp\\.artifact-index').val( knownIDPImpl[currentImpl].artifact.index );
    $('#aa\\.attributeservice').val( knownIDPImpl[currentImpl].attributeservice.uri.replace('$host', host) );
  }
};

fr.configureServiceProviderSAML = function(host) {
  $(".samloptional").val("");
  if(host.length > 0) {
    host = host.toLowerCase();

    $('#entity\\.identifier').val( knownSPImpl[currentImpl].entitydescriptor.replace('$host', host) );

    $('#sp\\.acs\\.post').val( knownSPImpl[currentImpl].acs.post.uri.replace('$host', host) );
    $('#sp\\.acs\\.post-index').val( knownSPImpl[currentImpl].acs.post.index );

    $('#sp\\.acs\\.artifact').val( knownSPImpl[currentImpl].acs.artifact.uri.replace('$host', host) );
    $('#sp\\.acs\\.artifact-index').val( knownSPImpl[currentImpl].acs.artifact.index );

    if(knownSPImpl[currentImpl].drs) {
      $('#sp\\.drs').val( knownSPImpl[currentImpl].drs.uri.replace('$host', host) );
      $('#sp\\.drs\\.index').val( knownSPImpl[currentImpl].drs.index );
    }

    if( knownSPImpl[currentImpl].slo ) {
      if( knownSPImpl[currentImpl].slo.artifact )
        $('#sp\\.slo\\.artifact').val( knownSPImpl[currentImpl].slo.artifact.uri.replace('$host', host) );

      if( knownSPImpl[currentImpl].slo.redirect )
        $('#sp\\.slo\\.redirect').val( knownSPImpl[currentImpl].slo.redirect.uri.replace('$host', host) );

      if( knownSPImpl[currentImpl].slo.soap )
        $('#sp\\.slo\\.soap').val( knownSPImpl[currentImpl].slo.soap.uri.replace('$host', host) );

      if( knownSPImpl[currentImpl].slo.post )
        $('#sp\\.slo\\.post').val( knownSPImpl[currentImpl].slo.post.uri.replace('$host', host) );
    }

    if( knownSPImpl[currentImpl].mnid ) {
      if( knownSPImpl[currentImpl].mnid.artifact )
        $('#sp\\.mnid\\.artifact').val( knownSPImpl[currentImpl].mnid.artifact.uri.replace('$host', host) );

      if( knownSPImpl[currentImpl].mnid.redirect )
        $('#sp\\.mnid\\.redirect').val( knownSPImpl[currentImpl].mnid.redirect.uri.replace('$host', host) );

      if( knownSPImpl[currentImpl].mnid.soap )
        $('#sp\\.mnid\\.soap').val( knownSPImpl[currentImpl].mnid.soap.uri.replace('$host', host) );

      if( knownSPImpl[currentImpl].mnid.post )
        $('#sp\\.mnid\\.post').val( knownSPImpl[currentImpl].mnid.post.uri.replace('$host', host) );
    }
  }
};

// Attribute request/require toggle for SP
$(document).on('click', '.request-attribute', function() {
    var attr = $(this).attr('data-attrid');
    var reason = $('.reason-attribute[data-attrid=' + attr +']');
    var req = $('.require-attribute[data-attrid=' + attr +']');

    if(!$(this).is(':checked')) {
      if(req.is(':checked')) {
        req.attr('checked', false);
      }
      reason.removeClass('required');
      reason.removeClass('error');
      reason.siblings('.error').remove();  //removes error notice
    } else {
        reason.addClass('required');
    }
});

$(document).on('click', '.require-attribute', function() {
    var attr = $(this).attr('data-attrid');
    var reason = $('.reason-attribute[data-attrid=' + attr +']');
    var req = $('.request-attribute[data-attrid=' + attr +']');

    if($(this).is(':checked') && !req.is(':checked')) {
        req.attr('checked', true);
        reason.addClass('required');
    }
});

// Reporting
$(document).on('click', '.create-sp-report', function() {
  var target_form = $('#reportrequirements');

  if(target_form.valid()) {
    if (Modernizr.svg) {
      fr.closeRefinement();
      $(".revealable").hide();

      var data = target_form.serialize() + fr.includeRobotsInReporting(true);

      if( $(".reporttype option:selected").val() == 'connections') {
        $.ajax({url: spReportsConnectivityEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderSPConnectivity(data, false);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }

      if( $(".reporttype option:selected").val() == 'sessions') {
        $.ajax({url: spReportsSessionsEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderSPSessions(data, false);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }

      if( $(".reporttype option:selected").val() == 'totals') {
        $.ajax({url: spReportsTotalsEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderSPTotals(data, false);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }

      if( $(".reporttype option:selected").val() == 'logins') {
        $.ajax({url: spReportsLoginsEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderSPLogins(data);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }
    } else {
      fr.toggleReportingContent(false);
    }
  }
});

fr.openRefinement = function() {
  $(".reportrefinementopen").hide();
  $(".reportrefinementinput").slideDown();

  return false;
};

fr.closeRefinement = function() {
  $(".reportrefinementinput").slideUp();
  $(".reportrefinementopen").show();

  return false;
};

$(document).on('click', '.create-idp-report', function() {
  var target_form = $('#reportrequirements');

  if(target_form.valid()) {
    if (Modernizr.svg) {

      fr.closeRefinement();
      $(".revealable").hide();

      var data = $("#reportrequirements").serialize() + fr.includeRobotsInReporting(true);

      if( $(".reporttype option:selected").val() == 'connections') {
        $.ajax({url: idpReportsConnectivityEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderIdPConnectivity(data, false);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }

      if( $(".reporttype option:selected").val() == 'sessions') {
        $.ajax({url: idpReportsSessionsEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderIdPSessions(data, false);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }

      if( $(".reporttype option:selected").val() == 'totals') {
        $.ajax({url: idpReportsTotalsEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderIdPTotals(data, false);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }

      if( $(".reporttype option:selected").val() == 'logins') {
        $.ajax({url: idpReportsLoginsEndpoint,
          data: data,
          dataType: 'json',
          async:true,
          success: function(data){
            fr.renderIdPLogins(data);
          },
          error: function (xhr, ajaxOptions, thrownError) {
            fr.popuperror();
          }
        });
      }
    } else {
      fr.toggleReportingContent(false);
    }
  }
});

fr.refineIdPReport = function(refinement) {
  if (Modernizr.svg) {

    fr.closeRefinement();

    var data = $("#reportrequirements").serialize() + "&" + refinement.serialize() + fr.includeRobotsInReporting(true);

    if( $(".reporttype option:selected").val() == 'connections') {
      $.ajax({url: idpReportsConnectivityEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderIdPConnectivity(data, true);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

    if( $(".reporttype option:selected").val() == 'totals') {
      $.ajax({url: idpReportsTotalsEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderIdPTotals(data, true);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }
  } else {
    fr.toggleReportingContent(false);
  }
};

fr.refineSPReport = function(refinement) {
  if (Modernizr.svg) {

    fr.closeRefinement();

    var data = $("#reportrequirements").serialize() + "&" + refinement.serialize() + fr.includeRobotsInReporting(true);

    if( $(".reporttype option:selected").val() == 'connections') {
      $.ajax({url: spReportsConnectivityEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderSPConnectivity(data, true);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

    if( $(".reporttype option:selected").val() == 'totals') {
      $.ajax({url: spReportsTotalsEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderSPTotals(data, true);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }
  } else {
    fr.toggleReportingContent(false);
  }
};

fr.renderFederationReport = function(type) {
  if (Modernizr.svg) {

    fr.closeRefinement();
    $(".revealable").hide();

    var data = $("#reportrequirements").serialize() + fr.includeRobotsInReporting(true);

    if( type == 'logins') {
      $.ajax({url: federationReportsLoginsEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderFederationLogins(data);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

    if( type == 'sessions') {
      $.ajax({url: federationReportsSessionsEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderFederationSessions(data);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

    if( type == 'sessiontotals') {
      $.ajax({url: federationReportsSessionTotalsEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderFederationServices(data);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

    if( type == 'registrations') {
      if( $("#registrationstype").val() == 'organization') {
        data = data + "&type=org"
      }
      if( $("#registrationstype").val() == 'identityprovider') {
        data = data + "&type=idp"
      }
      if( $("#registrationstype").val() == 'serviceprovider') {
        data = data + "&type=sp"
      }

      $.ajax({url: federationReportsRegistrationsEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderFederationRegistrations(data, true);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

    if( type == 'subscribers') {
      if( $("#subscriberstype").val() == 'organization') {
        data = data + "&type=org"
      }
      if( $("#subscriberstype").val() == 'identityprovider') {
        data = data + "&type=idp"
      }
      if( $("#subscriberstype").val() == 'serviceprovider') {
        data = data + "&type=sp"
      }

      $.ajax({url: federationReportsSubscribersEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderFederationSubscribers(data, true);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

    if( type == 'connectivity') {
      $.ajax({url: federationConnectivtyEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderFederationConnectivity(data);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }

  } else {
    fr.toggleReportingContent(false);
  }
};

fr.refineFederationReport = function(type, refinement) {
  if (Modernizr.svg) {

    fr.closeRefinement();

    var data = $("#reportrequirements").serialize() + "&" + refinement.serialize() + fr.includeRobotsInReporting(true);

    if( type == 'sessiontotals') {
      $.ajax({url: federationReportsSessionTotalsEndpoint,
        data: data,
        dataType: 'json',
        async:true,
        success: function(data){
          fr.renderFederationServices(data);
        },
        error: function (xhr, ajaxOptions, thrownError) {
          fr.popuperror();
        }
      });
    }
  } else {
    fr.toggleReportingContent(false);
  }
};

fr.renderFederationSummaryReport = function(type) {
  if (Modernizr.svg) {
    fr.toggleReportingContent(true);


    var data = fr.includeRobotsInReporting(false);

    $.ajax({url: federationReportsSummaryEndpoint,
      data: data,
      dataType: 'json',
      async:true,
      success: function(data){
        fr.renderCreationSummary(data);
      },
      error: function (xhr, ajaxOptions, thrownError) {
        fr.popuperror();
      }
    });
  } else {
    fr.toggleReportingContent(false);
  }
};

fr.toggleReportingContent = function(supported) {
  if(supported == true) {
    $('.reportingunsupported').hide();
    $('.reportingsupported').show();
  } else {
    $('.reportingsupported').hide();
    $('.reportingunsupported').show();
  }
};


fr.includeRobotsInReporting = function(append) {
  var robot = fr.getParameterByName('robots');
  return robot == 'true' ? (append ? "&robot=true" : "robot=true") : "" ;
};

window.nimble = window.nimble || {};
var nimble = window.nimble;

// Dialog support
$(function() {
	$(	'<div id="confirmationdialog" title="" style="display:none" class="popup">'+
		'	<p id="confirmationcontent">&nbsp;</p>'+
		'	<div class="buttons">'+
		'		<a id="confirmaccept" class="ui-button ui-button-text-icon ui-widget ui-state-default ui-corner-all">' +
		'			<span class="ui-button-icon-primary ui-icon ui-icon-check"></span>' +
		'			<span class="ui-button-text"></span>' +
		'		</a>' +
		'		<a id="confirmcancel" class="ui-button ui-button-text-icon ui-widget ui-state-default ui-corner-all">' +
		'			<span class="ui-button-icon-primary ui-icon ui-icon-cancel"></span>' +
		'			<span class="ui-button-text"></span>' +
		'		</a>' +	 
		'	</div>'+
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
	$("#confirmcancel").click(function() {
		$('#confirmationdialog').dialog('close');
	});
	$("#confirmaccept").click(function() {
		confirmAction();
		$('#confirmationdialog').dialog('close');
	});
});

nimble.wasConfirmed = function(title, msg, accept, cancel) {
	$("#confirmationtitle").html(title);
	$("#confirmationcontent").html(msg); 
	$("#confirmaccept>.ui-button-text").html(accept);
	$("#confirmcancel>.ui-button-text").html(cancel);
	
	$("#confirmationdialog").dialog('option', 'title', title);
	$("#confirmationdialog").dialog('open');		
};

// Session Termination
$(function() {
	$(	'<div id="sessionterminateddialog" style="display:none">'+
	'		<div class="errorpopup">'+
	'   		<div class="content" style="width:auto">'+
	'				<p id="sessionterminateddialogmsg"><g:message code="nimble.template.sessionterminated.descriptive" /></p>'+
	'				<div class="buttons">'+
	'					<a href="#" onClick="window.location.reload();return false;" id="sessionterminatedbtn" class="button icon icon_flag_blue">Login</a>'+
	'				</div>'+
	'			</div>'+
	'		</div>'+
	'	</div>').appendTo(document.body);

	$("#sessionterminateddialog").dialog({
		bgiframe: true,
		resizable: false,
		modal: true,
		autoOpen: false,
		title: 'Session Terminated',
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		}
	});

	$().ajaxError(function (event, xhr, ajaxOptions, thrownError) {
	  if ((xhr.status == 403) && (xhr.getResponseHeader("X-Nim-Session-Invalid") != null)) {
        $("#sessionterminateddialogmsg").text($('#sessionterminatedmsg').val());
        $("#sessionterminatedbtn").text($('#sessionterminatedlogin').val());
	    $("#sessionterminateddialog").dialog('open','title', $('#sessionterminatedtitle').val());
	  }
	});
});

nimble.createTabs = function(id) {
    $(function() {
        $('#'+id).tabs();
    });
};

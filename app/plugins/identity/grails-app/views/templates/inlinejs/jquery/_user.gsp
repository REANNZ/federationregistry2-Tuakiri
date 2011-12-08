jQuery.extend(nimble.endpoints,{
subject: { 'logins':'${createLink(action:'listlogins')}',
        'enableAPI':'${createLink(action:'enableapi')}',
        'disableAPI':'${createLink(action:'disableapi')}',
        'enable':'${createLink(action:'enable')}',
        'disable':'${createLink(action:'disable')}'
       }
});

$(function() {
	nimble.listLogins('${subject.id}');
	<g:if test="${subject?.enabled}">
	  $("#enablesubject").hide();
	  $("#enabledsubject").hide();
	</g:if>
	<g:else>
	  $("#disablesubject").hide();
	  $("#disabledsubject").hide();
	</g:else>

	<g:if test="${subject?.remoteapi}">
	  $("#disabledapi").hide();
	  $("#enablesubjectapi").hide();
	</g:if>
	<g:else>
	  $("#enabledapi").hide();
	  $("#disablesubjectapi").hide();
	</g:else>
});
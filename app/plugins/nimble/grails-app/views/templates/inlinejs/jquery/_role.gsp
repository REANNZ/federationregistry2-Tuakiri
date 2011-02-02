jQuery.extend(nimble.endpoints,{
role: { 'list':'${createLink(action: 'listroles')}',
            'search':'${createLink(action: 'searchroles')}',
            'remove':'${createLink(action: 'removerole')}',
            'grant':'${createLink(action: 'grantrole')}'
    }
});

$(function() {
	nimble.listRoles(${parent.id});
    $("#addroles").hide();
});
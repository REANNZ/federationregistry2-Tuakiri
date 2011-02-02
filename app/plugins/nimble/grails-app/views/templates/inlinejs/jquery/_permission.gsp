jQuery.extend(nimble.endpoints,{
  permission: { 'list':'${createLink(action:'listpermissions')}',
                'remove':'${createLink(action:'removepermission')}',
                'create':'${createLink(action:'createpermission')}' }
});

$(function() {
	nimble.listPermissions(${parent.id});
	$("#addpermissions").hide();
});
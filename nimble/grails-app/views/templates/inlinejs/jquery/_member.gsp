jQuery.extend(nimble.endpoints,{
    member: {
     'list':'${createLink(action:'listmembers')}',
     'search':'${createLink(action:'searchnewmembers')}',
     'remove':'${createLink(action:'removemember')}',
     'add':'${createLink(action:'addmember')}',
     'groupSearch':'${createLink(action:'searchnewgroupmembers')}',
     'groupAdd':'${createLink(action:'addgroupmember')}',
     'groupRemove':'${createLink(action:'removegroupmember')}'
    }
});

$(function() {
	nimble.listMembers(${parent.id});
    $("#addmembers").hide();
    $("#memberaddgroups").hide();
});
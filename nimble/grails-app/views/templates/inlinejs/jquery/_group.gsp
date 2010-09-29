jQuery.extend(nimble.endpoints,{
group: {
    'list':'${createLink(action:'listgroups')}',
    'search':'${createLink(action:'searchgroups')}',
    'remove':'${createLink(action:'removegroup')}',
    'grant':'${createLink(action:'grantgroup')}'
  }
});

$(function() {
    nimble.listGroups('${parent.id.encodeAsHTML()}');
    $("#addgroups").hide();
});
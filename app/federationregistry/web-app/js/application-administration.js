window.fr = window.fr || {};
var fr = window.fr;

$(".show-edit-role").click(function() {
  $("#overview-role").hide();
  $("#editor-role").fadeIn();
});

$(".cancel-edit-role").click(function() {
  $("#editor-role").hide();
  $("#overview-role").fadeIn();
});

$(".show-manage-members").click(function() {
  fr.set_button($(this));
  var btn = $(this);
  $.ajax({
    type: "GET",
    cache: false,
    url: searchNewAdministratorsEndpoint,
    success: function(res) {
      var target = $("#manage-role-members");
      target.html(res);
      applyBehaviourTo(target);
      target.fadeIn();
      
      fr.reset_button(btn);
      btn.hide();     
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.reset_button(btn);
      fr.popuperror();
    }
  });
  
});

$(".show-manage-report-viewers").click(function() {
  fr.set_button($(this));
  var btn = $(this);
  $.ajax({
    type: "GET",
    cache: false,
    url: searchNewReportViewersEndpoint,
    success: function(res) {
      var target = $("#manage-report-viewers");
      target.html(res);
      applyBehaviourTo(target);
      target.fadeIn();
      
      fr.reset_button(btn);
      btn.hide();     
    },
    error: function (xhr, ajaxOptions, thrownError) {
      fr.reset_button(btn);
      fr.popuperror();
    }
  });
  
});

$(".show-manage-permissions").click(function() {
  $(this).hide();
  $(".manage-role-permissions").fadeIn();
});

$(document).on('click', '.confirm-delete-role', function() {
  fr.set_button($(this));
  $("#delete-role-modal").modal('show');
});

$(".show-manage-subject-permissions").click(function() {
  $(this).hide();
  $(".manage-subject-permissions").fadeIn();
});


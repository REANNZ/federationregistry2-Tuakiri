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
  $(this).hide();
  $(".manage-role-members").fadeIn();
});

$(".show-manage-reportadmins").click(function() {
  $(this).hide();
  $(".manage-reportadmins").fadeIn();
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


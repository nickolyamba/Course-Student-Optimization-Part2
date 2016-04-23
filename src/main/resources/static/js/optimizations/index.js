$(document).ready(function() {
  $('#studentList').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget)
    var offeringId = button.closest('[data-offering-id]').data().offeringId;
    console.log(button)
    console.log(offeringId);
    $.getJSON('/offerings/' + offeringId).done(function(data) {
      console.log(data);
    })
  })
});
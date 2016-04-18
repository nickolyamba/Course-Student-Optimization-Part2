$(document).ready(function() {
  $('[data-request-id]').on('click', function() {
    var _this = this;
    var el = $(this);
    var id = el.data().requestId;
    $.get("course_preferences/" + id).complete(function(data) {
      var html = data.responseText;
      var table = $(html).find('table');
      $('table').replaceWith(table);
      console.log(table);
    });
  });
});
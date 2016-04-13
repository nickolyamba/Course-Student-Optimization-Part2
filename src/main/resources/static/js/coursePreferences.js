$(document).ready(function() {
  Sortable.create(notTaken, {
    group: 'awesome',
    draggable: 'li'
  });
  $('.semesterList').each(function() {
    var _this = this;
    var id = $(_this).attr('id');
    var semSort = Sortable.create(_this, {
      group: 'awesome',
      draggable: 'li',
      onSort: function(e) {
        semSort.options.group.put = $('#' + id + " > *").length < 2 ? true : false;
      }

    });
  });
});

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
        semSort.options.group.put = $('#' + id + " > *").length < 5 ? true : false;
      }

    });
  });
  
  
  $('[data-submit]').on('click', function() {
    var data = {};
    data["semester"] = [];
    $('[data-semester-id]').each(function(i, val) {
      var semester = $(val);
      var dataAttributes = semester.data();
      data.semester.push(dataAttributes.semesterId);
      data["offerings"] = [];
      semester.find('[data-offering-id]').each(function(j, cVal) {
        var offering = $(cVal);
        var offeringAttributes = offering.data();

        data.offerings.push(offeringAttributes.offeringId);
      });
    });
    $.ajax({
      url: "/course_preferences/edit",
      type: "post",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(data)
    }).complete(function(data) {
      window.location.assign(window.location.href.replace('/edit', ''));
    })
  });
});

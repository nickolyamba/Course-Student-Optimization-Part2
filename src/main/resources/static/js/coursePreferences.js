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
  
  
  $('[data-submit]').on('click', function() {
    var data = {};
    data["semester"] = [];
    $('[data-semester-id]').each(function(i, val) {
      var semester = $(val);
      var dataAttributes = semester.data();
      data.semester.push(dataAttributes.semesterId);
      data["courses"] = [];
      semester.find('[data-course-id]').each(function(j, cVal) {
        var course = $(cVal);
        var courseAttributes = course.data();

        data.courses.push(courseAttributes.courseId);
      });
    });
    console.log(data);
    $.ajax({
      url: "/course_preferences/edit",
      type: "post",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(data),
    }).done(function(data) {
      console.log(data)
    })
  });
});

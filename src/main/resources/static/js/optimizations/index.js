$(document).ready(function() {
  $('body').on('hidden.bs.modal', '.modal', function () {
    $(this).removeData('bs.modal');
  });

  $('[data-submit]').on('click', function() {
    var data = {};
        data.includeGpa = {};
        data.includeGpa["configs"] = [$('#include-gpa').is(':checked'),
                                        $('#include-seniority').is(':checked'),
                                        $('#ta-coefficient').val(),
                                        $('#min-ta').val(),
                                        $('#max-ta').val()];

    $.ajax({
      url: "/optimizations/renew",
      type: "post",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(data)
    }).complete(function(data) {
      console.log(data);
      window.location.assign(window.location.href.replace('/renew', ''));
    })
  });

});

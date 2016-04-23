$(document).ready(function() {
  Sortable.create(professors, {
    group: 'professors',
    draggable: 'li'
  });
  Sortable.create(tas, {
    group: 'tas',
    draggable: 'li'
  });
  $('.professor-list').each(function() {
    Sortable.create(this, {
      group: 'professors',
      draggable: 'li'
    });
  });
  $('.ta-list').each(function() {
    Sortable.create(this, {
      group: 'tas',
      draggable: 'li'
    });
  });

  $('[data-submit]').on('click', function() {
    var data = {};
    $('[data-offering-id]').each(function() {
      var offering = $(this);
      var offeringId = offering.data().offeringId;
      offering.find('[data-professor-id]').each(function() {
        if (data[offeringId] === undefined) {
          data[offeringId] = {};
        }
        if (data[offeringId].professors === undefined) {
          data[offeringId].professors = [];
        }
        data[offeringId].professors.push($(this).data().professorId);
      });
      offering.find('[data-ta-id]').each(function() {
        if (data[offeringId] === undefined) {
          data[offeringId] = {};
          data[offeringId].tas = [];
        }
        if (data[offeringId].tas === undefined) {
          data[offeringId].tas = [];
        }
        data[offeringId].tas.push($(this).data().taId);
      })
    });
    $.ajax({
      url: "/optimizations/new",
      type: "post",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(data)
    }).complete(function(data) {
      console.log(data);
      window.location.assign(window.location.href.replace('/new', ''));
    })
  });

  $('[data-offering-capacity]').each(function() {
    var $this = $(this);
    var offeringId = $this.closest('[data-offering-id]').data().offeringId
    $this.editable({
      ajaxOptions: {
        type: 'put',
        contentType: "application/json"
      },
      type: 'text',
      send: 'always',
      url: "/offerings/" + offeringId,
      success: function(response, newValue) {
        console.log(response);
        console.log(newValue);
      },
      params: function(params) {
        var data = {};
        data['capacity'] = params.value;
        return JSON.stringify(data);
      }
    })
  })

  $("[name='include-gpa']").bootstrapSwitch();
  $("[name='include-seniority']").bootstrapSwitch();

  console.log('yolo');
});

  function updateTextInput(val) {
        document.getElementById('slider-text').value = val;
  }

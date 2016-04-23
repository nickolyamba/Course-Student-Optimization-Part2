$(document).ready(function() {
  $('#studentList').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget)
    var offeringId = button.closest('[data-offering-id]').data().offeringId;
    console.log(button)
    console.log(offeringId);
    $.getJSON('/offerings/' + offeringId).done(function(data) {
      console.log(data);
      //for(i = 0; i < data._links.preferences.length; i++)
    })


    /*
    $("#studs").html("");
    for (var index = 0; index < contact_name.length; ++index) {
        var div_inner =
        '<li class="list-group-item" id="';

        $(".contact_element").append(div_inner);
    }*/

   }
  })
});

 $(document).ready(function(){
             $('#studentList').on('show.bs.modal', function (event) {
                 var button = $(event.relatedTarget)
                 var offeringId = button.closest('[data-offering-id]').data().offeringId;
                 console.log(button)
                 console.log(offeringId);
                 $.getJSON('/offerings/' + offeringId).done(function(data) {
                    console.log(data);

                 })

             $('table#studs tbody').append(
             "<tr th:each='preference:${offering.getPreferences()}'>"+
                 "<td th:text='${preference.getStudent().getFirst_name() + preference.getStudent().getLast_name()}'>"+"</td>"+
             "</tr>")



           });




    });//onclick
 });
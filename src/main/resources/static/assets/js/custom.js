$(document).ready(function () {
    $('#state').change(function(){
		var stateId = $(this).val();
		$.ajax({
			type: 'GET',
			url: '/admin/getCityByState/' + stateId,
			success: function(result){
				
				var result = JSON.parse(result);
				var c = '';
				for(var i=0; i<result.length; i++){
					c += '<option value="' + result[i].id + '">' + result[i].name + '</option>'
				}
				//$('#city').html(c);
				$('#city').empty().append(c);
			},
			error: (error) => {
                     console.log(JSON.stringify(error));
   			}
		});
	});
	
	$("reset").click(function(){
		$("#city").empty();
	});
	
});
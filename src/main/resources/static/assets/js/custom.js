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
	
	$("#checkUsername").click(function(){
		var username = $("#username").val();
		$.ajax({
			type: 'GET',
			url: '/admin/checkUsername/' + username,
			success: function(result){
				if(result){
					alert("Username Already Exist. Try Again!");
					$("#username").val("");
				}
			},
			error: (error) => {
				console.log(JSON.stringify(error));
			}
		});
	});
	
	$("#generateMeterNo").click(function(){
		length = 8
		meter = "";
		for(i=0;i<length;i++){
			num = Math.floor(Math.random()*10);
			meter += num;
		}
		$("#meterNo").val(meter);
	});
	
});
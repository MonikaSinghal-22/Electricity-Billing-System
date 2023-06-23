function closeButton(element){
	window.location.href = element.attributes.url.value;
}

function changeAccountNonLocked(element){
	$.ajax({
		type: 'GET',
		url: '/admin/changeIsAccountLocked/'+ element.id + '/' + element.checked,
		success: function(result){
			alert(result);
		},
		error: (error) => {
			console.log(JSON.stringify(error));
		}
	});
}

function getCustomerDetailsFromMeterNo(element){
	if(element.value == ' '){
			$("#customerName").val("");
			$("#customerAddress").val("");	
	}
	else{
		$.ajax({
			type: 'GET', 
			url: '/admin/getCustomerDetailsFromMeterId/' + element.value, 
			success: function(result){
				username = result[0];
				address = result[1];
				$("#customerUsername").val(username);
				$("#customerAddress").val(address);			
			},
			error: (error) => {
				console.log(JSON.stringify(error));
			}		
		});	
	}
}

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
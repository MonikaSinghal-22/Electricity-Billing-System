let container = document.getElementById('container')

toggle = () => {
	container.classList.toggle('sign-in')
	container.classList.toggle('sign-up')
}

setTimeout(() => {
	container.classList.add('sign-in')
}, 200)

$('document').ready(function(){
	//Meter Number
	$('input[type=radio][name=userType]').change(function(){
		if(this.value == 'ROLE_CUSTOMER')
		{
			$('.meterNo').show();
			$('#meterNo').prop('required',true);
		}
		else
		{
			$('.meterNo').hide();
			$('#meterNo').prop('required',false);
		}
	});	
	
	//Password
	var password = document.getElementById("password");
	var confirmPassword = document.getElementById("confirmPassword");
	
	function validatePassword(){
		if(password.value != confirmPassword.value){
			confirmPassword.setCustomValidity("Password Don't Match");
			confirmPassword.reportValidity();
		}
		else
			confirmPassword.setCustomValidity("");
	}
	
	password.onchange = validatePassword;
	confirmPassword.onkeyup = validatePassword;
	
});

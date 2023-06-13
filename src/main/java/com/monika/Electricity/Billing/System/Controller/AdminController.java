package com.monika.Electricity.Billing.System.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.monika.Electricity.Billing.System.Entity.BillType;
import com.monika.Electricity.Billing.System.Entity.City;
import com.monika.Electricity.Billing.System.Entity.Customers;
import com.monika.Electricity.Billing.System.Entity.MeterLocation;
import com.monika.Electricity.Billing.System.Entity.MeterType;
import com.monika.Electricity.Billing.System.Entity.Meters;
import com.monika.Electricity.Billing.System.Entity.PhaseCode;
import com.monika.Electricity.Billing.System.Entity.State;
import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Repository.BillTypeRepository;
import com.monika.Electricity.Billing.System.Repository.CityRepository;
import com.monika.Electricity.Billing.System.Repository.MeterLocationRepository;
import com.monika.Electricity.Billing.System.Repository.MeterTypeRepository;
import com.monika.Electricity.Billing.System.Repository.PhaseCodeRepository;
import com.monika.Electricity.Billing.System.Repository.StateRepository;
import com.monika.Electricity.Billing.System.Repository.UserRepository;
import com.monika.Electricity.Billing.System.Service.CustomerService;
import com.monika.Electricity.Billing.System.Service.MeterService;
import com.monika.Electricity.Billing.System.Service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private MeterService meterService;

	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private CityRepository cityRepo;
	@Autowired
	private BillTypeRepository billTypeRepo;
	@Autowired
	private MeterLocationRepository meterLocationRepo;
	@Autowired
	private MeterTypeRepository meterTypeRepo;
	@Autowired
	private PhaseCodeRepository phaseCodeRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute
	private void userDtls(Model m, Principal p) {
		String username = p.getName();
		Users user = userRepo.findByUsername(username);
		m.addAttribute("user", user);
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "admin/dashboard";
	}

	@GetMapping("/changePassword")
	public String loadChangePassword() {
		return "changePassword";
	}

	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttrs) {
		String username = p.getName();
		Users loginUser = userRepo.findByUsername(username);
		boolean oldPasswordMatch = passwordEncoder.matches(oldPassword, loginUser.getPassword());
		if (oldPasswordMatch) {
			loginUser.setPassword(passwordEncoder.encode(newPassword));
			Users user = userRepo.save(loginUser);
			if (user != null) {
				redirectAttrs.addFlashAttribute("successMessage", "Password Changed Successfully!");
			} else {
				redirectAttrs.addFlashAttribute("failureMessage", "Error occured, please try again later.");
			}

		} else {
			redirectAttrs.addFlashAttribute("failureMessage", "Incorrect old password.");
		}
		return "redirect:/admin/changePassword";
	}

	@GetMapping("/customers")
	public String getCustomers(Model m) {
		List<Customers> customersList = customerService.getByUserType("ROLE_CUSTOMER");
		m.addAttribute("customerList", customersList);
		 
		return "customers/index";
	}

	@GetMapping("/addCustomer")
	public String loadAddCustomer(Model m) {
		List<State> listState = stateRepo.findAll();
		m.addAttribute("listState", listState);
		List<MeterLocation> listMeterLocation = meterLocationRepo.findAll();
		m.addAttribute("listMeterLocation", listMeterLocation);
		List<MeterType> listMeterType = meterTypeRepo.findAll();
		m.addAttribute("listMeterType", listMeterType);
		List<PhaseCode> listPhaseCode = phaseCodeRepo.findAll();
		m.addAttribute("listPhaseCode", listPhaseCode);
		List<BillType> listBillType = billTypeRepo.findAll();
		m.addAttribute("listBillType", listBillType);

		return "customers/add";
	}

	@PostMapping("/saveCustomers")
	public String saveCustomers(@ModelAttribute Users user, @ModelAttribute Customers customer,
			@ModelAttribute Meters meter, RedirectAttributes redirectAttrs) {

		String username = user.getUsername();
		boolean usernameExistFlag = userService.checkUsername(username);
		if (usernameExistFlag) {
			redirectAttrs.addFlashAttribute("failureMessage", "Username Already Exist.");
		} else {
			Users userDetails = userService.createUser(user);
			customer.setUser(userDetails);
			Customers customerDetails = customerService.createCustomer(customer);
			meter.setCustomer(customerDetails);
			meterService.createMeter(meter);
			redirectAttrs.addFlashAttribute("successMessage", "Customer created successfully!");
		}

		return "redirect:/admin/addCustomer";
	}

	@GetMapping("/editCustomer/{customer_id}")
	public String loadEditCustomer(@PathVariable(value = "customer_id") int id, Model m) {
		Customers customer = customerService.getCustomerById(id);
		m.addAttribute("customer", customer);
		List<State> listState = stateRepo.findAll();
		m.addAttribute("listState", listState);
		if(customer.getState() != null)
		{
			List<City> cityList = cityRepo.getByStateId(Integer.parseInt(customer.getState()));
			m.addAttribute("cityList", cityList);
		}
		List<MeterLocation> listMeterLocation = meterLocationRepo.findAll();
		m.addAttribute("listMeterLocation", listMeterLocation);
		List<MeterType> listMeterType = meterTypeRepo.findAll();
		m.addAttribute("listMeterType", listMeterType);
		List<PhaseCode> listPhaseCode = phaseCodeRepo.findAll();
		m.addAttribute("listPhaseCode", listPhaseCode);
		List<BillType> listBillType = billTypeRepo.findAll();
		m.addAttribute("listBillType", listBillType);

		return "customers/edit";
	}

	@PostMapping("/updateCustomers")
	public String updateCustomers(@ModelAttribute Users user, @ModelAttribute Customers customer,
			@ModelAttribute Meters meter, RedirectAttributes redirectAttrs) {
		Users userDetails = userService.createUser(user);
		customer.setUser(userDetails);
		Customers c = customerService.getCustomerByUserId(user.getId());
		customer.setId(c.getId());		
		Customers customerDetails = customerService.createCustomer(customer);
		meter.setCustomer(customerDetails);
		Meters m = meterService.getMeterByCustromerId(customer.getId());
		meter.setId(m.getId());
		meterService.createMeter(meter);
		return "redirect:/admin/editCustomer/" + customer.getId();
	}
	
	@GetMapping("/viewCustomer/{customer_id}")
	public String loadViewCustomer(@PathVariable(value="customer_id") int id, Model m) {
		Customers customer = customerService.getCustomerById(id);
		int stateId = Integer.parseInt(customer.getState());
		State state = stateRepo.findById(stateId).get();
		int cityId = Integer.parseInt(customer.getCity());
		City city = cityRepo.findById(cityId).get();
		int meterId = customer.getMeter().getId();
		Meters meter = meterService.getMeterById(meterId);
		int meterLocationId = Integer.parseInt(meter.getMeterLocation());
		MeterLocation meterLocation = meterLocationRepo.findById(meterLocationId).get();
		int meterTypeId = Integer.parseInt(meter.getMeterType());
		MeterType meterType = meterTypeRepo.findById(meterTypeId).get();
		int phaseCodeId = Integer.parseInt(meter.getPhaseCode());
		PhaseCode phaseCode = phaseCodeRepo.findById(phaseCodeId).get();
		int billTypeId = Integer.parseInt(meter.getBillType());
		BillType billType = billTypeRepo.findById(billTypeId).get();
		m.addAttribute("customer", customer);
		m.addAttribute("state", state.getName());
		m.addAttribute("city", city.getName());
		m.addAttribute("meterNo", meter.getMeterNo());
		m.addAttribute("meterLocation", meterLocation.getName());
		m.addAttribute("meterType", meterType.getName());
		m.addAttribute("phaseCode", phaseCode.getName());
		m.addAttribute("billType", billType.getName());
		return "customers/view";
	}
	
	@GetMapping("/deleteCustomer/{user_id}")
	public String deleteCustomer(@PathVariable("user_id") int id) {
		Users user = userService.disableUser(id);
		return "redirect:/admin/customers";
	}

	@ResponseBody
	@GetMapping("/getCityByState/{state_id}")
	public String getCityByState(@PathVariable("state_id") int id) {
		List<City> cityList = cityRepo.getByStateId(id);
		Gson gson = new Gson();
		return gson.toJson(cityList);
	}

	@ResponseBody
	@GetMapping("/checkUsername/{username}")
	public boolean checkUsername(@PathVariable("username") String username) {
		boolean usernameExistFlag = userService.checkUsername(username);
		return usernameExistFlag;
	}
	
	@ResponseBody
	@GetMapping("/changeIsAccountLocked/{userId}/{flag}")
	public String changeIsAccountLocked(@PathVariable("flag") boolean flag, @PathVariable("userId") int userId) {
		String status = "";
		Users user = userService.findUserById(userId);
		if(user != null) {
			Users u = userService.changeIsAccountLocked(user,flag);
			status = "Status Updated.";
		}
		else {
			status = "User doesn't exist!";
		}
		return status;
	}

}

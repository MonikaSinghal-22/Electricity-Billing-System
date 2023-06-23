package com.monika.Electricity.Billing.System.Controller;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.monika.Electricity.Billing.Helper.Excel;
import com.monika.Electricity.Billing.System.Entity.Bill;
import com.monika.Electricity.Billing.System.Entity.BillType;
import com.monika.Electricity.Billing.System.Entity.City;
import com.monika.Electricity.Billing.System.Entity.Constant;
import com.monika.Electricity.Billing.System.Entity.Customers;
import com.monika.Electricity.Billing.System.Entity.MeterLocation;
import com.monika.Electricity.Billing.System.Entity.MeterType;
import com.monika.Electricity.Billing.System.Entity.Meters;
import com.monika.Electricity.Billing.System.Entity.PhaseCode;
import com.monika.Electricity.Billing.System.Entity.State;
import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Repository.BillTypeRepository;
import com.monika.Electricity.Billing.System.Repository.CityRepository;
import com.monika.Electricity.Billing.System.Repository.ConstantRepository;
import com.monika.Electricity.Billing.System.Repository.MeterLocationRepository;
import com.monika.Electricity.Billing.System.Repository.MeterTypeRepository;
import com.monika.Electricity.Billing.System.Repository.PhaseCodeRepository;
import com.monika.Electricity.Billing.System.Repository.StateRepository;
import com.monika.Electricity.Billing.System.Repository.UserRepository;
import com.monika.Electricity.Billing.System.Service.BillService;
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
	private BillService billService;

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
	private ConstantRepository constantRepo;

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
		userService.disableUser(id);
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
			userService.changeIsAccountLocked(user,flag);
			status = "Status Updated.";
		}
		else {
			status = "User doesn't exist!";
		}
		return status;
	}
	
	@GetMapping("/calculateBill")
	public String loadCalculateBill(Model m) {
		List<Integer> activeCustomerIds = customerService.getActiveCustomers();
		List<Meters> listMeter = meterService.getAllEnabledCustomersMeter(activeCustomerIds);
		m.addAttribute("listMeter", listMeter);
		return "bill/add";
	}
	
	@ResponseBody
	@GetMapping("/getCustomerDetailsFromMeterId/{meter_id}")
	public List<String> getCustomerDetailsFromMeterId(@PathVariable("meter_id") int meterId) {
		Meters meter = meterService.getMeterById(meterId);
		String username = meter.getCustomer().getUser().getUsername();
		String address = meter.getCustomer().getAddress();
		List<String> result = new ArrayList<>();
		result.add(username);
		result.add(address);
		return result;
	}
	
	@PostMapping("/saveBill")
	public String saveBill(@ModelAttribute Bill bill, RedirectAttributes redirectAttrs) {
		Meters meter = meterService.getMeterById(Integer.parseInt(bill.getMeterNo()));
		bill.setMeterNo(meter.getMeterNo());
		bill.setUsername(meter.getCustomer().getUser().getUsername());
		bill.setStatus(false);
		
		Constant constant = constantRepo.findById(1).get();
		Double costPerUnit = constant.getCostPerUnit();
		Double fixedCharge = constant.getFixedCharge();
		Double tax = constant.getTax()/100;
		
		bill.setCostPerUnit(costPerUnit);
		bill.setFixedCharge(fixedCharge);
		bill.setTax(constant.getTax());
		
		Double units = bill.getUnitsConsumed();
		Double cost = (units * costPerUnit) + fixedCharge;
		tax = cost * tax;
		Double totalBill = cost + tax;
		bill.setTotalBill(totalBill);
		
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		bill.setBillGenerationDate(df.format(new Date()));
		
		billService.createBill(bill);
		
		redirectAttrs.addFlashAttribute("successMessage", "Bill Generated Successfully.");
		
		return "redirect:/admin/calculateBill";
	}
	
	@GetMapping("/bills")
	public String getBills(Model m) {
		List<Bill> billList = billService.getAllBills();
		m.addAttribute("billList", billList);
		return "bill/index";
	}
	
	@GetMapping("/viewBill/{bill_id}")
	public String viewBill(@PathVariable("bill_id") int id ,Model m) {
		Bill bill = billService.getBillById(id);
		Meters meter = meterService.getMeterByMeterNo(bill.getMeterNo());
		State state = stateRepo.findById(Integer.parseInt( meter.getCustomer().getState())).get();
		City city = cityRepo.findById(Integer.parseInt( meter.getCustomer().getCity())).get();
		MeterLocation meterLocation = meterLocationRepo.findById(Integer.parseInt(meter.getMeterLocation())).get();
		MeterType meterType = meterTypeRepo.findById(Integer.parseInt(meter.getMeterType())).get();
		PhaseCode phaseCode = phaseCodeRepo.findById(Integer.parseInt(meter.getPhaseCode())).get();
		BillType billType = billTypeRepo.findById(Integer.parseInt(meter.getBillType())).get();
		m.addAttribute("meter", meter);
		m.addAttribute("bill", bill);
		m.addAttribute("state", state.getName());
		m.addAttribute("city", city.getName());
		m.addAttribute("meterLocation", meterLocation.getName());
		m.addAttribute("meterType", meterType.getName());
		m.addAttribute("phaseCode", phaseCode.getName());
		m.addAttribute("billType", billType.getName());
		
		return "bill/view";
	}
	
	@GetMapping("/editBill/{bill_id}")
	public String editBill(@PathVariable("bill_id") int id, Model m) {
		Bill bill = billService.getBillById(id);
		Meters meter = meterService.getMeterByMeterNo(bill.getMeterNo());
		String address = meter.getCustomer().getAddress();
		m.addAttribute("bill", bill);
		m.addAttribute("address", address);
		return "bill/edit";
	}
	
	@PostMapping("/updateBill")
	public String updateBill(@ModelAttribute Bill bill, RedirectAttributes redirectAttrs) {
		
		Bill originalBill = billService.getBillById(bill.getId());
		
		if(originalBill.getUnitsConsumed() != bill.getUnitsConsumed()) {
			Constant constant = constantRepo.findById(1).get();
			Double costPerUnit = constant.getCostPerUnit();
			Double fixedCharge = constant.getFixedCharge();
			Double tax = constant.getTax()/100;
			
			bill.setCostPerUnit(costPerUnit);
			bill.setFixedCharge(fixedCharge);
			bill.setTax(constant.getTax());
			
			Double units = bill.getUnitsConsumed();
			Double cost = (units * costPerUnit) + fixedCharge;
			tax = cost * tax;
			Double totalBill = cost + tax;
			bill.setTotalBill(totalBill);
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		bill.setBillGenerationDate(df.format(new Date()));
		
		Bill b = billService.createBill(bill);
		
		bill.setId(b.getId());
		redirectAttrs.addFlashAttribute("successMessage", "Bill Updated Successfully.");
		return "redirect:/admin/editBill/" + bill.getId();
	}
	
	@GetMapping("/deleteBill/{bill_id}")
	public String deleteBill(@PathVariable("bill_id") int id) {
		billService.deleteBill(id);
		return "redirect:/admin/bills";
	}

	@PostMapping("/uploadCustomers")
	public String uploadCustomers(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
		if(Excel.checkExcelFormat(file)) {
			try {
				List<Customers> customers = Excel.convertExcelToListOfCustomers(file.getInputStream());
				if(customers.isEmpty()) {
					redirectAttrs.addFlashAttribute("errorMessage", "Excel sheet not found. Please try again!");
				}
				
				Iterator<Customers> it = customers.iterator();
				
				while(it.hasNext()) {
					Customers cust = it.next();
					
					String stateId = "";
					String cityId = "";
					
					State stateExist = stateRepo.findByNameContainingIgnoreCase(cust.getState());
					if(stateExist == null) {
						State stateDetails = new State();
						stateDetails.setName(cust.getState());
						stateDetails = stateRepo.save(stateDetails);
						stateId = Integer.toString(stateDetails.getId());
						City cityDetails = new City();
						cityDetails.setName(cust.getCity());
						cityDetails.setState(stateDetails);
						cityDetails = cityRepo.save(cityDetails);
						cityId = Integer.toString(cityDetails.getId());
					}
					else {
						stateId = Integer.toString(stateExist.getId());
						
						City cityExist = cityRepo.findByNameContainingIgnoreCase(cust.getCity());
						if(cityExist == null) {
							City cityDetails = new City();
							cityDetails.setName(cust.getCity());
							cityDetails.setState(stateExist);
							cityDetails = cityRepo.save(cityDetails);
							cityId = Integer.toString(cityDetails.getId());
						}
						else if(cityExist.getState().getId() == stateExist.getId()) {
							cityId = Integer.toString(cityExist.getId());
						}
						else {
							cityId = null;
						}
					}
					
					String billTypeId = "";
					BillType billTypeExist = billTypeRepo.findByNameContainingIgnoreCase(cust.getMeter().getBillType());
					if(billTypeExist == null) {
						BillType billType = new BillType();
						billType.setName(cust.getMeter().getBillType());
						billType = billTypeRepo.save(billType);
						billTypeId = Integer.toString(billType.getId());
					}
					else {
						billTypeId = Integer.toString(billTypeExist.getId());
					}
					
					String meterLocationId = "";
					MeterLocation meterLocationExist = meterLocationRepo.findByNameContainingIgnoreCase(cust.getMeter().getMeterLocation());
					if(meterLocationExist == null) {
						MeterLocation meterLocation = new MeterLocation();
						meterLocation.setName(cust.getMeter().getMeterLocation());
						meterLocation = meterLocationRepo.save(meterLocation);
						meterLocationId = Integer.toString(meterLocation.getId());
					}
					else {
						meterLocationId = Integer.toString(meterLocationExist.getId());
					}
					
					String meterTypeId = "";
					MeterType meterTypeExist = meterTypeRepo.findByNameContainingIgnoreCase(cust.getMeter().getMeterType());
					if(meterTypeExist == null) {
						MeterType meterType = new MeterType();
						meterType.setName(cust.getMeter().getMeterType());
						meterType = meterTypeRepo.save(meterType);
						meterTypeId = Integer.toString(meterType.getId());
					}
					else {
						meterTypeId = Integer.toString(meterTypeExist.getId());
					}
					
					String phaseCodeId = "";
					PhaseCode phaseCodeExist = phaseCodeRepo.findByNameContainingIgnoreCase(cust.getMeter().getPhaseCode());
					if(phaseCodeExist == null) {
						PhaseCode phaseCode = new PhaseCode();
						phaseCode.setName(cust.getMeter().getPhaseCode());
						phaseCode = phaseCodeRepo.save(phaseCode);
						phaseCodeId = Integer.toString(phaseCode.getId());
					}
					else {
						phaseCodeId = Integer.toString(phaseCodeExist.getId());
					}
					
					Users user = new Users();
					user.setName(cust.getUser().getName());
					user.setPassword(cust.getUser().getPassword());
					user.setUsername(cust.getUser().getUsername());
					user.setUserType("ROLE_CUSTOMER");
					
					Customers c = new Customers();
					c.setAddress(cust.getAddress());
					c.setCity(cityId);
					c.setEmail(cust.getEmail());
					c.setName(cust.getName());
					c.setPhone(cust.getPhone());
					c.setState(stateId); 
					c.setUser(user);
					
					Meters meter = new Meters();
					meter.setBillType(billTypeId);
					meter.setDays("30");
					meter.setMeterLocation(meterLocationId);
					meter.setMeterNo(cust.getMeter().getMeterNo());
					meter.setMeterType(meterTypeId);
					meter.setPhaseCode(phaseCodeId);
					meter.setCustomer(c);
					
					redirectAttrs.addFlashAttribute("successMessage", "Customers Imported Successfully");
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("errorMessage", "Something went wrong. Please try again!");
			}
		}
		else {
			redirectAttrs.addFlashAttribute("errorMessage", "Incorrect Format!");
		}
		return "redirect:/admin/addCustomer";
	}
	
}

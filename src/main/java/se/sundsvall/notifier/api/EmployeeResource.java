package se.sundsvall.notifier.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/notifier2")
public class EmployeeResource {

	@GetMapping("/employee")
	public String employee() {
		return "detta är en employee";
	}
}

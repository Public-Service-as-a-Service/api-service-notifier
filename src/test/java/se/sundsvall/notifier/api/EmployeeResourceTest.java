package se.sundsvall.notifier.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.sundsvall.notifier.api.model.response.EmployeeManagerResponse;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.service.EmployeeService;

@WebMvcTest(EmployeeResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeResourceTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private EmployeeService service;

	@Test
	void getEmployee_succesful_test() throws Exception {
		var response = List.of(
			new EmployeeWithOrgNameResponse(1L, "personId1", "orgId1", "firstName1", "lastName1", "email1", "workMobile1", "workPhone1", "workTitle1", "avdelning1"),
			new EmployeeWithOrgNameResponse(2L, "personId2", "orgId1", "firstName2", "lastName2", "email2", "workMobile2", "workPhone2", "workTitle2", "avdelning2"));

		when(service.getEmployeesByOrg("orgId1")).thenReturn(response);

		mvc.perform(get("/api/notifier/employee/{orgId}", "orgId1")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(service).getEmployeesByOrg("orgId1");
	}

	@Test
	void getAll_succesfulTest() throws Exception {
		var response = List.of(
			new EmployeeWithOrgNameResponse(1L, "personId1", "orgId1", "firstName1", "lastName1", "email1", "workMobile1", "workPhone1", "workTitle1", "avdelning1"),
			new EmployeeWithOrgNameResponse(2L, "personId2", "orgId2", "firstName2", "lastName2", "email2", "workMobile2", "workPhone2", "workTitle2", "avdelning1"));

		when(service.getAllEmployees()).thenReturn(response);

		mvc.perform(get("/api/notifier/employee/employees"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2));

		verify(service).getAllEmployees();

	}

	@Test
	void getWithList_succesful() throws Exception {
		var response = List.of(
			new EmployeeWithOrgNameResponse(1L, "personId1", "orgId1", "firstName1", "lastName1", "email1", "workMobile1", "workPhone1", "workTitle1", "avdelning1"),
			new EmployeeWithOrgNameResponse(2L, "personId2", "orgId2", "firstName2", "lastName2", "email2", "workMobile2", "workPhone2", "workTitle2", "avdelning2"));

		when(service.getEmployeesByOrgList(List.of("orgId1", "orgId2"))).thenReturn(response);

		mvc.perform(get("/api/notifier/employee/ids").param("orgIds", "orgId1", "orgId2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2));

		verify(service).getEmployeesByOrgList(List.of("orgId1", "orgId2"));
	}

	@Test
	void GetEmployeesWithPartialSearch_test() throws Exception {
		var request = PageRequest.of(0, 2);
		Page<EmployeeWithOrgNameResponse> result = new PageImpl<>(List.of(), request, 0);

		when(service.getEmployeesWithSearch(eq("searchterm"), any(Pageable.class))).thenReturn(result);

		mvc.perform(get("/api/notifier/employee/employees/search")
			.param("search", "searchterm")
			.param("page", String.valueOf(request.getPageNumber()))
			.param("size", String.valueOf(request.getPageSize())))
			.andExpect(status().isOk());

		verify(service).getEmployeesWithSearch("searchterm", Pageable.ofSize(2));

	}

	@Test
	void getManagers_successful_test() throws Exception {
		var response = List.of(
			new EmployeeManagerResponse(1L, "personId1", "orgId1", "firstName1", "lastName1",
				"email1", "workMobile1", "workPhone1", "workTitle1", "A_"),
			new EmployeeManagerResponse(2L, "personId2", "orgId2", "firstName2", "lastName2",
				"email2", "workMobile2", "workPhone2", "workTitle2", "B_"));

		when(service.getAllEmployeeManagers()).thenReturn(response);

		mvc.perform(get("/api/notifier/employee/managers")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].managerCode").value("A_"))
			.andExpect(jsonPath("$[1].id").value(2))
			.andExpect(jsonPath("$[1].managerCode").value("B_"));

		verify(service).getAllEmployeeManagers();
	}

	@Test
	void getManagers_emptyList_successful_test() throws Exception {
		when(service.getAllEmployeeManagers()).thenReturn(List.of());

		mvc.perform(get("/api/notifier/employee/managers")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(0));

		verify(service).getAllEmployeeManagers();
	}

}

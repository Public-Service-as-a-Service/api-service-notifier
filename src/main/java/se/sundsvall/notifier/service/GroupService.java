package se.sundsvall.notifier.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.sundsvall.notifier.api.model.request.GroupRequest;
import se.sundsvall.notifier.api.model.request.GroupUpdateRequest;
import se.sundsvall.notifier.api.model.response.GroupResponse;
import se.sundsvall.notifier.integration.db.entity.Group;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.integration.db.repository.GroupRepository;
import se.sundsvall.notifier.service.mapper.EntityToResponseMapper;

@Service
public class GroupService {
	private final GroupRepository groupRepository;
	private final EmployeeRepository employeeRepository;
	private final EntityToResponseMapper mapper;

	public GroupService(GroupRepository groupRepo, EmployeeRepository employeeRepository, EntityToResponseMapper mapper) {
		this.groupRepository = groupRepo;
		this.employeeRepository = employeeRepository;
		this.mapper = mapper;

	}

	public List<GroupResponse> getAllGroups() {
		List<Group> groups = groupRepository.findAll();
		List<GroupResponse> groupList = new ArrayList<>();

		for (Group group : groups) {
			GroupResponse groupResponse = mapper.mapToGroupResponse(group);
			groupList.add(groupResponse);
		}
		return groupList;
	}

	public List<GroupResponse> getGroupsByCreatorId(String creatorId) {
		return groupRepository.findAllByCreatorId(creatorId)
			.stream()
			.map(mapper::mapToGroupResponse)
			.toList();
	}

	public GroupResponse getGroupById(Long id) {
		var group = groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

		return mapper.mapToGroupResponse(group);
	}

	@Transactional
	public Long createGroup(GroupRequest request) {
		var group = Group.builder()
			.withName(request.name())
			.withDescription(request.description())
			.withCreatorId(request.creatorId())
			.withEmployees(employeeRepository.findAllByIdIn(request.employees()))
			.build();

		return groupRepository.save(group).getId();
	}

	@Transactional
	public GroupResponse updateGroup(Long id, GroupUpdateRequest request) {
		var existingGroup = groupRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

		existingGroup.setName(request.name());
		existingGroup.setDescription(request.description());
		existingGroup.setEmployees(employeeRepository.findAllByIdIn(request.employees()));

		var savedGroup = groupRepository.save(existingGroup);
		return mapper.mapToGroupResponse(savedGroup);
	}

	public void deleteGroup(Long id) {
		var group = groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

		groupRepository.deleteById(group.getId());
	}

}

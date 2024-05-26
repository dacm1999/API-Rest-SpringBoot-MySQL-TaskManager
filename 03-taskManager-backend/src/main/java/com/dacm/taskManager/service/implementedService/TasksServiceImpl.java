package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.enums.Status;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.TaskRepository;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.TasksByUsernameResponse;
import com.dacm.taskManager.responses.TasksErrorResponse;
import com.dacm.taskManager.responses.TasksPaginationResponse;
import com.dacm.taskManager.service.interfaceService.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TaskRepository tasksRepository;
    private Tasks tasks;
    private TasksDTO dto;
    private List<Tasks> tasksList;
    private List<TasksDTO> tasksDTOList;
    private UserRepository userRepository;

    @Autowired
    public TasksServiceImpl(TaskRepository tasksRepository, UserRepository userRepository) {
        this.tasksRepository = tasksRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TasksDTO convertToDTO(Tasks tasks) {
        dto = new TasksDTO();
        dto.setName(tasks.getName());
        dto.setDescription(tasks.getDescription());
        dto.setStatus(String.valueOf(tasks.getStatus()));
        dto.setCreation_date(tasks.getCreation_date());
        dto.setDue_date(tasks.getDue_date());
        dto.setPriority(String.valueOf(tasks.getPriority_id()));
        return dto;
    }

    @Override
    public TasksDTO addSingleTask(Tasks tasks) {
        // Verificar si el user_id está vacío o nulo
        String userId = String.valueOf(tasks.getUser_id());
        if (userId == null || userId.trim().isEmpty()) {
            // Si el user_id está vacío o nulo, retornar null y establecer el motivo
            return null;
        }

        // Verificar si el usuario asociado existe en la base de datos
        Optional<User> user = userRepository.findById(tasks.getUser_id());
        if (user.isEmpty()) {
            // Si el usuario no existe, retornar null y establecer el motivo
            return null;
        }

        // Verificar si el nombre de la tarea ya está duplicado
        List<TasksDTO> tasksDTOList = getAllTasks();
        for (TasksDTO tasksDTO1 : tasksDTOList) {
            if (tasks.getName().equals(tasksDTO1.getName())) {
                // Si el nombre de la tarea está duplicado, retornar null y establecer el motivo
                return null;
            }
        }

        // Guardar la tarea en la base de datos
        Tasks savedTask = tasksRepository.save(tasks);
        return convertToDTO(savedTask);
    }

    @Override
    public TasksDTO getById(int id) {
        Tasks tasks = tasksRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + id));
        return convertToDTO(tasks);
    }

    @Override
    public AddedResponse addManyTasks(Tasks[] tasks) {
        List<TasksDTO> tasksDTOList = getAllTasks();
        AddedResponse result;
        String reason = "";
        int total = tasks.length;
        int count_added = 0;
        int count_failed = 0;
        List<TasksErrorResponse> failed = new ArrayList<>();
        List<Tasks> addedTasks = new ArrayList<>();

        for (Tasks tempTasks : tasks) {
            boolean duplicateName = false;
            for (TasksDTO tempTasksDTO : tasksDTOList) {
                if (tempTasksDTO.getName().equals(tempTasks.getName())) {
                    TasksErrorResponse tasksErrorModel = new TasksErrorResponse(
                            tempTasks.getName(), tempTasks.getUser_id()
                            , "TASK NAME DUPLICATED " + tempTasks.getName());
                    failed.add(tasksErrorModel);
                    count_failed++;
                    duplicateName = true;
                    reason = "COULD NOT ADD THIS TASKS";
                    break;
                }
            }
            if (!duplicateName) {
                tasksRepository.save(tempTasks);
                reason = "TASKS ADDED SUCCESSFULLY";
                addedTasks.add(tempTasks);
                count_added++;
            }
        }

        result = new AddedResponse(true, total, count_added, count_failed, (ArrayList<Tasks>) addedTasks, (ArrayList<TasksErrorResponse>) failed, reason);

        return result;
    }

    @Override
    public List<TasksDTO> getByUserId(int user_id) {
        List<Tasks> tasksList = tasksRepository.findByUserId(user_id);

        if (tasksList.isEmpty()) {
            throw new CommonErrorResponse("COULD NOT FIND TASKS WITH USER ID: " + user_id);
        }

        return tasksList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TasksDTO> getAllTasks() {
        tasksList = tasksRepository.findAll();
        tasksDTOList = new ArrayList<>();

        for (Tasks tempTasks : tasksList) {
            dto = convertToDTO(tempTasks);
            tasksDTOList.add(dto);
        }
        return tasksDTOList;
    }

    @Override
    public Page<TasksDTO> getAllTask(PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority) {
        Specification<Tasks> specification = Specification.where(null);
        if (name != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
        }
        if (description != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("description"), description));
        }
        if (status != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }
        if (creation_date != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("creation_date"), creation_date));
        }
        if (due_date != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("due_date"), due_date));
        }
        if (priority != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority));
        }

        Page<Tasks> tasksPage = tasksRepository.findAll(specification, pageRequest);
        return tasksPage.map(this::convertToDTO);
    }

    @Override
    public TasksPaginationResponse createPaginationResponse(Page<TasksDTO> result) {
        TasksPaginationResponse response = new TasksPaginationResponse();
        response.setTasks(result.getContent());
        response.setTotalPages(result.getTotalPages());
        response.setTotalElements(result.getTotalElements());
        response.setPage(result.getNumber());
        response.setSize(result.getSize());
        response.setNumberOfElements(result.getNumberOfElements());
        return response;
    }

    @Override
    public Page<TasksDTO> getAllTasksByUserId(int userId, PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority) {
        List<TasksDTO> tasksDTOList = getByUserId(userId);

        List<TasksDTO> filteredTasks = new ArrayList<>();
        for (TasksDTO task : tasksDTOList) {
            boolean matchesCriteria = true;
            if (name != null && !task.getName().equals(name)) {
                matchesCriteria = false;
            }
            if (description != null && !task.getDescription().equals(description)) {
                matchesCriteria = false;
            }
            if (status != null && !task.getStatus().equals(status)) {
                matchesCriteria = false;
            }
            if (creation_date != null && !task.getCreation_date().equals(creation_date)) {
                matchesCriteria = false;
            }
            if (due_date != null && !task.getDue_date().equals(due_date)) {
                matchesCriteria = false;
            }
            if (priority != null && !task.getPriority().equals(priority)) {
                matchesCriteria = false;
            }
            if (matchesCriteria) {
                filteredTasks.add(task);
            }
        }


        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), filteredTasks.size());
        Page<TasksDTO> page = new PageImpl<>(filteredTasks.subList(start, end), pageRequest, filteredTasks.size());

        return page;
    }

    @Override
    public TasksByUsernameResponse getTasksByUsername(String username, int page, int size, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority) {
        Optional<User> user = userRepository.findByUsername(username);
        int userId = user.map(User::getUserId).orElseThrow(() -> new CommonErrorResponse("User not found with name: " + username));

        Page<TasksDTO> result = getAllTasksByUserId(
                userId,
                PageRequest.of(page, size),
                name,
                description,
                status,
                creation_date,
                due_date,
                priority
        );

        List<TasksDTO> tasksDTOList = getByUserId(userId);
        int totalTasks = tasksDTOList.size();
        int count_completed = (int) tasksDTOList.stream().filter(task -> task.getStatus().equalsIgnoreCase("Completed")).count();
        int count_pending = (int) tasksDTOList.stream().filter(task -> task.getStatus().equalsIgnoreCase("Pending")).count();

        return createTasksByUsernameResponse(username, result, totalTasks, count_completed, count_pending);
    }

    @Override
    public TasksByUsernameResponse createTasksByUsernameResponse(String username, Page<TasksDTO> result, int totalTasks, int count_completed, int count_pending) {
        List<TasksDTO> completedTasks = result.getContent().stream().filter(task -> task.getStatus().equalsIgnoreCase("Completed")).collect(Collectors.toList());
        List<TasksDTO> pendingTasks = result.getContent().stream().filter(task -> task.getStatus().equalsIgnoreCase("Pending")).collect(Collectors.toList());

        count_completed = completedTasks.size();
        count_pending = pendingTasks.size();
        totalTasks = count_completed + count_pending;

        // Asigna las listas de tareas completadas y pendientes directamente
        return new TasksByUsernameResponse(username, totalTasks, count_completed, count_pending, completedTasks, pendingTasks);
    }

    @Override
    public TasksDTO updateById(Integer id, TasksDTO tasksDTO) {
        tasks = tasksRepository.findById(id).orElseThrow();
        tasks.setName(tasksDTO.getName());
        tasks.setDescription(tasksDTO.getDescription());
        tasks.setStatus(Status.valueOf(tasksDTO.getStatus()));

        // Trim the date string provided by the user and then parse it to LocalDate
        tasks.setCreation_date(tasksDTO.getCreation_date());
        tasks.setDue_date(tasksDTO.getDue_date());
        tasks.setPriority_id(Integer.parseInt(tasksDTO.getPriority()));
        Tasks updated = tasksRepository.save(tasks);

        return convertToDTO(updated);
    }

    @Override
    public TasksDTO deleteById(Integer id) {
        tasks = tasksRepository.findById(id).orElseThrow();
        tasksRepository.deleteById(id);
        return convertToDTO(tasks);
    }

    @Override
    public void saveManyTasks(Tasks tasks) {
        Optional<Tasks> existingTasks = tasksRepository.findById(tasks.getId());
        if (!existingTasks.isPresent()) {
            tasksRepository.save(tasks);
        }

    }

}

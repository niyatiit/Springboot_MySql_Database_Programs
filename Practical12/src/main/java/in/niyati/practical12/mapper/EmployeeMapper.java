package in.niyati.practical12.mapper;

import in.niyati.practical12.dto.EmployeeDTO;
import in.niyati.practical12.entity.Employee;

// Utility class with static conversion methods between Entity and DTO.
public class EmployeeMapper {

    // Converts an Employee entity (internal, full data) into an EmployeeDTO (external, safe data)
    public static EmployeeDTO toDto(Employee employee) {
        return new EmployeeDTO(
                employee.getEmpId(),
                employee.getEmpName(),
                employee.getDepartment()
        );
    }

    // Converts an EmployeeDTO (from client) into an Employee entity (for persistence).
    // Note: password and salary are NOT set here since the DTO doesn't carry them -
    // they'll need to be set separately (or left as default/existing values) wherever this is used.
    public static Employee toEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setEmpId(dto.getEmpId());
        employee.setEmpName(dto.getEmpName());
        employee.setDepartment(dto.getDepartment());
        return employee;
    }
}
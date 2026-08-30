package in.niyati.practical12.dto;

// DTO (Data Transfer Object) - only contains fields SAFE to expose to the client.
// Notice: no "password" field here, and no "salary" either per the assignment spec -
// this is the actual API contract exposed to the outside world.
public class EmployeeDTO {

    private int empId;
    private String empName;
    private String department;

    public EmployeeDTO() {
    }

    public EmployeeDTO(int empId, String empName, String department) {
        this.empId = empId;
        this.empName = empName;
        this.department = department;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
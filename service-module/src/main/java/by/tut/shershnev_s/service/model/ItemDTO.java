package by.tut.shershnev_s.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

public class ItemDTO {

    private Long id;

    @NotEmpty(message = "Cant be empty")
    @Size(max = 40, message = "Max length for Name - 40 symbols")
    private String name;
    @NotEmpty(message = "Cant be empty")
    @Pattern(regexp = "(?:READY|COMPLETED)", message = "Status can be only READY or COMPLETED")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

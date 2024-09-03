package swp.todoapp.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import swp.todoapp.model.User;

import java.util.Date;

@Getter
@Setter
@Builder
public class TodoItemDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("createDate")
    private Date createdDate;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isDone")
    private boolean isDone;

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}

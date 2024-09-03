package swp.todoapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "nvarchar(150)", nullable = false)
    private String name;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "description", columnDefinition = "nvarchar(1024)")
    private String description;

    @Column(name = "is_done", columnDefinition = "bit default 0")
    private boolean isDone;

    @Column(name = "is_deleted", columnDefinition = "bit default 0")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

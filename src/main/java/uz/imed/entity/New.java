package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "news")
public class New {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "news", orphanRemoval = true)
    NewOption head;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "newness", orphanRemoval = true)
    List<NewOption> newOptions;

    @Column(unique = true)
    String slug;

    Integer orderNum;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    Date createdDate;

    Boolean active;

    @PostPersist
    @PostUpdate
    private void setOptionsId() {
        if (this.newOptions != null)
            this.newOptions.forEach(i -> i.setNewness(this));
        if (this.head != null)
            this.head.setNews(this);
    }

}

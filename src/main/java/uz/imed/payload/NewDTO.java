package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.New;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewDTO {

    Long id;

    NewOptionDTO head;

    List<NewOptionDTO> newOptions;

    String slug;

    Integer orderNum;

    Date createdDate;

    Boolean active;

    public NewDTO(New newness, String lang){
        this.id= newness.getId();
        this.slug= newness.getSlug();
        this.orderNum=newness.getOrderNum();
        this.createdDate=newness.getCreatedDate();
        this.active=newness.getActive();
        this.head=new NewOptionDTO(newness.getHead(),lang);
        this.newOptions=newness.getNewOptions().stream()
                .map(newOption -> new NewOptionDTO(newOption,lang))
                .collect(Collectors.toList());
    }

}

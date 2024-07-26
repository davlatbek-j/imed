package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.CategoryItem;
import uz.imed.exeptions.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryItemDTO {
    String title;

    String slug;

    PhotoDTO photoDTO;

    List<CatalogDTO> catalogDTOS;


    Integer orderNum;

    public CategoryItemDTO(CategoryItem categoryItem,String lang){
        this.slug= categoryItem.getSlug();
        this.photoDTO=new PhotoDTO(categoryItem.getPhoto());
        this.catalogDTOS=new ArrayList<>();
        categoryItem.getCatalogList().forEach(i -> this.catalogDTOS.add(new CatalogDTO( i,lang)));
        this.orderNum=categoryItem.getOrderNum();
       switch (lang.toLowerCase()){
           case "uz":
           {
               this.title= categoryItem.getTitleUz();
           }
           case "ru":
           {
               this.title=categoryItem.getTitleRu();
           }
           case "eng":
           {
               this.title=categoryItem.getTitleEng();
           }
           default:{
               throw new LanguageNotSupportException("Language not supported: " + lang);
           }
       }
    }
}

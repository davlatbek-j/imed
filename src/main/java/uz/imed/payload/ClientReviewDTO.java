package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.ClientReview;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientReviewDTO {

    Long id;

    String clientName;

    Photo logo;

    String comment;

    Date createdDate;

    public ClientReviewDTO(ClientReview clientReview, String lang) {
        this.id = clientReview.getId();
        this.clientName = clientReview.getClientName();
        this.logo = clientReview.getLogo();
        this.createdDate=clientReview.getCreatedDate();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.comment = clientReview.getCommentUz();
                break;
            }

            case "ru": {
                this.comment = clientReview.getCommentRu();
                break;
            }

            case "en": {
                this.comment = clientReview.getCommentEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }

}

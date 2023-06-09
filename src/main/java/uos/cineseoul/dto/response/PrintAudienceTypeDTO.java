package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.utils.enums.AudienceType;

@Data
public class PrintAudienceTypeDTO {
    private AudienceType audienceType;
    private String displayName;
    private Integer discountPrice;

    public PrintAudienceTypeDTO(AudienceType audienceType) {
        this.audienceType = audienceType;
        this.displayName = audienceType.getDisplayName();
        this.discountPrice = audienceType.getDiscountPrice();
    }
}

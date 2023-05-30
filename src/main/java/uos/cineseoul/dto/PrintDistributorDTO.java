package uos.cineseoul.dto;

import lombok.Data;
import uos.cineseoul.entity.Distributor;

@Data
public class PrintDistributorDTO {
    private Long distNum;
    private String name;

    public PrintDistributorDTO(Distributor distributor) {
        this.distNum = distributor.getDistNum();
        this.name = distributor.getName();
    }
}

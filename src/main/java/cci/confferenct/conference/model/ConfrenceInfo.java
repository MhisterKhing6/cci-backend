package cci.confferenct.conference.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfrenceInfo {
    private String confrenceId;
    private String confrenceName;
    private String location;
    private String startDate;
    private String endDate;
}
